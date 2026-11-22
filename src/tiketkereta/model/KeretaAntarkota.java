package tiketkereta.model;

import java.util.ArrayList;

public class KeretaAntarkota extends Kereta {

    public KeretaAntarkota(String trainNumber, String name, ArrayList<GerbongConfig> carriages,
            String originCode, String destinationCode, int baseFare) {
        super(trainNumber, name, carriages, originCode, destinationCode, baseFare);
    }

    @Override
    public String getJenisLayanan() {
        return "Antarkota";
    }

    @Override
    public int getKecepatanKmh() {
        return 90;
    }

    @Override
    public long getTarifMinimum() {
        return 10000L;
    }

    @Override
    public String getInfoLayanan() {
        return "Layanan reguler penghubung kabupaten dan stasiun utama";
    }
}
