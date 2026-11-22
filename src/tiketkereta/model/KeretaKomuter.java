package tiketkereta.model;

import java.util.ArrayList;

public class KeretaKomuter extends Kereta {

    public KeretaKomuter(String trainNumber, String name, ArrayList<GerbongConfig> carriages,
            String originCode, String destinationCode, int baseFare) {
        super(trainNumber, name, carriages, originCode, destinationCode, baseFare);
    }

    @Override
    public String getJenisLayanan() {
        return "Komuter";
    }

    @Override
    public int getKecepatanKmh() {
        return 60;
    }

    @Override
    public long getTarifMinimum() {
        return 3000L;
    }

    @Override
    public String getInfoLayanan() {
        return "Layanan komuter harian berhenti di seluruh stasiun jalur";
    }
}
