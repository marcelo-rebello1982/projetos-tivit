package br.com.cadastroit.services.repositories.impl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.cadastroit.services.api.domain.Pessoa;

public interface PessoaRepositoryImpl extends JpaRepository<Pessoa, Long> {

	@Query("SELECT p.id, p.razaosocial FROM Pessoa p INNER JOIN p.uf u WHERE u.sigla = :sigla ORDER BY p.id DESC")
	List<Pessoa> findByQueryParam(@Param("sigla") String sigla);

}
