package bd.org.quantum.education.common;

public abstract class NumberConverter {

    private static final String TAKA = "";
    private static final String PAISA = "Paisa";

    private static final double UNITS = 1;
    private static final double TENS = 10 * UNITS;
    private static final double HUNDREDS = 10 * TENS;
    private static final double THOUSANDS = 10 * HUNDREDS;
    private static final double TENTHOUSANDS = 10 * THOUSANDS;
    private static final double LAKHS = 10 * TENTHOUSANDS;
    private static final double TENLAKHS = 10 * LAKHS;
    private static final double CRORES = 10 * TENLAKHS;
    private static final double TENCRORES = 10 * CRORES;
    private static final double HUNDREDCRORES = 10 * TENCRORES;
    private static final double THOUSANDCRORES = 10 * HUNDREDCRORES;
    private static final double LIMITEXCEED = -1;

    private static double getPlace(String number) {
        switch (number.length()) {
            case 1:
                return UNITS;
            case 2:
                return TENS;
            case 3:
                return HUNDREDS;
            case 4:
                return THOUSANDS;
            case 5:
                return TENTHOUSANDS;
            case 6:
                return LAKHS;
            case 7:
                return TENLAKHS;
            case 8:
                return CRORES;
            case 9:
                return TENCRORES;
            case 10:
                return HUNDREDCRORES;
            case 11:
                return THOUSANDCRORES;
        }
        return LIMITEXCEED;
    }

    private static String getWord(int number) {
        switch (number) {
            case 1:
                return "One";
            case 2:
                return "Two";
            case 3:
                return "Three";
            case 4:
                return "Four";
            case 5:
                return "Five";
            case 6:
                return "Six";
            case 7:
                return "Seven";
            case 8:
                return "Eight";
            case 9:
                return "Nine";
            case 0:
                return "Zero";
            case 10:
                return "Ten";
            case 11:
                return "Eleven";
            case 12:
                return "Twelve";
            case 13:
                return "Thirteen";
            case 14:
                return "Fourteen";
            case 15:
                return "Fifteen";
            case 16:
                return "Sixteen";
            case 17:
                return "Seventeen";
            case 18:
                return "Eighteen";
            case 19:
                return "Nineteen";
            case 20:
                return "Twenty";
            case 30:
                return "Thirty";
            case 40:
                return "Forty";
            case 50:
                return "Fifty";
            case 60:
                return "Sixty";
            case 70:
                return "Seventy";
            case 80:
                return "Eighty";
            case 90:
                return "Ninety";
            case 100:
                return "Hundred";
        }
        return "";
    }

    private static String cleanNumber(String number) {
        number = number.replaceAll(" ", "");
        number = number.replace(',', ' ').replaceAll(" ", "");
        while (number.startsWith("0")) {
            number = number.replaceFirst("0", "");
        }
        return number;
    }

    public static String convertNumberWithDecimal(String number) {
        String returnValue = "";
        number = cleanNumber(number);
        String[] a = number.split("\\.");
        returnValue += convertNumber(a[0]) + " " + TAKA;
        if (a.length > 1 && cleanNumber(a[1]).length() > 0) {
            returnValue += " " + convertNumber(a[1]) + " " + PAISA;
        }
        return returnValue;
    }

    private static String convertNumber(String number) {

        double num;
        try {
            num = Double.parseDouble(number);
        } catch (Exception e) {
            return "Invalid Number Sent to Convert";
        }

        String returnValue = "";
        while (num > 0) {
            number = "" + (int) num;
            double place = getPlace(number);
            if (place == LIMITEXCEED) {
                return "In Words Conversion Error ";
            } else if (place == TENS || place == TENTHOUSANDS ||
                    place == TENLAKHS || place == TENCRORES ||
                    place == HUNDREDCRORES || place == THOUSANDCRORES) {
                int subNum = Integer.parseInt(number.charAt(0) + "" + number.charAt(1));

                if (place > TENCRORES) {
                    subNum = Integer.parseInt(number.charAt(0) + "" + number.charAt(1) + "" + number.charAt(2));
                }

                if (subNum > 99) {
                    returnValue += convertNumber(Integer.toString(subNum));
                } else if (subNum >= 21 && (subNum % 10) != 0) {
                    returnValue += getWord(Integer.parseInt("" + number.charAt(0)) * 10) + " " + getWord(subNum % 10);
                } else {
                    returnValue += getWord(subNum);
                }

                if (place == TENS) {
                    num = 0;
                } else if (place == TENTHOUSANDS) {
                    num -= subNum * THOUSANDS;
                    returnValue += " Thousand ";
                } else if (place == TENLAKHS) {
                    num -= subNum * LAKHS;
                    returnValue += " Lac ";
                } else if (place == TENCRORES) {
                    num -= subNum * CRORES;
                    returnValue += " Crore ";
                } else if (place == HUNDREDCRORES) {
                    num -= subNum * CRORES;
                    returnValue += " Crore ";
                } else if (place == THOUSANDCRORES) {
                    num -= subNum * CRORES;
                    returnValue += " Crore ";
                }
            } else {
                int subNum = Integer.parseInt("" + number.charAt(0));

                returnValue += getWord(subNum);
                if (place == UNITS) {
                    num = 0;
                } else if (place == HUNDREDS) {
                    num -= subNum * HUNDREDS;
                    returnValue += " Hundred ";
                } else if (place == THOUSANDS) {
                    num -= subNum * THOUSANDS;
                    returnValue += " Thousand ";
                } else if (place == LAKHS) {
                    num -= subNum * LAKHS;
                    returnValue += " Lac ";
                } else if (place == CRORES) {
                    num -= subNum * CRORES;
                    returnValue += " Crore ";
                }
            }
        }
        return returnValue;
    }
}