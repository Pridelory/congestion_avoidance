package strategy;

import entity.PacketWrapper;
import queue.PacketQueueManager;

/**
 * @ClassName DefaultPacketProcessingStrategy
 * @Description default packet processing strategy
 * @Author wangmeng
 * @Date 2021/11/9
 */
public class DefaultPacketProcessingStrategy implements PacketProcessingStrategy {

    @Override
    public boolean packetProcessing(PacketWrapper packetWrapper) {
        PacketQueueManager packetQueueManager = packetWrapper.getPacketQueueManager();
        return packetQueueManager.insert(packetWrapper.getPacket());
    }
}
