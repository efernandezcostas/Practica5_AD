package org.efdezc.practica5_mongodb.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "albums")
public class Album {
    @Id
    private String id;
    private String idGrupo;
    private String titulo;
    private Date dataLanzamiento;
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
