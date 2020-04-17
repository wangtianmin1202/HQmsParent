package com.hand.hqm.hqm_utils;
/**
 *  求t值
 * @author jhb
 *
 */
public class Tinvs {
	public static final double DoublePrecision = Math.pow(2, -53);
    public static final double PositiveDoublePrecision = 2 * DoublePrecision;
    public static final double DefaultDoubleAccuracy = DoublePrecision * 10;
    final static int GammaN = 10;
    final static double GammaR = 10.900511;
    static double[] GammaDk =
    {
        2.48574089138753565546e-5,
        1.05142378581721974210,
        -3.45687097222016235469,
        4.51227709466894823700,
        -2.98285225323576655721,
        1.05639711577126713077,
        -1.95428773191645869583e-1,
        1.70970543404441224307e-2,
        -5.71926117404305781283e-4,
        4.63399473359905636708e-6,
        -2.71994908488607703910e-9
    };
    public final static double LogTwoSqrtEOverPi = 0.6207822376352452223455184457816472122518527279025978;
    public final static double LnPi = 1.1447298858494001741434273513530587116472948129153d;
    public final static double  Increment = 4.94065645841247E-324;
    public static Double Tinv(Double probability, Double degreesFreedom)
    {
        return -InvCDF(0d, 1d, degreesFreedom, probability / 2);
    }
    public static boolean AlmostEqualNormRelative(double a, double b, double diff, double maximumError)
    {
        // If A or B are infinity (positive or negative) then
        // only return true if they are exactly equal to each other -
        // that is, if they are both infinities of the same sign.
        if (Double.isInfinite(a) || Double.isInfinite(b))
        {
            return a == b;
        }

        // If A or B are a NAN, return false. NANs are equal to nothing,
        // not even themselves.
        if (Double.isNaN(a) || Double.isNaN(b))
        {
            return false;
        }

        // If one is almost zero, fall back to absolute equality
        if (Math.abs(a) < DoublePrecision || Math.abs(b) < DoublePrecision)
        {
            return Math.abs(diff) < maximumError;
        }

        if ((a == 0 & Math.abs(b) < maximumError) || (b == 0 & Math.abs(a) < maximumError))
        {
            return true;
        }

        return Math.abs(diff) < maximumError * Math.max(Math.abs(a), Math.abs(b));
    }
    public static boolean AlmostEqualRelative(double a, double b)
    {
        return AlmostEqualNormRelative(a, b, a - b, DefaultDoubleAccuracy);
    }
    /// <summary>Helper method useful for preventing rounding errors.</summary>
    /// <returns>a*sign(b)</returns>
    static double Sign(double a, double b)
    {
        return b >= 0 ? (a >= 0 ? a : -a) : (a >= 0 ? -a : a);
    }
    public static double GammaLn(double z)
    {
        if (z < 0.5)
        {
            double s = GammaDk[0];
            for (int i = 1; i <= GammaN; i++)
            {
                s += GammaDk[i] / (i - z);
            }

            return LnPi
                   - Math.log(Math.sin(Math.PI * z))
                   - Math.log(s)
                   -LogTwoSqrtEOverPi
                   - ((0.5 - z) * Math.log((0.5 - z + GammaR) / Math.E));
        }
        else
        {
            double s = GammaDk[0];
            for (int i = 1; i <= GammaN; i++)
            {
                s += GammaDk[i] / (z + i - 1.0);
            }

            return Math.log(s)
                   + LogTwoSqrtEOverPi
                   + ((z - 0.5) * Math.log((z - 0.5 + GammaR) / Math.E));
        }
    }
    public static double BetaRegularized(double a, double b, double x)
    {
        if (a < 0.0)
        {
            return 0;
        }

        if (b < 0.0)
        {
            return 0;
        }

        if (x < 0.0 || x > 1.0)
        {
            return 0;
        }

        double bt = (x == 0.0 || x == 1.0)
            ? 0.0
            : Math.exp(GammaLn(a + b) - GammaLn(a) - GammaLn(b) + (a * Math.log(x)) + (b * Math.log(1.0 - x)));

        boolean symmetryTransformation = x >= (a + 1.0) / (a + b + 2.0);

        /* Continued fraction representation */
        double eps = DoublePrecision;
        double fpmin = Increment / eps;

        if (symmetryTransformation)
        {
            x = 1.0 - x;
            double swap = a;
            a = b;
            b = swap;
        }

        double qab = a + b;
        double qap = a + 1.0;
        double qam = a - 1.0;
        double c = 1.0;
        double d = 1.0 - (qab * x / qap);

        if (Math.abs(d) < fpmin)
        {
            d = fpmin;
        }

        d = 1.0 / d;
        double h = d;

        for (int m = 1, m2 = 2; m <= 50000; m++, m2 += 2)
        {
          double aa = m * (b - m) * x / ((qam + m2) * (a + m2));
            d = 1.0 + (aa * d);

            if (Math.abs(d) < fpmin)
            {
                d = fpmin;
            }

            c = 1.0 + (aa / c);
            if (Math.abs(c) < fpmin)
            {
                c = fpmin;
            }

            d = 1.0 / d;
            h *= d * c;
            aa = -(a + m) * (qab + m) * x / ((a + m2) * (qap + m2));
            d = 1.0 + (aa * d);

            if (Math.abs(d) < fpmin)
            {
                d = fpmin;
            }

            c = 1.0 + (aa / c);

            if (Math.abs(c) < fpmin)
            {
                c = fpmin;
            }

            d = 1.0 / d;
            double del = d * c;
            h *= del;

            if (Math.abs(del - 1.0) <= eps)
            {
                return symmetryTransformation ? 1.0 - (bt * h / a) : bt * h / a;
            }
        }

        return symmetryTransformation ? 1.0 - (bt * h / a) : bt * h / a;
    }
    public static double f(double location, double scale, double freedom, double p, double x)
    {
      double k = (x-location)/scale;
      double h = freedom / (freedom + (k*k));
      double ib = 0.5 * BetaRegularized(freedom / 2.0, 0.5, h);
      return x <= location ? ib - p : 1.0 - ib - p;
    }
    public static double TryFindRoot(double location, double scale, double freedom, double p, double lowerBound, double upperBound, double accuracy, int maxIterations )
    {
        double root;
         double fmin = f(location,scale,freedom,p,lowerBound);
        double fmax = f(location,scale,freedom,p,upperBound);
        double froot = fmax;
        double d = 0.0, e = 0.0;

        root = upperBound;
        double xMid = Double.NaN;

        // Root must be bracketed.
        if (Math.signum(fmin) == Math.signum(fmax))
        {
            return 0;
        }

        for (int i = 0; i <= maxIterations; i++)
        {
            // adjust bounds
            if (Math.signum(froot) == Math.signum(fmax))
            {
                upperBound = lowerBound;
                fmax = fmin;
                e = d = root - lowerBound;
            }

            if (Math.abs(fmax) < Math.abs(froot))
            {
                lowerBound = root;
                root = upperBound;
                upperBound = lowerBound;
                fmin = froot;
                froot = fmax;
                fmax = fmin;
            }

            // convergence check
            double xAcc1 = PositiveDoublePrecision * Math.abs(root) + 0.5 * accuracy;
            double xMidOld = xMid;
            xMid = (upperBound - root) / 2.0;

            if (Math.abs(xMid) <= xAcc1 || AlmostEqualNormRelative(froot,0, froot, accuracy))
            {
                return root;
            }

            if (xMid == xMidOld)
            {
                // accuracy not sufficient, but cannot be improved further
                return 0;
            }

            if (Math.abs(e) >= xAcc1 & Math.abs(fmin) > Math.abs(froot))
            {
                // Attempt inverse quadratic interpolation
                double s = froot / fmin;
                double p1;
                double q;
                if (AlmostEqualRelative(lowerBound,upperBound))
                {
                    p1 = 2.0 * xMid * s;
                    q = 1.0 - s;
                }
                else
                {
                    q = fmin / fmax;
                    double r = froot / fmax;
                    p1 = s * (2.0 * xMid * q * (q - r) - (root - lowerBound) * (r - 1.0));
                    q = (q - 1.0) * (r - 1.0) * (s - 1.0);
                }

                if (p1 > 0.0)
                {
                    // Check whether in bounds
                    q = -q;
                }

                p1 = Math.abs(p1);
                if (2.0 * p1 < Math.min(3.0 * xMid * q - Math.abs(xAcc1 * q), Math.abs(e * q)))
                {
                    // Accept interpolation
                    e = d;
                    d = p1 / q;
                }
                else
                {
                    // Interpolation failed, use bisection
                    d = xMid;
                    e = d;
                }
            }
            else
            {
                // Bounds decreasing too slowly, use bisection
                d = xMid;
                e = d;
            }

            lowerBound = root;
            fmin = froot;
            if (Math.abs(d) > xAcc1)
            {
                root += d;
            }
            else
            {
                root += Sign(xAcc1, xMid);
            }

            froot = f(location,scale,freedom,p,root);

        }

        return 0;
    }
    public static double FindRoot(double location, double scale, double freedom, double p, double lowerBound, double upperBound, double accuracy, int maxIterations)
    {
       return TryFindRoot(location,scale,freedom,p , lowerBound, upperBound, accuracy, maxIterations);
     }
    public static double InvCDF(double location, double scale, double freedom, double p)
    {
        if (scale <= 0.0 || freedom <= 0.0)
        {
            return 0;
        }


        if (p == 0.5d)
        {
            return location;
        }

        // TODO PERF: We must implement this explicitly instead of solving for CDF^-1
        return FindRoot( location,scale,freedom,p , -800, 800,1e-12,100);
    }
    public  static void main(String[] args)
    {
      double s = 1;
      System.out.println(Tinv(0.05d,20d));
    }
}
