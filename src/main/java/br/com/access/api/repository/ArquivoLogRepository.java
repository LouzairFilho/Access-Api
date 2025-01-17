package br.com.access.api.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.access.api.model.ArquivoLog;

public interface ArquivoLogRepository extends JpaRepository<ArquivoLog, Long> {


}
