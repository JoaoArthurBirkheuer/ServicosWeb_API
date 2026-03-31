package com.servicosweb.apirest.joao.repositories;

import com.servicosweb.apirest.joao.entities.Estacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstacaoRepository extends JpaRepository<Long, Estacao> {
    boolean existsByCodigo(String codigo);
    List<Estacao> findAllByAtivaTrue();
}
