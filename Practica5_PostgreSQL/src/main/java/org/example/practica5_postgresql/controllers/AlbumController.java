package org.example.practica5_postgresql.controllers;

import org.example.practica5_postgresql.models.Album;
import org.example.practica5_postgresql.repositories.AlbumRepository;
import org.example.practica5_postgresql.repositories.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/albums")
public class AlbumController {
    private final AlbumRepository albumRepository;
    private final GrupoRepository grupoRepository;

    @Autowired
    public AlbumController(AlbumRepository albumRepository, GrupoRepository grupoRepository){
        this.albumRepository = albumRepository;
        this.grupoRepository = grupoRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Album album){
        if (albumRepository.existsById(album.getId())){
            return ResponseEntity.badRequest().body("El ID ya existe.");
        }
        if (!grupoRepository.existsById(album.getIdGrupo())){
            return ResponseEntity.badRequest().body("El ID del grupo no existe");
        }
        if (!comprobarCampos(album.getId(), album.getTitulo())){
            return ResponseEntity.badRequest().body("El ID y el título no pueden estar vacíos");
        }
        albumRepository.save(album);
        return ResponseEntity.ok("Album guardado.");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable String id){
        if (!albumRepository.existsById(id)){
            return ResponseEntity.badRequest().body("El ID no existe.");
        }
        albumRepository.deleteById(id);
        return ResponseEntity.ok("Album eliminado.");
    }

    public void crear(){}

    public void borrar(){}

    private Boolean comprobarCampos(String id, String titulo){
        return id != null && !id.isBlank() && titulo != null && titulo.isBlank();
    }
}
