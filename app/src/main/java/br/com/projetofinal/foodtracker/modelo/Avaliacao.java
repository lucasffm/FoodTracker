package br.com.projetofinal.foodtracker.modelo;

import java.util.Date;

/**
 * Created by Lucas on 19/01/2017.
 */

public class Avaliacao {

    private int id_avaliacao;
    private Date data_avaliacao;
    private Float descricao;
    private AreaServico areaServico;
    private Atendimento atendimento;

    public Avaliacao() {
    }

    public Avaliacao(int id_avaliacao, Date data_avaliacao, Float descricao, AreaServico areaServico, Atendimento atendimento) {
        this.id_avaliacao = id_avaliacao;
        this.data_avaliacao = data_avaliacao;
        this.descricao = descricao;
        this.areaServico = areaServico;
        this.atendimento = atendimento;
    }

    public int getId_avaliacao() {
        return id_avaliacao;
    }

    public void setId_avaliacao(int id_avaliacao) {
        this.id_avaliacao = id_avaliacao;
    }

    public Date getData_avaliacao() {
        return data_avaliacao;
    }

    public void setData_avaliacao(Date data_avaliacao) {
        this.data_avaliacao = data_avaliacao;
    }

    public Float getDescricao() {
        return descricao;
    }

    public void setDescricao(Float descricao) {
        this.descricao = descricao;
    }

    public AreaServico getAreaServico() {
        return areaServico;
    }

    public void setAreaServico(AreaServico areaServico) {
        this.areaServico = areaServico;
    }

    public Atendimento getAtendimento() {
        return atendimento;
    }

    public void setAtendimento(Atendimento atendimento) {
        this.atendimento = atendimento;
    }

}