package tiketkereta.service;

import tiketkereta.data.DataStore;
import tiketkereta.model.Jadwal;
import tiketkereta.model.Kereta;
import tiketkereta.model.Stasiun;

public class TarifService {

    private final DataStore dataStore;

    public TarifService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public long hitungTarifPerPenumpang(Kereta kereta, Jadwal jadwal,
            Stasiun asal, Stasiun tujuan, String kelas) {
        Stasiun awalRute = dataStore.cariStasiunByCode(jadwal.getOriginCode());
        Stasiun akhirRute = dataStore.cariStasiunByCode(jadwal.getDestinationCode());

        int rentangRute = 1;
        if (awalRute != null && akhirRute != null) {
            rentangRute = Math.abs(awalRute.getId() - akhirRute.getId());
            if (rentangRute == 0) {
                rentangRute = 1;
            }
        }

        int rentangPerjalanan = Math.abs(tujuan.getId() - asal.getId());
        if (rentangPerjalanan == 0) {
            rentangPerjalanan = 1;
        }

        double rasioJarak = (double) rentangPerjalanan / (double) rentangRute;
        double pengaliKelas = dataStore.getPengaliKelas(kelas);
        return kereta.hitungTarif(rasioJarak, pengaliKelas, dataStore.getFareRounding());
    }
}
