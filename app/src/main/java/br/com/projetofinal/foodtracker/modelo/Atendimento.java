package br.com.projetofinal.foodtracker.modelo;

import java.util.Date;

/**
 * Created by Lucas on 18/01/2017.
 */

public class Atendimento {

    private int idAtendimento;
    private Date dataAtendimento;
    private Carro carro;
    private Pessoa pessoa;

    public Atendimento() {
    }

    public Atendimento(int idAtendimento, Date dataAtendimento, Carro carro, Pessoa pessoa) {
        this.idAtendimento = idAtendimento;
        this.dataAtendimento = dataAtendimento;
        this.carro = carro;
        this.pessoa = pessoa;
    }

    public int getIdAtendimento() {
        return idAtendimento;
    }

    public void setIdAtendimento(int idAtendimento) {
        this.idAtendimento = idAtendimento;
    }

    public Date getDataAtendimento() {
        return dataAtendimento;
    }

    public void setDataAtendimento(Date dataAtendimento) {
        this.dataAtendimento = dataAtendimento;
    }

    public Carro getCarro() {
        return carro;
    }

    public void setCarro(Carro carro) {
        this.carro = carro;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
}
