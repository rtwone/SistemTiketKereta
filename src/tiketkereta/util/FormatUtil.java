package tiketkereta.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class FormatUtil {

    private FormatUtil() {
    }

    public static String formatRupiah(long nominal) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("id", "ID"));
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat formatter = new DecimalFormat("#,###", symbols);
        return "Rp " + formatter.format(nominal);
    }

    public static long bulatkanTarif(double nilai, int pembulatan) {
        int unit = pembulatan > 0 ? pembulatan : 1000;
        return Math.round(nilai / (double) unit) * unit;
    }

    public static String waktuSekarang() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date());
    }

    public static boolean kosong(String teks) {
        return teks == null || teks.trim().isEmpty();
    }

    public static String rapi(String teks) {
        return teks == null ? "" : teks.trim();
    }
}
