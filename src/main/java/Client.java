import entity.Event;
import entity.Packet;
import event.EventListManager;
import queue.PacketQueueManager;
import util.Generator;

import java.util.Random;

/**
 * @ClassName Clent
 * @Description TODO
 * @Author wangmeng
 * @Date 2021/10/19
 */
public class Client {

    public double clock;

    public long numberInService;

    public EventListManager futureEventList;

    public PacketQueueManager packetQueue;

    public double interArrivalLambda;

    public double meanServiceRateLambda;

    public double linkRate;

    public long meanPacketLength;

    public double offerLoaded;

    public Random random1;

    public Random random2;

    public double totalRunTime;

    public double totalDelayTime;

    public double batchDelayTime;

    public double currentBatchDelayTime;

    public long batchPackets;

    public long packetsWithLongerDelay;

    public double proportionOfTimeBusy;

    public long totalPackets;

    public long batches;

    public double statisticsInterval;

    public double batchBusyTime;

    public double totalBusyTime;

    public double lastEventTime;


    public static void main(String[] args) {
        new Client().start();
    }

    public void start() {
        // initialization
        initialization();

        // loop for specific seconds
        while (clock < totalRunTime) {
            // get future event list
            Event evt = futureEventList.poll();
            // advance in time
            clock = evt.getEventClock();
            System.out.println(clock);
            if (evt.getEventType() == 0) {
                // if event type is ARRIVAL
                // process arrival event
                processaArrival();
            } else if (evt.getEventType() == 1) {
                // if event type is DEPARTURE
                // process departure event
                processDeparture();
            } else {
                // if event type is STATISTICS
                // process statistic event
                processStatistics();
            }

        }
        System.out.println("size: " + packetQueue.size());
        System.out.println(totalDelayTime);
        System.out.println(totalPackets);
        System.out.println("mean packet delay : " + totalDelayTime / batches);
        System.out.println("mean proportion of time busy : " + totalBusyTime / batches);
    }

    public void initialization() {
        // initialize every attribute
        random1 = new Random();
        random2 = new Random();
        linkRate = 10000000;
        meanPacketLength = 4000;
        offerLoaded =0.9;
        clock = 0.0;
        totalRunTime = 100 * 1000;
        totalDelayTime = 0.0;
        batchDelayTime = 0.0;
        batchPackets = 0;
        totalPackets = 0;
        batches = 100;
        currentBatchDelayTime = 0.0;
        packetsWithLongerDelay = 0;
        proportionOfTimeBusy = 0.0;
        totalBusyTime = 0.0;
        lastEventTime = 0.0;
        batchBusyTime = 0.0;
        statisticsInterval = totalRunTime / batches;
        meanServiceRateLambda = 1 / ((meanPacketLength / linkRate) * 1000);
        interArrivalLambda = offerLoaded * meanServiceRateLambda;
        futureEventList = EventListManager.getInstance();
        packetQueue = PacketQueueManager.getInstance();
        // create first arrival event
        Event arrivalEvent = new Event(clock, 0);
        futureEventList.insertEvent(arrivalEvent);
        // create first statics event
        scheduleStatistics();
    }

    public void processaArrival() {
        // get a packet whose length is from a distribution
        double lambda = 1.0 / meanPacketLength;
        double packetLength = Generator.generatePacketLength(lambda, random2);
        Packet packet = new Packet(packetLength, clock);
        packetQueue.insert(packet);
        totalPackets++;
        batchPackets++;
        // if the server is idle, schedule the departure event
        if (numberInService == 0) {
            scheduleDeparture();
        } else {
            batchBusyTime += (clock - lastEventTime);
        }
        // schedule the next arrival event
        double interArrival = Generator.generateInterArrival(interArrivalLambda, random1);
        // calculate the inter arrival time
        Event event = new Event(clock + interArrival, 0);
        futureEventList.insertEvent(event);
        lastEventTime = clock;
    }

    public void processDeparture() {
        // if the length of packetQueue > 0, schedule the next packet departure
        Packet packet = packetQueue.poll();
        double arriveClock = packet.getArriveClock();
        double delay = clock - arriveClock;
        if (totalDelayTime > 0 && delay > currentBatchDelayTime) {
            packetsWithLongerDelay++;
        }
        batchDelayTime += delay;
        if (packetQueue.size() > 0) {
            scheduleDeparture();
        } else {
            numberInService = 0;
        }
        batchBusyTime += (clock - lastEventTime);
        lastEventTime = clock;
    }

    public void scheduleDeparture() {
        // get the service time from the packet length and the distribution
        Packet packet = packetQueue.element();
        double packetLength = packet.getLength();
        double serviceTime = packetLength / linkRate * 1000;
        Event event = new Event(clock + serviceTime, 1);
        futureEventList.insertEvent(event);
        numberInService = 1;
    }

    public void processStatistics() {
        currentBatchDelayTime = batchDelayTime / batchPackets;
        totalDelayTime += currentBatchDelayTime;
        if (packetsWithLongerDelay > 0) {
            double currentBatchProportionWithLongerDelay = 1.0 * packetsWithLongerDelay / batchPackets;
            proportionOfTimeBusy += currentBatchProportionWithLongerDelay;
        }

        totalBusyTime += batchBusyTime / statisticsInterval;
        batchDelayTime = 0.0;
        batchBusyTime = 0.0;
        packetsWithLongerDelay = 0;
        batchPackets = 0;
        // schedule the next statistics event
        scheduleStatistics();
    }

    public void scheduleStatistics() {
        Event statisticsEvent = new Event(clock + statisticsInterval, 2);
        futureEventList.insertEvent(statisticsEvent);
    }
}
