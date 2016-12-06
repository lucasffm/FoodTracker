package br.com.projetofinal.foodtracker.interfaces;

import br.com.projetofinal.foodtracker.modelo.CartaoFidelidade;
import br.com.projetofinal.foodtracker.modelo.QrCode;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Lucas on 06/10/2016.
 */

public interface CartaoService {
    public static final String BASE_URL = "http://nikkotemaki.ddns.net:8080/NikkoWS/webresources/";

    @GET("cartaofidelidade/inserirqrcode/{inserir}")
    Call<String> gerarQrCode(@Path("inserir") String content);

    @GET("cartaofidelidade/recuperaQrCode/{codigo}")
    Call<QrCode> getQrCode(@Path("codigo") String codigo);

    @GET("cartaofidelidade/getCartoesPessoa/{id}")
    Call<String> getQrCode(@Path("id") int id);

    @POST("cartaofidelidade/inserirCartao/")
    Call<Boolean> inserirCartao(@Body CartaoFidelidade cf);

    @PUT("cartaofidelidade/atualizaQrcode/")
    Call<Boolean> atualizaQrcode(@Body QrCode code);

    @PUT("cartaofidelidade/atualizaCartao/")
    Call<Boolean> atualizaCartao(@Body CartaoFidelidade cf);

}
