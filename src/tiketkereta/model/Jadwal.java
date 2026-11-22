package tiketkereta.model;

import java.util.ArrayList;

public class Jadwal {

    private String scheduleId;
    private String trainNumber;
    private String direction;
    private String originCode;
    private String destinationCode;
    private ArrayList<StopJadwal> stops;

    public Jadwal(String scheduleId, String trainNumber, String direction,
            String originCode, String destinationCode, ArrayList<StopJadwal> stops) {
        this.scheduleId = scheduleId;
        this.trainNumber = trainNumber;
        this.direction = direction;
        this.originCode = originCode;
        this.destinationCode = destinationCode;
        this.stops = stops == null ? new ArrayList<StopJadwal>() : new ArrayList<StopJadwal>(stops);
    }

    public int indexStop(String stationCode) {
        for (int i = 0; i < stops.size(); i++) {
            if (stops.get(i).getStationCode().equalsIgnoreCase(stationCode)) {
                return i;
            }
        }
        return -1;
    }

    public StopJadwal getStop(String stationCode) {
        int idx = indexStop(stationCode);
        return idx >= 0 ? stops.get(idx) : null;
    }

    public String getJamBerangkatAwal() {
        if (stops.isEmpty()) {
            return "-";
        }
        String dep = stops.get(0).getDeparture();
        return dep != null ? dep : "-";
    }

    public String getJamTibaAkhir() {
        if (stops.isEmpty()) {
            return "-";
        }
        String arr = stops.get(stops.size() - 1).getArrival();
        return arr != null ? arr : "-";
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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
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

    public ArrayList<StopJadwal> getStops() {
        return stops;
    }

    public void setStops(ArrayList<StopJadwal> stops) {
        this.stops = stops == null ? new ArrayList<StopJadwal>() : new ArrayList<StopJadwal>(stops);
    }
}
