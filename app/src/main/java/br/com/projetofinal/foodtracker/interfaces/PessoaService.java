package br.com.projetofinal.foodtracker.interfaces;

import java.util.List;

import br.com.projetofinal.foodtracker.modelo.Pessoa;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Lucas on 17/09/2016.
 */
public interface PessoaService {
    String BASE_URL = "http://192.168.43.122:8080/NikkoWS/webresources/";

    @GET("pessoa/recuperaPessoas")
    Call<List<Pessoa>> getPessoas();

    @GET("pessoa/verificaLogin/{login}/{senha}")
    Call<Pessoa> verificaLogin(@Path("login") String login, @Path("senha") String senha); ;

    @GET("pessoa/recuperaPessoaId/{id}")
    Call<Pessoa> recuperaPessoaId(@Path("id") int id);

    @POST("pessoa/inserirPessoa")
    Call<Boolean> inserirPessoa(@Body Pessoa pessoa);

    @PUT("pessoa/atualizarPessoa")
    Call<Boolean> atualizarPessoa(@Body Pessoa pessoa);

    @GET("pessoa/excluirPessoa/{id}")
    Call<Boolean> excluirPessoa(@Path("id") int id);


}
