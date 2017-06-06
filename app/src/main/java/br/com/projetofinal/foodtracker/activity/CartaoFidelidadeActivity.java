package br.com.projetofinal.foodtracker.activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import br.com.projetofinal.foodtracker.dec.CartaoDec;
import br.com.projetofinal.foodtracker.dec.QrCodeDec;
import br.com.projetofinal.foodtracker.R;
import br.com.projetofinal.foodtracker.interfaces.CartaoService;
import br.com.projetofinal.foodtracker.modelo.CartaoFidelidade;
import br.com.projetofinal.foodtracker.modelo.Pessoa;
import br.com.projetofinal.foodtracker.modelo.QrCode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartaoFidelidadeActivity extends AppCompatActivity {
    private Button cadQrCode, utilizarPremio;
    private TextView total_cadastrados, codigos_restantes;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private Intent intent;
    private int id_usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartao_fidelidade);
        Bundle extras = getIntent().getExtras();
        if(extras.getInt("id_usuario") != 0)
        {
            id_usuario = extras.getInt("id_usuario");
        }
        total_cadastrados = (TextView) findViewById(R.id.total_cadastrados);
        codigos_restantes = (TextView) findViewById(R.id.codigos_restantes);

        Gson g = new GsonBuilder().registerTypeAdapter(Pessoa.class, new CartaoDec()).create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CartaoService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(g))
                .build();
        CartaoService cartaoService = retrofit.create(CartaoService.class);
        Call<String> call = cartaoService.getQrCode(id_usuario);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    if(response.body() != null){
                        String total = response.body();
                        total_cadastrados.setText("Códigos Cadastrados: " + total);
                        codigos_restantes.setText("Insira mais " + (10 - Integer.parseInt(total)) + " códigos para ser contemplado!");

                        if(Integer.parseInt(total) < 10){
                            utilizarPremio.setVisibility(View.INVISIBLE);
                        }else if (10 - Integer.parseInt(total) == 0){
                            cadQrCode.setVisibility(View.INVISIBLE);
                            codigos_restantes.setText("Você já cadastrou 10 códigos, clique no botão utilizar prêmio para ganhar seu cupom de desconto.");
                        }else{
                            utilizarPremio.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });




        final Activity activity = this;
        cadQrCode = (Button) findViewById(R.id.btn_cadastrar_qrcode);
        utilizarPremio = (Button) findViewById(R.id.btn_utilizar_premio);

        cadQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Posicione o QRCODE na câmera para leitura.");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });


        utilizarPremio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(CartaoFidelidadeActivity.this, PremioActivity.class);
                intent.putExtra("id_usuario", id_usuario);
                startActivity(intent);
                finish();
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_usuario);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.minha_conta:
                        intent = new Intent(CartaoFidelidadeActivity.this, PrincipalActivity.class);
                        intent.putExtra("id_usuario", id_usuario);
                        startActivity(intent);
                        break;
                    case R.id.localizacao_nikko:
                        intent = new Intent(getApplicationContext(), LocalizacaoAtualActivity.class);
                        intent.putExtra("id_usuario", id_usuario);
                        startActivity(intent);
                        break;
                    case R.id.agenda_nikko:
                        intent = new Intent(getApplicationContext(), AgendaUsuarioActivity.class);
                        intent.putExtra("id_usuario", id_usuario);
                        startActivity(intent);
                        break;
                    case R.id.cartao_fidelidade:
                        Toast.makeText(getApplicationContext(), "Você já está nessa opção", Toast.LENGTH_SHORT).show();
                        intent.putExtra("id_usuario", id_usuario);
                        break;
                    case R.id.sugestoes:
                        intent = new Intent(getApplicationContext(), SugestaoActivity.class);
                        intent.putExtra("id_usuario", id_usuario);
                        startActivity(intent);
                        break;
                    case R.id.avaliação:
                        intent = new Intent(getApplicationContext(), AvaliacaoActivity.class);
                        intent.putExtra("id_usuario", id_usuario);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() == null){
                Toast.makeText(this, "Você cancelou o scaneamento!", Toast.LENGTH_SHORT).show();
            }else{
                Gson g = new GsonBuilder().registerTypeAdapter(QrCode.class, new QrCodeDec()).create();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(CartaoService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(g))
                        .build();
                final CartaoService cartaoService = retrofit.create(CartaoService.class);
                Call<QrCode> call = cartaoService.getQrCode(result.getContents());
                call.enqueue(new Callback<QrCode>() {
                    @Override
                    public void onResponse(Call<QrCode> call, Response<QrCode> response) {
                        if(response.isSuccessful()){
                            if(response.body() != null){
                                final QrCode qrCode = response.body();
                                //Toast.makeText(getApplicationContext(), "ID: " + qrCode.getId_qrcode(), Toast.LENGTH_SHORT).show();
                                if(qrCode.getStatus().equals("ativo")){
                                    Bundle extras = getIntent().getExtras();
                                    if(extras.getInt("id_usuario") != 0)
                                    {
                                        id_usuario = extras.getInt("id_usuario");
                                    }
                                    CartaoFidelidade cf = new CartaoFidelidade();
                                    Pessoa p = new Pessoa();
                                    p.setId_pessoa(id_usuario);
                                    final int id_qrcode = qrCode.getId_qrcode();
                                    qrCode.setId_qrcode(id_qrcode);
                                    cf.setQrCode(qrCode);
                                    cf.setPessoa(p);
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(CartaoService.BASE_URL)
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
                                    CartaoService service = retrofit.create(CartaoService.class);
                                    Call<Boolean> cartao = service.inserirCartao(cf);
                                    cartao.enqueue(new Callback<Boolean>() {
                                        @Override
                                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                            if (response.isSuccessful()){
                                                if (response.body() == true){
                                                    Retrofit retrofit = new Retrofit.Builder()
                                                            .baseUrl(CartaoService.BASE_URL)
                                                            .addConverterFactory(GsonConverterFactory.create())
                                                            .build();
                                                    CartaoService service = retrofit.create(CartaoService.class);
                                                    QrCode code = new QrCode();
                                                    code.setStatus("inativo");
                                                    code.setId_qrcode(id_qrcode);
                                                    Call<Boolean> qrcode = service.atualizaQrcode(code);
                                                    qrcode.enqueue(new Callback<Boolean>() {
                                                        @Override
                                                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                                            if (response.isSuccessful()){
                                                                if (response.body() == true){
                                                                    Toast.makeText(getApplicationContext(), "Cartão Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                                                                    recreate();
                                                                }
                                                                else{
                                                                    Toast.makeText(getApplicationContext(), "Não foi possível cadastrar o qrcode!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<Boolean> call, Throwable t) {

                                                        }
                                                    });

                                                }
                                            }
                                            else{
                                                Toast.makeText(getApplicationContext(), "Código inválido", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Boolean> call, Throwable t) {

                                        }
                                    });
                                }else{
                                    Toast.makeText(getApplicationContext(), "QrCode já foi utilizado!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    }
                    @Override
                    public void onFailure(Call<QrCode> call, Throwable t) {
                    }
                });
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
