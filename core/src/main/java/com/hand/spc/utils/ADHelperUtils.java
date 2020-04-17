package com.hand.spc.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.alibaba.fastjson.JSONArray;

/**
 * description
 *
 * @author 60201 2019/08/26 15:30
 */
public abstract class ADHelperUtils {

    public static final List<Double> convertedY;

    static {
        Double[] convertedYData = {0.01, 0.05, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 0.95, 0.99};
        convertedY = new ArrayList(Arrays.asList(convertedYData));
    }
    /**
     * @Author han.zhang
     * @Description TODO 查询 右上角 P值
     * @Date 18:01 2019/9/5
     * @Param [stats]
     */
    public static BigDecimal getPOfAD(List<BigDecimal> stats) {

        double AD2a = getAD(stats).doubleValue();
        double P;
        if (AD2a >= 0.00 && AD2a < 0.200) {
            P = 1 - Math.exp(-13.436 + 101.14 * AD2a - 223.73 * AD2a * AD2a);
        } else if (AD2a >= 0.200 && AD2a < 0.340) {
            P = 1 - Math.exp(-8.318 + 42.796 * AD2a - 59.938 * AD2a * AD2a);
        } else if (AD2a >= 0.340 && AD2a < 0.600) {
            P = Math.exp(0.9177 - 4.279 * AD2a - 1.38 * AD2a * AD2a);
        } else {
            P = Math.exp(1.2937 - 5.709 * AD2a + 0.0186 * AD2a * AD2a);
        }
        return BigDecimal.valueOf(P).setScale(6, RoundingMode.HALF_UP);
    }
    /**
     * @Author han.zhang
     * @Description TODO 获得 A的平方
     * @Date 18:01 2019/9/5
     * @Param [stats]
     */
    public static BigDecimal getAD(List<BigDecimal> stats) {
        int size = stats.size();
        BigDecimal sizeBigDecimal = BigDecimal.valueOf(size);
        List<BigDecimal> standardization = getStanderdizationList(stats);

        BigDecimal adSumTemp = BigDecimal.ZERO;
        for (int index = 0; index < Objects.requireNonNull(standardization).size(); index++) {
            BigDecimal statMilderValue = standardization.get(index);
            BigDecimal statLastValue = standardization.get(size - 1 - index);
            BigDecimal sz = BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(index).add(BigDecimal.valueOf(1))).subtract(BigDecimal.valueOf(1)).divide(sizeBigDecimal, 4, RoundingMode.HALF_UP);
            double calculatedLog = Math.log(NormSDist(statMilderValue.doubleValue())) +
                    Math.log(1 - NormSDist(statLastValue.doubleValue()));

            BigDecimal szValue = sz.multiply(BigDecimal.valueOf(calculatedLog));
            adSumTemp = adSumTemp.add(szValue);
        }
        BigDecimal AD2 = sizeBigDecimal.multiply(BigDecimal.valueOf(-1)).subtract(adSumTemp);

        BigDecimal operator = BigDecimal.valueOf(1).add(BigDecimal.valueOf(0.75).divide(sizeBigDecimal, 8, RoundingMode.HALF_UP)).add(BigDecimal.valueOf(2.25).divide(sizeBigDecimal.pow(2), 8, RoundingMode.HALF_UP));
        return AD2.multiply(operator);
    }
    /**
     * @Author han.zhang
     * @Description TODO
     * @Date 18:02 2019/9/5
     * @Param [stats]
     */
    private static List<BigDecimal> getStanderdizationList(List<BigDecimal> stats) {
        if (CollectionUtils.isEmpty(stats)) {
            return null;
        }
        Collections.sort(stats);

        BigDecimal sampleMean = getAverage(stats);
        BigDecimal standardDeviationOver = getSigma(stats, sampleMean);

        List<BigDecimal> standardization = new ArrayList<>(stats.size());
        for (BigDecimal s : stats) {
            BigDecimal statSampleValueTemp = s.subtract(sampleMean);
            BigDecimal statSampleValue = statSampleValueTemp.divide(standardDeviationOver, 8, BigDecimal.ROUND_HALF_UP);
            standardization.add(statSampleValue);
        }
        return standardization;
    }

    public static BigDecimal getAverage(List<BigDecimal> stats) {
        int size = stats.size();
        BigDecimal sizeBigDecimal = BigDecimal.valueOf(size);

        BigDecimal sum = BigDecimal.ZERO;

        for (BigDecimal sampleValue : stats) {
            sum = sum.add(sampleValue);
        }
        //平均值
        return sum.divide(sizeBigDecimal, 8, RoundingMode.HALF_UP);
    }

    public static BigDecimal getSigma(List<BigDecimal> stats) {
        int size = stats.size();
        //平均数
        BigDecimal sampleMean = getAverage(stats);
        //标准差
        return getSigma(stats, sampleMean, size);
    }

    private static BigDecimal getSigma(List<BigDecimal> stats, BigDecimal sampleMean) {
        int size = stats.size();
        return getSigma(stats, sampleMean, size);
    }

    private static BigDecimal getSigma(List<BigDecimal> stats, BigDecimal sampleMean, int size) {
        BigDecimal standardDeviationOver;
        BigDecimal spowBigDecimal = BigDecimal.ZERO;
        for (BigDecimal s : stats) {
            spowBigDecimal = spowBigDecimal.add((s.subtract(sampleMean)).pow(2));
        }
        spowBigDecimal = spowBigDecimal.divide(BigDecimal.valueOf(size - 1L), 8, BigDecimal.ROUND_HALF_UP);
        double mathS = Math.sqrt(spowBigDecimal.doubleValue());
        standardDeviationOver = BigDecimal.valueOf(mathS).setScale(8, RoundingMode.HALF_UP);
        return standardDeviationOver;
    }

    public static List<BigDecimal> getEstimatedProbability(List<BigDecimal> stats) {
        int size = stats.size();
        List<BigDecimal> estimatedProbability = new ArrayList<>();
        BigDecimal denominator = BigDecimal.valueOf(size).add(BigDecimal.valueOf(0.4));
        for (int count = 1; count <= size; count++) {
            BigDecimal numerator = BigDecimal.valueOf(count).subtract(BigDecimal.valueOf(0.3));
            estimatedProbability.add(numerator.divide(denominator, 4, RoundingMode.HALF_UP));
        }
        return estimatedProbability;
    }

    public static List<BigDecimal> getNormSInv(List<BigDecimal> stats) {
        List<BigDecimal> estimatedProbability = getEstimatedProbability(stats);
        return estimatedProbability.stream().map(v ->
                BigDecimal.valueOf(normsinv(v.doubleValue()))
        ).collect(Collectors.toList());
    }

    private static double normsInv(double p, double mu, double sigma) {
        if (sigma == 0) {
            return mu;
        }

        double q, r, val;

        q = p - 0.5;

        if (Math.abs(q) <= .425) {/* 0.075 <= p <= 0.925 */
            r = .180625 - q * q;
            val =
                    q * (((((((r * 2509.0809287301226727 +
                            33430.575583588128105) * r + 67265.770927008700853) * r +
                            45921.953931549871457) * r + 13731.693765509461125) * r +
                            1971.5909503065514427) * r + 133.14166789178437745) * r +
                            3.387132872796366608)
                            / (((((((r * 5226.495278852854561 +
                            28729.085735721942674) * r + 39307.89580009271061) * r +
                            21213.794301586595867) * r + 5394.1960214247511077) * r +
                            687.1870074920579083) * r + 42.313330701600911252) * r + 1);
        } else { /* closer than 0.075 from {0,1} boundary */

            /* r = min(p, 1-p) < 0.075 */
            if (q > 0)
                r = 1 - p;
            else
                r = p;

            r = Math.sqrt(-Math.log(r));
            /* r = sqrt(-log(r))  <==>  min(p, 1-p) = exp( - r^2 ) */

            if (r <= 5) { /* <==> min(p,1-p) >= exp(-25) ~= 1.3888e-11 */
                r += -1.6;
                val = (((((((r * 7.7454501427834140764e-4 +
                        .0227238449892691845833) * r + .24178072517745061177) *
                        r + 1.27045825245236838258) * r +
                        3.64784832476320460504) * r + 5.7694972214606914055) *
                        r + 4.6303378461565452959) * r +
                        1.42343711074968357734)
                        / (((((((r *
                        1.05075007164441684324e-9 + 5.475938084995344946e-4) *
                        r + .0151986665636164571966) * r +
                        .14810397642748007459) * r + .68976733498510000455) *
                        r + 1.6763848301838038494) * r +
                        2.05319162663775882187) * r + 1);
            } else { /* very close to  0 or 1 */
                r += -5;
                val = (((((((r * 2.01033439929228813265e-7 +
                        2.71155556874348757815e-5) * r +
                        .0012426609473880784386) * r + .026532189526576123093) *
                        r + .29656057182850489123) * r +
                        1.7848265399172913358) * r + 5.4637849111641143699) *
                        r + 6.6579046435011037772)
                        / (((((((r *
                        2.04426310338993978564e-15 + 1.4215117583164458887e-7) *
                        r + 1.8463183175100546818e-5) * r +
                        7.868691311456132591e-4) * r + .0148753612908506148525)
                        * r + .13692988092273580531) * r +
                        .59983220655588793769) * r + 1);
            }

            if (q < 0.0) {
                val = -val;
            }
        }
        return mu + sigma * val;
    }

    private static double NormSDist(double z) {
        // this guards against overflow
        if (z > 6)
            return 1;
        if (z < -6)
            return 0;

        double gamma = 0.231641900,
                a1 = 0.319381530,
                a2 = -0.356563782,
                a3 = 1.781477973,
                a4 = -1.821255978,
                a5 = 1.330274429;

        double x = Math.abs(z);
        double t = 1 / (1 + gamma * x);


        double n = 1
                - (1 / (Math.sqrt(2 * Math.PI)) * Math.exp(-z * z / 2))
                * (a1 * t + a2 * Math.pow(t, 2) + a3 * Math.pow(t, 3) + a4
                * Math.pow(t, 4) + a5 * Math.pow(t, 5));
        if (z < 0)
            return 1.0 - n;

        return n;
    }

    private static double normsinv(double p) {
        double LOW = 0.02425;
        double HIGH = 0.97575;

        double[] a = {-3.969683028665376e+01, 2.209460984245205e+02,
                -2.759285104469687e+02, 1.383577518672690e+02,
                -3.066479806614716e+01, 2.506628277459239e+00};

        double[] b = {-5.447609879822406e+01, 1.615858368580409e+02,
                -1.556989798598866e+02, 6.680131188771972e+01,
                -1.328068155288572e+01};

        double[] c = {-7.784894002430293e-03, -3.223964580411365e-01,
                -2.400758277161838e+00, -2.549732539343734e+00,
                4.374664141464968e+00, 2.938163982698783e+00};

        double[] d = {7.784695709041462e-03, 3.224671290700398e-01,
                2.445134137142996e+00, 3.754408661907416e+00};

        double q, r;

        if (p < LOW) {
            q = Math.sqrt(-2 * Math.log(p));
            return (((((c[0] * q + c[1]) * q + c[2]) * q + c[3]) * q + c[4])
                    * q + c[5])
                    / ((((d[0] * q + d[1]) * q + d[2]) * q + d[3]) * q + 1);
        } else if (p > HIGH) {
            q = Math.sqrt(-2 * Math.log(1 - p));
            return -(((((c[0] * q + c[1]) * q + c[2]) * q + c[3]) * q + c[4])
                    * q + c[5])
                    / ((((d[0] * q + d[1]) * q + d[2]) * q + d[3]) * q + 1);
        } else {
            q = p - 0.5;
            r = q * q;
            return (((((a[0] * r + a[1]) * r + a[2]) * r + a[3]) * r + a[4])
                    * r + a[5])
                    * q
                    / (((((b[0] * r + b[1]) * r + b[2]) * r + b[3]) * r + b[4])
                    * r + 1);
        }
    }

    public static List<BigDecimal> getConvertedX(List<Double> doubles, double mu, double sigma) {
        List<BigDecimal> convertedX = new ArrayList<>();
        for (Double dou : doubles) {
            convertedX.add(BigDecimal.valueOf(normsInv(dou, mu, sigma)));

        }
        return convertedX;
    }

    public static List<BigDecimal> getConvertedZ(List<BigDecimal> convertedX, BigDecimal mu, BigDecimal sigma) {
        List<BigDecimal> convertedZ = new ArrayList<>();
        for (BigDecimal dou : convertedX) {
            convertedZ.add(dou.subtract(mu).divide(sigma, 4, RoundingMode.HALF_UP));
        }
        return convertedZ;
    }

    public static RegressionEquationVo getRegressionQquation(List<BigDecimal> dataX, List<BigDecimal> dataY, int count) {

        List<BigDecimal> subListX = dataX.subList(0, count);
        List<BigDecimal> subListY = dataY.subList(0, count);

        double sumX = subListX.stream().mapToDouble(BigDecimal::doubleValue).sum();
        double sumY = subListY.stream().mapToDouble(BigDecimal::doubleValue).sum();
        OptionalDouble optionalDoubleX = subListX.stream().mapToDouble(BigDecimal::doubleValue).average();
        double muX = 0;
        if (optionalDoubleX.isPresent())
        {
            muX = optionalDoubleX.getAsDouble();
        }
        OptionalDouble optionalDoubleY = subListY.stream().mapToDouble(BigDecimal::doubleValue).average();
        double muY = 0;
        if (optionalDoubleY.isPresent())
        {
            muY = optionalDoubleY.getAsDouble();
        }

        BigDecimal muXBD = BigDecimal.valueOf(muX);
        BigDecimal muYBD = BigDecimal.valueOf(muY);

        BigDecimal sumProduct = BigDecimal.ZERO;
        BigDecimal powX = BigDecimal.ZERO;

        BigDecimal varianceXX = BigDecimal.ZERO;
        BigDecimal varianceYY = BigDecimal.ZERO;
        BigDecimal varianceXY = BigDecimal.ZERO;
        for (int i = 0; i < count; i++) {
            sumProduct = sumProduct.add(subListX.get(i).multiply(subListY.get((i))));

            powX = powX.add(subListX.get(i).pow(2));

            BigDecimal dXValue = subListX.get(i).subtract(muXBD);
            BigDecimal dYValue = subListY.get(i).subtract(muYBD);

            varianceXX = varianceXX.add(dXValue.pow(2));
            varianceYY = varianceYY.add(dYValue.pow(2));

            varianceXY = varianceXY.add(dXValue.multiply(dYValue));
        }

        BigDecimal sumProductMultiCount = sumProduct.multiply(BigDecimal.valueOf(count));
        BigDecimal powXMultiCount = powX.multiply(BigDecimal.valueOf(count));
        BigDecimal powSumX = BigDecimal.valueOf(sumX).pow(2);

        BigDecimal numeratorOfLopeB = sumProductMultiCount.subtract(BigDecimal.valueOf(sumX).multiply((BigDecimal.valueOf(sumY))));
        BigDecimal denominatorOfLopeB = powXMultiCount.subtract(powSumX);

        BigDecimal lopeB = numeratorOfLopeB.divide(denominatorOfLopeB, 4, RoundingMode.HALF_UP);

        BigDecimal alpha = BigDecimal.valueOf(muY).subtract(lopeB.multiply(BigDecimal.valueOf(sumX).divide(BigDecimal.valueOf(count), 4, RoundingMode.HALF_UP))).setScale(4, RoundingMode.HALF_UP);

        BigDecimal rValue = varianceXY.divide(BigDecimal.valueOf(Math.sqrt(varianceXX.multiply(varianceYY).doubleValue())), 4, RoundingMode.HALF_UP);
        return  new RegressionEquationVo(count, lopeB, alpha, rValue, muXBD, muYBD);
    }

    public static void demoRun(){

        List<BigDecimal> data = new ArrayList<>();
        Double[] dou = {8.14,
                8.3,
                8.44,
                8.45,
                8.62,
                8.77,
                8.82,
                8.82,
                8.9,
                8.97,
                9.01,
                9.28,
                9.34,
                9.41,
                9.44,
                9.51,
                9.54,
                9.62,
                9.72,
                9.74,
                9.78,
                9.92,
                9.94,
                9.98,
                9.99,
                10.02,
                10.04,
                10.06,
                10.16,
                10.22,
                10.32,
                10.36,
                10.37,
                10.38,
                10.38,
                10.43,
                10.48,
                10.5,
                10.56,
                10.58,
                10.69,
                10.77,
                10.8,
                11.25,
                11.44,
                11.68,
                11.84,
                12.04,
                12.12,
                12.51};

        List<Double> dataDou = new ArrayList(Arrays.asList(dou));
        List<BigDecimal> bigDecimalList = dataDou.stream().map(v -> BigDecimal.valueOf(v)).collect(Collectors.toList());
        System.out.println("AD " +getAD(bigDecimalList).doubleValue());

        System.out.println("P of AD " + getPOfAD(bigDecimalList).doubleValue());
        System.out.println(JSONArray.toJSONString(getEstimatedProbability(bigDecimalList)));
        System.out.println(JSONArray.toJSONString(getNormSInv(bigDecimalList)));

        List<BigDecimal> convertedX = getConvertedX(convertedY, getAverage(bigDecimalList).doubleValue(), getSigma(bigDecimalList).doubleValue());
        System.out.println("X'");
        System.out.println(JSONArray.toJSONString(convertedX));

        System.out.println(JSONArray.toJSONString(getConvertedZ(convertedX, getAverage(bigDecimalList), getSigma(bigDecimalList))));

    }


}
