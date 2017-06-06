package br.com.projetofinal.foodtracker.modelo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Lucas on 30/09/2016.
 */

public class CartaoFidelidade implements Serializable {
    private int id_cartao;
    private Date data_ativacao;
    private QrCode qrCode;
    private Pessoa pessoa;
    private String status;

    public CartaoFidelidade() {
    }

    public CartaoFidelidade(int id_cartao, Date data_ativacao, QrCode qrCode, Pessoa pessoa, String status) {
        this.id_cartao = id_cartao;
        this.data_ativacao = data_ativacao;
        this.qrCode = qrCode;
        this.pessoa = pessoa;
        this.status = status;
    }

    public int getId_cartao() {
        return id_cartao;
    }

    public void setId_cartao(int id_cartao) {
        this.id_cartao = id_cartao;
    }

    public Date getData_ativacao() {
        return data_ativacao;
    }

    public void setData_ativacao(Date data_ativacao) {
        this.data_ativacao = data_ativacao;
    }

    public QrCode getQrCode() {
        return qrCode;
    }

    public void setQrCode(QrCode qrCode) {
        this.qrCode = qrCode;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
