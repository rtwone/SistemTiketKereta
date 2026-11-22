package tiketkereta.model;

import java.util.ArrayList;

public class Pemesanan {

    public static final String STATUS_DIPESAN = "Dipesan";
    public static final String STATUS_LUNAS = "Lunas";
    public static final String STATUS_DIBATALKAN = "Dibatalkan";

    private String bookingCode;
    private String userIdentifier;
    private String scheduleId;
    private String trainNumber;
    private String trainName;
    private String kelas;
    private String originCode;
    private String destinationCode;
    private String departureTime;
    private String arrivalTime;
    private ArrayList<Penumpang> penumpangList;
    private long tarifPerPenumpang;
    private long totalBayar;
    private String status;
    private String createdAt;

    public Pemesanan(String bookingCode, String userIdentifier, String scheduleId,
            String trainNumber, String trainName, String kelas,
            String originCode, String destinationCode, String departureTime, String arrivalTime,
            ArrayList<Penumpang> penumpangList, long tarifPerPenumpang, long totalBayar,
            String status, String createdAt) {
        this.bookingCode = bookingCode;
        this.userIdentifier = userIdentifier;
        this.scheduleId = scheduleId;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.kelas = kelas;
        this.originCode = originCode;
        this.destinationCode = destinationCode;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.penumpangList = penumpangList == null
                ? new ArrayList<Penumpang>()
                : new ArrayList<Penumpang>(penumpangList);
        this.tarifPerPenumpang = tarifPerPenumpang;
        this.totalBayar = totalBayar;
        this.status = status;
        this.createdAt = createdAt;
    }

    public boolean isKursiAktif() {
        return STATUS_DIPESAN.equalsIgnoreCase(status) || STATUS_LUNAS.equalsIgnoreCase(status);
    }

    public String getDaftarKursiRingkas() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < penumpangList.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(penumpangList.get(i).getKodeKursi());
        }
        return sb.toString();
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getDestinationCode() {
        return destinationCode;
    }

    public void setDestinationCode(String destinationCode) {
        this.destinationCode = destinationCode;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public ArrayList<Penumpang> getPenumpangList() {
        return penumpangList;
    }

    public void setPenumpangList(ArrayList<Penumpang> penumpangList) {
        this.penumpangList = penumpangList == null
                ? new ArrayList<Penumpang>()
                : new ArrayList<Penumpang>(penumpangList);
    }

    public long getTarifPerPenumpang() {
        return tarifPerPenumpang;
    }

    public void setTarifPerPenumpang(long tarifPerPenumpang) {
        this.tarifPerPenumpang = tarifPerPenumpang;
    }

    public long getTotalBayar() {
        return totalBayar;
    }

    public void setTotalBayar(long totalBayar) {
        this.totalBayar = totalBayar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
