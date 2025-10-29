package com.example.clinica_medica.infrastructure.persistence.mongodb;

import com.example.clinica_medica.domain.model.Usuario;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataUsuarioRepository extends MongoRepository<Usuario, String> {
  Optional<Usuario> findByCpf(String cpf);

  Optional<Usuario> findByEmail(String email);
}
