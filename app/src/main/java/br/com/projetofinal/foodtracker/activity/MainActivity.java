package br.com.projetofinal.foodtracker.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {
    private Button btnLogar;
    private EditText edtLogin, edtSenha;
    private TextView txtRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        btnLogar = (Button) findViewById(R.id.btnLogar);
        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logar();
            }
        });


        txtRegistrar = (TextView) findViewById(R.id.txtRegistrar);
        txtRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

    }

    public void logar(){
        edtLogin = (EditText) findViewById(R.id.edtLogin);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        if(edtLogin.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this, "Insira seu Login", Toast.LENGTH_SHORT).show();
        }else if(edtSenha.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this, "Insira sua Senha", Toast.LENGTH_SHORT).show();
        }

        String login = edtLogin.getText().toString();
        String senha = edtSenha.getText().toString();

        Gson g = new GsonBuilder().registerTypeAdapter(Pessoa.class, new PessoaDec()).create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PessoaService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(g))
                .build();

        PessoaService pessoaService = retrofit.create(PessoaService.class);

        Call<Pessoa> call = pessoaService.verificaLogin(login, senha);
        call.enqueue(new Callback<Pessoa>() {
            @Override
            public void onResponse(Call<Pessoa> call, Response<Pessoa> response) {
                if (response.isSuccessful()) {
                    if(response.body() == null){
                        Toast.makeText(MainActivity.this, "Login ou senha inválidos, tente novamente", Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        Pessoa p = response.body();
                        int id = p.getId_pessoa();
                        int tipo = p.getTipoUsuario();
                        if(tipo == 1){
                            Intent intent = new Intent(MainActivity.this, PrincipalActivity.class);
                            intent.putExtra("id_usuario", id);
                            startActivity(intent);
                            finish();
                        }else if(tipo == 3){
                            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                            intent.putExtra("id_usuario", id);
                            startActivity(intent);
                            finish();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<Pessoa> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Não foi possível conectar no aplicativo, verifique sua conexão.", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
