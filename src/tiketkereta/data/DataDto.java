package tiketkereta.data;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataDto {

    @SerializedName("fare_rounding")
    public int fareRounding = 1000;

    @SerializedName("class_multipliers")
    public Map<String, Double> classMultipliers = new LinkedHashMap<String, Double>();

    @SerializedName("stasiun")
    public List<StasiunDto> stasiun = new ArrayList<StasiunDto>();

    @SerializedName("kereta")
    public List<KeretaDto> kereta = new ArrayList<KeretaDto>();

    @SerializedName("jadwal")
    public List<JadwalDto> jadwal = new ArrayList<JadwalDto>();

    @SerializedName("users")
    public List<UserDto> users = new ArrayList<UserDto>();

    @SerializedName("pemesanan")
    public List<PemesananDto> pemesanan = new ArrayList<PemesananDto>();

    public static class StasiunDto {
        public int id;
        public String code;
        public String name;
        public String district;
        public String regency;
        @SerializedName("class")
        public String kelasStasiun;
        @SerializedName("distance_km")
        public int distanceKm;
    }

    public static class GerbongDto {
        @SerializedName("class")
        public String kelas;
        public int count;
        @SerializedName("seats_per_carriage")
        public int seatsPerCarriage;
    }

    public static class KeretaDto {
        @SerializedName("train_number")
        public String trainNumber;
        public String name;
        public List<GerbongDto> carriages = new ArrayList<GerbongDto>();
        @SerializedName("origin_code")
        public String originCode;
        @SerializedName("destination_code")
        public String destinationCode;
        @SerializedName("service_type")
        public String serviceType;
        @SerializedName("base_fare")
        public int baseFare;
    }

    public static class StopDto {
        @SerializedName("station_code")
        public String stationCode;
        public String arrival;
        public String departure;
    }

    public static class JadwalDto {
        @SerializedName("schedule_id")
        public String scheduleId;
        @SerializedName("train_number")
        public String trainNumber;
        public String direction;
        @SerializedName("origin_code")
        public String originCode;
        @SerializedName("destination_code")
        public String destinationCode;
        public List<StopDto> stops = new ArrayList<StopDto>();
    }

    public static class UserDto {
        public String role;
        public String username;
        @SerializedName("id_type")
        public String idType;
        @SerializedName("id_number")
        public String idNumber;
        @SerializedName("full_name")
        public String fullName;
        public String phone;
        public String password;
    }

    public static class PenumpangDto {
        public String nama;
        @SerializedName("id_type")
        public String idType;
        @SerializedName("id_number")
        public String idNumber;
        @SerializedName("kode_kursi")
        public String kodeKursi;
    }

    public static class PemesananDto {
        @SerializedName("booking_code")
        public String bookingCode;
        @SerializedName("user_identifier")
        public String userIdentifier;
        @SerializedName("schedule_id")
        public String scheduleId;
        @SerializedName("train_number")
        public String trainNumber;
        @SerializedName("train_name")
        public String trainName;
        public String kelas;
        @SerializedName("origin_code")
        public String originCode;
        @SerializedName("destination_code")
        public String destinationCode;
        @SerializedName("departure_time")
        public String departureTime;
        @SerializedName("arrival_time")
        public String arrivalTime;
        public List<PenumpangDto> penumpang = new ArrayList<PenumpangDto>();
        @SerializedName("tarif_per_penumpang")
        public long tarifPerPenumpang;
        @SerializedName("total_bayar")
        public long totalBayar;
        public String status;
        @SerializedName("created_at")
        public String createdAt;
    }
}
