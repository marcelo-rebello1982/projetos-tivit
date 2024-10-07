package br.com.cadastroit.services.desafio4;
public enum TipoEndereco {

	ENDERECO_CORRESPONDENCIA("Correspondência"), ENDERECO_COBRANCA("Cobrança"), ENDERECO_ENTREGA("Entrega"), ENDERECO_SEDE("Sede");

	private String descricao;

	TipoEndereco(String descricao) {

		this.descricao = descricao;
	}

	public String getDescricao() {

		return descricao;
	}
}