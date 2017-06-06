package br.com.projetofinal.foodtracker.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.projetofinal.foodtracker.dec.PessoaDec;
import br.com.projetofinal.foodtracker.R;
import br.com.projetofinal.foodtracker.interfaces.PessoaService;
import br.com.projetofinal.foodtracker.modelo.Pessoa;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrincipalActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private TextView lbl_login,lbl_nome,lbl_cpf, lbl_endereco;
    private NavigationView navigationView;
    private Intent intent;
    private Button btnAlterar, btnExcluir;
    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        btnAlterar = (Button) findViewById(R.id.btn_alterar_dados);
        btnExcluir = (Button) findViewById(R.id.btn_excluir_dados);
        lbl_login = (TextView) findViewById(R.id.lbl_login);
        lbl_nome = (TextView) findViewById(R.id.lbl_nome);
        lbl_cpf = (TextView) findViewById(R.id.lbl_cpf);
        lbl_endereco = (TextView) findViewById(R.id.lbl_endereco);
        int id_usuario = 0;
        Bundle extras = getIntent().getExtras();
        if(extras.getInt("id_usuario") != 0)
        {
            id_usuario = extras.getInt("id_usuario");
        }

        Gson g = new GsonBuilder().registerTypeAdapter(Pessoa.class, new PessoaDec()).create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PessoaService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(g))
                .build();
        PessoaService pessoaService = retrofit.create(PessoaService.class);
        Call<Pessoa> call = pessoaService.recuperaPessoaId(id_usuario);
        call.enqueue(new Callback<Pessoa>() {
            @Override
            public void onResponse(Call<Pessoa> call, Response<Pessoa> response) {
                Pessoa p = response.body();
                lbl_login.setText("Login: " + p.getLogin());
                lbl_nome.setText("Nome: " + p.getNome());
                lbl_cpf.setText("CPF: " + p.getCpf().toString());
                lbl_endereco.setText("Endereço: " + p.getEndereco().getComplemento()+ " " + p.getEndereco().getCidade() + "," + p.getEndereco().getEstado() + " CEP: " + p.getEndereco().getCep());
            }

            @Override
            public void onFailure(Call<Pessoa> call, Throwable t) {

            }
        });

        final int finalId_usuario1 = id_usuario;
        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), AtualizaPessoaActivity.class);
                intent.putExtra("id_usuario", finalId_usuario1);
                startActivity(intent);
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PrincipalActivity.this);
                builder.setTitle("Tem Certeza que deseja excluir sua conta?");
                //define um botão como positivo
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Gson g = new GsonBuilder().registerTypeAdapter(Pessoa.class, new PessoaDec()).create();
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(PessoaService.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create(g))
                                .build();
                        PessoaService pessoaService = retrofit.create(PessoaService.class);
                        Call<Boolean> call = pessoaService.excluirPessoa(finalId_usuario1);
                        call.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if(response.body() == true){
                                    Toast.makeText(getApplicationContext(), "Conta excluida com sucesso!", Toast.LENGTH_SHORT).show();
                                    intent = new Intent(PrincipalActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Não foi possível excluir sua conta, verifique se você possui conexão com a internet", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(), "Você já está nessa opção", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
