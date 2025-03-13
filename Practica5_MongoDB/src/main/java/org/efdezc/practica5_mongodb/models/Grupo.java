package org.efdezc.practica5_mongodb.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "grupos")
public class Grupo {
    @Id
    private String id;
    private String nombre;
    private String genero;
    private Date dataFormacion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Date getDataFormacion() {
        return dataFormacion;
    }

    public void setDataFormacion(Date dataFormacion) {
        this.dataFormacion = dataFormacion;
    }

    @Override
    public String toString() {
        return "Grupo{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", genero='" + genero + '\'' +
                ", dataFormacion=" + dataFormacion +
                '}';
    }
}
