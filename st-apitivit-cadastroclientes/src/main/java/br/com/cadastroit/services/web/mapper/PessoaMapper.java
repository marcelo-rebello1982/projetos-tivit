package br.com.cadastroit.services.web.mapper;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import br.com.cadastroit.services.api.domain.Pessoa;
import br.com.cadastroit.services.web.dto.PessoaDTO;
import br.com.cadastroit.services.web.dto.PessoaResumoDTO;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = PessoaMapper.class)
public abstract class  PessoaMapper {

	public abstract Pessoa toEntity(PessoaDTO dto);
	public abstract PessoaDTO toDto(Pessoa entity);
	public abstract List<PessoaDTO> toDto(List<Pessoa> entity);
	
	@Mappings({
		@Mapping(target = "id", source = "id"),
		@Mapping(target = "nome", source = "nome"),
		@Mapping(target = "nomeEstado", source = "uf.nome"),
		@Mapping(target = "siglaEstado", source = "uf.sigla"),
	})
	public abstract PessoaResumoDTO toResumoDto(Pessoa entity);
	
	public abstract List<PessoaResumoDTO> toResumoDto(List<Pessoa> entity);
	
}


