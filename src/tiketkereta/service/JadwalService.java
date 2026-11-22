package tiketkereta.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import tiketkereta.data.DataStore;
import tiketkereta.data.ModelFactory;
import tiketkereta.model.GerbongConfig;
import tiketkereta.model.Jadwal;
import tiketkereta.model.Kereta;
import tiketkereta.model.OpsiJadwal;
import tiketkereta.model.Stasiun;
import tiketkereta.model.StopJadwal;
import tiketkereta.util.FormatUtil;
import tiketkereta.util.ValidasiException;

public class JadwalService {

    private static final List<Integer> KAPASITAS_VALID = Arrays.asList(50, 64, 80, 90);

    private final DataStore dataStore;

    public JadwalService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public ArrayList<OpsiJadwal> cariJadwalCocok(String originCode, String destCode, int jumlahPenumpang,
            PemesananService pemesananService, TarifService tarifService) throws ValidasiException {
        if (FormatUtil.kosong(originCode) || FormatUtil.kosong(destCode)) {
            throw new ValidasiException("Stasiun asal dan stasiun tujuan wajib dipilih.");
        }
        if (originCode.equalsIgnoreCase(destCode)) {
            throw new ValidasiException("Stasiun asal dan stasiun tujuan tidak boleh sama.");
        }
        if (jumlahPenumpang < 1 || jumlahPenumpang > 4) {
            throw new ValidasiException("Jumlah penumpang harus antara 1 sampai 4 orang.");
        }
        Stasiun stasiunAsal = dataStore.cariStasiunByCode(originCode);
        Stasiun stasiunTujuan = dataStore.cariStasiunByCode(destCode);
        if (stasiunAsal == null || stasiunTujuan == null) {
            throw new ValidasiException("Data stasiun tidak ditemukan.");
        }

        ArrayList<OpsiJadwal> hasil = new ArrayList<OpsiJadwal>();
        for (Jadwal jadwal : dataStore.getJadwalList()) {
            int idxAsal = jadwal.indexStop(originCode);
            int idxTujuan = jadwal.indexStop(destCode);
            if (idxAsal < 0 || idxTujuan <= idxAsal) {
                continue;
            }
            Kereta kereta = dataStore.cariKeretaByNumber(jadwal.getTrainNumber());
            if (kereta == null) {
                continue;
            }
            StopJadwal stopAsal = jadwal.getStops().get(idxAsal);
            StopJadwal stopTujuan = jadwal.getStops().get(idxTujuan);
            String berangkat = stopAsal.getDeparture() != null ? stopAsal.getDeparture() : stopAsal.getArrival();
            String tiba = stopTujuan.getArrival() != null ? stopTujuan.getArrival() : stopTujuan.getDeparture();

            for (String kelas : kereta.getDaftarKelas()) {
                int sisa = pemesananService.hitungSisaKursi(jadwal, kereta, kelas);
                long tarif = tarifService.hitungTarifPerPenumpang(kereta, jadwal, stasiunAsal, stasiunTujuan, kelas);
                hasil.add(new OpsiJadwal(jadwal, kereta, kelas, stasiunAsal, stasiunTujuan,
                        berangkat, tiba, sisa, tarif));
            }
        }

        Collections.sort(hasil, new Comparator<OpsiJadwal>() {
            @Override
            public int compare(OpsiJadwal a, OpsiJadwal b) {
                String jamA = a.getJamBerangkat() == null ? "" : a.getJamBerangkat();
                String jamB = b.getJamBerangkat() == null ? "" : b.getJamBerangkat();
                int cmp = jamA.compareTo(jamB);
                if (cmp != 0) {
                    return cmp;
                }
                return Long.compare(a.getTarifPerPenumpang(), b.getTarifPerPenumpang());
            }
        });
        return hasil;
    }

    public void tambahStasiun(Stasiun baru) throws ValidasiException {
        validasiStasiun(baru);
        if (dataStore.cariStasiunByCode(baru.getCode()) != null) {
            throw new ValidasiException("Kode stasiun '" + baru.getCode() + "' sudah terdaftar.");
        }
        for (Stasiun s : dataStore.getStasiunList()) {
            if (s.getId() == baru.getId()) {
                throw new ValidasiException("ID stasiun " + baru.getId() + " sudah digunakan.");
            }
        }
        dataStore.getStasiunList().add(baru);
        dataStore.save();
    }

    public void ubahStasiun(String kodeLama, Stasiun update) throws ValidasiException {
        validasiStasiun(update);
        Stasiun target = dataStore.cariStasiunByCode(kodeLama);
        if (target == null) {
            throw new ValidasiException("Stasiun yang akan diubah tidak ditemukan.");
        }
        for (Stasiun s : dataStore.getStasiunList()) {
            if (s != target && s.getCode().equalsIgnoreCase(update.getCode())) {
                throw new ValidasiException("Kode stasiun '" + update.getCode() + "' sudah dipakai stasiun lain.");
            }
            if (s != target && s.getId() == update.getId()) {
                throw new ValidasiException("ID stasiun " + update.getId() + " sudah dipakai stasiun lain.");
            }
        }
        target.setId(update.getId());
        target.setCode(update.getCode().toUpperCase());
        target.setName(update.getName());
        target.setDistrict(update.getDistrict());
        target.setRegency(update.getRegency());
        target.setKelasStasiun(update.getKelasStasiun());
        target.setDistanceKm(update.getDistanceKm());
        dataStore.save();
    }

    public void hapusStasiun(String kode) throws ValidasiException {
        Stasiun target = dataStore.cariStasiunByCode(kode);
        if (target == null) {
            throw new ValidasiException("Stasiun tidak ditemukan.");
        }
        if (dataStore.getStasiunList().size() <= 2) {
            throw new ValidasiException("Minimal harus tersisa 2 stasiun di sistem.");
        }
        for (Jadwal j : dataStore.getJadwalList()) {
            if (j.indexStop(kode) >= 0) {
                throw new ValidasiException("Stasiun " + kode + " masih digunakan pada jadwal " + j.getScheduleId() + ".");
            }
        }
        dataStore.getStasiunList().remove(target);
        dataStore.save();
    }

    private void validasiStasiun(Stasiun s) throws ValidasiException {
        if (s.getId() <= 0) {
            throw new ValidasiException("ID stasiun harus lebih dari 0.");
        }
        if (FormatUtil.kosong(s.getCode()) || s.getCode().trim().length() < 2) {
            throw new ValidasiException("Kode stasiun minimal 2 huruf.");
        }
        if (FormatUtil.kosong(s.getName())) {
            throw new ValidasiException("Nama stasiun wajib diisi.");
        }
        if (FormatUtil.kosong(s.getDistrict()) || FormatUtil.kosong(s.getRegency())) {
            throw new ValidasiException("Kecamatan dan kabupaten wajib diisi.");
        }
        if (s.getDistanceKm() < 0) {
            throw new ValidasiException("Jarak km tidak boleh negatif.");
        }
    }

    public void tambahKereta(String serviceType, String trainNumber, String name,
            String originCode, String destinationCode, int baseFare,
            String konfigurasiGerbong) throws ValidasiException {
        ArrayList<GerbongConfig> gerbong = parseKonfigurasiGerbong(konfigurasiGerbong);
        validasiKereta(trainNumber, name, originCode, destinationCode, baseFare);
        if (dataStore.cariKeretaByNumber(trainNumber) != null) {
            throw new ValidasiException("Nomor kereta '" + trainNumber + "' sudah terdaftar.");
        }
        Kereta baru = ModelFactory.createKereta(serviceType, trainNumber.trim(), name.trim(),
                gerbong, originCode.trim().toUpperCase(), destinationCode.trim().toUpperCase(), baseFare);
        dataStore.getKeretaList().add(baru);
        dataStore.save();
    }

    public void ubahKereta(String nomorLama, String serviceType, String trainNumber, String name,
            String originCode, String destinationCode, int baseFare,
            String konfigurasiGerbong) throws ValidasiException {
        ArrayList<GerbongConfig> gerbong = parseKonfigurasiGerbong(konfigurasiGerbong);
        validasiKereta(trainNumber, name, originCode, destinationCode, baseFare);
        int idx = -1;
        for (int i = 0; i < dataStore.getKeretaList().size(); i++) {
            if (dataStore.getKeretaList().get(i).getTrainNumber().equalsIgnoreCase(nomorLama)) {
                idx = i;
                break;
            }
        }
        if (idx < 0) {
            throw new ValidasiException("Kereta yang akan diubah tidak ditemukan.");
        }
        for (int i = 0; i < dataStore.getKeretaList().size(); i++) {
            if (i != idx && dataStore.getKeretaList().get(i).getTrainNumber().equalsIgnoreCase(trainNumber.trim())) {
                throw new ValidasiException("Nomor kereta '" + trainNumber + "' sudah digunakan kereta lain.");
            }
        }
        Kereta pengganti = ModelFactory.createKereta(serviceType, trainNumber.trim(), name.trim(),
                gerbong, originCode.trim().toUpperCase(), destinationCode.trim().toUpperCase(), baseFare);
        dataStore.getKeretaList().set(idx, pengganti);
        dataStore.save();
    }

    public void hapusKereta(String trainNumber) throws ValidasiException {
        Kereta target = dataStore.cariKeretaByNumber(trainNumber);
        if (target == null) {
            throw new ValidasiException("Kereta tidak ditemukan.");
        }
        for (Jadwal j : dataStore.getJadwalList()) {
            if (j.getTrainNumber().equalsIgnoreCase(trainNumber)) {
                throw new ValidasiException("Kereta " + trainNumber + " masih dipakai oleh jadwal " + j.getScheduleId() + ".");
            }
        }
        dataStore.getKeretaList().remove(target);
        dataStore.save();
    }

    private void validasiKereta(String trainNumber, String name, String originCode,
            String destinationCode, int baseFare) throws ValidasiException {
        if (FormatUtil.kosong(trainNumber)) {
            throw new ValidasiException("Nomor kereta wajib diisi (misal: KA 111).");
        }
        if (FormatUtil.kosong(name)) {
            throw new ValidasiException("Nama kereta wajib diisi.");
        }
        if (dataStore.cariStasiunByCode(originCode) == null || dataStore.cariStasiunByCode(destinationCode) == null) {
            throw new ValidasiException("Stasiun asal dan tujuan harus merupakan kode stasiun yang valid.");
        }
        if (originCode.equalsIgnoreCase(destinationCode)) {
            throw new ValidasiException("Stasiun asal dan tujuan kereta tidak boleh sama.");
        }
        if (baseFare <= 0) {
            throw new ValidasiException("Tarif dasar harus lebih dari 0.");
        }
    }

    public ArrayList<GerbongConfig> parseKonfigurasiGerbong(String teks) throws ValidasiException {
        if (FormatUtil.kosong(teks)) {
            throw new ValidasiException("Konfigurasi gerbong wajib diisi (contoh: Eksekutif:3:50, Bisnis:3:64).");
        }
        ArrayList<GerbongConfig> list = new ArrayList<GerbongConfig>();
        String[] bagian = teks.split(",");
        for (String item : bagian) {
            String bersih = item.trim();
            if (bersih.isEmpty()) {
                continue;
            }
            String[] kolom = bersih.split(":");
            if (kolom.length != 3) {
                throw new ValidasiException("Format gerbong harus Kelas:Jumlah:Kursi (contoh: Eksekutif:3:50).");
            }
            String kelas = kolom[0].trim();
            if (FormatUtil.kosong(kelas)) {
                throw new ValidasiException("Nama kelas gerbong tidak boleh kosong.");
            }
            int jumlah;
            int kursi;
            try {
                jumlah = Integer.parseInt(kolom[1].trim());
                kursi = Integer.parseInt(kolom[2].trim());
            } catch (NumberFormatException ex) {
                throw new ValidasiException("Jumlah gerbong dan kursi per gerbong harus berupa angka.");
            }
            if (jumlah <= 0) {
                throw new ValidasiException("Jumlah gerbong minimal 1.");
            }
            if (!KAPASITAS_VALID.contains(kursi)) {
                throw new ValidasiException("Kapasitas kursi per gerbong harus 50, 64, 80, atau 90.");
            }
            list.add(new GerbongConfig(kelas, jumlah, kursi));
        }
        if (list.isEmpty()) {
            throw new ValidasiException("Minimal harus ada 1 konfigurasi gerbong.");
        }
        return list;
    }

    public void tambahJadwal(String scheduleId, String trainNumber, String direction,
            String originCode, String destinationCode, String stopsText) throws ValidasiException {
        ArrayList<StopJadwal> stops = parseStops(stopsText);
        validasiJadwal(scheduleId, trainNumber, originCode, destinationCode, stops);
        if (dataStore.cariJadwalById(scheduleId) != null) {
            throw new ValidasiException("ID jadwal '" + scheduleId + "' sudah terdaftar.");
        }
        Jadwal baru = new Jadwal(scheduleId.trim(), trainNumber.trim(), direction.trim(),
                originCode.trim().toUpperCase(), destinationCode.trim().toUpperCase(), stops);
        dataStore.getJadwalList().add(baru);
        dataStore.save();
    }

    public void ubahJadwal(String idLama, String scheduleId, String trainNumber, String direction,
            String originCode, String destinationCode, String stopsText) throws ValidasiException {
        ArrayList<StopJadwal> stops = parseStops(stopsText);
        validasiJadwal(scheduleId, trainNumber, originCode, destinationCode, stops);
        Jadwal target = dataStore.cariJadwalById(idLama);
        if (target == null) {
            throw new ValidasiException("Jadwal yang akan diubah tidak ditemukan.");
        }
        for (Jadwal j : dataStore.getJadwalList()) {
            if (j != target && j.getScheduleId().equalsIgnoreCase(scheduleId.trim())) {
                throw new ValidasiException("ID jadwal '" + scheduleId + "' sudah dipakai jadwal lain.");
            }
        }
        target.setScheduleId(scheduleId.trim());
        target.setTrainNumber(trainNumber.trim());
        target.setDirection(direction.trim());
        target.setOriginCode(originCode.trim().toUpperCase());
        target.setDestinationCode(destinationCode.trim().toUpperCase());
        target.setStops(stops);
        dataStore.save();
    }

    public void hapusJadwal(String scheduleId) throws ValidasiException {
        Jadwal target = dataStore.cariJadwalById(scheduleId);
        if (target == null) {
            throw new ValidasiException("Jadwal tidak ditemukan.");
        }
        dataStore.getJadwalList().remove(target);
        dataStore.save();
    }

    private void validasiJadwal(String scheduleId, String trainNumber, String originCode,
            String destinationCode, ArrayList<StopJadwal> stops) throws ValidasiException {
        if (FormatUtil.kosong(scheduleId)) {
            throw new ValidasiException("ID jadwal wajib diisi (misal: JD-101-05).");
        }
        if (dataStore.cariKeretaByNumber(trainNumber) == null) {
            throw new ValidasiException("Kereta '" + trainNumber + "' tidak ditemukan.");
        }
        if (dataStore.cariStasiunByCode(originCode) == null || dataStore.cariStasiunByCode(destinationCode) == null) {
            throw new ValidasiException("Stasiun asal dan tujuan jadwal harus valid.");
        }
        if (originCode.equalsIgnoreCase(destinationCode)) {
            throw new ValidasiException("Stasiun asal dan tujuan jadwal tidak boleh sama.");
        }
        if (stops.size() < 2) {
            throw new ValidasiException("Jadwal wajib memiliki minimal 2 pemberhentian stasiun.");
        }
    }

    public ArrayList<StopJadwal> parseStops(String teks) throws ValidasiException {
        if (FormatUtil.kosong(teks)) {
            throw new ValidasiException("Daftar pemberhentian wajib diisi (format: KODE|TIBA|BERANGKAT per baris).");
        }
        ArrayList<StopJadwal> stops = new ArrayList<StopJadwal>();
        String[] baris = teks.split("\\r?\\n");
        for (String b : baris) {
            String bersih = b.trim();
            if (bersih.isEmpty()) {
                continue;
            }
            String[] kolom = bersih.split("\\|");
            if (kolom.length != 3) {
                throw new ValidasiException("Format pemberhentian harus KODE|TIBA|BERANGKAT (gunakan '-' jika kosong).");
            }
            String kode = kolom[0].trim().toUpperCase();
            if (dataStore.cariStasiunByCode(kode) == null) {
                throw new ValidasiException("Kode stasiun '" + kode + "' pada daftar pemberhentian tidak ditemukan.");
            }
            String tiba = "-".equals(kolom[1].trim()) || kolom[1].trim().isEmpty() ? null : kolom[1].trim();
            String berangkat = "-".equals(kolom[2].trim()) || kolom[2].trim().isEmpty() ? null : kolom[2].trim();
            stops.add(new StopJadwal(kode, tiba, berangkat));
        }
        return stops;
    }

    public String formatStops(ArrayList<StopJadwal> stops) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stops.size(); i++) {
            StopJadwal s = stops.get(i);
            if (i > 0) {
                sb.append("\n");
            }
            sb.append(s.getStationCode())
                    .append("|")
                    .append(s.getArrival() == null ? "-" : s.getArrival())
                    .append("|")
                    .append(s.getDeparture() == null ? "-" : s.getDeparture());
        }
        return sb.toString();
    }
}
