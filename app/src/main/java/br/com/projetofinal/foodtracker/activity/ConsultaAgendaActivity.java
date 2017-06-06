package br.com.projetofinal.foodtracker.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import br.com.projetofinal.foodtracker.R;
import br.com.projetofinal.foodtracker.adapters.AgendaAdapter;
import br.com.projetofinal.foodtracker.dec.AgendaDec;
import br.com.projetofinal.foodtracker.interfaces.AgendaService;
import br.com.projetofinal.foodtracker.modelo.Agenda;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConsultaAgendaActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    private ListView lv_agenda;
    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_agenda);

        dialog = new ProgressDialog(ConsultaAgendaActivity.this);
        dialog.setMessage("Carregando...");
        dialog.setCancelable(false);
        dialog.show();

        Gson g = new GsonBuilder().registerTypeAdapter(Agenda.class, new AgendaDec()).create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AgendaService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(g))
                .build();
        AgendaService agendaService = retrofit.create(AgendaService.class);
        final Call<List<Agenda>> agendas = agendaService.recuperaAgendas();
        agendas.enqueue(new Callback<List<Agenda>>() {
            @Override
            public void onResponse(Call<List<Agenda>> call, Response<List<Agenda>> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        dialog.dismiss();
                        lv_agenda = (ListView) findViewById(R.id.lv_agenda);
                        final List<Agenda> listaAgendas = response.body();
                        AgendaAdapter adapter = new AgendaAdapter(getBaseContext(), listaAgendas);
                        lv_agenda.setAdapter(adapter);


                        lv_agenda.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                final int idAgenda = listaAgendas.get(i).getId_agenda();

                                AlertDialog.Builder builder = new AlertDialog.Builder(ConsultaAgendaActivity.this);
                                builder.setTitle("Agenda Nikko Temaki");
                                builder.setMessage("Selecione o que deseja fazer com essa agenda.");
                                builder.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(ConsultaAgendaActivity.this, AtualizaAgendaActivity.class);
                                        intent.putExtra("id_agenda", idAgenda);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                //define um botão como negativo.
                                builder.setNegativeButton("Excluir", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ConsultaAgendaActivity.this);
                                        builder.setTitle("Tem Certeza que deseja excluir essa agenda?");
                                        //define um botão como positivo
                                        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                Gson g = new GsonBuilder().registerTypeAdapter(Agenda.class, new AgendaDec()).create();
                                                Retrofit retrofit = new Retrofit.Builder()
                                                        .baseUrl(AgendaService.BASE_URL)
                                                        .addConverterFactory(GsonConverterFactory.create(g))
                                                        .build();
                                                AgendaService agendaService = retrofit.create(AgendaService.class);
                                                Call<Boolean> call = agendaService.deletarAgenda(idAgenda);
                                                call.enqueue(new Callback<Boolean>() {
                                                    @Override
                                                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                                        if(response.body() == true){
                                                            Toast.makeText(getApplicationContext(), "Agenda excluida com sucesso", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(ConsultaAgendaActivity.this, ConsultaAgendaActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }else{
                                                            Toast.makeText(getApplicationContext(), "Não foi possível excluir essa agenda, verifique se você possui conexão com a internet", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Boolean> call, Throwable t) {

                                                    }
                                                });
                                            }
                                        });
                                        //define um botão como negativo.
                                        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                return;
                                            }
                                        });
                                        //cria o AlertDialog
                                        alerta = builder.create();
                                        //Exibe
                                        alerta.show();



                                    }
                                });
                                //cria o AlertDialog
                                alerta = builder.create();
                                //Exibe
                                alerta.show();
                            }
                        });
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Agenda>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Erro: " + t.getCause(), Toast.LENGTH_SHORT).show();
            }
        });




    }

}
