package org.example.practica5_postgresql.repositories;

import org.example.practica5_postgresql.models.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoRepository extends JpaRepository<Grupo, String> {
}
