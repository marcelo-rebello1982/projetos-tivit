package br.com.cadastroit.services.desafio4;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name = "PAIS")
@SequenceGenerator(name = "PAIS_SEQ", sequenceName = "PAIS_SEQ", allocationSize = 1, initialValue = 1)
public class Pais implements Serializable {

	private static final long serialVersionUID = -6929476734888635020L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PAIS_SEQ")
	@Column(name = "ID")
	private Long id;

	@Column(name = "SIGLA")
	private String sigla;

	@OneToMany(mappedBy = "pais")
	private Set<Estado> estadoCollection;

}
