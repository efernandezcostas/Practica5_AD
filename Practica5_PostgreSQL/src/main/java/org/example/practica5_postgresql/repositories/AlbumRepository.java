package org.example.practica5_postgresql.repositories;

import org.example.practica5_postgresql.models.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, String> {
}
