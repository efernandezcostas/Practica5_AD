package org.example.practica5_postgresql.controllers;

import org.example.practica5_postgresql.models.Grupo;
import org.example.practica5_postgresql.repositories.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grupos")
public class GrupoController {
    private final GrupoRepository grupoRepository;

    @Autowired
    public GrupoController(GrupoRepository grupoRepository){
        this.grupoRepository = grupoRepository;
    }

    // Solo en PostgreSQL
    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Grupo grupo){
        if (grupoRepository.existsById(grupo.getId())){
            return ResponseEntity.badRequest().body("El ID ya existe.");
        }
        if (!comprobarCampos(grupo.getId(), grupo.getNombre())){
            return ResponseEntity.badRequest().body("El ID y el nombre no pueden estar vacíos.");
        }
        grupoRepository.save(grupo);
        return ResponseEntity.ok("Grupo guardado.");
    }

    // Solo en PostgreSQL
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable String id){
        if (!grupoRepository.existsById(id)){
            return ResponseEntity.badRequest().body("El ID no existe.");
        }
        grupoRepository.deleteById(id);
        return ResponseEntity.ok("Grupo eliminado.");
    }

    // PostgreSQL y MongoDB
    @PostMapping("/crear")
    public void crear(){

    }

    // PostgreSQL y MongoDB
    @DeleteMapping("/borrar/{id}")
    public void borrar(@PathVariable String id){

    }

    private Boolean comprobarCampos(String id, String nombre){
        return id != null && !id.isBlank() && nombre != null && !nombre.isBlank();
    }
}
