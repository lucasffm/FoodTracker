package br.com.projetofinal.foodtracker.modelo;

/**
 * Created by Lucas on 19/01/2017.
 */
public class AreaServico {
    private int idAreaServico;
    private String descricaoAreaServico;

    public AreaServico() {
    }

    public AreaServico(int idAreaServico, String descricaoAreaServico) {
        this.idAreaServico = idAreaServico;
        this.descricaoAreaServico = descricaoAreaServico;
    }

    public int getIdAreaServico() {
        return idAreaServico;
    }

    public void setIdAreaServico(int idAreaServico) {
        this.idAreaServico = idAreaServico;
    }

    public String getDescricaoAreaServico() {
        return descricaoAreaServico;
    }

    public void setDescricaoAreaServico(String descricaoAreaServico) {
        this.descricaoAreaServico = descricaoAreaServico;
    }



}