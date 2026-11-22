package tiketkereta.model;

import java.util.ArrayList;
import tiketkereta.util.FormatUtil;

public abstract class Kereta {

    private String trainNumber;
    private String name;
    private ArrayList<GerbongConfig> carriages;
    private String originCode;
    private String destinationCode;
    private int baseFare;

    protected Kereta(String trainNumber, String name, ArrayList<GerbongConfig> carriages,
            String originCode, String destinationCode, int baseFare) {
        this.trainNumber = trainNumber;
        this.name = name;
        this.carriages = carriages == null ? new ArrayList<GerbongConfig>() : new ArrayList<GerbongConfig>(carriages);
        this.originCode = originCode;
        this.destinationCode = destinationCode;
        this.baseFare = baseFare;
    }

    public abstract String getJenisLayanan();

    public abstract int getKecepatanKmh();

    public abstract long getTarifMinimum();

    public abstract String getInfoLayanan();

    public long hitungTarif(double rasioJarak, double pengaliKelas, int pembulatan) {
        double mentah = baseFare * rasioJarak * pengaliKelas;
        long bulat = FormatUtil.bulatkanTarif(mentah, pembulatan);
        return Math.max(getTarifMinimum(), bulat);
    }

    public ArrayList<String> getDaftarKelas() {
        ArrayList<String> result = new ArrayList<String>();
        for (GerbongConfig cfg : carriages) {
            if (!result.contains(cfg.getKelas())) {
                result.add(cfg.getKelas());
            }
        }
        return result;
    }

    public ArrayList<Integer> getNomorGerbongUntukKelas(String kelas) {
        ArrayList<Integer> nomor = new ArrayList<Integer>();
        int idx = 1;
        for (GerbongConfig cfg : carriages) {
            for (int i = 0; i < cfg.getCount(); i++) {
                if (cfg.getKelas().equalsIgnoreCase(kelas)) {
                    nomor.add(idx);
                }
                idx++;
            }
        }
        return nomor;
    }

    public int getKapasitasGerbong(int nomorGerbong) {
        int idx = 1;
        for (GerbongConfig cfg : carriages) {
            for (int i = 0; i < cfg.getCount(); i++) {
                if (idx == nomorGerbong) {
                    return cfg.getSeatsPerCarriage();
                }
                idx++;
            }
        }
        return 0;
    }

    public int getTotalKursiKelas(String kelas) {
        int total = 0;
        for (GerbongConfig cfg : carriages) {
            if (cfg.getKelas().equalsIgnoreCase(kelas)) {
                total += cfg.getTotalKursi();
            }
        }
        return total;
    }

    public int getTotalKursiSemua() {
        int total = 0;
        for (GerbongConfig cfg : carriages) {
            total += cfg.getTotalKursi();
        }
        return total;
    }

    public String getRingkasanGerbong() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < carriages.size(); i++) {
            GerbongConfig cfg = carriages.get(i);
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(cfg.getKelas()).append(":").append(cfg.getCount()).append(":").append(cfg.getSeatsPerCarriage());
        }
        return sb.toString();
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<GerbongConfig> getCarriages() {
        return carriages;
    }

    public void setCarriages(ArrayList<GerbongConfig> carriages) {
        this.carriages = carriages == null ? new ArrayList<GerbongConfig>() : new ArrayList<GerbongConfig>(carriages);
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getDestinationCode() {
        return destinationCode;
    }

    public void setDestinationCode(String destinationCode) {
        this.destinationCode = destinationCode;
    }

    public int getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(int baseFare) {
        this.baseFare = baseFare;
    }

    @Override
    public String toString() {
        return trainNumber + " - " + name + " (" + getJenisLayanan() + ")";
    }
}
