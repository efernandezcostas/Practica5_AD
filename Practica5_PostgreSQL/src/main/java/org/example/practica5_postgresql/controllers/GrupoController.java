package org.example.practica5_postgresql.controllers;

import org.example.practica5_postgresql.models.Grupo;
import org.example.practica5_postgresql.repositories.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/grupos")
public class GrupoController {
    private final GrupoRepository grupoRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public GrupoController(GrupoRepository grupoRepository){
        this.grupoRepository = grupoRepository;
        this.restTemplate = new RestTemplate();
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

    @DeleteMapping("/delete/todos")
    public ResponseEntity<String> delete(){
        grupoRepository.deleteAll();
        return ResponseEntity.ok("Grupos eliminados");
    }

    // PostgreSQL y MongoDB
    @PostMapping("/crear")
    public ResponseEntity<String> crear(@RequestBody Grupo grupo){
        String respuesta = "";

        ResponseEntity<String> respuestaPostgres = add(grupo);
        if (respuestaPostgres.getStatusCode() != HttpStatus.OK){
            respuesta += "El grupo no ha sido añadido a PostgreSQL: "+respuestaPostgres.getBody()+"\n";
        }
        try {
            restTemplate.postForEntity("http://localhost:8080/grupos/crear", grupo, String.class);
        } catch (HttpClientErrorException e){
            respuesta += "El grupo no ha sido añadido a MongoDB: "+e.getResponseBodyAsString();
        }

        if (!respuesta.isEmpty()){
            return ResponseEntity.badRequest().body(respuesta);
        }

        return ResponseEntity.ok("Grupo añadido en PostgreSQL y MongoDB");
    }

    // PostgreSQL y MongoDB
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<String> borrar(@PathVariable String id){
        delete(id);
        restTemplate.delete("http://localhost:8080/grupos/borrar/"+id);
        return ResponseEntity.ok("Grupo eliinado en PostgreSQL y MongoDB");
    }

    private Boolean comprobarCampos(String id, String nombre){
        return id != null && !id.isBlank() && nombre != null && !nombre.isBlank();
    }
}
