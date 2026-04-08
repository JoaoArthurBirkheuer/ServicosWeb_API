package com.servicosweb.apirest.joao.services;

import com.servicosweb.apirest.joao.dtos.estacao.EstacaoRequestDTO;
import com.servicosweb.apirest.joao.dtos.estacao.EstacaoResponseDTO;
import com.servicosweb.apirest.joao.entities.Estacao;
import com.servicosweb.apirest.joao.entities.Usuario;
import com.servicosweb.apirest.joao.enums.Role;
import com.servicosweb.apirest.joao.exceptions.AcessoNegadoException;
import com.servicosweb.apirest.joao.exceptions.RecursoNaoEncontradoException;
import com.servicosweb.apirest.joao.exceptions.RegraDeNegocioException;
import com.servicosweb.apirest.joao.repositories.EstacaoRepository;
import com.servicosweb.apirest.joao.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EstacaoService {

    private final EstacaoRepository estacaoRepository;
    private final UsuarioRepository usuarioRepository;

    public EstacaoService(EstacaoRepository estacaoRepository, UsuarioRepository usuarioRepository) {
        this.estacaoRepository = estacaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<EstacaoResponseDTO> listarAtivas() {
        return estacaoRepository.findAllByAtivaTrue().stream()
                .map(this::mapToResponse).toList();
    }

    public EstacaoResponseDTO buscarPorId(Long id) {
        return mapToResponse(buscarEntidade(id));
    }

    @Transactional
    public EstacaoResponseDTO criar(EstacaoRequestDTO dto, Usuario usuario) {
        if (estacaoRepository.existsByCodigo(dto.codigo())) {
            throw new RegraDeNegocioException("Não foi possível cadastrar: o código '" + dto.codigo() + "' já está em uso.");
        }

        Estacao estacao = Estacao.builder()
                .nome(dto.nome())
                .codigo(dto.codigo())
                .metadata(dto.metadata())
                .usuario(usuario)
                .ativa(true)
                .build();

        return mapToResponse(estacaoRepository.save(estacao));
    }

    @Transactional
    public EstacaoResponseDTO atualizar(Long id, EstacaoRequestDTO dto, Usuario executor) {
        Estacao estacao = buscarEntidade(id);

        verificarPropriedade(estacao, executor);
        if (!estacao.getCodigo().equals(dto.codigo()) && estacaoRepository.existsByCodigo(dto.codigo())) {
            throw new RegraDeNegocioException("Operação negada: O código '" + dto.codigo() + "' já pertence a outra estação.");
        }

        estacao.setNome(dto.nome());
        estacao.setCodigo(dto.codigo());
        estacao.setMetadata(dto.metadata());

        return mapToResponse(estacaoRepository.save(estacao));
    }

    @Transactional
    public void desativar(Long id, Usuario executor) {
        Estacao estacao = estacaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Impossível desativar: Estação não encontrada com o ID: " + id));

        verificarPropriedade(estacao, executor);

        estacao.setAtiva(false);
        estacaoRepository.save(estacao);
    }

    private Estacao buscarEntidade(Long id) {
        return estacaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estação de ID " + id + " não foi encontrada no sistema."));
    }

    private void verificarPropriedade(Estacao estacao, Usuario executor) {
        boolean ehDono = estacao.getUsuario().getId().equals(executor.getId());
        boolean ehAdmin = executor.getRole() == Role.ADMIN;

        if (!ehDono && !ehAdmin) {
            throw new AcessoNegadoException("Acesso negado: Você não tem permissão para gerenciar esta estação.");
        }
    }

    private EstacaoResponseDTO mapToResponse(Estacao e) {
        return new EstacaoResponseDTO(e.getNome(), e.getCodigo(), e.getAtiva(),
                e.getMetadata(), e.getUsuario().getEmail(), e.getCreatedAt());
    }

    @Transactional
    public EstacaoResponseDTO criarComEmail(EstacaoRequestDTO dto, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new AcessoNegadoException("Usuário não encontrado."));

        return criar(dto, usuario);
    }
}