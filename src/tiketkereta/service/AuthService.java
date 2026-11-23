package tiketkereta.service;

import tiketkereta.data.DataStore;
import tiketkereta.model.Pelanggan;
import tiketkereta.model.User;
import tiketkereta.util.FormatUtil;
import tiketkereta.util.ValidasiException;

public class AuthService {

    private final DataStore dataStore;
    private User currentUser;

    public AuthService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public User login(String identifier, String password) throws ValidasiException {
        String id = FormatUtil.rapi(identifier);
        if (FormatUtil.kosong(id)) {
            throw new ValidasiException("Username atau nomor identitas wajib diisi.");
        }
        if (FormatUtil.kosong(password)) {
            throw new ValidasiException("Password wajib diisi.");
        }
        for (User user : dataStore.getUserList()) {
            if (user.getIdentifier().equalsIgnoreCase(id) && user.getPassword().equals(password)) {
                this.currentUser = user;
                return user;
            }
        }
        throw new ValidasiException("Username / nomor identitas atau password tidak sesuai.");
    }

    public Pelanggan registerPelanggan(String fullName, String idType, String idNumber,
            String phone, String password, String confirmPassword) throws ValidasiException {
        String nama = FormatUtil.rapi(fullName);
        String jenisId = FormatUtil.rapi(idType);
        String nomorId = FormatUtil.rapi(idNumber);
        String telepon = FormatUtil.rapi(phone);

        if (FormatUtil.kosong(nama)) {
            throw new ValidasiException("Nama lengkap wajib diisi.");
        }
        if (FormatUtil.kosong(jenisId)) {
            jenisId = "KTP";
        }
        if (FormatUtil.kosong(nomorId)) {
            throw new ValidasiException("Nomor identitas (KTP/NIK atau Paspor) wajib diisi.");
        }
        if (FormatUtil.kosong(telepon)) {
            throw new ValidasiException("Nomor HP wajib diisi.");
        }
        if (FormatUtil.kosong(password) || password.length() < 4) {
            throw new ValidasiException("Password minimal terdiri dari 4 karakter.");
        }
        if (!password.equals(confirmPassword)) {
            throw new ValidasiException("Konfirmasi password tidak cocok.");
        }
        for (User u : dataStore.getUserList()) {
            if (u.getIdentifier().equalsIgnoreCase(nomorId)) {
                throw new ValidasiException("Nomor identitas '" + nomorId + "' sudah terdaftar.");
            }
        }

        Pelanggan baru = new Pelanggan(jenisId, nomorId, nama, telepon, password);
        dataStore.getUserList().add(baru);
        return baru;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void logout() {
        this.currentUser = null;
    }
}
