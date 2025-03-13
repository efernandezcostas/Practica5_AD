package org.example.practica5_postgresql.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "Grupos")
public class Grupo {
    @Id
    private String id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "genero", length = 100)
    private String genero;

    @Column(name = "dataformacion")
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
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", genero='" + genero + '\'' +
                ", dataFormacion=" + dataFormacion +
                '}';
    }
}
