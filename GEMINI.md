# Spesifikasi Proyek: Sistem Informasi Penjadwalan dan Pemesanan Tiket Kereta

Dokumen ini adalah panduan tunggal untuk membangun aplikasi desktop Java Swing tugas mata kuliah Pemrograman Berorientasi Objek (PBO) bertema **Access by KAI**. Bangun sistem yang **simpel, rapi, stabil, dan realistis untuk tugas kuliah** tanpa over-engineering, dengan arsitektur PBO yang kuat dan UI modern.

---

## 1. Lingkungan & Kompatibilitas Wajib (NetBeans IDE 8.2)

- **IDE & Tipe Proyek**: NetBeans IDE 8.2, proyek Java Application standar (**Ant**), kode sumber berada di folder `src/`.
- **WAJIB JAVA 8 (JDK 1.8)**: Seluruh kode harus 100% kompatibel dengan Java 8.
  - **DILARANG** memakai fitur Java 9+: `var`, `record`, `List.of()`, `Map.of()`, *switch expression* (`->`), *text block* (`"""`), atau `String.isBlank()`.
  - Gunakan `new ArrayList<>()`, `Arrays.asList()`, `trim().isEmpty()`, dan `switch-case` klasik.
- **Library di folder `lib/`**:
  - `gson-2.10.1.jar` (untuk baca/tulis dan Backup/Restore JSON).
  - `flatlaf-3.5.4.jar` (untuk tema UI modern).
- **Data Awal**: Gunakan file `data/data.json` yang sudah tersedia di root proyek (berisi 21 stasiun jalur Madura, 10 kereta, 48 jadwal, dan 3 akun bawaan: `admin`/`admin123`, `3529000000000001`/`budi123`, `A1234567`/`sari123`).

---

## 2. Arsitektur PBO & Struktur Package

Gunakan struktur package di dalam `src/tiketkereta/`:
- `model` — Entity dan hierarki class PBO.
- `data` — `DataStore` (`ArrayList`), DTO, Factory, dan `JsonStorage`.
- `service` — Logika bisnis (`AuthService`, `JadwalService`, `PemesananService`, `TarifService`).
- `ui` — `MainFrame`, `LoginPanel`, `RegisterPanel`, `CariTiketPanel`, `PilihKeretaPanel`, `PesanKursiPanel`, `SeatMapPanel`, `PembayaranPanel`, `ETiketPanel`, `TiketSayaPanel`.
- `ui/admin` — `AdminDashboardPanel`, `KelolaStasiunPanel`, `KelolaKeretaPanel`, `KelolaJadwalPanel`, `KelolaPemesananPanel`, `BackupRestorePanel`.
- `util` — `Tema`, `FormatUtil`, `ValidasiException`.

### Prinsip PBO Wajib
1. **Abstract Class & Inheritance**:
   - `User` (abstract) → `Admin`, `Pelanggan`.
   - `Kereta` (abstract) → `KeretaEkspres`, `KeretaAntarkota`, `KeretaLokal`, `KeretaKomuter`.
2. **Polymorphism**:
   - Subclass `Kereta` meng-override method layanan (seperti `getJenisLayanan()` dan informasi kelas/kecepatan).
   - `User` memiliki method abstract seperti `getRole()` untuk penentuan hak akses.
3. **Encapsulation**: Semua field `private` dengan getter/setter yang diperlukan.
4. **Custom Exception**: Buat `ValidasiException` (turunan `Exception`) untuk menangani error validasi input dan aturan bisnis secara bersih.

---

## 3. Database `ArrayList` & Persistensi JSON

- Seluruh data di memori **wajib** disimpan dalam `ArrayList` di dalam class `DataStore`:
  - `ArrayList<Stasiun>`
  - `ArrayList<Kereta>`
  - `ArrayList<Jadwal>`
  - `ArrayList<User>`
  - `ArrayList<Pemesanan>`
- Saat aplikasi dimulai, `DataStore` otomatis memuat `data/data.json`. Setiap perubahan data (CRUD, pemesanan, pembayaran, pembatalan) otomatis disimpan kembali ke `data/data.json`.
- **Gson DTO + Factory**: Jangan mengandalkan serialisasi polimorfisme otomatis untuk `User` dan `Kereta`. Gunakan class DTO sederhana + Factory method untuk memetakan JSON ke subclass yang tepat.
- **Backup & Restore JSON**:
  - Sediakan fitur Export (Backup) dan Import (Restore) melalui `JFileChooser`.
  - Sebelum Restore menimpa data aktif, lakukan validasi dasar: file terbaca, JSON valid, minimal ada 1 akun `ADMIN`, dan daftar stasiun tidak kosong. Jika gagal, data lama tetap aman.

---

## 4. Ruang Lingkup Fitur (Versi Realistis 1 Bulan)

### A. Autentikasi
- **Login**: Satu kolom input ("Username / No. Identitas") dan kolom Password. Mendukung login Admin dan Pelanggan.
- **Registrasi Pelanggan**: Input Nama Lengkap, No. Identitas (KTP/NIK atau Paspor, wajib unik dan tidak kosong), No. HP, dan Password.

### B. Alur Pelanggan (1 Jendela `MainFrame` dengan Sidebar + `CardLayout`)
1. **Cari Tiket**: Pilih stasiun asal, stasiun tujuan, tombol tukar asal-tujuan, dan jumlah penumpang (1–4).
2. **Pilih Kereta**: Tampilkan jadwal yang melewati stasiun asal lalu stasiun tujuan (sesuai urutan arah trip) dalam `JTable` rapi berisi kode & nama kereta, jenis, kelas yang tersedia, jam berangkat–tiba, sisa kursi, dan tarif.
3. **Input Penumpang & Pilih Kursi**:
   - Input nama dan nomor identitas tiap penumpang (sesuai jumlah penumpang).
   - Pilih gerbong & kursi lewat `SeatMapPanel` (kode Java murni tanpa `.form`): grid kotak-kotak dengan warna **Hijau** (Kosong), **Biru** (Dipilih), **Abu-abu** (Terisi).
   - Konfigurasi kursi: 50 kursi = 2-2 × 13 baris (baris 13 hanya A, B), 64 = 2-2 × 16, 80 = 2-2 × 20, 90 = 3-3 × 15.
   - Format kode kursi: `G{gerbong}-{baris}{huruf}` (contoh: `G2-7C`).
4. **Pembayaran & E-Tiket**:
   - Tampilkan ringkasan pesanan dan total bayar.
   - Tombol **"Bayar Sekarang"** langsung mengubah status pesanan dari `Dipesan` menjadi `Lunas` dan menampilkan **E-Tiket** (kode booking `TKT-0001`, rute, jam, daftar penumpang, nomor kursi, total bayar).
5. **Tiket Saya**:
   - Daftar pesanan milik pelanggan yang sedang login.
   - Aksi: lihat ulang E-Tiket, bayar (jika masih `Dipesan`), atau **Batalkan Tiket**.

### C. Alur Admin (di `MainFrame` yang sama)
1. **Dashboard Ringkas**: Kartu statistik (Total Stasiun, Total Kereta, Total Jadwal, Total Pemesanan, Total Pendapatan Lunas) + tombol cepat Backup & Restore JSON.
2. **Kelola Stasiun**: CRUD (Tambah, Ubah, Hapus).
3. **Kelola Kereta**: CRUD (Tambah, Ubah, Hapus).
4. **Kelola Jadwal**: CRUD (Tambah, Ubah, Hapus) untuk jadwal eksisting.
5. **Kelola Pemesanan**: Lihat semua pemesanan, ubah status menjadi `Lunas` atau `Dibatalkan`.
6. **Backup & Restore JSON**: Export dan Import file JSON dengan konfirmasi & validasi.

### D. Aturan Bisnis Singkat
- **Rumus Tarif**: `tarif = base_fare * (|id_tujuan - id_asal| / |id_awal_rute - id_akhir_rute|) * pengali_kelas`, dibulatkan ke ribuan terdekat (`Math.round(nilai / 1000.0) * 1000`).
- **Pengali Kelas** (sesuai `data.json`): Ekonomi `1.0`, Ekonomi Premium `1.2`, Bisnis `1.4`, Eksekutif `1.8`.
- **Format Uang**: `Rp xx.xxx` (contoh `Rp 48.000`).
- **Ketersediaan Kursi**: Versi sederhana — kursi yang sudah dipesan (`Dipesan` atau `Lunas`) terpakai untuk seluruh trip tersebut.
- **Kode Booking**: `TKT-0001`, `TKT-0002`, dst.

### E. Fitur yang SENGAJA TIDAK DIBUAT (Jangan Ditambahkan)
- Jangan buat simulasi jam virtual / slider waktu.
- Jangan buat countdown timer pembayaran 15 menit.
- Jangan buat fitur Ubah Jadwal (Reschedule) atau Info Stasiun.
- Jangan buat generator jadwal otomatis 48 trip atau Merge JSON.
- Jangan buat masking NIK/Paspor.

---

## 5. Standar UI/UX Modern (FlatLaf + Prinsip Desain Impeccable)

Panggil `FlatLightLaf.setup()` di `main()` sebelum membuat `MainFrame`. Pusatkan gaya visual di `util/Tema.java`:

- **Palet Warna (Access by KAI)**:
  - Primary Navy: `#1B365D` (Sidebar, header, judul utama)
  - Accent Orange: `#F37021` (Tombol CTA utama: Cari Tiket, Lanjut, Bayar)
  - Canvas Background: `#F5F7FA` (Latar belakang panel utama)
  - Surface / Card: `#FFFFFF` dengan border halus `#E2E8F0`
  - Text Primary: `#0F172A`, Text Secondary: `#64748B`
  - Status: Hijau `#16A34A` (Lunas / Kursi Kosong), Biru `#2563EB` (Kursi Dipilih), Amber `#D97706` (Dipesan), Merah `#DC2626` (Dibatalkan / Hapus).
- **Spasi & Tipografi**:
  - Wajib beri `EmptyBorder` (padding 16–24px) di setiap panel utama, kartu, dan form agar tidak menempel ke pinggir jendela.
  - Font: `Segoe UI` (fallback `SansSerif`). Judul Halaman Bold 20px, Subjudul Bold 14px, Body 13px.
- **FlatLaf Client Properties**:
  - Gunakan `FlatClientProperties` untuk sudut membulat (`arc: 10`), `PLACEHOLDER_TEXT` pada input, dan gaya tombol modern.
- **Tabel & Interaksi**:
  - Tinggi baris `JTable` minimal 32px, tanpa garis grid vertikal berlebihan, header tebal, dan `Cursor.HAND_CURSOR` pada semua tombol serta menu sidebar.
  - Gunakan `JOptionPane` dengan pesan manusiawi untuk validasi dan konfirmasi aksi hapus/batal.

---

## 6. Aturan Ketat File `.form` NetBeans 8.2 (KRITIS)

Setiap layar di `ui/` dan `ui/admin/` (`MainFrame` dan seluruh `JPanel` layar, **KECUALI** `SeatMapPanel`) WAJIB dibuat sebagai pasangan `.java` + `.form` yang valid di tab **Design** NetBeans 8.2:

1. **Header XML**: Gunakan `<Form version="1.9" maxVersion="1.9" type="org.netbeans.modules.form.forminfo.JPanelFormInfo">` (atau `JFrameFormInfo` untuk `MainFrame`).
2. **DILARANG `GroupLayout`**: Gunakan HANYA `BorderLayout`, `GridLayout`, `FlowLayout`, atau `CardLayout`.
3. **Sinkronisasi Blok GEN**: Blok `// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents` sampai `//GEN-END:initComponents` serta `// Variables declaration - do not modify//GEN-BEGIN:variables` sampai `// End of variables declaration//GEN-END:variables` WAJIB sinkron sempurna dengan struktur XML di file `.form`.
4. **Penamaan Komponen**: Gunakan prefix jelas (`btn`, `txt`, `pwd`, `cmb`, `tbl`, `lbl`, `pnl`, `scr`, `spn`).
5. **Styling & Konten Dinamis di Luar GEN**: Semua pemanggilan `Tema`, `FlatClientProperties`, border kustom, listener tambahan, dan pengisian data WAJIB ditulis di method terpisah `setupStyle()` / `initData()` yang dipanggil setelah `initComponents()`.

---

## 7. Aturan Kode Bersih (Anti-AI Slop)

1. **NOL KOMENTAR**: Jangan tulis komentar apa pun (`//`, `/* */`, maupun Javadoc `/** */`) di seluruh file kode. **SATU-SATUNYA PENGECUALIAN** adalah komentar bawaan NetBeans GUI Builder (`// <editor-fold...`, `//GEN-BEGIN:...`, `//GEN-END:...`, `// Variables declaration...`, `// End of variables declaration...`) yang wajib dipertahankan utuh agar tab Design tidak rusak.
2. **SELF-DOCUMENTING CODE**: Gunakan nama class, method, dan variabel yang jelas dan langsung terbaca maksudnya (misal `hitungTarifPerPenumpang()`, `cariJadwalCocok()`).
3. **ANTI OVER-ENGINEERING**: Tulis kode yang lurus, simpel, dan mudah dipahami mahasiswa. Gunakan *early return* untuk menghindari *nested if* yang dalam.
4. **METHOD PENDEK**: Usahakan satu method maksimal 20–30 baris dan fokus pada satu tugas.
5. **BEBAS KODE SAMPAH**: Dilarang ada `System.out.println` debug, import tidak terpakai, *empty catch block*, atau placeholder `TODO`.