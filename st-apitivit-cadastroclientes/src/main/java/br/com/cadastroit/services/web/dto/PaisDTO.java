package br.com.cadastroit.services.web.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaisDTO  {
	
	private Long id;
	private String descr;
	private String siglaPais;
	private Set<EstadoDTO> estadoCollection;
	
}
