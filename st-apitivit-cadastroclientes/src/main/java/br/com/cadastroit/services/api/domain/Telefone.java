package br.com.cadastroit.services.api.domain;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.cadastroit.services.api.enums.TipoTelefone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@SequenceGenerator(name = "TELEFONE_SEQ", sequenceName = "TELEFONE_SEQ", allocationSize = 1, initialValue = 1)
public class Telefone implements Serializable {
	
	private static final long serialVersionUID = 8765506202749397479L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TELEFONE_SEQ")
	@Column(name = "ID")
	private Long id;

	@Column(name = "DDD")
	private int ddd;

	@Column(name = "NUMERO")
	private String numero;
	
	@ManyToOne
	@JoinColumn(name = "PESSOA_ID")
	private Pessoa pessoa;
	
	@Enumerated(EnumType.STRING)
	private TipoTelefone tipoTelefone;
	
}
