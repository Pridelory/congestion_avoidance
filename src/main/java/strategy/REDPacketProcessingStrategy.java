package strategy;

import entity.Packet;
import entity.PacketWrapper;
import queue.PacketQueueManager;
import util.Generator;

/**
 * @ClassName REDPacketProcessingStrategy
 * @Description RED congestion control algorithm
 * @Author wangmeng
 * @Date 2021/11/9
 */
public class REDPacketProcessingStrategy implements PacketProcessingStrategy {

    public double clock;

    public double averageQueueSize;

    public double weightFactor = 0.005;

    public double maxXp = 0.05;

    public double minThreshold = 5;

    public double maxThreshold = 15;

    public double s;

    public double idleStartTime;

    /**
     * Number of packets that are not dropped since the last dropped packet
     */
    public int count;

    @Override
    public boolean packetProcessing(PacketWrapper packetWrapper) {
//        System.out.println(averageQueueSize);
        PacketQueueManager packetQueue = packetWrapper.getPacketQueueManager();
        this.clock = packetWrapper.getClock();
        this.s = 0.2 / packetWrapper.getΜ();
        this.idleStartTime = packetWrapper.getIdleStartTime();
        computeAverageQueueSize(packetQueue);
        if (averageQueueSize < minThreshold) {
            // add this packet to the queue directly
            if (packetQueue.size() < packetWrapper.getMaxQueueSize()) {
                return packetQueue.insert(packetWrapper.getPacket());
            }
        } else if (averageQueueSize <= maxThreshold) {
            // mark the packet with specific drop probability
            double pa = computeDroppedProbability(packetWrapper.getPacket());
            double randomNumberBetween0and1 = Generator.generateRandomNumberBetween0and1() * maxXp;
            double randomNumber = randomNumberBetween0and1 / (1 - count * randomNumberBetween0and1);
            boolean dropped = false;
            if (randomNumber <= pa) {
                dropped = true;
            }
            if (dropped) {
                count = 0;
            } else {
                if (packetQueue.size() < packetWrapper.getMaxQueueSize()) {
                    packetQueue.insert(packetWrapper.getPacket());
                    count++;
                }
            }
        } else {
            // drop every packet
            System.out.println("");
        }
        return false;
    }

    public void computeAverageQueueSize(PacketQueueManager packetQueue) {
        if (packetQueue.size() > 0) {
            averageQueueSize = (1 - weightFactor) * averageQueueSize + weightFactor * packetQueue.size();
        } else {
            double m = (clock - idleStartTime) / s;
            averageQueueSize = Math.pow((1 - weightFactor), m) * averageQueueSize;
        }
    }

    public double computeDroppedProbability(Packet packet) {
        double pb = maxXp * (averageQueueSize - minThreshold) / (maxThreshold - minThreshold);
//        pb = pb * packet.getLength() / 4000;
        double pa = pb / (1 - count * pb);
        return pa;
    }
}
