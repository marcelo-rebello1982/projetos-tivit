package br.com.cadastroit.services.desafio4;
public enum TipoTelefone {

	CELULAR("Celular"), RESIDENCIAL("Residencial"), COMERCIAL("Comercial"), RECADO("Recado");

	private String descricao;

	private TipoTelefone(String descricao) {

		this.descricao = descricao;
	}

	public String getDescricao() {

		return this.descricao;
	}

}