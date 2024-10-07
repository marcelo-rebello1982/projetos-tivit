package br.com.cadastroit.services.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PessoaResumoDTO {

	private Long id;
	private String nome;
    private String nomeEstado;
    private String siglaEstado;


}