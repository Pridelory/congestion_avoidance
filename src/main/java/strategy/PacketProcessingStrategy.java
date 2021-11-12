package strategy;

import entity.PacketWrapper;

/**
 * @ClassName PacketProcessingStrategy
 * @Description drop packet by some strategies -- strategy design mode
 * @Author wangmeng
 * @Date 2021/11/9
 */
public interface PacketProcessingStrategy {

    public boolean packetProcessing(PacketWrapper packetWrapper);
}