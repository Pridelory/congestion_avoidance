package entity;

/**
 * @ClassName Packet
 * @Description TODO
 * @Author wangmeng
 * @Date 2021/10/19
 */
public class Packet {

    /**
     * Packet length
     *
     * generated from the distribution
     */
    private double length;

    /**
     * Arrival time of this packet
     */
    private double arriveClock;

    public Packet(double length, double arriveClock) {
        this.length = length;
        this.arriveClock = arriveClock;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getArriveClock() {
        return arriveClock;
    }

    public void setArriveClock(double arriveClock) {
        this.arriveClock = arriveClock;
    }
}
