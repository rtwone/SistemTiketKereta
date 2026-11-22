package tiketkereta.model;

public class StopJadwal {

    private String stationCode;
    private String arrival;
    private String departure;

    public StopJadwal(String stationCode, String arrival, String departure) {
        this.stationCode = stationCode;
        this.arrival = arrival;
        this.departure = departure;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }
}
