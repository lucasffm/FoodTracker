package br.com.projetofinal.foodtracker.modelo;

/**
 * Created by Lucas on 29/01/2017.
 */

public class GraficoAvaliacao {

    private int notaPositiva;
    private int notaNegativa;
    private int notaImparcial;


    public GraficoAvaliacao() {
    }

    public GraficoAvaliacao(int notaPositiva, int notaNegativa, int notaImparcial) {
        this.notaPositiva = notaPositiva;
        this.notaNegativa = notaNegativa;
        this.notaImparcial = notaImparcial;
    }

    public int getNotaPositiva() {
        return notaPositiva;
    }

    public void setNotaPositiva(int notaPositiva) {
        this.notaPositiva = notaPositiva;
    }

    public int getNotaNegativa() {
        return notaNegativa;
    }

    public void setNotaNegativa(int notaNegativa) {
        this.notaNegativa = notaNegativa;
    }

    public int getNotaImparcial() {
        return notaImparcial;
    }

    public void setNotaImparcial(int notaImparcial) {
        this.notaImparcial = notaImparcial;
    }
}

