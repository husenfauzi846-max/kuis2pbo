package com.mycompany.mainbioskop;

import java.io.Serializable;

public class JadwalFilm implements Serializable {
    private static final long serialVersionUID = 1L;
    private String kodeFilm;
    private String namaFilm;
    private int kuota;

    public JadwalFilm(String kodeFilm, String namaFilm, int kuota) {
        this.kodeFilm = kodeFilm;
        this.namaFilm = namaFilm;
        this.kuota = kuota;
    }

    public String getKodeFilm() {
        return kodeFilm;
    }

    public String getNamaFilm() {
        return namaFilm;
    }

    public int getKuota() {
        return kuota;
    }

    public void setKuota(int kuota) {
        this.kuota = kuota;
    }
}