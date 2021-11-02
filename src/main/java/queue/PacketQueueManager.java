package queue;

import entity.Packet;
import java.util.LinkedList;

/**
 * @ClassName PacketQueueManager
 * @Description Manage packet queue
 * @Author wangmeng
 * @Date 2021/10/19
 */
public class PacketQueueManager {

    private static PacketQueueManager instance;

    private LinkedList<Packet> packetQueue;

    private boolean isQueueLimited;

    private long queueLength;

    private PacketQueueManager(boolean isQueueLimited, long queueLength) {
        this.isQueueLimited = isQueueLimited;
        this.queueLength = queueLength;
        packetQueue = new LinkedList<>();
    }

    /**
     * Singleton design mode
     *
     * return only one instance
     * @param isQueueLimited
     * @param queueLength
     * @return
     */
    public static synchronized PacketQueueManager getInstance(boolean isQueueLimited, long queueLength) {
        if (instance == null) {
            instance = new PacketQueueManager(isQueueLimited, queueLength);
        }
        return instance;
    }

    /**
     * Insert a packet to the packet queue
     *
     * different situations according to different condition
     * @param packet
     * @return
     */
    public boolean insert(Packet packet) {
        if (isQueueLimited) {
            long currQueueSize = size();
            if (currQueueSize <= queueLength) {
                packetQueue.offerLast(packet);
                return true;
            }
        } else {
            packetQueue.offerLast(packet);
            return true;
        }
        return false;
    }

    /**
     * Remove and return the first packet in the packet queue
     * @return
     */
    public Packet poll() {
        return packetQueue.removeFirst();
    }

    /**
     * Return the first packet in the packet queue
     * @return
     */
    public Packet element() {
        return packetQueue.getFirst();
    }

    /**
     * Judge is the packet queue is empty
     * @return
     */
    public boolean isEmpty() {
        return packetQueue.isEmpty();
    }

    /**
     * Return the packet size
     * @return
     */
    public long size() {
        return packetQueue.size();
    }
}
