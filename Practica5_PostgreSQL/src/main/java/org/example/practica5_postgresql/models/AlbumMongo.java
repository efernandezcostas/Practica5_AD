package org.example.practica5_postgresql.models;

import java.math.BigDecimal;
import java.util.Date;

public class AlbumMongo {
    private String id;
    private String idGrupo;
    private String titulo;
    private Date dataLanzamiento;
    private BigDecimal puntuacion;

    public AlbumMongo(Album album){
        id = album.getId();
        idGrupo = album.getIdGrupoText();
        titulo = album.getTitulo();
        dataLanzamiento = album.getDataLanzamiento();
        puntuacion = album.getPuntuacion();
    }

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

    public BigDecimal getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(BigDecimal puntuacion) {
        this.puntuacion = puntuacion;
    }

    @Override
    public String toString() {
        return "AlbumMongo{" +
                "id='" + id + '\'' +
                ", idGrupo='" + idGrupo + '\'' +
                ", titulo='" + titulo + '\'' +
                ", dataLanzamiento=" + dataLanzamiento +
                ", puntuacion=" + puntuacion +
                '}';
    }
}
