package org.example.practica5_postgresql.controllers;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Guarda un Grupo", description = "Guarda un Grupo en PostgreSQL y devuelve un ResponseEntity en función de si se creó correctamente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Grupo guardado correctamente."),
                    @ApiResponse(responseCode = "400", description = "<ul><li>El ID y el nombre no pueden estar vacíos.</li><li>El ID ya existe.</li></ul>")
            }
    )
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

    @Operation(summary = "Elimina un grupo", description = "Elimina un grupo de PostgreSQL y devuelve un ResponseEntity en función de si se eliminó correctamente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Grupo eliminado correctamente."),
                    @ApiResponse(responseCode = "400", description = "El ID no existe.")
            }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable String id){
        if (!grupoRepository.existsById(id)){
            return ResponseEntity.badRequest().body("El ID no existe.");
        }
        grupoRepository.deleteById(id);
        return ResponseEntity.ok("Grupo eliminado.");
    }

    @Operation(summary = "Elimina todos los grupos", description = "Elimina todos los grupos de PostgreSQL y devuelve un ResponseEntity en función de si se eliminaron correctamente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Grupos eliminados correctamente."),
            }
    )
    @DeleteMapping("/delete/todos")
    public ResponseEntity<String> delete(){
        grupoRepository.deleteAll();
        return ResponseEntity.ok("Grupos eliminados");
    }

    @Operation(summary = "Guarda un Grupo en PostgreSQL y MongoDB",
            description = "Guardo un Grupo en PostgreSQL y MongoDB. Devuelve un ResponseEntity en función de si se creó correctamente" +
                    "</br></br>Utiliza '/grupos/add' de PostgreSQL y '/grupos/crear' de MongoDB.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Grupo guardado correctamente en ambas bbdd."),
                    @ApiResponse(responseCode = "400", description = "<ul><li>El grupo no ha sido añadido a PostgreSQL: (motivo).</li><li>El grupo no ha sido añadido a MongoDB: (motivo).</li></ul>")
            },
            externalDocs = @ExternalDocumentation(
                    description = "Documentación MongoDB",
                    url = "http://localhost:8080/swagger-ui/index.html#/grupo-controller/crear"
            )
    )
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

    @Operation(summary = "Elimina un Grupo de PostgreSQL y MongoDB",
            description = "Elimina un Grupo de PostgreSQL y MongoDB. Devuelve un ResponseEntity en función de si se eliminó correctamente" +
                    "</br></br>Utiliza '/grupos/delete/{id}' de PostgreSQL y '/grupos/borrar/{id}' de MongoDB.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Grupo eliminado correctamente de ambas bbdd."),
                    @ApiResponse(responseCode = "400", description = "<ul><li>El grupo no ha sido eliminado de PostgreSQL: (motivo).</li><li>El grupo no ha sido eliminado de MongoDB: (motivo).</li></ul>")
            },
            externalDocs = @ExternalDocumentation(
                    description = "Documentación MongoDB",
                    url = "http://localhost:8080/swagger-ui/index.html#/grupo-controller/borrar"
            )
    )
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<String> borrar(@PathVariable String id){
        String respuesta = "";

        ResponseEntity<String> respuestaPostgres = delete(id);
        if (respuestaPostgres.getStatusCode() != HttpStatus.OK){
            respuesta += "El grupo no ha sido eliminado de PostgreSQL: "+respuestaPostgres.getBody()+"\n";
        }
        try {
            restTemplate.delete("http://localhost:8080/grupos/borrar/"+id);
        } catch (HttpClientErrorException e){
            respuesta += "El grupo no ha sido eliminado de MongoDB: "+e.getResponseBodyAsString();
        }

        if (!respuesta.isEmpty()){
            return ResponseEntity.badRequest().body(respuesta);
        }
        return ResponseEntity.ok("Grupo eliminado de PostgreSQL y MongoDB");
    }

    private Boolean comprobarCampos(String id, String nombre){
        return id != null && !id.isBlank() && nombre != null && !nombre.isBlank();
    }
}
