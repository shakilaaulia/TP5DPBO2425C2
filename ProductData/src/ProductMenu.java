import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMenu extends JFrame {
    public static void main(String[] args) {
        // buat object window
        ProductMenu menu = new ProductMenu();
        // atur ukuran window
        menu.setSize(700, 600);
        // letakkan window di tengah layar
        menu.setLocationRelativeTo(null);
        // isi window
        menu.setContentPane(menu.mainPanel);
        // ubah warna background
        menu.getContentPane().setBackground(Color.WHITE);
        // tampilkan window
        menu.setVisible(true);
        // agar program ikut berhenti saat window diclose
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // index baris yang diklik
    private int selectedIndex = -1;
    // objek untuk koneksi ke database
    private Database database;

    // komponen GUI
    private JPanel mainPanel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField hargaField;
    private JTable productTable;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JComboBox<String> kategoriComboBox;
    private JSlider stokSlider;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JLabel idLabel;
    private JLabel namaLabel;
    private JLabel hargaLabel;
    private JLabel kategoriLabel;
    private JLabel stokLabel;

    // constructor
    public ProductMenu() {
        // inisialisasi objek database
        database = new Database();

        // isi tabel produk dengan data dari database
        productTable.setModel(setTable());

        // ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        // atur isi combo box
        String[] kategoriData = {"???", "Elektronik", "Makanan", "Minuman", "Pakaian", "Alat Tulis"};
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // atur label stok sesuai nilai slider
        stokLabel.setText("Stok: " + stokSlider.getValue());
        stokSlider.addChangeListener(e -> stokLabel.setText("Stok: " + stokSlider.getValue()));

        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // jika tidak ada baris yang dipilih, maka insert data
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    // jika ada baris yang dipilih, maka update data
                    updateData();
                }
            }
        });

        // saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // menampilkan dialog konfirmasi
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Apakah Anda yakin ingin menghapus data ini?",
                        "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                // jika user memilih ya, maka hapus data
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteData();
                }
            }
        });

        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // membersihkan form
                clearForm();
            }
        });

        // saat salah satu baris tabel ditekan
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = productTable.getSelectedRow();

                // simpan value dari tabel ke variabel
                String curId = productTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curNama = productTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curHarga = productTable.getModel().getValueAt(selectedIndex, 3).toString();
                String curKategori = productTable.getModel().getValueAt(selectedIndex, 4).toString();
                String curStok = productTable.getModel().getValueAt(selectedIndex, 5).toString();

                // ubah isi textfield dan combo box sesuai data yang dipilih
                idField.setText(curId);
                namaField.setText(curNama);
                hargaField.setText(curHarga);
                kategoriComboBox.setSelectedItem(curKategori);
                stokSlider.setValue(Integer.parseInt(curStok));

                // nonaktifkan field ID agar tidak bisa diubah
                idField.setEditable(false);
                // ubah button "Add" menjadi "Update"
                addUpdateButton.setText("Update");
                // tampilkan button delete
                deleteButton.setVisible(true);
            }
        });
    }

    // method untuk mengatur model tabel
    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] cols = {"NO", "ID PRODUK", "NAMA", "HARGA", "KATEGORI", "STOK"};
        // buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel tmp = new DefaultTableModel(null, cols);
        try {
            // ambil data dari database
            ResultSet resultSet = database.selectQuery("SELECT * FROM product");
            int i = 1;
            // isi tabel dengan data dari database
            while (resultSet.next()) {
                Object[] row = new Object[6];
                row[0] = i;
                row[1] = resultSet.getString("id");
                row[2] = resultSet.getString("nama");
                row[3] = resultSet.getDouble("harga");
                row[4] = resultSet.getString("kategori");
                row[5] = resultSet.getInt("stok");
                tmp.addRow(row);
                i++;
            }
        } catch (SQLException e) {
            // tangani error jika terjadi masalah saat mengambil data
            JOptionPane.showMessageDialog(null, "Error fetching data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return tmp;
    }

    // method untuk menambahkan data baru
    public void insertData() {
        try {
            // ambil value dari textfield dan combobox
            String id = idField.getText();
            String nama = namaField.getText();
            String harga = hargaField.getText();
            String kategori = kategoriComboBox.getSelectedItem().toString();
            int stok = stokSlider.getValue();


            // validasi input ID tidak boleh duplikat
            ResultSet rs = database.selectQuery("SELECT * FROM product WHERE id='" + id + "'");
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "ID Produk sudah ada!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // buat query untuk insert data
            String sqlQuery = String.format("INSERT INTO product VALUES ('" + id + "', '" + nama + "', '" + harga + "', '" + kategori + "', '" + stok + "')");
            // eksekusi query
            database.insertUpdateDeleteQuery(sqlQuery);

            // update tabel
            productTable.setModel(setTable());
            // bersihkan form
            clearForm();
            // feedback
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");

        } catch (NumberFormatException ex) {
            // tangani error jika harga bukan angka
            JOptionPane.showMessageDialog(null, "Harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            // tangani error jika terjadi masalah saat query
            JOptionPane.showMessageDialog(null, "Error saat menambah data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // method untuk mengubah data yang ada
    public void updateData() {
        try {
            // ambil data dari form
            String id = idField.getText();
            String nama = namaField.getText();
            String harga = hargaField.getText();
            String kategori = kategoriComboBox.getSelectedItem().toString();
            int stok = stokSlider.getValue();

            // validasi input: semua field kecuali ID harus diisi
            if (nama.isEmpty() || harga.isEmpty() || kategori.equals("???")) {
                JOptionPane.showMessageDialog(null, "Semua kolom input harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            // buat query untuk update data
            // Hapus String.format jika hanya ingin menggunakan konkatenasi
            String sql = "UPDATE product SET nama='" + nama + "', harga=" + harga + ", kategori='" + kategori + "', stok=" + stok + " WHERE id='" + id + "'";
            // eksekusi query
            database.insertUpdateDeleteQuery(sql);

            // update tabel
            productTable.setModel(setTable());
            // bersihkan form
            clearForm();
            // feedback
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");

        } catch (NumberFormatException ex) {
            // tangani error jika harga bukan angka
            JOptionPane.showMessageDialog(null, "Harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // method untuk menghapus data
    public void deleteData() {
        // ambil id dari form
        String id = idField.getText();
        // buat query untuk delete data
        String sql = String.format("DELETE FROM product WHERE id='" + id + "'");
        // eksekusi query
        database.insertUpdateDeleteQuery(sql);

        // update tabel
        productTable.setModel(setTable());
        // bersihkan form
        clearForm();
        // feedback
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
    }

    // method untuk membersihkan form
    public void clearForm() {
        // kosongkan semua textfield dan combo box
        idField.setText("");
        namaField.setText("");
        hargaField.setText("");
        kategoriComboBox.setSelectedItem("???");
        stokSlider.setValue(50); // reset slider ke nilai default

        // aktifkan kembali field ID
        idField.setEditable(true);
        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");
        // sembunyikan button delete
        deleteButton.setVisible(false);
        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;
    }
}