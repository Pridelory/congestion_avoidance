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

    private PacketQueueManager() {
        packetQueue = new LinkedList<>();
    }

    /**
     * Singleton design mode
     *
     * return only one instance
     * @return
     */
    public static synchronized PacketQueueManager getInstance() {
        if (instance == null) {
            instance = new PacketQueueManager();
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
        return packetQueue.offerLast(packet);
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
