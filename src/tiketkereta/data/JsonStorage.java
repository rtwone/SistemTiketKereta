package tiketkereta.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import tiketkereta.util.FormatUtil;
import tiketkereta.util.ValidasiException;

public class JsonStorage {

    private final Gson gson;

    public JsonStorage() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public DataDto readFromFile(File file) throws ValidasiException {
        if (file == null || !file.exists() || !file.isFile()) {
            throw new ValidasiException("File JSON tidak ditemukan: " + (file == null ? "-" : file.getPath()));
        }
        Reader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            DataDto dto = gson.fromJson(reader, DataDto.class);
            validasiStruktur(dto);
            return dto;
        } catch (JsonParseException ex) {
            throw new ValidasiException("Format JSON tidak valid atau rusak.", ex);
        } catch (IOException ex) {
            throw new ValidasiException("Gagal membaca file JSON: " + ex.getMessage(), ex);
        } finally {
            tutupDiam(reader);
        }
    }

    public void writeToFile(File file, DataDto dto) throws ValidasiException {
        if (file == null) {
            throw new ValidasiException("Lokasi file tujuan tidak valid.");
        }
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            gson.toJson(dto, writer);
            writer.flush();
        } catch (IOException ex) {
            throw new ValidasiException("Gagal menyimpan file JSON: " + ex.getMessage(), ex);
        } finally {
            tutupDiam(writer);
        }
    }

    public void validasiStruktur(DataDto dto) throws ValidasiException {
        if (dto == null) {
            throw new ValidasiException("Isi file JSON kosong.");
        }
        if (dto.stasiun == null || dto.stasiun.isEmpty()) {
            throw new ValidasiException("Data stasiun tidak boleh kosong.");
        }
        if (dto.users == null || dto.users.isEmpty()) {
            throw new ValidasiException("Data pengguna tidak boleh kosong.");
        }
        boolean adaAdmin = false;
        for (DataDto.UserDto u : dto.users) {
            if (u != null && "ADMIN".equalsIgnoreCase(u.role) && !FormatUtil.kosong(u.username)) {
                adaAdmin = true;
                break;
            }
        }
        if (!adaAdmin) {
            throw new ValidasiException("Data JSON wajib memiliki minimal 1 akun ADMIN.");
        }
        if (dto.kereta == null) {
            throw new ValidasiException("Daftar kereta tidak ditemukan di JSON.");
        }
        if (dto.jadwal == null) {
            throw new ValidasiException("Daftar jadwal tidak ditemukan di JSON.");
        }
    }

    private void tutupDiam(java.io.Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException ex) {
            throw new IllegalStateException("Gagal menutup stream file.", ex);
        }
    }
}
