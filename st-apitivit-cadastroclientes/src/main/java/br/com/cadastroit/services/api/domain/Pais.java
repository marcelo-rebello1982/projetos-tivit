package br.com.cadastroit.services.api.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class Pais  implements Serializable{
	
	private static final long serialVersionUID = -8911174076409978905L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PAIS_SEQ")
	@Column(name = "ID", nullable = false)
	private Long id;
	
	@Column(name="DESCR", nullable = false)
	private String descr;
	
	@Column(name="SIGLA_PAIS", nullable = true)
	private String siglaPais;
	
	// @OneToMany(mappedBy="pais")
	// private Set<Estado> estadoCollection;
	
}
