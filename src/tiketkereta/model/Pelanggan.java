package tiketkereta.model;

public class Pelanggan extends User {

    private String idType;
    private String idNumber;
    private String phone;

    public Pelanggan(String idType, String idNumber, String fullName, String phone, String password) {
        super(fullName, password);
        this.idType = idType;
        this.idNumber = idNumber;
        this.phone = phone;
    }

    @Override
    public String getRole() {
        return "PELANGGAN";
    }

    @Override
    public String getIdentifier() {
        return idNumber;
    }

    @Override
    public String getDisplaySubtitle() {
        return idType + " " + idNumber;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
