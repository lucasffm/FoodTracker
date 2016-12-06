package br.com.projetofinal.foodtracker.modelo;

/**
 * Created by Lucas on 17/09/2016.
 */
public class Endereco {
    private Integer idEndereco;
    private Integer cep;
    private String estado;
    private String cidade;
    private String complemento;

    public Endereco() {
    }

    public Endereco(Integer idEndereco, Integer cep, String estado, String cidade, String complemento) {
        this.idEndereco = idEndereco;
        this.cep = cep;
        this.estado = estado;
        this.cidade = cidade;
        this.complemento = complemento;
    }

    public Integer getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(Integer idEndereco) {
        this.idEndereco = idEndereco;
    }

    public Integer getCep() {
        return cep;
    }

    public void setCep(Integer cep) {
        this.cep = cep;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
}
