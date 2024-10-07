package br.com.cadastroit.services.web.dto;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

import br.com.cadastroit.services.api.domain.Endereco;
import br.com.cadastroit.services.api.domain.Telefone;
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
public class PessoaDTO {

	private Long id;
	private String nome;
	private String razaosocial;
	private TipoPessoa tipoPessoa;
	private TipoTelefone tipoTelefone;
	private Status status;
	private EstadoDTO uf;
	private String createAt;
	private String updateAt;
	private EnderecoDTO endereco;
	private TelefoneDTO telefone;
	private List<EnderecoDTO> enderecos;
	private List<TelefoneDTO> telefones;

}