package util;

import java.util.Random;

/**
 * @ClassName Generator
 * @Description Distribution Generator
 * @Author wangmeng
 * @Date 2021/10/20
 */
public class Generator {

    /**
     * Generate inter arrival according to the Negative-Exponential distribution
     * @param lambda
     * @param random
     * @return
     */
    public static double generateInterArrival(double lambda, Random random) {
        return -1 / lambda * Math.log(1 - random.nextDouble());
    }

    /**
     * Generate packet length according to the Negative-Exponential distribution
     * @param lambda
     * @param random
     * @return
     */
    public static double generatePacketLength(double lambda, Random random) {
        return -1 / lambda * Math.log(1 - random.nextDouble());
    }

    /**
     * Generate Random Number Between 0 and 1
     * @return
     */
    public static double generateRandomNumberBetween0and1() {
        return new Random(2021).nextDouble();
    }
}
