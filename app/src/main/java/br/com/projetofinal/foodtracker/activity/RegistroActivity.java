package br.com.projetofinal.foodtracker.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import br.com.projetofinal.foodtracker.R;
import br.com.projetofinal.foodtracker.interfaces.PessoaService;
import br.com.projetofinal.foodtracker.mascaras.Mask;
import br.com.projetofinal.foodtracker.modelo.Endereco;
import br.com.projetofinal.foodtracker.modelo.Pessoa;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistroActivity extends AppCompatActivity {

    private TextWatcher cpfMask;
    private EditText edtNome, edtRegistraLogin, edtRegistraSenha, edtCPF, edtCEP, edtCidade, edtComplemento;
    private Spinner spinner_s;
    private Button btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        // Recuperando as views
        edtCPF = (EditText) findViewById(R.id.etCPF);
        edtNome = (EditText) findViewById(R.id.etNome);
        edtRegistraLogin = (EditText) findViewById(R.id.etRegistraLogin);
        edtRegistraSenha = (EditText) findViewById(R.id.etRegistraSenha);
        edtCEP = (EditText) findViewById(R.id.etCEP);
        edtCidade = (EditText) findViewById(R.id.etCidade);
        edtComplemento = (EditText) findViewById(R.id.etComplemento);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);

        // Inserindo máscara cpf no edit text
        edtCPF.addTextChangedListener(Mask.insert(Mask.CPF_MASK, edtCPF));
        edtCEP.addTextChangedListener(Mask.insert(Mask.CEP_MASK, edtCEP));

        spinner_s = (Spinner)findViewById(R.id.spinner_estado);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.category_state, android.R.layout.simple_list_item_1);
        spinner_s.setAdapter(adapter);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarPessoa();
            }
        });
    }

    private void registrarPessoa() {
        final String cepTratado;

        if (edtNome.getText().toString().isEmpty()) {
            Toast.makeText(RegistroActivity.this, "Insira o seu nome", Toast.LENGTH_SHORT).show();
            return;
        } else if (edtRegistraLogin.getText().toString().isEmpty()) {
            Toast.makeText(RegistroActivity.this, "Insira o seu Login", Toast.LENGTH_SHORT).show();
            return;
        } else if (edtRegistraSenha.getText().toString().isEmpty()) {
            Toast.makeText(RegistroActivity.this, "Insira sua senha", Toast.LENGTH_SHORT).show();
            return;
        } else if (edtCPF.getText().toString().isEmpty()) {
            Toast.makeText(RegistroActivity.this, "Insira o seu CPF", Toast.LENGTH_SHORT).show();
            return;
        } else if (edtCEP.getText().toString().isEmpty()) {
            Toast.makeText(RegistroActivity.this, "Insira o seu CEP", Toast.LENGTH_SHORT).show();
            return;
        } else if (spinner_s.getSelectedItem().toString().isEmpty()) {
            Toast.makeText(RegistroActivity.this, "Selecione seu estado", Toast.LENGTH_SHORT).show();
            return;
        } else if (edtCidade.getText().toString().isEmpty()) {
            Toast.makeText(RegistroActivity.this, "Insira sua cidade", Toast.LENGTH_SHORT).show();
            return;
        } else if (edtComplemento.getText().toString().isEmpty()) {
            Toast.makeText(RegistroActivity.this, "Insira um complemento para seu endereço", Toast.LENGTH_SHORT).show();
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PessoaService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

            PessoaService service = retrofit.create(PessoaService.class);
            Pessoa p = new Pessoa();
            Endereco e = new Endereco();
            p.setNome(edtNome.getText().toString());
            p.setLogin(edtRegistraLogin.getText().toString());
            p.setSenha(edtRegistraSenha.getText().toString());
            p.setCpf(Mask.unmask(edtCPF.getText().toString()));
            cepTratado = Mask.unmask(edtCEP.getText().toString());
            e.setCep(Integer.parseInt(cepTratado));
            e.setEstado(spinner_s.getSelectedItem().toString());
            e.setCidade(edtCidade.getText().toString());
            e.setComplemento(edtComplemento.getText().toString());
            p.setEndereco(e);
            Call<Boolean> pessoa = service.inserirPessoa(p);
            pessoa.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful()) {
                        if (response.body()) {
                            Toast.makeText(getApplicationContext(), "Registrado com sucesso", Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(RegistroActivity.this, MainActivity.class);
                            startActivity(myIntent);
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
}
