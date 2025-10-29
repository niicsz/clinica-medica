package com.example.clinica_medica.domain.port.out;

import com.example.clinica_medica.domain.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepositoryPort {
  Usuario save(Usuario usuario);

  List<Usuario> findAll();

  Optional<Usuario> findById(String id);

  void deleteById(String id);

  Optional<Usuario> findByEmail(String email);

  Optional<Usuario> findByCpf(String cpf);
}
