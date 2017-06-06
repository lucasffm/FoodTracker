package br.com.projetofinal.foodtracker.interfaces;

import java.util.List;

import br.com.projetofinal.foodtracker.modelo.Avaliacao;
import br.com.projetofinal.foodtracker.modelo.MediaAvaliacao;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Lucas on 19/01/2017.
 */

public interface AvaliacaoService {

    String BASE_URL = "http://192.168.43.122:8080/NikkoWS/webresources/";

    @POST("avaliacao/inserirAvaliacaoCozinha")
    Call<Boolean> inserirAvaliacaoCozinha(@Body Avaliacao avaliacao);
    @POST("avaliacao/inserirAvaliacaoAtendimento")
    Call<Boolean> inserirAvaliacaoAtendimento(@Body Avaliacao avaliacao);
    @POST("avaliacao/inserirAvaliacaoComida")
    Call<Boolean> inserirAvaliacaoComida(@Body Avaliacao avaliacao);
    @POST("avaliacao/inserirAvaliacaoAmbiente")
    Call<Boolean> inserirAvaliacaoAmbiente(@Body Avaliacao avaliacao);

    @GET("avaliacao/verificacheckin/{id}")
    Call<Boolean> verificaCheckIn(@Path("id") int id);

    @GET("avaliacao/getMediaAvaliacoes/")
    Call<MediaAvaliacao> getMediaNotas();

    @GET("avaliacao/verificaExistenciaAvaliacao/{id}")
    Call<Boolean> verificaExistenciaAvaliacao(@Path("id") int idAtendimento);

    @GET("avaliacao/verificaatendimento/{id}")
    Call<String> verificaAtendimento(@Path("id") int idPessoa);
}
