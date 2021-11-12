import entity.Event;
import entity.Packet;
import entity.PacketWrapper;
import event.EventListManager;
import queue.PacketQueueManager;
import strategy.DefaultPacketProcessingStrategy;
import strategy.DropTailPacketProcessingStrategy;
import strategy.PacketProcessingStrategy;
import strategy.REDPacketProcessingStrategy;
import util.Generator;
import util.StatisticsUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @ClassName Starter
 * @Description Starter used to start the program
 * @Author wangmeng
 * @Date 2021/10/19
 */
public class Starter {

    public double clock;

    public long numberInService;

    public EventListManager futureEventList;

    public PacketQueueManager  packetQueue;

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

    public long batchPackets;

    public long totalPackets;

    public long batches;

    public double statisticsInterval;

    public double batchBusyTime;

    public double totalBusyTime;

    public double lastEventTime;

    public List<Double> batchMeanTimeList;

    public List<Double> batchThroughputList;

    public List<Double> batchDelayMoreThanGivenValueList;

    public long packetsDelayMoreThanGivenValue;

    public long queueLength = 0;

    public long count = 0;

    public List<Long> queueSize;

    public double delayThreshold;

    /**
     * 0 unlimited 1 drop tail strategy 2 RED strategy
     */
    public int strategyType = 2;

    public PacketProcessingStrategy packetProcessingStrategy;

    public double averageQueueSize;

    public double idleStartTime;


    public static void main(String[] args) {
        new Starter().start();
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
//            System.out.println(clock);
            if (clock < 1000) {
                queueSize.add(packetQueue.size());
            }

            // this code block is used to calculate the run time that makes mean delay confidence
            // interval is no greater than +/- 3%
//            int size = batchMeanTimeList.size();
//            if (size > 0) {
//                double[] temp = StatisticsUtil.countMeanAndVariance(batchMeanTimeList);
//                double currentDelayVariance = temp[1];
//                double limit = currentDelayVariance * 1.984217 / Math.sqrt(size);
//                if (limit <= 0.03) {
//                    System.out.println("current clock: " + clock);
//                    break;
//                }
//            }

            if (evt.getEventType() == 0) {
                // if event type is ARRIVAL
                // process arrival event
                processArrival();
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

        // print information used for assignment
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("size: " + packetQueue.size());
        System.out.println(totalDelayTime);
        System.out.println(totalPackets);
//        System.out.println(Arrays.toString(batchMeanTimeList.toArray()));
        System.out.println("mean packet delay : " + totalDelayTime / batches);
        double[] temp1 = StatisticsUtil.countMeanAndVariance(batchMeanTimeList);
        System.out.println("mean of the delay time: " + temp1[0]);
        System.out.println("standard variance of the dalay time: " + temp1[1]);
        double[] delayInterval = StatisticsUtil.countConfidenceInterval(temp1[0], temp1[1], (batches - 1));
        System.out.println("confidence interval of delay time: [" + delayInterval[0] + ", " + delayInterval[1] + "]");
        System.out.println("Mean Delay: 95% C.I. : " + "-/+ " + (delayInterval[1] - temp1[0]) * 100 + "%");
        System.out.println("-----------------------------------------------------------------------------------");
//        System.out.println(Arrays.toString(batchThroughputList.toArray()));
        System.out.println("throughput : " + totalBusyTime / batches);
        double[] temp2 = StatisticsUtil.countMeanAndVariance(batchThroughputList);
        System.out.println("mean of the throughput: " + temp2[0]);
        System.out.println("standard variance of the throughput: " + temp2[1]);
        double[] throughputInterval = StatisticsUtil.countConfidenceInterval(temp2[0], temp2[1], (batches - 1));
        System.out.println("confidence interval of throughput: [" + throughputInterval[0] + ", " + throughputInterval[1] + "]");
        System.out.println("Mean throughput: 95% C.I. : " + "-/+ " + (throughputInterval[1] - temp2[0]) * 100 + "%");
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("queue size array ");
        System.out.println("size: " + queueSize.size());
//        System.out.println(Arrays.toString(queueSize.toArray()));
        System.out.println("-----------------------------------------------------------------------------------");
        double[] temp3 = StatisticsUtil.countMeanAndVariance(batchDelayMoreThanGivenValueList);
        System.out.println("mean of the proportion of packets that delay more than given value: " + temp3[0]);
        System.out.println("standard variance of the mean of the proportion of packets that delay more than given value: " + temp3[1]);
        double[] packetsThatDelayMoreThanGiveValueInterval = StatisticsUtil.countConfidenceInterval(temp3[0], temp3[1], (batches - 1));
        System.out.println("Mean throughput: 95% C.I. : " + "-/+ " + (packetsThatDelayMoreThanGiveValueInterval[1] - temp3[0]) * 100 + "%");

    }

    public void initialization() {
        // initialize every attribute
        random1 = new Random();
        random2 = new Random();
        random1.setSeed(2020);
        random2.setSeed(2021);
        linkRate = 10000000;
        meanPacketLength = 4000;
        offerLoaded = 1.1;
        clock = 0.0;
        // total run time
        totalRunTime = 3760 * 1000;
//        totalRunTime = 1000 * 1000;
        // batch count
        batches = 752;
//        batches = 100;
        queueLength = 30;
        totalDelayTime = 0.0;
        batchDelayTime = 0.0;
        batchPackets = 0;
        totalPackets = 0;
        totalBusyTime = 0.0;
        lastEventTime = 0.0;
        batchBusyTime = 0.0;
        delayThreshold = 2.5;
        averageQueueSize = 0.0;
        idleStartTime = 0.0;
        packetsDelayMoreThanGivenValue = 0;
        queueSize = new ArrayList<>();
        batchMeanTimeList = new ArrayList<>();
        batchThroughputList = new ArrayList<>();
        batchDelayMoreThanGivenValueList = new ArrayList<>();
        statisticsInterval = totalRunTime / batches;
        // service rate
        meanServiceRateLambda = 1 / ((meanPacketLength / linkRate) * 1000);
        // inter arrival rate
        interArrivalLambda = offerLoaded * meanServiceRateLambda;
        // future event list
        futureEventList = EventListManager.getInstance();
        // packet queue
        packetQueue = PacketQueueManager.getInstance();
        // create first arrival event -- start event
        Event arrivalEvent = new Event(clock, 0);
        futureEventList.insertEvent(arrivalEvent);
        // create first statics event
        scheduleStatistics();
        // initialize the packet processing strategy
        if (strategyType == 0) {
            packetProcessingStrategy = new DefaultPacketProcessingStrategy();
        } else if (strategyType == 1) {
            packetProcessingStrategy = new DropTailPacketProcessingStrategy();
        } else if (strategyType == 2) {
            packetProcessingStrategy = new REDPacketProcessingStrategy();
        }
    }

    /**
     * When arrival event occurs, use this function to process
     */
    public void processArrival() {
        // get a packet whose length is from a distribution
        double lambda = 1.0 / meanPacketLength;
        double packetLength = Generator.generatePacketLength(lambda, random2);
        Packet packet = new Packet(packetLength, clock);
//        boolean isInsertSuccess = packetQueue.insert(packet);
        PacketWrapper packetWrapper = new PacketWrapper();
        packetWrapper.setPacket(packet);
        packetWrapper.setClock(clock);
        packetWrapper.setIdleStartTime(idleStartTime);
        packetWrapper.setPacketQueueManager(packetQueue);
        packetWrapper.setΜ(meanServiceRateLambda);
        packetWrapper.setMaxQueueSize(queueLength);
        boolean isInsertSuccess = packetProcessingStrategy.packetProcessing(packetWrapper);
        if (!isInsertSuccess) {
            count++;
        }
        totalPackets++;
        batchPackets++;
        // if the server is idle, schedule the departure event
        if (numberInService == 0 && packetQueue.size() != 0) {
            scheduleDeparture();
        } else {
            batchBusyTime += (clock - lastEventTime);
        }
        // schedule the next arrival event`
        double interArrival = Generator.generateInterArrival(interArrivalLambda, random1);
        // calculate the inter arrival time
        Event event = new Event(clock + interArrival, 0);
        futureEventList.insertEvent(event);
        lastEventTime = clock;
    }

    /**
     * When departure event occurs, use this method to process
     */
    public void processDeparture() {
        // if the length of packetQueue > 0, schedule the next packet departure
        Packet packet = packetQueue.poll();
        if (packetQueue.size() == 0) idleStartTime = clock;
        double arriveClock = packet.getArriveClock();
        double delay = clock - arriveClock;
        if (delay > delayThreshold) {
            packetsDelayMoreThanGivenValue++;
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

    /**
     * When statistics event occurs, use this method to process
     */
    public void processStatistics() {
        double currentBatchDelayTime = batchDelayTime / batchPackets;
        batchMeanTimeList.add(currentBatchDelayTime);
        totalDelayTime += currentBatchDelayTime;

        double currentBatchThroughput = batchBusyTime / statisticsInterval;
        batchThroughputList.add(currentBatchThroughput);
        totalBusyTime += currentBatchThroughput;

        double packetsDelayMoreThanGivenValueProportion = 1.0 * packetsDelayMoreThanGivenValue / batchPackets;
        batchDelayMoreThanGivenValueList.add(packetsDelayMoreThanGivenValueProportion);

        packetsDelayMoreThanGivenValue = 0;
        batchDelayTime = 0.0;
        batchBusyTime = 0.0;
        batchPackets = 0;
        // schedule the next statistics event
        scheduleStatistics();
    }

    /**
     * schedule departure event
     */
    public void scheduleDeparture() {
        // get the service time from the packet length and the distribution
        Packet packet = packetQueue.element();
        double packetLength = packet.getLength();
        double serviceTime = packetLength / linkRate * 1000;
        Event event = new Event(clock + serviceTime, 1);
        futureEventList.insertEvent(event);
        numberInService = 1;
    }

    /**
     * schedule statistics event
     */
    public void scheduleStatistics() {
        Event statisticsEvent = new Event(clock + statisticsInterval, 2);
        futureEventList.insertEvent(statisticsEvent);
    }
}
