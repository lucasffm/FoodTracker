package br.com.projetofinal.foodtracker.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.projetofinal.foodtracker.R;
import br.com.projetofinal.foodtracker.dec.AvaliacaoDec;
import br.com.projetofinal.foodtracker.dec.SugestaoDec;
import br.com.projetofinal.foodtracker.interfaces.AvaliacaoService;
import br.com.projetofinal.foodtracker.interfaces.SugestaoService;
import br.com.projetofinal.foodtracker.modelo.Atendimento;
import br.com.projetofinal.foodtracker.modelo.Avaliacao;
import br.com.projetofinal.foodtracker.modelo.Sugestao;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SugestaoActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private Spinner spinner_sugestao;
    private EditText edtSugestao;
    private Intent intent;
    private int id_usuario, id_atendimento;
    private String descricaoSugestao;
    private Button btnEnviarSugestao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugestao);
        Bundle extras = getIntent().getExtras();
        if(extras.getInt("id_usuario") != 0)
        {
            id_usuario = extras.getInt("id_usuario");
        }
        verificaCheckInDias();
        verificaAtendimento();

        edtSugestao = (EditText) findViewById(R.id.edt_sugestao);
        btnEnviarSugestao = (Button) findViewById(R.id.btnEnviarSugestao);
        spinner_sugestao = (Spinner)findViewById(R.id.spinner_sugestao);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.areas_nikko, android.R.layout.simple_list_item_1);
        spinner_sugestao.setAdapter(adapter);



        btnEnviarSugestao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descricaoSugestao = edtSugestao.getText().toString();
                if(descricaoSugestao.equals("")){
                    Toast.makeText(getApplicationContext(), "Digite sua sugestão antes de enviar.", Toast.LENGTH_SHORT).show();
                }else{
                    if(spinner_sugestao.getSelectedItem().toString().equals("Cozinha")){
                        inserirSugestaoCozinha(descricaoSugestao, id_atendimento);
                    }else if(spinner_sugestao.getSelectedItem().toString().equals("Atendimento")){
                        inserirSugestaoAtendimento(descricaoSugestao, id_atendimento);
                    }else if(spinner_sugestao.getSelectedItem().toString().equals("Comida")){
                        inserirSugestaoComida(descricaoSugestao, id_atendimento);
                    }else if(spinner_sugestao.getSelectedItem().toString().equals("Ambiente")){
                        inserirSugestaoAmbiente(descricaoSugestao, id_atendimento);
                    }else{
                        Toast.makeText(getApplicationContext(), "Ocorreu algo de errado.", Toast.LENGTH_SHORT).show();
                    }
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
                        Toast.makeText(getApplicationContext(), "Você já está nessa opção", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        break;
                    case R.id.avaliação:
                        intent = new Intent(getApplicationContext(), AvaliacaoActivity.class);
                        intent.putExtra("id_usuario", finalId_usuario);
                        startActivity(intent);
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
        Gson g = new GsonBuilder().registerTypeAdapter(Sugestao.class, new SugestaoDec()).create();
        Retrofit retrofitBuscar = new Retrofit.Builder()
                .baseUrl(SugestaoService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(g))
                .build();
        SugestaoService service = retrofitBuscar.create(SugestaoService.class);
        Call<Boolean> call = service.verificaCheckIn(id_usuario);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    boolean resposta = response.body();
                    if (resposta){
                        btnEnviarSugestao.setEnabled(true);
                    }else{
                        btnEnviarSugestao.setEnabled(false);
                        btnEnviarSugestao.setBackgroundColor(getResources().getColor(R.color.colorGray));
                        Toast.makeText(getApplicationContext(), "Botão desativado por que você nao efetuou nenhum check-in nos últimos 7 dias.", Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    btnEnviarSugestao.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Erro na conexão com o servidor.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });

    }

    private void verificaAtendimento(){
        Gson g = new GsonBuilder().registerTypeAdapter(Avaliacao.class, new SugestaoDec()).create();
        Retrofit retrofitBuscar = new Retrofit.Builder()
                .baseUrl(SugestaoService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(g))
                .build();
        SugestaoService service = retrofitBuscar.create(SugestaoService.class);
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
                        btnEnviarSugestao.setEnabled(false);
                        btnEnviarSugestao.setBackgroundColor(getResources().getColor(R.color.colorGray));
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

    public void inserirSugestaoCozinha(String descSugestao, int idAtendimento){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SugestaoService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SugestaoService service = retrofit.create(SugestaoService.class);
        Sugestao sugestao = new Sugestao();
        Atendimento atendimento = new Atendimento();
        atendimento.setIdAtendimento(idAtendimento);
        sugestao.setDescricao(descSugestao);
        sugestao.setAtendimento(atendimento);
        Call<Boolean> call = service.inserirSugestaoCozinha(sugestao);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body()) {
                        Log.i("AVAL", "Cozinha enviado");
                        Toast.makeText(getApplicationContext(), "Sugestão enviada com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();
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
    public void inserirSugestaoAtendimento(String descSugestao, int idAtendimento){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SugestaoService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SugestaoService service = retrofit.create(SugestaoService.class);
        Sugestao sugestao = new Sugestao();
        Atendimento atendimento = new Atendimento();
        atendimento.setIdAtendimento(idAtendimento);
        sugestao.setDescricao(descSugestao);
        sugestao.setAtendimento(atendimento);
        Call<Boolean> call = service.inserirSugestaoAtendimento(sugestao);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body()) {
                        Log.i("AVAL", "Atendimento enviado");
                        Toast.makeText(getApplicationContext(), "Sugestão enviada com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();
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
    public void inserirSugestaoComida(String descSugestao, int idAtendimento){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SugestaoService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SugestaoService service = retrofit.create(SugestaoService.class);
        Sugestao sugestao = new Sugestao();
        Atendimento atendimento = new Atendimento();
        atendimento.setIdAtendimento(idAtendimento);
        sugestao.setDescricao(descSugestao);
        sugestao.setAtendimento(atendimento);
        Call<Boolean> call = service.inserirSugestaoComida(sugestao);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body()) {
                        Log.i("AVAL", "Comida enviado");
                        Toast.makeText(getApplicationContext(), "Sugestão enviada com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();
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
    public void inserirSugestaoAmbiente(String descSugestao, int idAtendimento){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SugestaoService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SugestaoService service = retrofit.create(SugestaoService.class);
        Sugestao sugestao = new Sugestao();
        Atendimento atendimento = new Atendimento();
        atendimento.setIdAtendimento(idAtendimento);
        sugestao.setDescricao(descSugestao);
        sugestao.setAtendimento(atendimento);
        Call<Boolean> call = service.inserirSugestaoAmbiente(sugestao);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body()) {
                        Log.i("AVAL", "Ambiente enviado");
                        Toast.makeText(getApplicationContext(), "Sugestão enviada com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();
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
