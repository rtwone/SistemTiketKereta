package tiketkereta.model;

public class Penumpang {

    private String nama;
    private String idType;
    private String idNumber;
    private String kodeKursi;

    public Penumpang(String nama, String idType, String idNumber, String kodeKursi) {
        this.nama = nama;
        this.idType = idType;
        this.idNumber = idNumber;
        this.kodeKursi = kodeKursi;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getKodeKursi() {
        return kodeKursi;
    }

    public void setKodeKursi(String kodeKursi) {
        this.kodeKursi = kodeKursi;
    }
}
