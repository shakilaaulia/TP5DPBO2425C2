# TP5DPBO2425C2 
## JANJI
Saya Shakila Aulia dengan NIM 2403086 mengerjakan Tugas Praktikum 4 dalam mata kuliah Desain dan Pemograman Berorientasi Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin

---
## Desain dan Alur Program
**Desain Program**

Program ini merupakan implementasi game sederhana Flappy Bird menggunakan Java GUI (Swing).
Terdapat beberapa kelas utama yaitu:
1. App
   - Kelas utama (main class) yang menjalankan program.
   - Membuat objek Logic dan menginisialisasi window game.
   - Mengatur ukuran, judul, serta menampilkan frame utama.
   
2. Logic
   - Mengatur seluruh logika permainan (inti dari game loop).
   - Meng-handle Gerakan burung (gravitasi, loncatan), Pergerakan pipa (Pipe), Deteksi tabrakan antara pemain dan pipa dan Skor permainan.
   - Mengatur event keyboard saat tombol spasi untuk membuat burung terbang dan ketika mati, bisa tekan tombol R untuk restart.

3. Player
   Merepresentasikan karakter utama (burung).
   - Atribut
      - x, y → posisi burung.
      - width, height → ukuran sprite burung.
      - velocity → kecepatan jatuh/terbang.
   - Method
      - update() → memperbarui posisi berdasarkan gravitasi.
      - jump() → memberi gaya ke atas saat pemain menekan spasi.
      - draw(Graphics g) → menggambar burung di layar.
4. Pipe
   Merepresentasikan rintangan (pipa atas & bawah).
   - Atribut
      - x → posisi horizontal.
      - width, height → ukuran pipa.
      - isTop → menentukan apakah pipa di atas atau bawah.
   - Method
      - update() → menggerakkan pipa ke kiri layar.
      - draw(Graphics g) → menggambar pipa.
      - collides(Player p) → mendeteksi tabrakan dengan pemain.

**Alur Program**

Penjelasan Alur dari program:
1. Saat program dijalankan, App membuat window utama dan menampilkan menu.
2. Ketika menekan tomol start langsung masuk ke game utama (memanggil logic).
3. Di dalam Logic, game mulai dalam keadaan idle (belum mulai).
4. Ketika user menekan spasi, permainan dimulai.
5. Burung mulai jatuh akibat gravitasi.
6. Pipa mulai bergerak dari kiri ke kanan.
7. Pemain harus terus menekan spasi agar burung tidak jatuh dan bisa melewati celah antara pipa atas dan bawah.
8. Setiap kali burung berhasil melewati satu set pipa, skor bertambah satu.
9. Jika burung menabrak pipa atau tanah, permainan berakhir dan status berubah ke game over.
10. Pemain dapat menekan tombol R untuk restart permainan dari awal.
     
---
## DOKUMENTASI
**Tampilan Output**

![Tampilan Output](TP6.gif)

