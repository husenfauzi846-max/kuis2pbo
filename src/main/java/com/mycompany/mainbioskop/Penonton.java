package com.mycompany.mainbioskop;

import java.io.Serializable;

public class Penonton implements Serializable {
    private static final long serialVersionUID = 1L;
    private String idPenonton;
    private String nama;

    public Penonton(String idPenonton, String nama) {
        this.idPenonton = idPenonton;
        this.nama = nama;
    }

    public String getIdPenonton() {
        return idPenonton;
    }

    public String getNama() {
        return nama;
    }
}