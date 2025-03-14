package org.efdezc.practica5_mongodb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Guarda un Grupo", description = "Guarda un Grupo en MongoDB y devuelve un ResponseEntity en función de si se creó correctamente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Grupo guardado correctamente."),
                    @ApiResponse(responseCode = "400", description = "<ul><li>El ID y el nombre no pueden estar vacíos.</li><li>El ID ya existe.</li></ul>")
            }
    )
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

    @Operation(summary = "Elimina un grupo", description = "Elimina un grupo de MongoDB y devuelve un ResponseEntity en función de si se eliminó correctamente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Grupo eliminado correctamente."),
                    @ApiResponse(responseCode = "400", description = "El ID no existe.")
            }
    )
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<String> borrar(@PathVariable String id){
        if ( ! grupoRepository.existsById(id)){
            return ResponseEntity.badRequest().body("El ID no existe");
        }
        grupoRepository.deleteById(id);
        return ResponseEntity.ok("Grupo eliminado");
    }

    @Operation(summary = "Elimina todos los grupos", description = "Elimina todos los grupos de MongoDB y devuelve un ResponseEntity en función de si se eliminaron correctamente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Grupos eliminados correctamente."),
            }
    )
    @DeleteMapping("/borrar/todos")
    public ResponseEntity<String> borrarTodos(){
        grupoRepository.deleteAll();
        return ResponseEntity.ok("Grupos eliminados");
    }

    @Operation(summary = "Actualiza un grupo", description = "Actualiza un grupo de MongoDB y devuelve un ResponseEntity en función de si se actualizó correctamente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Grupo guardado correctamente."),
                    @ApiResponse(responseCode = "400", description = "No se puede cambiar el ID.")
            }
    )
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<String> actualizar(@PathVariable String id, @RequestBody Grupo grupo){
        if (grupo.getId() != null) {
            return ResponseEntity.badRequest().body("No se puede cambiar el ID.");
        }
        grupo.setId(id);
        grupoRepository.save(grupo);
        return ResponseEntity.ok("Grupo actualizado");
    }

    @Operation(summary = "Lista los grupos", description = "Lista los grupos de MongoDB.")
    @GetMapping("/listar")
    public List<Grupo> listar(){
        return grupoRepository.findAll();
    }

    @Operation(summary = "Lista un grupo", description = "Lista un grupo de MongoDB si existe.")
    @GetMapping("/listar/{id}")
    public Grupo listar(@PathVariable String id){
        Optional<Grupo> grupoOptional =  grupoRepository.findById(id);
        return grupoOptional.orElse(null);
    }

    private Boolean comprobarCampos(String id, String nombre){
        return id != null && !id.isBlank() && nombre!=null && !nombre.isBlank();
    }
}
