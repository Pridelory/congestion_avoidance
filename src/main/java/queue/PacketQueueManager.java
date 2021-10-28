package queue;

import entity.Packet;

import java.util.LinkedList;

/**
 * @ClassName PacketQueueManager
 * @Description TODO
 * @Author wangmeng
 * @Date 2021/10/19
 */
public class PacketQueueManager {

    private static PacketQueueManager instance;

    private LinkedList<Packet> packetQueue;

    private PacketQueueManager() {
        packetQueue = new LinkedList<>();
    }

    public static synchronized PacketQueueManager getInstance() {
        if (instance == null) {
            instance = new PacketQueueManager();
        }
        return instance;
    }

    public void insert(Packet packet) {
        packetQueue.offerLast(packet);
    }

    public Packet poll() {
        return packetQueue.removeFirst();
    }

    public Packet element() {
        return packetQueue.getFirst();
    }

    public boolean isEmpty() {
        return packetQueue.isEmpty();
    }

    public long size() {
        return packetQueue.size();
    }
}
