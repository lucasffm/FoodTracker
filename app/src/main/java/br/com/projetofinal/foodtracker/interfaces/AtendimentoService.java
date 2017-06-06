package br.com.projetofinal.foodtracker.interfaces;

import br.com.projetofinal.foodtracker.modelo.Atendimento;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Lucas on 18/01/2017.
 */

public interface AtendimentoService {

    String BASE_URL = "http://192.168.43.122:8080/NikkoWS/webresources/";

    @POST("atendimento/inseriratendimento")
    Call<Boolean> inserirAtendimento(@Body Atendimento atendimento);

    @GET("atendimento/checkagenda/{id}")
    Call<Boolean> checkAgenda(@Path("id") int id);
}
