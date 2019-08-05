package optifine;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils
{
    public static int getAverage(int[] vals)
    {
        if (vals.length <= 0)
        {
            return 0;
        }
        else
        {
            int sum = 0;
            int avg;

            for (avg = 0; avg < vals.length; ++avg)
            {
                int val = vals[avg];
                sum += val;
            }

            avg = sum / vals.length;
            return avg;
        }
    }
    public static int round(double value, int places)
    {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.intValue();
    }
}
