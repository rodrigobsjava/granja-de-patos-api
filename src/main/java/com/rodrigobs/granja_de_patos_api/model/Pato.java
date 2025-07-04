package com.rodrigobs.granja_de_patos_api.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pato")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Pato {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private String nome;

	@Column(name = "nome_mae")
	private String nomeMae;

	@ManyToOne
	@JoinColumn(name = "pata_mae_id", referencedColumnName = "id")
	private Pato pataMae;

	@OneToMany(mappedBy = "pataMae")
	public Set<Pato> filhos = new HashSet<>();

	@Column(name = "vendido", nullable = false)
	private boolean vendido = false;
	
	public double getPreco() {
	    int qtdFilhos = filhos != null ? filhos.size() : 0;
	    if (qtdFilhos == 0) return 70.0;
	    if (qtdFilhos == 1) return 50.0;
	    if (qtdFilhos == 2) return 25.0;
	    return 25.0;
	}

}
