package com.example.clinica_medica.application.port.in;

import com.example.clinica_medica.domain.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioUseCase {
  Usuario incluirUsuario(Usuario usuario);

  List<Usuario> listarTodosUsuarios();

  Usuario atualizarUsuario(String id, Usuario usuario);

  void excluirUsuario(String id);

  Usuario buscarUsuarioPorId(String id);

  Optional<Usuario> buscarPorEmail(String email);
}
