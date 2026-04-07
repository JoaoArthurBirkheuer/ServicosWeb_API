package com.servicosweb.apirest.joao.repositories;

import com.servicosweb.apirest.joao.entities.Leitura;
import com.servicosweb.apirest.joao.enums.QualidadeLeitura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeituraRepository extends JpaRepository<Leitura, Long> {
    List<Leitura> findAllByEstacaoId(Long estacaoId);
    List<Leitura> findAllByEstacaoIdAndQualidade(Long estacaoId, QualidadeLeitura qualidade);
    Optional<Leitura> findByIdAndEstacaoId(Long id, Long estacaoId);
    Optional<Leitura> findFirstByEstacaoIdOrderByTimestampLeituraDesc(Long estacaoId);
}
