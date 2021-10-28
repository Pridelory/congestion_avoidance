package producer;

import entity.Packet;

/**
 * @ClassName PacketProducer
 * @Description TODO
 * @Author wangmeng
 * @Date 2021/10/19
 */
public class PacketProducer {

    private double lambda;

    public PacketProducer(double lambda) {
        this.lambda = lambda;
    }

    public Packet getPacket() {

        Packet packet = new Packet(23, 23);
        return packet;
    }
}
