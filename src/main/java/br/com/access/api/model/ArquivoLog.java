package br.com.access.api.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ArquivoLog{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	@Temporal(TemporalType.TIMESTAMP)
	private Date data = new Date();

	private Boolean processado = Boolean.FALSE;

	@JsonIgnore
	private String endereco;

	@JsonIgnore
	private String nomeArquivoRenomeado;

	private Long quantidade;

	public ArquivoLog() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Boolean getProcessado() {
		return processado;
	}

	public void setProcessado(Boolean processado) {
		this.processado = processado;
	}

	public String getNomeArquivoRenomeado() {
		return nomeArquivoRenomeado;
	}

	public void setNomeArquivoRenomeado(String nomeArquivoRenomeado) {
		this.nomeArquivoRenomeado = nomeArquivoRenomeado;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((nomeArquivoRenomeado == null) ? 0 : nomeArquivoRenomeado.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArquivoLog other = (ArquivoLog) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (nomeArquivoRenomeado == null) {
			if (other.nomeArquivoRenomeado != null)
				return false;
		} else if (!nomeArquivoRenomeado.equals(other.nomeArquivoRenomeado))
			return false;
		return true;
	}

}
