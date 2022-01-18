package application.util.format;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class DecimalDisplayFormat extends DecimalFormat {
    public DecimalDisplayFormat(int decimalPlaces) {
        super(generatePattern(decimalPlaces), new DecimalFormatSymbols(Locale.ENGLISH));
    }

    private static String generatePattern(int decimalPlaces) {
        StringBuilder sb = new StringBuilder();
        sb.append("#0.");
        for (int i = 0; i < decimalPlaces - 1; i++) {
            sb.append('0');
        }
        sb.append('#');
        return sb.toString();
    }
}
