package br.com.cadastroit.services.web.dto;
import javax.validation.constraints.NotNull;

import br.com.cadastroit.services.api.domain.Pessoa;
import br.com.cadastroit.services.api.enums.TipoTelefone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelefoneDTO  {
	
	private Long id;

	@NotNull(message = "Campo ddd sem preenchimento")
	private int ddd;
	
	@NotNull(message = "Campo numero sem preenchimento")
	private String numero;
	
	@NotNull(message = "Campo pessoa sem preenchimento")
	private PessoaResumoDTO pessoa;
	
	@NotNull(message = "Campo tipoTelefone sem preenchimento")
	private TipoTelefone tipoTelefone;

}
