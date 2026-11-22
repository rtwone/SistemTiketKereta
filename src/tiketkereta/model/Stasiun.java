package tiketkereta.model;

public class Stasiun {

    private int id;
    private String code;
    private String name;
    private String district;
    private String regency;
    private String kelasStasiun;
    private int distanceKm;

    public Stasiun(int id, String code, String name, String district,
            String regency, String kelasStasiun, int distanceKm) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.district = district;
        this.regency = regency;
        this.kelasStasiun = kelasStasiun;
        this.distanceKm = distanceKm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getRegency() {
        return regency;
    }

    public void setRegency(String regency) {
        this.regency = regency;
    }

    public String getKelasStasiun() {
        return kelasStasiun;
    }

    public void setKelasStasiun(String kelasStasiun) {
        this.kelasStasiun = kelasStasiun;
    }

    public int getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(int distanceKm) {
        this.distanceKm = distanceKm;
    }

    public String getLabelSingkat() {
        return name + " (" + code + ")";
    }

    @Override
    public String toString() {
        return name + " (" + code + ") - " + regency;
    }
}
