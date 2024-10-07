package br.com.cadastroit.services.web.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.cadastroit.services.api.enums.TipoEndereco;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoDTO  {

	private Long id;
	
	@NotNull @Size(max = 100)
	private String endereco;
	
	@NotNull
	private String numero;
	
	@NotNull
	private String bairro;
	
	private PessoaEnderecoDTO pessoa;
	
	private EstadoDTO estado;
	
	@NotNull
	private TipoEndereco tipoEndereco;

}
