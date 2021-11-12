package strategy;

import entity.PacketWrapper;
import queue.PacketQueueManager;

/**
 * @ClassName DropTailPacketProcessingStrategy
 * @Description Drop tail strategy
 * @Author wangmeng
 * @Date 2021/11/9
 */
public class DropTailPacketProcessingStrategy implements PacketProcessingStrategy{

    @Override
    public boolean packetProcessing(PacketWrapper packetWrapper) {
        long maxQueueSize = packetWrapper.getMaxQueueSize();
        PacketQueueManager packetQueueManager = packetWrapper.getPacketQueueManager();
        return packetQueueManager.size() < maxQueueSize && packetQueueManager.insert(packetWrapper.getPacket());
    }
}
