package com.example.clinica_medica.repositories;

import com.example.clinica_medica.entities.Usuario;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
  Optional<Usuario> findByCpf(String cpf);

  Optional<Usuario> findByEmail(String email);
}
