package util;

import java.util.Random;

/**
 * @ClassName Generator
 * @Description TODO
 * @Author wangmeng
 * @Date 2021/10/20
 */
public class Generator {

    public static double generateInterArrival(double lambda, Random random) {
        return -1 / lambda * Math.log(1 - random.nextDouble());
    }

    public static double generatePacketLength(double lambda, Random random) {
        return -1 / lambda * Math.log(1 - random.nextDouble());
    }
}
