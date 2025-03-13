package org.example.practica5_postgresql.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "Albums")
public class Album {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "idgrupo")
    private Grupo idGrupo;

    @Transient
    private String idGrupoText;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "datalanzamiento")
    private Date dataLanzamiento;

    @Column(name = "puntuacion", precision = 3, scale = 1)
    private BigDecimal puntuacion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Grupo getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Grupo idGrupo) {
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

    public BigDecimal getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(BigDecimal puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getIdGrupoText() {
        return idGrupoText;
    }

    public void setIdGrupoText(String idGrupoText) {
        this.idGrupoText = idGrupoText;
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
