package com.example.clinica_medica.infrastructure.persistence.mongodb;

import com.example.clinica_medica.domain.model.Usuario;
import com.example.clinica_medica.domain.port.out.UsuarioRepositoryPort;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class MongoUsuarioRepositoryAdapter implements UsuarioRepositoryPort {

  private final SpringDataUsuarioRepository repository;

  public MongoUsuarioRepositoryAdapter(SpringDataUsuarioRepository repository) {
    this.repository = repository;
  }

  @Override
  public Usuario save(Usuario usuario) {
    return repository.save(usuario);
  }

  @Override
  public List<Usuario> findAll() {
    return repository.findAll();
  }

  @Override
  public Optional<Usuario> findById(String id) {
    return repository.findById(id);
  }

  @Override
  public void deleteById(String id) {
    repository.deleteById(id);
  }

  @Override
  public Optional<Usuario> findByEmail(String email) {
    return repository.findByEmail(email);
  }

  @Override
  public Optional<Usuario> findByCpf(String cpf) {
    return repository.findByCpf(cpf);
  }
}
