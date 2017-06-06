package br.com.projetofinal.foodtracker.interfaces;

import br.com.projetofinal.foodtracker.modelo.Sugestao;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Lucas on 24/01/2017.
 */

public interface SugestaoService {

    String BASE_URL = "http://192.168.43.122:8080/NikkoWS/webresources/";

    @POST("sugestao/inserirSugestaoCozinha")
    Call<Boolean> inserirSugestaoCozinha(@Body Sugestao sugestao);
    @POST("sugestao/inserirSugestaoAtendimento")
    Call<Boolean> inserirSugestaoAtendimento(@Body Sugestao sugestao);
    @POST("sugestao/inserirSugestaoComida")
    Call<Boolean> inserirSugestaoComida(@Body Sugestao sugestao);
    @POST("sugestao/inserirSugestaoAmbiente")
    Call<Boolean> inserirSugestaoAmbiente(@Body Sugestao sugestao);
    @GET("sugestao/verificacheckin/{id}")
    Call<Boolean> verificaCheckIn(@Path("id") int id);
    @GET("sugestao/verificaatendimento/{id}")
    Call<String> verificaAtendimento(@Path("id") int idPessoa);
}
