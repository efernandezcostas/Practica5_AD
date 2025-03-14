package org.efdezc.practica5_mongodb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Guarda un Album", description = "Guarda un Album en MongoDB y devuelve un ResponseEntity en función de si se creó correctamente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Album guardado correctamente."),
                    @ApiResponse(responseCode = "400", description = "<ul><li>id, idGrupo y titulo no pueden estar vacíos.</li><li>El ID ya existe.</li><li>El ID del grupo no existe.</li></ul>")
            }
    )
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

    @Operation(summary = "Elimina un Album", description = "Elimina un Album de MongoDB y devuelve un ResponseEntity en función de si se eliminó correctamente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Album eliminado correctamente."),
                    @ApiResponse(responseCode = "400", description = "El ID no existe.")
            }
    )
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<String> borrar(@PathVariable String id){
        if ( ! albumRepository.existsById(id)){
            return ResponseEntity.badRequest().body("El ID no existe");
        }
        albumRepository.deleteById(id);
        return ResponseEntity.ok("Album eliminado");
    }

    @Operation(summary = "Elimina todos los Album", description = "Elimina todos los Album de MongoDB y devuelve un ResponseEntity en función de si se eliminaron correctamente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Albums eliminados correctamente."),
            }
    )
    @DeleteMapping("/borrar/todos")
    public ResponseEntity<String> borrar(){
        albumRepository.deleteAll();
        return ResponseEntity.ok("Albums eliminados");
    }

    @Operation(summary = "Actualiza un Album", description = "Actualiza un Album de MongoDB y devuelve un ResponseEntity en función de si se actualizó correctamente",
            responses = {
                @ApiResponse(responseCode = "200", description = "Album guardado correctamente."),
                @ApiResponse(responseCode = "400", description = "<ul><li>No se puede cambiar el ID.</li><li>El ID del grupo no existe.</li></ul>")
            }
    )
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

    @Operation(summary = "Lista los Albums", description = "Lista los Albums de MongoDB.")
    @GetMapping("/listar")
    public List<Album> listar(){
        return albumRepository.findAll();
    }

    @Operation(summary = "Lista los Albums", description = "Lista un Album de MongoDB, si existe.")
    @GetMapping("/listar/{id}")
    public Album listar(@PathVariable String id){
        Optional<Album> albumOptional =  albumRepository.findById(id);
        return albumOptional.orElse(null);
    }

    private Boolean comprobarCampos(String id, String idGrupo, String titulo){
        return id != null && !id.isBlank() && idGrupo != null && !idGrupo.isBlank() && titulo!=null && !titulo.isBlank();
    }
}
