package tiketkereta.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import tiketkereta.data.DataStore;
import tiketkereta.model.Jadwal;
import tiketkereta.model.Kereta;
import tiketkereta.model.OpsiJadwal;
import tiketkereta.model.Pemesanan;
import tiketkereta.model.Penumpang;
import tiketkereta.model.User;
import tiketkereta.util.FormatUtil;
import tiketkereta.util.ValidasiException;

public class PemesananService {

    private final DataStore dataStore;

    public PemesananService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Set<String> getKursiTerisi(String scheduleId) {
        Set<String> terisi = new HashSet<String>();
        for (Pemesanan p : dataStore.getPemesananList()) {
            if (p.getScheduleId().equalsIgnoreCase(scheduleId) && p.isKursiAktif()) {
                for (Penumpang pen : p.getPenumpangList()) {
                    terisi.add(pen.getKodeKursi().toUpperCase());
                }
            }
        }
        return terisi;
    }

    public int hitungSisaKursi(Jadwal jadwal, Kereta kereta, String kelas) {
        int total = kereta.getTotalKursiKelas(kelas);
        ArrayList<Integer> nomorGerbong = kereta.getNomorGerbongUntukKelas(kelas);
        Set<String> terisi = getKursiTerisi(jadwal.getScheduleId());
        int terpakai = 0;
        for (String kode : terisi) {
            for (Integer g : nomorGerbong) {
                if (kode.startsWith("G" + g + "-")) {
                    terpakai++;
                    break;
                }
            }
        }
        return Math.max(0, total - terpakai);
    }

    public Pemesanan buatPemesanan(User user, OpsiJadwal opsi,
            ArrayList<Penumpang> penumpangList) throws ValidasiException {
        if (user == null) {
            throw new ValidasiException("Silakan login terlebih dahulu.");
        }
        if (opsi == null) {
            throw new ValidasiException("Jadwal kereta belum dipilih.");
        }
        if (penumpangList == null || penumpangList.isEmpty()) {
            throw new ValidasiException("Data penumpang tidak boleh kosong.");
        }

        Set<String> terisi = getKursiTerisi(opsi.getJadwal().getScheduleId());
        Set<String> kursiBaru = new HashSet<String>();
        for (int i = 0; i < penumpangList.size(); i++) {
            Penumpang pen = penumpangList.get(i);
            if (FormatUtil.kosong(pen.getNama())) {
                throw new ValidasiException("Nama penumpang ke-" + (i + 1) + " wajib diisi.");
            }
            if (FormatUtil.kosong(pen.getIdNumber())) {
                throw new ValidasiException("Nomor identitas penumpang ke-" + (i + 1) + " wajib diisi.");
            }
            if (FormatUtil.kosong(pen.getKodeKursi())) {
                throw new ValidasiException("Kursi penumpang ke-" + (i + 1) + " belum dipilih.");
            }
            String kodeUpper = pen.getKodeKursi().trim().toUpperCase();
            if (terisi.contains(kodeUpper)) {
                throw new ValidasiException("Kursi " + kodeUpper + " sudah terisi. Silakan pilih kursi lain.");
            }
            if (!kursiBaru.add(kodeUpper)) {
                throw new ValidasiException("Kursi " + kodeUpper + " tidak boleh dipilih ganda.");
            }
            pen.setKodeKursi(kodeUpper);
        }

        String bookingCode = generateKodeBooking();
        long tarif = opsi.getTarifPerPenumpang();
        long total = tarif * penumpangList.size();

        Pemesanan baru = new Pemesanan(
                bookingCode,
                user.getIdentifier(),
                opsi.getJadwal().getScheduleId(),
                opsi.getKereta().getTrainNumber(),
                opsi.getKereta().getName(),
                opsi.getKelas(),
                opsi.getStasiunAsal().getCode(),
                opsi.getStasiunTujuan().getCode(),
                opsi.getJamBerangkat(),
                opsi.getJamTiba(),
                penumpangList,
                tarif,
                total,
                Pemesanan.STATUS_DIPESAN,
                FormatUtil.waktuSekarang()
        );
        dataStore.getPemesananList().add(baru);
        dataStore.save();
        return baru;
    }

    public Pemesanan bayarPemesanan(String bookingCode) throws ValidasiException {
        Pemesanan p = cariWajib(bookingCode);
        if (Pemesanan.STATUS_DIBATALKAN.equalsIgnoreCase(p.getStatus())) {
            throw new ValidasiException("Pesanan yang sudah dibatalkan tidak dapat dibayar.");
        }
        p.setStatus(Pemesanan.STATUS_LUNAS);
        dataStore.save();
        return p;
    }

    public Pemesanan batalkanPemesanan(String bookingCode) throws ValidasiException {
        Pemesanan p = cariWajib(bookingCode);
        if (Pemesanan.STATUS_DIBATALKAN.equalsIgnoreCase(p.getStatus())) {
            throw new ValidasiException("Pesanan ini sudah berstatus Dibatalkan.");
        }
        p.setStatus(Pemesanan.STATUS_DIBATALKAN);
        dataStore.save();
        return p;
    }

    public Pemesanan ubahStatusAdmin(String bookingCode, String statusBaru) throws ValidasiException {
        Pemesanan p = cariWajib(bookingCode);
        if (Pemesanan.STATUS_LUNAS.equalsIgnoreCase(statusBaru)) {
            if (Pemesanan.STATUS_DIBATALKAN.equalsIgnoreCase(p.getStatus())) {
                Set<String> terisi = getKursiTerisi(p.getScheduleId());
                for (Penumpang pen : p.getPenumpangList()) {
                    if (terisi.contains(pen.getKodeKursi().toUpperCase())) {
                        throw new ValidasiException("Tidak dapat mengaktifkan pesanan karena kursi "
                                + pen.getKodeKursi() + " sudah dipesan penumpang lain.");
                    }
                }
            }
            p.setStatus(Pemesanan.STATUS_LUNAS);
        } else if (Pemesanan.STATUS_DIBATALKAN.equalsIgnoreCase(statusBaru)) {
            p.setStatus(Pemesanan.STATUS_DIBATALKAN);
        } else {
            throw new ValidasiException("Status tujuan tidak valid: " + statusBaru);
        }
        dataStore.save();
        return p;
    }

    public ArrayList<Pemesanan> getPemesananByUser(String userIdentifier) {
        ArrayList<Pemesanan> hasil = new ArrayList<Pemesanan>();
        if (userIdentifier == null) {
            return hasil;
        }
        ArrayList<Pemesanan> semua = dataStore.getPemesananList();
        for (int i = semua.size() - 1; i >= 0; i--) {
            Pemesanan p = semua.get(i);
            if (p.getUserIdentifier().equalsIgnoreCase(userIdentifier)) {
                hasil.add(p);
            }
        }
        return hasil;
    }

    public ArrayList<Pemesanan> getSemuaPemesanan() {
        ArrayList<Pemesanan> hasil = new ArrayList<Pemesanan>();
        ArrayList<Pemesanan> semua = dataStore.getPemesananList();
        for (int i = semua.size() - 1; i >= 0; i--) {
            hasil.add(semua.get(i));
        }
        return hasil;
    }

    public long getTotalPendapatanLunas() {
        long total = 0L;
        for (Pemesanan p : dataStore.getPemesananList()) {
            if (Pemesanan.STATUS_LUNAS.equalsIgnoreCase(p.getStatus())) {
                total += p.getTotalBayar();
            }
        }
        return total;
    }

    private Pemesanan cariWajib(String bookingCode) throws ValidasiException {
        Pemesanan p = dataStore.cariPemesananByCode(bookingCode);
        if (p == null) {
            throw new ValidasiException("Kode booking " + bookingCode + " tidak ditemukan.");
        }
        return p;
    }

    private String generateKodeBooking() {
        int maxNum = 0;
        for (Pemesanan p : dataStore.getPemesananList()) {
            String code = p.getBookingCode();
            if (code != null && code.startsWith("TKT-")) {
                try {
                    int n = Integer.parseInt(code.substring(4));
                    if (n > maxNum) {
                        maxNum = n;
                    }
                } catch (NumberFormatException ignored) {
                    maxNum = Math.max(maxNum, dataStore.getPemesananList().size());
                }
            }
        }
        return String.format("TKT-%04d", maxNum + 1);
    }
}
