package util;

import java.util.List;

/**
 * @ClassName StatisticsUtil
 * @Description statistics util
 * @Author wangmeng
 * @Date 2021/10/30
 */
public class StatisticsUtil {

    /**
     * Count mean and variance
     * @param batchMeanTime
     * @return
     */
    public static double[] countMeanAndVariance(List<Double> batchMeanTime) {
        // count mean
        double totalMean = 0.0;
        double mean = 0.0;
        double totalVariance = 0.0;
        double variance = 0.0;
        double[] result = new double[2];
        for (int i = 0; i < batchMeanTime.size(); i++) {
            totalMean += batchMeanTime.get(i);
        }
        mean = totalMean / batchMeanTime.size();
        // count standard variance
        for (int i = 0; i < batchMeanTime.size(); i++) {
            totalVariance += Math.pow(mean - batchMeanTime.get(i), 2);
        }
        variance = Math.sqrt(totalVariance / (batchMeanTime.size() - 1));
        result[0] = mean;
        result[1] = variance;
        return result;
    }

    /**
     * Count 95% confidence interval
     * @param mean
     * @param variance
     * @param n
     * @return
     */
    public static double[] countConfidenceInterval(double mean, double variance, long n) {
        double[] ans = new double[2];
        ans[0] = mean - variance / Math.sqrt(n) * 1.984217;
        ans[1] = mean + variance / Math.sqrt(n) * 1.984217;
        return ans;
    }
}
