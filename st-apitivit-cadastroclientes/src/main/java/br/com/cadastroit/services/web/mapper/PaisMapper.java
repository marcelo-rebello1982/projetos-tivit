package br.com.cadastroit.services.web.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import br.com.cadastroit.services.api.domain.Pais;
import br.com.cadastroit.services.web.dto.PaisDTO;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = PaisMapper.class)
public abstract class  PaisMapper {

	public abstract Pais toEntity(PaisDTO dto);
	public abstract PaisDTO toDto(Pais entity);
	public abstract List<PaisDTO> toDto(List<Pais> entity);

	
}


