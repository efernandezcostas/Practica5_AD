package org.example.practica5_postgresql.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Albums")
public class Album {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "idgrupo")
    private String idGrupo;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "datalanzamiento")
    private Date dataLanzamiento;

    @Column(precision = 3, scale = 1)
    private Double puntuacion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getDataLanzamiento() {
        return dataLanzamiento;
    }

    public void setDataLanzamiento(Date dataLanzamiento) {
        this.dataLanzamiento = dataLanzamiento;
    }

    public Double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Double puntuacion) {
        this.puntuacion = puntuacion;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id='" + id + '\'' +
                ", idGrupo='" + idGrupo + '\'' +
                ", titulo='" + titulo + '\'' +
                ", dataLanzamiento=" + dataLanzamiento +
                ", puntuacion=" + puntuacion +
                '}';
    }
}
