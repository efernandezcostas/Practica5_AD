package org.efdezc.practica5_mongodb.controllers;

import org.efdezc.practica5_mongodb.models.Album;
import org.efdezc.practica5_mongodb.repositories.AlbumRepository;
import org.efdezc.practica5_mongodb.repositories.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @PostMapping("/crear")
    public ResponseEntity<String> crear(@RequestBody Album album){
        if ( ! comprobarCampos(album.getId(), album.getIdGrupo(), album.getTitulo())){
            return ResponseEntity.badRequest().body("id, idGrupo y titulo no pueden estar vacíos");
        }
        if (albumRepository.existsById(album.getId())){
            return ResponseEntity.badRequest().body("El ID ya existe");
        }
        if ( ! grupoRepository.existsById(album.getIdGrupo())){
            return ResponseEntity.badRequest().body("El ID del grupo no existe");
        }
        albumRepository.save(album);
        return ResponseEntity.ok("Album creado");
    }

    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<String> borrar(@PathVariable String id){
        if ( ! albumRepository.existsById(id)){
            return ResponseEntity.badRequest().body("El ID no existe");
        }
        albumRepository.deleteById(id);
        return ResponseEntity.ok("Album eliminado");
    }

    @DeleteMapping("/borrar/todos")
    public ResponseEntity<String> borrar(){
        albumRepository.deleteAll();
        return ResponseEntity.ok("Albums eliminados");
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<String> actualizar(@PathVariable String id, @RequestBody Album album){
        if (album.getId() != null) {
            return ResponseEntity.badRequest().body("No se puede cambiar el ID.");
        }
        if (!grupoRepository.existsById(album.getIdGrupo())){
            return ResponseEntity.badRequest().body("El ID del grupo no existe");
        }

        album.setId(id);
        albumRepository.save(album);
        return ResponseEntity.ok("Album actualizado");
    }

    @GetMapping("/listar")
    public List<Album> listar(){
        return albumRepository.findAll();
    }

    @GetMapping("/listar/{id}")
    public Album listar(@PathVariable String id){
        Optional<Album> albumOptional =  albumRepository.findById(id);
        return albumOptional.orElse(null);
    }

    private Boolean comprobarCampos(String id, String idGrupo, String titulo){
        return id != null && !id.isBlank() && idGrupo != null && !idGrupo.isBlank() && titulo!=null && !titulo.isBlank();
    }
}
