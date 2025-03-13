package org.efdezc.practica5_mongodb.repositories;

import org.efdezc.practica5_mongodb.models.Album;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlbumRepository extends MongoRepository<Album, String> {
}
