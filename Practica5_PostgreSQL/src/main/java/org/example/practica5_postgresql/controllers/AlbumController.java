package org.example.practica5_postgresql.controllers;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.practica5_postgresql.models.Album;
import org.example.practica5_postgresql.models.AlbumMongo;
import org.example.practica5_postgresql.models.Grupo;
import org.example.practica5_postgresql.repositories.AlbumRepository;
import org.example.practica5_postgresql.repositories.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/albums")
public class AlbumController {
    private final AlbumRepository albumRepository;
    private final GrupoRepository grupoRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public AlbumController(AlbumRepository albumRepository, GrupoRepository grupoRepository){
        this.albumRepository = albumRepository;
        this.grupoRepository = grupoRepository;
        this.restTemplate = new RestTemplate();
    }

    @Operation(summary = "Guarda un Album", description = "Guarda un Album en PostgreSQL y devuelve un ResponseEntity en función de si se guardó correctamente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Album guardado correctamente."),
                    @ApiResponse(responseCode = "400", description = "<ul><li>El ID y el título no pueden estar vacíos.</li><li>El ID ya existe.</li><li>El ID del grupo no existe</li></ul>")
            }
    )
    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Album album){
        if (albumRepository.existsById(album.getId())){
            return ResponseEntity.badRequest().body("El ID ya existe.");
        }
        if (!comprobarCampos(album.getId(), album.getTitulo())){
            return ResponseEntity.badRequest().body("El ID y el título no pueden estar vacíos");
        }

        Grupo grupoTemp = grupoRepository.findById(album.getIdGrupoText()).orElse(null);
        if (grupoTemp == null){
            return ResponseEntity.badRequest().body("El ID del grupo no existe");
        } else {
            album.setIdGrupo(grupoTemp);
        }

        albumRepository.save(album);
        return ResponseEntity.ok("Album guardado.");
    }

    @Operation(summary = "Elimina un album", description = "Elimina un grupo de PostgreSQL y devuelve un ResponseEntity en función de si se eliminó correctamente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Grupo eliminado correctamente."),
                    @ApiResponse(responseCode = "400", description = "El ID no existe.")
            }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable String id){
        if (!albumRepository.existsById(id)){
            return ResponseEntity.badRequest().body("El ID no existe.");
        }
        albumRepository.deleteById(id);
        return ResponseEntity.ok("Album eliminado.");
    }

    @Operation(summary = "Elimina todos los albums", description = "Elimina todos los albums de PostgreSQL y devuelve un ResponseEntity en función de si se eliminaron correctamente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Albums eliminados correctamente."),
            }
    )
    @DeleteMapping("/delete/todos")
    public ResponseEntity<String> delete(){
        albumRepository.deleteAll();
        return ResponseEntity.ok("Albums eliminados");
    }

    @Operation(summary = "Guarda un Album en PostgreSQL y MongoDB",
            description = "Guarda un Album en PostgreSQL y MongoDB. Devuelve un ResponseEntity en función de si se guardaron correctamente" +
                    "</br></br>Utiliza '/albums/add' de PostgreSQL y '/albums/crear' de MongoDB.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Album guardado correctamente en ambas bbdd."),
                    @ApiResponse(responseCode = "400", description = "<ul><li>El Album no ha sido añadido a PostgreSQL: (motivo).</li><li>El Album no ha sido añadido a MongoDB: (motivo).</li></ul>")
            },
            externalDocs = @ExternalDocumentation(
                    description = "Documentación MongoDB",
                    url = "http://localhost:8080/swagger-ui/index.html#/album-controller/crear_1"
            )
    )
    @PostMapping("/crear")
    public ResponseEntity<String> crear(@RequestBody Album album){
        String respuesta = "";

        ResponseEntity<String> respuestaPostgres = add(album);
        if (respuestaPostgres.getStatusCode() != HttpStatus.OK){
            respuesta += "El album no ha sido añadido a PostgreSQL: "+respuestaPostgres.getBody()+"\n";
        }
        try {
            AlbumMongo albumMongo = new AlbumMongo(album);
            restTemplate.postForEntity("http://localhost:8080/albums/crear", albumMongo, String.class);
        } catch (HttpClientErrorException e){
            respuesta += "El album no ha sido añadido a MongoDB: "+e.getResponseBodyAsString();
        }

        if (!respuesta.isEmpty()){
            return ResponseEntity.badRequest().body(respuesta);
        }
        return ResponseEntity.ok("Album añadido en PostgreSQL y MongoDB");
    }

    @Operation(summary = "Elimina un Album de PostgreSQL y MongoDB",
            description = "Elimina un Album de PostgreSQL y MongoDB. Devuelve un ResponseEntity en función de si se eliminaron correctamente" +
                    "</br></br>Utiliza '/albums/delete/{id}' de PostgreSQL y '/albums/borrar/{id}' de MongoDB.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Album eliminado correctamente de ambas bbdd."),
                    @ApiResponse(responseCode = "400", description = "<ul><li>El Album no ha sido eliminado de PostgreSQL: (motivo).</li><li>El Album no ha sido eliminado de MongoDB: (motivo).</li></ul>")
            },
            externalDocs = @ExternalDocumentation(
                    description = "Documentación MongoDB",
                    url = "http://localhost:8080/swagger-ui/index.html#/album-controller/borrar_1"
            )
    )
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<String> borrar(@PathVariable String id){
        String respuesta = "";

        ResponseEntity<String> respuestaPostgres = delete(id);
        if (respuestaPostgres.getStatusCode() != HttpStatus.OK){
            respuesta += "El album no ha sido eliminado de PostgreSQL: "+respuestaPostgres.getBody()+"\n";
        }
        try {
            restTemplate.delete("http://localhost:8080/albums/borrar/"+id);
        } catch (HttpClientErrorException e){
            respuesta += "El album no ha sido eliminado de MongoDB: "+e.getResponseBodyAsString();
        }

        if (!respuesta.isEmpty()){
            return ResponseEntity.badRequest().body(respuesta);
        }
        return ResponseEntity.ok("Album eliminado de PostgreSQL y MongoDB");
    }

    private Boolean comprobarCampos(String id, String titulo){
        return id != null && !id.isBlank() && titulo != null && !titulo.isBlank();
    }
}
