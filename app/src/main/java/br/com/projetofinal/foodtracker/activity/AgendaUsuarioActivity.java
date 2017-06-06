package br.com.projetofinal.foodtracker.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

public class AgendaUsuarioActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private Intent intent;
    private int id_usuario;
    private ProgressDialog dialog;
    private ListView lv_agenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_usuario);
        Bundle extras = getIntent().getExtras();
        if(extras.getInt("id_usuario") != 0)
        {
            id_usuario = extras.getInt("id_usuario");
        }
        dialog = new ProgressDialog(AgendaUsuarioActivity.this);
        dialog.setMessage("Carregando...");
        dialog.setCancelable(false);
        dialog.show();

        Gson g = new GsonBuilder().registerTypeAdapter(Agenda.class, new AgendaDec()).create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AgendaService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(g))
                .build();
        AgendaService agendaService = retrofit.create(AgendaService.class);
        Call<List<Agenda>> agendas = agendaService.recuperaAgendas();
        agendas.enqueue(new Callback<List<Agenda>>() {
            @Override
            public void onResponse(Call<List<Agenda>> call, Response<List<Agenda>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        dialog.dismiss();
                        lv_agenda = (ListView) findViewById(R.id.lv_agenda_usuario);
                        List<Agenda> listaAgendas = response.body();
                        AgendaAdapter adapter = new AgendaAdapter(getBaseContext(), listaAgendas);
                        lv_agenda.setAdapter(adapter);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Agenda>> call, Throwable t){
                Toast.makeText(getApplicationContext(), "Erro: " + t.getCause(), Toast.LENGTH_SHORT).show();
            }
        });


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_usuario);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        final int finalId_usuario = id_usuario;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.minha_conta:
                        intent = new Intent(getApplicationContext(), PrincipalActivity.class);
                        intent.putExtra("id_usuario", finalId_usuario);
                        startActivity(intent);
                        break;
                    case R.id.localizacao_nikko:
                        intent = new Intent(getApplicationContext(), LocalizacaoAtualActivity.class);
                        intent.putExtra("id_usuario", finalId_usuario);
                        startActivity(intent);
                        break;
                    case R.id.agenda_nikko:
                        Toast.makeText(getApplicationContext(), "Você já está nessa opção", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        break;
                    case R.id.cartao_fidelidade:
                        intent = new Intent(getApplicationContext(), CartaoFidelidadeActivity.class);
                        intent.putExtra("id_usuario", finalId_usuario);
                        startActivity(intent);
                        break;
                    case R.id.sugestoes:
                        intent = new Intent(getApplicationContext(), SugestaoActivity.class);
                        intent.putExtra("id_usuario", finalId_usuario);
                        startActivity(intent);
                        break;
                    case R.id.avaliação:
                        intent = new Intent(getApplicationContext(), AvaliacaoActivity.class);
                        intent.putExtra("id_usuario", finalId_usuario);
                        break;
                    case R.id.logout:
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                return false;
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
