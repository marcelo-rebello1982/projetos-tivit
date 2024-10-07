package br.com.cadastroit.services.web.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import br.com.cadastroit.services.api.domain.Estado;
import br.com.cadastroit.services.web.dto.EstadoDTO;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = EstadoMapper.class)
public abstract class  EstadoMapper {

	public abstract Estado toEntity(EstadoDTO dto);
	public abstract EstadoDTO toDto(Estado entity);
	public abstract List<EstadoDTO> toDto(List<Estado> entity);

	
}


