package com.servicosweb.apirest.joao.services;

import com.servicosweb.apirest.joao.dtos.usuario.UsuarioRequestDTO;
import com.servicosweb.apirest.joao.dtos.usuario.UsuarioResponseDTO;
import com.servicosweb.apirest.joao.entities.Usuario;
import com.servicosweb.apirest.joao.enums.Role;
import com.servicosweb.apirest.joao.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private final UsuarioRepository usuarioRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("Email já cadastrado.");
        }

        Usuario novoUsuario = Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(passwordEncoder.encode(dto.senha()))
                .role(dto.role() != null ? dto.role() : Role.USER)
                .build();

        usuarioRepository.save(novoUsuario);
        return mapToResponse(novoUsuario);
    }

    @Transactional
    public UsuarioResponseDTO atualizarRole(String emailAlvo, Role novaRole, Usuario executor) {
        Usuario alvo = usuarioRepository.findByEmail(emailAlvo)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        if (alvo.getRole() == Role.ADMIN && novaRole == Role.USER) {
            throw new RuntimeException("Operação negada: Um ADMIN não pode ser rebaixado.");
        }

        if (novaRole == Role.ADMIN) {
            alvo.setRole(Role.ADMIN);
        }

        return mapToResponse(usuarioRepository.save(alvo));
    }

    @Transactional
    public void deletarConta(String emailParaDeletar, Usuario executor) {
        Usuario alvo = usuarioRepository.findByEmail(emailParaDeletar)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        if (executor.getEmail().equals(alvo.getEmail()) || executor.getRole() == Role.ADMIN) {
            usuarioRepository.delete(alvo);
        } else {
            throw new RuntimeException("Sem permissão para deletar esta conta.");
        }
    }

    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private UsuarioResponseDTO mapToResponse(Usuario u) {
        return new UsuarioResponseDTO(u.getNome(), u.getEmail(), u.getRole(), u.getCreatedAt());
    }
}