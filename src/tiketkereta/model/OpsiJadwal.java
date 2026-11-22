package tiketkereta.model;

public class OpsiJadwal {

    private final Jadwal jadwal;
    private final Kereta kereta;
    private final String kelas;
    private final Stasiun stasiunAsal;
    private final Stasiun stasiunTujuan;
    private final String jamBerangkat;
    private final String jamTiba;
    private final int sisaKursi;
    private final long tarifPerPenumpang;

    public OpsiJadwal(Jadwal jadwal, Kereta kereta, String kelas,
            Stasiun stasiunAsal, Stasiun stasiunTujuan,
            String jamBerangkat, String jamTiba, int sisaKursi, long tarifPerPenumpang) {
        this.jadwal = jadwal;
        this.kereta = kereta;
        this.kelas = kelas;
        this.stasiunAsal = stasiunAsal;
        this.stasiunTujuan = stasiunTujuan;
        this.jamBerangkat = jamBerangkat;
        this.jamTiba = jamTiba;
        this.sisaKursi = sisaKursi;
        this.tarifPerPenumpang = tarifPerPenumpang;
    }

    public Jadwal getJadwal() {
        return jadwal;
    }

    public Kereta getKereta() {
        return kereta;
    }

    public String getKelas() {
        return kelas;
    }

    public Stasiun getStasiunAsal() {
        return stasiunAsal;
    }

    public Stasiun getStasiunTujuan() {
        return stasiunTujuan;
    }

    public String getJamBerangkat() {
        return jamBerangkat;
    }

    public String getJamTiba() {
        return jamTiba;
    }

    public int getSisaKursi() {
        return sisaKursi;
    }

    public long getTarifPerPenumpang() {
        return tarifPerPenumpang;
    }
}
