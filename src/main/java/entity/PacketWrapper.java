package entity;

import queue.PacketQueueManager;

/**
 * @ClassName PacketWrapper
 * @Description packet wrapper
 * @Author wangmeng
 * @Date 2021/11/9
 */
public class PacketWrapper {

    private double clock;

    private Packet packet;

    private PacketQueueManager packetQueueManager;

    private long maxQueueSize;

    private double idleStartTime;

    private double μ;

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public PacketQueueManager getPacketQueueManager() {
        return packetQueueManager;
    }

    public void setPacketQueueManager(PacketQueueManager packetQueueManager) {
        this.packetQueueManager = packetQueueManager;
    }

    public double getClock() {
        return clock;
    }

    public void setClock(double clock) {
        this.clock = clock;
    }

    public double getIdleStartTime() {
        return idleStartTime;
    }

    public void setIdleStartTime(double idleStartTime) {
        this.idleStartTime = idleStartTime;
    }

    public double getΜ() {
        return μ;
    }

    public void setΜ(double μ) {
        this.μ = μ;
    }

    public long getMaxQueueSize() {
        return maxQueueSize;
    }

    public void setMaxQueueSize(long maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
    }
}
