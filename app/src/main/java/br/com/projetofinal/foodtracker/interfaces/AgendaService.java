package br.com.projetofinal.foodtracker.interfaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.projetofinal.foodtracker.modelo.Agenda;
import br.com.projetofinal.foodtracker.modelo.Pessoa;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Lucas on 02/01/2017.
 */

public interface AgendaService {

    String BASE_URL = "http://192.168.43.122:8080/NikkoWS/webresources/";

    @POST("agenda/inseriragenda")
    Call<Boolean> inserirAgenda(@Body Agenda agenda);

    @GET("agenda/recuperaAgendas")
    Call<List<Agenda>> recuperaAgendas();

    @GET("agenda/getAgenda/{id}")
    Call<Agenda> getAgendaID(@Path("id") int id);

    @PUT("agenda/atualizaAgenda")
    Call<Boolean> atualizaAgenda(@Body Agenda agenda);

    @GET("agenda/excluirAgenda/{id}")
    Call<Boolean> deletarAgenda(@Path("id") int id);

    @GET("agenda/getAgendaData")
    Call<Agenda> getAgendaData();

}
