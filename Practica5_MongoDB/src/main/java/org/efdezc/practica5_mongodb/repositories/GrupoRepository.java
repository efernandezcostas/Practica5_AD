package org.efdezc.practica5_mongodb.repositories;

import org.efdezc.practica5_mongodb.models.Grupo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GrupoRepository extends MongoRepository<Grupo, String> {
}
