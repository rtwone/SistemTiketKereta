package tiketkereta.model;

public class GerbongConfig {

    private String kelas;
    private int count;
    private int seatsPerCarriage;

    public GerbongConfig(String kelas, int count, int seatsPerCarriage) {
        this.kelas = kelas;
        this.count = count;
        this.seatsPerCarriage = seatsPerCarriage;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getSeatsPerCarriage() {
        return seatsPerCarriage;
    }

    public void setSeatsPerCarriage(int seatsPerCarriage) {
        this.seatsPerCarriage = seatsPerCarriage;
    }

    public int getTotalKursi() {
        return count * seatsPerCarriage;
    }
}
