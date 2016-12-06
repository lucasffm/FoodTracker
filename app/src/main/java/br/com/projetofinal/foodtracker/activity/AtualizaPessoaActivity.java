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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.projetofinal.foodtracker.Dec.PessoaDec;
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

public class AtualizaPessoaActivity extends AppCompatActivity {
    private TextWatcher cpfMask;
    private EditText edtAtualizaNome, edtAtualizaLogin, edtAtualizaSenha, edtAtualizaCPF, edtAtualizaCEP, edtAtualizaCidade, edtAtualizaComplemento;
    private Spinner spinner_s;
    private Button btnAtualizar;
    private int id_usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualiza_pessoa);
        edtAtualizaCPF = (EditText) findViewById(R.id.etAtualizaCPF);
        edtAtualizaNome = (EditText) findViewById(R.id.etAtualizaNome);
        edtAtualizaLogin = (EditText) findViewById(R.id.etAtualizaLogin);
        edtAtualizaSenha = (EditText) findViewById(R.id.etAtualizaSenha);
        edtAtualizaCEP = (EditText) findViewById(R.id.etAtualizaCEP);
        edtAtualizaCidade = (EditText) findViewById(R.id.etAtualizaCidade);
        edtAtualizaComplemento = (EditText) findViewById(R.id.etAtualizaComplemento);
        btnAtualizar = (Button) findViewById(R.id.btnAtualizar);
        Bundle extras = getIntent().getExtras();
        if(extras.getInt("id_usuario") != 0)
        {
            id_usuario = extras.getInt("id_usuario");        }



        spinner_s = (Spinner)findViewById(R.id.spinner_atualiza_estado);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.category_state, android.R.layout.simple_list_item_1);
        spinner_s.setAdapter(adapter);

        Gson g = new GsonBuilder().registerTypeAdapter(Pessoa.class, new PessoaDec()).create();
        Retrofit retrofitBuscar = new Retrofit.Builder()
                .baseUrl(PessoaService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(g))
                .build();
        PessoaService pessoaService = retrofitBuscar.create(PessoaService.class);
        Call<Pessoa> call = pessoaService.recuperaPessoaId(id_usuario);
        call.enqueue(new Callback<Pessoa>() {
            @Override
            public void onResponse(Call<Pessoa> call, Response<Pessoa> response) {
                Pessoa p = response.body();
                edtAtualizaNome.setText(p.getNome());
                edtAtualizaLogin.setText(p.getLogin());
                edtAtualizaSenha.setText(p.getSenha());
                edtAtualizaCPF.setText(p.getCpf().toString());
                edtAtualizaCEP.setText(p.getEndereco().getCep().toString());
                String estado = p.getEndereco().getEstado();
                spinner_s.setSelection(getIndex(spinner_s, estado));
                edtAtualizaCidade.setText(p.getEndereco().getCidade());
                edtAtualizaComplemento.setText(p.getEndereco().getComplemento());
                // Inserindo máscara cpf no edit text
                edtAtualizaCPF.addTextChangedListener(Mask.insert(Mask.CPF_MASK, edtAtualizaCPF));
                edtAtualizaCEP.addTextChangedListener(Mask.insert(Mask.CEP_MASK, edtAtualizaCEP));
            }
            @Override
            public void onFailure(Call<Pessoa> call, Throwable t) {

            }
        });

        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atualizaPessoa();
            }
        });


    }
    public void atualizaPessoa(){
        final String cepTratado;
        if (edtAtualizaNome.getText().toString().isEmpty()) {
            Toast.makeText(AtualizaPessoaActivity.this, "Insira o seu nome", Toast.LENGTH_SHORT).show();
            return;
        } else if (edtAtualizaLogin.getText().toString().isEmpty()) {
            Toast.makeText(AtualizaPessoaActivity.this, "Insira o seu Login", Toast.LENGTH_SHORT).show();
            return;
        } else if (edtAtualizaSenha.getText().toString().isEmpty()) {
            Toast.makeText(AtualizaPessoaActivity.this, "Insira sua senha", Toast.LENGTH_SHORT).show();
            return;
        } else if (edtAtualizaCPF.getText().toString().isEmpty()) {
            Toast.makeText(AtualizaPessoaActivity.this, "Insira o seu CPF", Toast.LENGTH_SHORT).show();
            return;
        } else if (edtAtualizaCEP.getText().toString().isEmpty()) {
            Toast.makeText(AtualizaPessoaActivity.this, "Insira o seu CEP", Toast.LENGTH_SHORT).show();
            return;
        } else if (spinner_s.getSelectedItem().toString().isEmpty()) {
            Toast.makeText(AtualizaPessoaActivity.this, "Selecione seu estado", Toast.LENGTH_SHORT).show();
            return;
        } else if (edtAtualizaCidade.getText().toString().isEmpty()) {
            Toast.makeText(AtualizaPessoaActivity.this, "Insira sua cidade", Toast.LENGTH_SHORT).show();
            return;
        } else if (edtAtualizaComplemento.getText().toString().isEmpty()) {
            Toast.makeText(AtualizaPessoaActivity.this, "Insira um complemento para seu endereço", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PessoaService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PessoaService service = retrofit.create(PessoaService.class);
        Pessoa p = new Pessoa();
        Endereco e = new Endereco();
        p.setId_pessoa(id_usuario);
        p.setNome(edtAtualizaNome.getText().toString());
        p.setLogin(edtAtualizaLogin.getText().toString());
        p.setSenha(edtAtualizaSenha.getText().toString());
        p.setCpf(Mask.unmask(edtAtualizaCPF.getText().toString()));
        cepTratado = Mask.unmask(edtAtualizaCEP.getText().toString());
        e.setCep(Integer.parseInt(cepTratado));
        e.setEstado(spinner_s.getSelectedItem().toString());
        e.setCidade(edtAtualizaCidade.getText().toString());
        e.setComplemento(edtAtualizaComplemento.getText().toString());
        p.setEndereco(e);
        Call<Boolean> pessoa = service.atualizarPessoa(p);
        pessoa.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body()) {
                        Toast.makeText(getApplicationContext(), "Atualizado com sucesso", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(AtualizaPessoaActivity.this, PrincipalActivity.class);
                        myIntent.putExtra("id_usuario", id_usuario);
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

    //private method of your class
    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

}
