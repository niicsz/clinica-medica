package com.example.clinica_medica.repositories;

import com.example.clinica_medica.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  Usuario findByCpf(String cpf);
}
