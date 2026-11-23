package tiketkereta.data;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import tiketkereta.model.Jadwal;
import tiketkereta.model.Kereta;
import tiketkereta.model.Pemesanan;
import tiketkereta.model.Stasiun;
import tiketkereta.model.User;
import tiketkereta.util.ValidasiException;

public class DataStore {

    private static final String DEFAULT_DATA_PATH = "data/data.json";

    private final ArrayList<Stasiun> stasiunList;
    private final ArrayList<Kereta> keretaList;
    private final ArrayList<Jadwal> jadwalList;
    private final ArrayList<User> userList;
    private final ArrayList<Pemesanan> pemesananList;
    private final Map<String, Double> classMultipliers;
    private final JsonStorage jsonStorage;
    private File activeFile;
    private int fareRounding;

    public DataStore() {
        this(new File(DEFAULT_DATA_PATH));
    }

    public DataStore(File activeFile) {
        this.activeFile = activeFile;
        this.stasiunList = new ArrayList<Stasiun>();
        this.keretaList = new ArrayList<Kereta>();
        this.jadwalList = new ArrayList<Jadwal>();
        this.userList = new ArrayList<User>();
        this.pemesananList = new ArrayList<Pemesanan>();
        this.classMultipliers = new LinkedHashMap<String, Double>();
        this.jsonStorage = new JsonStorage();
        this.fareRounding = 1000;
        initDefaultMultipliers();
    }

    private void initDefaultMultipliers() {
        classMultipliers.clear();
        classMultipliers.put("Ekonomi", 1.0);
        classMultipliers.put("Ekonomi Premium", 1.2);
        classMultipliers.put("Bisnis", 1.4);
        classMultipliers.put("Eksekutif", 1.8);
    }

    public void load() throws ValidasiException {
        DataDto dto = jsonStorage.readFromFile(activeFile);
        terapkanDto(dto);
    }

    public void backupToFile(File tujuan) throws ValidasiException {
        jsonStorage.writeToFile(tujuan, buatDto());
    }

    public void restoreFromFile(File sumber) throws ValidasiException {
        DataDto dto = jsonStorage.readFromFile(sumber);
        terapkanDto(dto);
        this.activeFile = sumber;
    }

    private void terapkanDto(DataDto dto) {
        ArrayList<Stasiun> stasiunBaru = new ArrayList<Stasiun>();
        for (DataDto.StasiunDto s : dto.stasiun) {
            stasiunBaru.add(ModelFactory.createStasiun(s));
        }

        ArrayList<Kereta> keretaBaru = new ArrayList<Kereta>();
        for (DataDto.KeretaDto k : dto.kereta) {
            keretaBaru.add(ModelFactory.createKereta(k));
        }

        ArrayList<Jadwal> jadwalBaru = new ArrayList<Jadwal>();
        for (DataDto.JadwalDto j : dto.jadwal) {
            jadwalBaru.add(ModelFactory.createJadwal(j));
        }

        ArrayList<User> userBaru = new ArrayList<User>();
        for (DataDto.UserDto u : dto.users) {
            userBaru.add(ModelFactory.createUser(u));
        }

        ArrayList<Pemesanan> pemesananBaru = new ArrayList<Pemesanan>();
        if (dto.pemesanan != null) {
            for (DataDto.PemesananDto p : dto.pemesanan) {
                pemesananBaru.add(ModelFactory.createPemesanan(p));
            }
        }

        stasiunList.clear();
        stasiunList.addAll(stasiunBaru);
        keretaList.clear();
        keretaList.addAll(keretaBaru);
        jadwalList.clear();
        jadwalList.addAll(jadwalBaru);
        userList.clear();
        userList.addAll(userBaru);
        pemesananList.clear();
        pemesananList.addAll(pemesananBaru);

        this.fareRounding = dto.fareRounding > 0 ? dto.fareRounding : 1000;
        if (dto.classMultipliers != null && !dto.classMultipliers.isEmpty()) {
            classMultipliers.clear();
            classMultipliers.putAll(dto.classMultipliers);
        } else {
            initDefaultMultipliers();
        }
    }

    private DataDto buatDto() {
        DataDto dto = new DataDto();
        dto.fareRounding = this.fareRounding;
        dto.classMultipliers = new LinkedHashMap<String, Double>(this.classMultipliers);

        for (Stasiun s : stasiunList) {
            dto.stasiun.add(ModelFactory.toStasiunDto(s));
        }
        for (Kereta k : keretaList) {
            dto.kereta.add(ModelFactory.toKeretaDto(k));
        }
        for (Jadwal j : jadwalList) {
            dto.jadwal.add(ModelFactory.toJadwalDto(j));
        }
        for (User u : userList) {
            dto.users.add(ModelFactory.toUserDto(u));
        }
        for (Pemesanan p : pemesananList) {
            dto.pemesanan.add(ModelFactory.toPemesananDto(p));
        }
        return dto;
    }

    public Stasiun cariStasiunByCode(String code) {
        if (code == null) {
            return null;
        }
        for (Stasiun s : stasiunList) {
            if (s.getCode().equalsIgnoreCase(code.trim())) {
                return s;
            }
        }
        return null;
    }

    public Kereta cariKeretaByNumber(String trainNumber) {
        if (trainNumber == null) {
            return null;
        }
        for (Kereta k : keretaList) {
            if (k.getTrainNumber().equalsIgnoreCase(trainNumber.trim())) {
                return k;
            }
        }
        return null;
    }

    public Jadwal cariJadwalById(String scheduleId) {
        if (scheduleId == null) {
            return null;
        }
        for (Jadwal j : jadwalList) {
            if (j.getScheduleId().equalsIgnoreCase(scheduleId.trim())) {
                return j;
            }
        }
        return null;
    }

    public Pemesanan cariPemesananByCode(String bookingCode) {
        if (bookingCode == null) {
            return null;
        }
        for (Pemesanan p : pemesananList) {
            if (p.getBookingCode().equalsIgnoreCase(bookingCode.trim())) {
                return p;
            }
        }
        return null;
    }

    public double getPengaliKelas(String kelas) {
        if (kelas == null) {
            return 1.0;
        }
        for (Map.Entry<String, Double> entry : classMultipliers.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(kelas.trim())) {
                return entry.getValue();
            }
        }
        return 1.0;
    }

    public ArrayList<Stasiun> getStasiunList() {
        return stasiunList;
    }

    public ArrayList<Kereta> getKeretaList() {
        return keretaList;
    }

    public ArrayList<Jadwal> getJadwalList() {
        return jadwalList;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public ArrayList<Pemesanan> getPemesananList() {
        return pemesananList;
    }

    public int getFareRounding() {
        return fareRounding;
    }

    public Map<String, Double> getClassMultipliers() {
        return classMultipliers;
    }

    public File getActiveFile() {
        return activeFile;
    }
}
