package tiketkereta.model;

import java.util.ArrayList;

public class KeretaEkspres extends Kereta {

    public KeretaEkspres(String trainNumber, String name, ArrayList<GerbongConfig> carriages,
            String originCode, String destinationCode, int baseFare) {
        super(trainNumber, name, carriages, originCode, destinationCode, baseFare);
    }

    @Override
    public String getJenisLayanan() {
        return "Ekspres";
    }

    @Override
    public int getKecepatanKmh() {
        return 110;
    }

    @Override
    public long getTarifMinimum() {
        return 25000L;
    }

    @Override
    public String getInfoLayanan() {
        return "Layanan cepat antar kota besar dengan pemberhentian minimal";
    }
}
