package tiketkereta.model;

import java.util.ArrayList;

public class KeretaLokal extends Kereta {

    public KeretaLokal(String trainNumber, String name, ArrayList<GerbongConfig> carriages,
            String originCode, String destinationCode, int baseFare) {
        super(trainNumber, name, carriages, originCode, destinationCode, baseFare);
    }

    @Override
    public String getJenisLayanan() {
        return "Lokal";
    }

    @Override
    public int getKecepatanKmh() {
        return 70;
    }

    @Override
    public long getTarifMinimum() {
        return 5000L;
    }

    @Override
    public String getInfoLayanan() {
        return "Layanan lokal berhenti di stasiun menengah hingga kecil";
    }
}
