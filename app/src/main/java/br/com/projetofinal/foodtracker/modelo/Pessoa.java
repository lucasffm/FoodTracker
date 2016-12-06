package br.com.projetofinal.foodtracker.modelo;

import java.io.Serializable;

/**
 * Created by Lucas on 17/09/2016.
 */
public class Pessoa implements Serializable {
    private Integer id_pessoa;
    private String nome;
    private String login;
    private String senha;
    private String cpf;
    private Integer tipoUsuario;
    private Endereco endereco;

    public Pessoa() {
    }

    public Pessoa(Integer id_pessoa, String nome, String login, String senha, String cpf, String complemento, Integer tipoUsuario, Endereco endereco) {
        this.id_pessoa = id_pessoa;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.cpf = cpf;
        this.tipoUsuario = tipoUsuario;
        this.endereco = endereco;
    }

    public Integer getId_pessoa() {
        return id_pessoa;
    }

    public int setId_pessoa(Integer id_pessoa) {
        this.id_pessoa = id_pessoa;
        return 0;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Integer getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(Integer tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

}
