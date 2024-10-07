package br.com.cadastroit.services.api.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "ESTADO")
@SequenceGenerator(name = "ESTADO_SEQ", sequenceName = "ESTADO_SEQ", allocationSize = 1, initialValue = 1)
public class Estado implements Serializable{

	private static final long serialVersionUID = 6142975172600365207L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ESTADO_SEQ")
    @Column(name = "ID")
    private Long id;
	
    @Column(name = "NOME")
    private String nome;

    @Column(name = "SIGLA")
    private String sigla;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PAIS_ID")
    private Pais pais;

}
