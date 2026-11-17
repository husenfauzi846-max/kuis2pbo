package com.mycompany.mainbioskop;

import java.io.*;
import java.util.*;

public class SistemBioskop implements Serializable {
    private static final long serialVersionUID = 1L;
    private String namaBioskop;
    private HashMap<String, Penonton> daftarPenonton;
    private HashMap<String, JadwalFilm> daftarJadwal;
    private ArrayList<String> riwayatTransaksi;

    public SistemBioskop(String namaBioskop) {
        this.namaBioskop = namaBioskop;
        this.daftarPenonton = new HashMap<>();
        this.daftarJadwal = new HashMap<>();
        this.riwayatTransaksi = new ArrayList<>();
    }

    public void tambahPenonton(String id, String nama) {
        daftarPenonton.put(id, new Penonton(id, nama));
    }

    public void tambahJadwalFilm(String kode, String namaFilm, int kuota) {
        daftarJadwal.put(kode, new JadwalFilm(kode, namaFilm, kuota));
    }

    // Pembacaan dan Parsing File Transaksi 
    public void jalankanTransaksiDariFile(String namaFile) throws Exception {
        File file = new File(namaFile);
        if (!file.exists()) {
            throw new FileNotFoundException("File " + namaFile + " tidak ditemukan!");
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String baris = reader.readLine();
        reader.close();

        if (baris == null || baris.trim().isEmpty()) {
            return;
        }

        // Format Varian 1: ID-ACT-01/KD-OBJ-A/JML-3/|ID-ACT-02/
        String[] transaksiArray = baris.split("\\|");

        for (String trx : transaksiArray) {
            try {
                String[] bagian = trx.split("/");
                
                String idPenonton = bagian[0].split("-")[1] + "-" + bagian[0].split("-")[2];       // ACT-01
                String kodeFilm = bagian[1].split("-")[1] + "-" + bagian[1].split("-")[2];         // OBJ-A
                int jumlah = Integer.parseInt(bagian[2].split("-")[1]);                            // 3

                if (!daftarPenonton.containsKey(idPenonton)) {
                    throw new TransaksiException("Penonton dengan ID " + idPenonton + " tidak terdaftar!");
                }

                if (!daftarJadwal.containsKey(kodeFilm)) {
                    throw new TransaksiException("Kode film " + kodeFilm + " tidak ditemukan!");
                }

                JadwalFilm film = daftarJadwal.get(kodeFilm);
                if (film.getKuota() < jumlah) {
                    throw new TransaksiException("Kuota film " + film.getNamaFilm() + " tidak mencukupi!");
                }

                film.setKuota(film.getKuota() - jumlah);
                riwayatTransaksi.add("Sukses: " + idPenonton + " memesan " + kodeFilm + " sebanyak " + jumlah);
                System.out.println("[TRANSAKSI BERHASIL] ID: " + idPenonton + " | Film: " + kodeFilm + " | Qty: " + jumlah);

            } catch (Exception e) {
                System.out.println("[GAGAL TRANSAKSI] " + e.getMessage());
            }
        }
    }

    // Metode Laporan Akhir & Serialisasi
    public void simpanLaporanAkhir() {
        // Object Serialization ke file snapshot.obj
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("snapshot.obj"))) {
            oos.writeObject(this);
            System.out.println("\n[INFO] Objek sistem berhasil diserialisasi ke 'snapshot.obj'");
        } catch (IOException e) {
            System.out.println("[ERROR SERIALIZATION] " + e.getMessage());
        }

        // File I/O Teks (laporan.txt) menggunakan .keySet() dari HashMap
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("laporan.txt"))) {
            writer.write("=== LAPORAN DAFTAR KUNCI ID PENONTON (.keySet()) ===\n");
            for (String id : daftarPenonton.keySet()) {
                writer.write(id + "\n");
            }
            
            writer.write("\n=== LAPORAN DAFTAR KUNCI KODE JADWAL FILM (.keySet()) ===\n");
            for (String kode : daftarJadwal.keySet()) {
                writer.write(kode + "\n");
            }
            System.out.println("[INFO] Laporan teks berhasil disimpan ke 'laporan.txt'");
        } catch (IOException e) {
            System.out.println("[ERROR FILE I/O] " + e.getMessage());
        }
    }
}