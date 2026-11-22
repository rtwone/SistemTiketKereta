package tiketkereta.data;

import java.util.ArrayList;
import tiketkereta.model.Admin;
import tiketkereta.model.GerbongConfig;
import tiketkereta.model.Jadwal;
import tiketkereta.model.Kereta;
import tiketkereta.model.KeretaAntarkota;
import tiketkereta.model.KeretaEkspres;
import tiketkereta.model.KeretaKomuter;
import tiketkereta.model.KeretaLokal;
import tiketkereta.model.Pelanggan;
import tiketkereta.model.Pemesanan;
import tiketkereta.model.Penumpang;
import tiketkereta.model.Stasiun;
import tiketkereta.model.StopJadwal;
import tiketkereta.model.User;

public final class ModelFactory {

    private ModelFactory() {
    }

    public static Stasiun createStasiun(DataDto.StasiunDto dto) {
        return new Stasiun(
                dto.id,
                dto.code,
                dto.name,
                dto.district,
                dto.regency,
                dto.kelasStasiun,
                dto.distanceKm
        );
    }

    public static DataDto.StasiunDto toStasiunDto(Stasiun s) {
        DataDto.StasiunDto dto = new DataDto.StasiunDto();
        dto.id = s.getId();
        dto.code = s.getCode();
        dto.name = s.getName();
        dto.district = s.getDistrict();
        dto.regency = s.getRegency();
        dto.kelasStasiun = s.getKelasStasiun();
        dto.distanceKm = s.getDistanceKm();
        return dto;
    }

    public static Kereta createKereta(String serviceType, String trainNumber, String name,
            ArrayList<GerbongConfig> carriages, String originCode, String destinationCode, int baseFare) {
        String jenis = serviceType == null ? "" : serviceType.trim();
        if ("Ekspres".equalsIgnoreCase(jenis)) {
            return new KeretaEkspres(trainNumber, name, carriages, originCode, destinationCode, baseFare);
        }
        if ("Lokal".equalsIgnoreCase(jenis)) {
            return new KeretaLokal(trainNumber, name, carriages, originCode, destinationCode, baseFare);
        }
        if ("Komuter".equalsIgnoreCase(jenis)) {
            return new KeretaKomuter(trainNumber, name, carriages, originCode, destinationCode, baseFare);
        }
        return new KeretaAntarkota(trainNumber, name, carriages, originCode, destinationCode, baseFare);
    }

    public static Kereta createKereta(DataDto.KeretaDto dto) {
        ArrayList<GerbongConfig> list = new ArrayList<GerbongConfig>();
        if (dto.carriages != null) {
            for (DataDto.GerbongDto g : dto.carriages) {
                list.add(new GerbongConfig(g.kelas, g.count, g.seatsPerCarriage));
            }
        }
        return createKereta(dto.serviceType, dto.trainNumber, dto.name, list,
                dto.originCode, dto.destinationCode, dto.baseFare);
    }

    public static DataDto.KeretaDto toKeretaDto(Kereta k) {
        DataDto.KeretaDto dto = new DataDto.KeretaDto();
        dto.trainNumber = k.getTrainNumber();
        dto.name = k.getName();
        dto.originCode = k.getOriginCode();
        dto.destinationCode = k.getDestinationCode();
        dto.serviceType = k.getJenisLayanan();
        dto.baseFare = k.getBaseFare();
        dto.carriages = new ArrayList<DataDto.GerbongDto>();
        for (GerbongConfig cfg : k.getCarriages()) {
            DataDto.GerbongDto g = new DataDto.GerbongDto();
            g.kelas = cfg.getKelas();
            g.count = cfg.getCount();
            g.seatsPerCarriage = cfg.getSeatsPerCarriage();
            dto.carriages.add(g);
        }
        return dto;
    }

    public static Jadwal createJadwal(DataDto.JadwalDto dto) {
        ArrayList<StopJadwal> stops = new ArrayList<StopJadwal>();
        if (dto.stops != null) {
            for (DataDto.StopDto s : dto.stops) {
                stops.add(new StopJadwal(s.stationCode, s.arrival, s.departure));
            }
        }
        return new Jadwal(dto.scheduleId, dto.trainNumber, dto.direction,
                dto.originCode, dto.destinationCode, stops);
    }

    public static DataDto.JadwalDto toJadwalDto(Jadwal j) {
        DataDto.JadwalDto dto = new DataDto.JadwalDto();
        dto.scheduleId = j.getScheduleId();
        dto.trainNumber = j.getTrainNumber();
        dto.direction = j.getDirection();
        dto.originCode = j.getOriginCode();
        dto.destinationCode = j.getDestinationCode();
        dto.stops = new ArrayList<DataDto.StopDto>();
        for (StopJadwal s : j.getStops()) {
            DataDto.StopDto sd = new DataDto.StopDto();
            sd.stationCode = s.getStationCode();
            sd.arrival = s.getArrival();
            sd.departure = s.getDeparture();
            dto.stops.add(sd);
        }
        return dto;
    }

    public static User createUser(DataDto.UserDto dto) {
        if ("ADMIN".equalsIgnoreCase(dto.role)) {
            return new Admin(dto.username, dto.password, dto.fullName);
        }
        String jenisId = dto.idType == null ? "KTP" : dto.idType;
        return new Pelanggan(jenisId, dto.idNumber, dto.fullName, dto.phone, dto.password);
    }

    public static DataDto.UserDto toUserDto(User u) {
        DataDto.UserDto dto = new DataDto.UserDto();
        dto.role = u.getRole();
        dto.fullName = u.getFullName();
        dto.password = u.getPassword();
        if (u instanceof Admin) {
            dto.username = ((Admin) u).getUsername();
        } else if (u instanceof Pelanggan) {
            Pelanggan p = (Pelanggan) u;
            dto.idType = p.getIdType();
            dto.idNumber = p.getIdNumber();
            dto.phone = p.getPhone();
        }
        return dto;
    }

    public static Pemesanan createPemesanan(DataDto.PemesananDto dto) {
        ArrayList<Penumpang> penumpang = new ArrayList<Penumpang>();
        if (dto.penumpang != null) {
            for (DataDto.PenumpangDto p : dto.penumpang) {
                penumpang.add(new Penumpang(p.nama, p.idType, p.idNumber, p.kodeKursi));
            }
        }
        return new Pemesanan(
                dto.bookingCode, dto.userIdentifier, dto.scheduleId,
                dto.trainNumber, dto.trainName, dto.kelas,
                dto.originCode, dto.destinationCode, dto.departureTime, dto.arrivalTime,
                penumpang, dto.tarifPerPenumpang, dto.totalBayar, dto.status, dto.createdAt
        );
    }

    public static DataDto.PemesananDto toPemesananDto(Pemesanan p) {
        DataDto.PemesananDto dto = new DataDto.PemesananDto();
        dto.bookingCode = p.getBookingCode();
        dto.userIdentifier = p.getUserIdentifier();
        dto.scheduleId = p.getScheduleId();
        dto.trainNumber = p.getTrainNumber();
        dto.trainName = p.getTrainName();
        dto.kelas = p.getKelas();
        dto.originCode = p.getOriginCode();
        dto.destinationCode = p.getDestinationCode();
        dto.departureTime = p.getDepartureTime();
        dto.arrivalTime = p.getArrivalTime();
        dto.tarifPerPenumpang = p.getTarifPerPenumpang();
        dto.totalBayar = p.getTotalBayar();
        dto.status = p.getStatus();
        dto.createdAt = p.getCreatedAt();
        dto.penumpang = new ArrayList<DataDto.PenumpangDto>();
        for (Penumpang pen : p.getPenumpangList()) {
            DataDto.PenumpangDto pd = new DataDto.PenumpangDto();
            pd.nama = pen.getNama();
            pd.idType = pen.getIdType();
            pd.idNumber = pen.getIdNumber();
            pd.kodeKursi = pen.getKodeKursi();
            dto.penumpang.add(pd);
        }
        return dto;
    }
}
