package br.com.projetofinal.foodtracker.interfaces;

import java.util.Date;

import br.com.projetofinal.foodtracker.modelo.GraficoAvaliacao;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Lucas on 29/01/2017.
 */

public interface RelatorioService {

    String BASE_URL = "http://192.168.43.122:8080/NikkoWS/webresources/";

    @GET("relatorios/getAvaliacoes/{data}")
    Call<GraficoAvaliacao> getRelatorioAvaliacao(@Path("data") String data);
}
