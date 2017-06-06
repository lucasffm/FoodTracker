package br.com.projetofinal.foodtracker.modelo;

import java.util.Date;

/**
 * Created by Lucas on 24/01/2017.
 */

public class Sugestao {
    private int id_sugestao;
    private Date data_avaliacao;
    private String descricao;
    private AreaServico areaServico;
    private Atendimento atendimento;

    public Sugestao() {
    }

    public Sugestao(int id_sugestao, Date data_avaliacao, String descricao, AreaServico areaServico, Atendimento atendimento) {
        this.id_sugestao = id_sugestao;
        this.data_avaliacao = data_avaliacao;
        this.descricao = descricao;
        this.areaServico = areaServico;
        this.atendimento = atendimento;
    }

    public int getId_sugestao() {
        return id_sugestao;
    }

    public void setId_sugestao(int id_sugestao) {
        this.id_sugestao = id_sugestao;
    }

    public Date getData_avaliacao() {
        return data_avaliacao;
    }

    public void setData_avaliacao(Date data_avaliacao) {
        this.data_avaliacao = data_avaliacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
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

    public  void setAtendimento(Atendimento atendimento) {
        this.atendimento = atendimento;
    }


}
