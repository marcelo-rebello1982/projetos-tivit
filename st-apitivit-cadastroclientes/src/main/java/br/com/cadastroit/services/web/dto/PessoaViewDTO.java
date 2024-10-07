package br.com.cadastroit.services.web.dto;

import java.util.List;

import br.com.cadastroit.services.api.enums.Status;
import br.com.cadastroit.services.api.enums.TipoPessoa;
import br.com.cadastroit.services.api.enums.TipoTelefone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PessoaViewDTO {

	private Long id;
	private String nome;
	private String razaosocial;
	private TipoPessoa tipoPessoa;
	private TipoTelefone tipoTelefone;
	private Status status;
	private EstadoDTO uf;
	private List<EnderecoDTO> enderecos;
	private TelefoneDTO telefone;

}