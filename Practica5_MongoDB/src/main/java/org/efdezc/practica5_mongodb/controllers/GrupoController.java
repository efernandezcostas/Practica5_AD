package org.efdezc.practica5_mongodb.controllers;

import org.efdezc.practica5_mongodb.models.Grupo;
import org.efdezc.practica5_mongodb.repositories.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/grupos")
public class GrupoController {
    private final GrupoRepository grupoRepository;

    @Autowired
    public GrupoController(GrupoRepository grupoRepository){
        this.grupoRepository = grupoRepository;
    }

    @PostMapping("/crear")
    public ResponseEntity<String> crear(@RequestBody Grupo grupo){
        if ( ! comprobarCampos(grupo.getId(), grupo.getNombre())){
            return ResponseEntity.badRequest().body("El ID y el nombre no pueden estar vacíos");
        }
        if (grupoRepository.existsById(grupo.getId())){
            return ResponseEntity.badRequest().body("El ID ya existe");
        }
        grupoRepository.save(grupo);
        return ResponseEntity.ok("Grupo creado");
    }

    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<String> borrar(@PathVariable String id){
        if ( ! grupoRepository.existsById(id)){
            return ResponseEntity.badRequest().body("El ID no existe");
        }
        grupoRepository.deleteById(id);
        return ResponseEntity.ok("Grupo eliminado");
    }

    @DeleteMapping("/borrar/todos")
    public ResponseEntity<String> borrarTodos(){
        grupoRepository.deleteAll();
        return ResponseEntity.ok("Grupos eliminados");
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<String> actualizar(@PathVariable String id, @RequestBody Grupo grupo){
        if (grupo.getId() != null) {
            return ResponseEntity.badRequest().body("No se puede cambiar el ID.");
        }
        grupo.setId(id);
        grupoRepository.save(grupo);
        return ResponseEntity.ok("Grupo actualizado");
    }

    @GetMapping("/listar")
    public List<Grupo> listar(){
        return grupoRepository.findAll();
    }

    @GetMapping("/listar/{id}")
    public Grupo listar(@PathVariable String id){
        Optional<Grupo> grupoOptional =  grupoRepository.findById(id);
        return grupoOptional.orElse(null);
    }

    private Boolean comprobarCampos(String id, String nombre){
        return id != null && !id.isBlank() && nombre!=null && !nombre.isBlank();
    }
}
