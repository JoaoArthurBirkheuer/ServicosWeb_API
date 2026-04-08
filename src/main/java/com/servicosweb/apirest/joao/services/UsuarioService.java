package com.servicosweb.apirest.joao.services;

import com.servicosweb.apirest.joao.dtos.usuario.UsuarioRequestDTO;
import com.servicosweb.apirest.joao.dtos.usuario.UsuarioResponseDTO;
import com.servicosweb.apirest.joao.entities.Usuario;
import com.servicosweb.apirest.joao.enums.Role;
import com.servicosweb.apirest.joao.exceptions.AcessoNegadoException;
import com.servicosweb.apirest.joao.exceptions.RecursoNaoEncontradoException;
import com.servicosweb.apirest.joao.exceptions.RegraDeNegocioException;
import com.servicosweb.apirest.joao.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.master-key}")
    private String adminMasterKey;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new RegraDeNegocioException("Email já cadastrado.");
        }

        if (Role.ADMIN.equals(dto.role())) {
            if (dto.adminKey() == null || !dto.adminKey().equals(adminMasterKey)) {
                throw new RegraDeNegocioException("Chave mestra inválida. Você não tem permissão para criar um administrador.");
            }
        }

        Usuario novoUsuario = Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(passwordEncoder.encode(dto.senha()))
                .role(dto.role() != null ? dto.role() : Role.USER)
                .build();

        return mapToResponse(usuarioRepository.save(novoUsuario));
    }

    @Transactional
    public UsuarioResponseDTO atualizarMe(UsuarioRequestDTO dto, Usuario logado) {
        Usuario usuario = usuarioRepository.findById(logado.getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));

        if (!usuario.getEmail().equals(dto.email()) && usuarioRepository.existsByEmail(dto.email())) {
            throw new RegraDeNegocioException("Novo email já está em uso.");
        }

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());

        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.senha()));
        }

        return mapToResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponseDTO atualizarRole(String emailAlvo, Role novaRole) {
        Usuario alvo = usuarioRepository.findByEmail(emailAlvo)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));

        if (alvo.getRole() == Role.ADMIN && novaRole == Role.USER) {
            throw new RegraDeNegocioException("Operação negada: Um ADMIN não pode ser rebaixado.");
        }

        alvo.setRole(novaRole);

        return mapToResponse(usuarioRepository.save(alvo));
    }

    public Usuario buscarEntidadePorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new AcessoNegadoException("Usuário autenticado não encontrado."));
    }

    @Transactional
    public void deletarConta(String emailParaDeletar, Usuario executor) {
        Usuario alvo = usuarioRepository.findByEmail(emailParaDeletar)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));

        if (executor.getRole() == Role.ADMIN && executor.getEmail().equals(alvo.getEmail())) {
            throw new RegraDeNegocioException("Um administrador não pode remover a própria conta por este endpoint.");
        }

        if (executor.getEmail().equals(alvo.getEmail()) || executor.getRole() == Role.ADMIN) {
            usuarioRepository.delete(alvo);
        } else {
            throw new AcessoNegadoException("Sem permissão para deletar esta conta.");
        }
    }

    private UsuarioResponseDTO mapToResponse(Usuario u) {
        return new UsuarioResponseDTO(u.getNome(), u.getEmail(), u.getRole(), u.getCreatedAt());
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com o ID: " + id));
    }
}