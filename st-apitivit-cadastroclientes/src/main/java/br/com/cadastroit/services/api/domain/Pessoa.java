package br.com.cadastroit.services.api.domain;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import br.com.cadastroit.services.api.enums.Status;
import br.com.cadastroit.services.api.enums.TipoPessoa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "PESSOA")
@SequenceGenerator(name = "PESSOA_SEQ", sequenceName = "PESSOA_SEQ", allocationSize = 1, initialValue = 1)
public class Pessoa implements Serializable {

	private static final long serialVersionUID = 272755268202589678L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PESSOA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NOME", nullable = false)
	private String nome;
	
	@Column(name = "RAZAOSOCIAL", nullable = false)
	private String razaosocial;

	@CreationTimestamp
	private LocalDateTime createAt;
	
	@UpdateTimestamp
	private LocalDateTime updateAt;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPOPESSOA", nullable = false)
	private TipoPessoa tipoPessoa;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@ManyToOne
	@JoinColumn(name = "ESTADO_ID")
	private Estado uf;
	
	@ManyToOne
	@JoinColumn(name = "ENDERECO_ID")
	private Endereco endereco;
	
	@ManyToOne
	@JoinColumn(name = "TELEFONE_ID")
	private Telefone telefone;
		
	// aqui propositalmente deixei ManyToOne com Endereco e telefone, poderia ter deixado 
	// como abaixo,  e usar sob demanda qdo necessário ( proxy type  ) 
	// Marcelo Paulo. ou poderia ter deixado EAGER , o que não seria recomendado devido performance.
	
	// @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	// private List<Endereco> enderecos;
	
	// @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	//private List<Telefone> telefones;
}