package br.com.projetofinal.foodtracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.projetofinal.foodtracker.R;
import br.com.projetofinal.foodtracker.dec.AvaliacaoDec;
import br.com.projetofinal.foodtracker.interfaces.AvaliacaoService;
import br.com.projetofinal.foodtracker.modelo.Atendimento;
import br.com.projetofinal.foodtracker.modelo.Avaliacao;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AvaliacaoActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private Intent intent;
    private int id_usuario, id_atendimento;
    private Float notaCozinha, notaAtendimento, notaComida, notaAmbiente;
    private RatingBar ratingCozinha,ratingAtendimento, ratingComida,ratingAmbiente;
    private Button btnEnviarAvaliacao;
    private boolean checkJaAvaliou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao);
        Bundle extras = getIntent().getExtras();
        if(extras.getInt("id_usuario") != 0)
        {
            id_usuario = extras.getInt("id_usuario");
        }



        verificaCheckInDias();
        verificaAtendimento();


        ratingCozinha = (RatingBar) findViewById(R.id.ratingCozinha);
        ratingAtendimento = (RatingBar) findViewById(R.id.ratingAtendimento);
        ratingComida = (RatingBar) findViewById(R.id.ratingComida);
        ratingAmbiente = (RatingBar) findViewById(R.id.ratingAmbiente);


        ratingCozinha.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                notaCozinha = v;
            }
        });
        ratingAtendimento.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                notaAtendimento = v;
            }
        });
        ratingComida.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                notaComida = v;
            }
        });
        ratingAmbiente.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                notaAmbiente = v;
            }
        });






        btnEnviarAvaliacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id_atendimento != 0) {
                    if (notaCozinha != null && notaAtendimento != null && notaComida != null && notaAmbiente != null) {
                        verificaExistenciaAvaliacao(id_atendimento);
                    } else {
                        Toast.makeText(getApplicationContext(), "Insira sua nota para todas as áreas de serviços do nikko temaki.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_SHORT).show();
                    return;
                }
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
                        intent = new Intent(getApplicationContext(), AgendaUsuarioActivity.class);
                        intent.putExtra("id_usuario", finalId_usuario);
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
                        Toast.makeText(getApplicationContext(), "Você já está nessa opção", Toast.LENGTH_SHORT).show();
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

    private void verificaCheckInDias(){
        btnEnviarAvaliacao = (Button) findViewById(R.id.btn_enviar_avaliacao);
        Gson g = new GsonBuilder().registerTypeAdapter(Avaliacao.class, new AvaliacaoDec()).create();
        Retrofit retrofitBuscar = new Retrofit.Builder()
                .baseUrl(AvaliacaoService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(g))
                .build();
        AvaliacaoService service = retrofitBuscar.create(AvaliacaoService.class);
        Call<Boolean> call = service.verificaCheckIn(id_usuario);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    boolean resposta = response.body();
                    if (resposta){
                        btnEnviarAvaliacao.setEnabled(true);
                    }else{
                        btnEnviarAvaliacao.setEnabled(false);
                        btnEnviarAvaliacao.setBackgroundColor(getResources().getColor(R.color.colorGray));
                        Toast.makeText(getApplicationContext(), "Botão desativado por que você nao efetuou nenhum check-in nos últimos 7 dias.", Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    btnEnviarAvaliacao.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Erro na conexão com o servidor.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });

    }



    private void verificaAtendimento(){
        Gson g = new GsonBuilder().registerTypeAdapter(Avaliacao.class, new AvaliacaoDec()).create();
        Retrofit retrofitBuscar = new Retrofit.Builder()
                .baseUrl(AvaliacaoService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(g))
                .build();
        AvaliacaoService service = retrofitBuscar.create(AvaliacaoService.class);
        Call<String> call = service.verificaAtendimento(id_usuario);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    String resposta = response.body();
                    int idAtendimento = Integer.parseInt(resposta);
                    if(idAtendimento != 0){
                        id_atendimento = idAtendimento;
                    }else{
                        Toast.makeText(getApplicationContext(), "Não foi possível encontrar seu último check-in", Toast.LENGTH_SHORT).show();
                        btnEnviarAvaliacao.setEnabled(false);
                        btnEnviarAvaliacao.setBackgroundColor(getResources().getColor(R.color.colorGray));
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Erro na conexão com o servidor.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("FAILURE", t.getCause().toString());

            }
        });

    }

    private void verificaExistenciaAvaliacao(int idAtendimento){
        Gson g = new GsonBuilder().registerTypeAdapter(Avaliacao.class, new AvaliacaoDec()).create();
        Retrofit retrofitBuscar = new Retrofit.Builder()
                .baseUrl(AvaliacaoService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(g))
                .build();
        AvaliacaoService service = retrofitBuscar.create(AvaliacaoService.class);
        Call<Boolean> call = service.verificaExistenciaAvaliacao(idAtendimento);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    if (response.body()){
                        btnEnviarAvaliacao.setEnabled(false);
                        btnEnviarAvaliacao.setBackgroundColor(getResources().getColor(R.color.colorGray));
                        Toast.makeText(getApplicationContext(), "Você já avaliou o nikko temaki referente ao seu último check-in.", Toast.LENGTH_SHORT).show();
                    }else{
                        inserirAvaliacaoCozinha(notaCozinha, id_atendimento);
                        inserirAvaliacaoAtendimento(notaAtendimento, id_atendimento);
                        inserirAvaliacaoComida(notaComida, id_atendimento);
                        inserirAvaliacaoAmbiente(notaAmbiente, id_atendimento);
                        Toast.makeText(getApplicationContext(), "Avaliação enviada com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else{
                    checkJaAvaliou = false;
                    btnEnviarAvaliacao.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Erro na conexão com o servidor.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });

    }

    public void inserirAvaliacaoCozinha(float nota, int idAtendimento){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AvaliacaoService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AvaliacaoService service = retrofit.create(AvaliacaoService.class);
        Avaliacao avaliacao = new Avaliacao();
        Atendimento atendimento = new Atendimento();
        atendimento.setIdAtendimento(idAtendimento);
        avaliacao.setDescricao(nota);
        avaliacao.setAtendimento(atendimento);
        Call<Boolean> call = service.inserirAvaliacaoCozinha(avaliacao);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body()) {
                        Log.i("AVAL", "Cozinha enviado");
                    } else {
                        Toast.makeText(getApplicationContext(), "Ocorreu algum erro, verifique todos os campos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Erro" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }


    public void inserirAvaliacaoAtendimento(float nota, int idAtendimento){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AvaliacaoService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AvaliacaoService service = retrofit.create(AvaliacaoService.class);
        Avaliacao avaliacao = new Avaliacao();
        Atendimento atendimento = new Atendimento();
        atendimento.setIdAtendimento(idAtendimento);
        avaliacao.setDescricao(nota);
        avaliacao.setAtendimento(atendimento);
        Call<Boolean> call = service.inserirAvaliacaoAtendimento(avaliacao);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body()) {
                        Log.i("AVAL", "Atendimento enviado");
                    } else {
                        Toast.makeText(getApplicationContext(), "Ocorreu algum erro, verifique todos os campos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Erro" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }
    public void inserirAvaliacaoComida(float nota, int idAtendimento){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AvaliacaoService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AvaliacaoService service = retrofit.create(AvaliacaoService.class);
        Avaliacao avaliacao = new Avaliacao();
        Atendimento atendimento = new Atendimento();
        atendimento.setIdAtendimento(idAtendimento);
        avaliacao.setDescricao(nota);
        avaliacao.setAtendimento(atendimento);
        Call<Boolean> call = service.inserirAvaliacaoComida(avaliacao);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body()) {
                        Log.i("AVAL", "Comida enviado");
                    } else {
                        Toast.makeText(getApplicationContext(), "Ocorreu algum erro, verifique todos os campos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Erro" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }
    public void inserirAvaliacaoAmbiente(float nota, int idAtendimento){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AvaliacaoService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AvaliacaoService service = retrofit.create(AvaliacaoService.class);
        Avaliacao avaliacao = new Avaliacao();
        Atendimento atendimento = new Atendimento();
        atendimento.setIdAtendimento(idAtendimento);
        avaliacao.setDescricao(nota);
        avaliacao.setAtendimento(atendimento);
        Call<Boolean> call = service.inserirAvaliacaoAmbiente(avaliacao);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body()) {
                        Log.i("AVAL", "Ambiente enviado");
                    } else {
                        Toast.makeText(getApplicationContext(), "Ocorreu algum erro, verifique todos os campos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Erro" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

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
