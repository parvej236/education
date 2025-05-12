package bd.org.quantum.education.common;

import java.text.DecimalFormat;

public class NumberUtils {
    public static double generateDecimalNumber(double decimalVal, DecimalFormat df){
        return Double.parseDouble(df.format(decimalVal));
    }

    public static double getTwoDigitDecimalNumber(double decimalVal){
        return generateDecimalNumber(decimalVal, new DecimalFormat("#.##"));
    }

    public static double getFiveDigitDecimalNumber(double decimalVal){
        return generateDecimalNumber(decimalVal, new DecimalFormat("#.#####"));
    }
}