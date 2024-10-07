package br.com.cadastroit.services.desafio4;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TELEFONE")
@SequenceGenerator(name = "TELEFONE_SEQ", sequenceName = "TELEFONE_SEQ", allocationSize = 1, initialValue = 1)
public class Telefone implements Serializable {

	private static final long serialVersionUID = 8780520116298574710L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PEDIDO_SEQ")
	@Column(name = "ID")
	private Long id;

	@Column(name = "CODIGOPAIS")
	private int codigoPais;

	@Column(name = "DDD")
	private int ddd;

	@Column(name = "NUMERO")
	private String numero;

	@Column(name = "RAMAL")
	private int ramal;

	@Enumerated(EnumType.STRING)
	private TipoTelefone tipo;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "TELEFONE_ID", referencedColumnName = "id")
	private Cliente cliente;

}