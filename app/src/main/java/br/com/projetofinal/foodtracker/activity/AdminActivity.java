package br.com.projetofinal.foodtracker.activity;

import android.content.Context;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.projetofinal.foodtracker.Dec.QrCodeDec;
import br.com.projetofinal.foodtracker.R;
import br.com.projetofinal.foodtracker.interfaces.CartaoService;
import br.com.projetofinal.foodtracker.modelo.QrCode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Button btnGerarQr;
    private Intent intent;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        btnGerarQr = (Button) findViewById(R.id.btnCarregarQr);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        int id_usuario = 0;
        Bundle extras = getIntent().getExtras();
        if(extras.getInt("id_usuario") != 0)
        {
            id_usuario = extras.getInt("id_usuario");
        }


        btnGerarQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                builder.setTitle("Confirme a inclusão dos QrCodes no Banco:");
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Gson g = new GsonBuilder().registerTypeAdapter(QrCode.class, new QrCodeDec()).create();
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(CartaoService.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create(g))
                                .build();
                        CartaoService cartaoService = retrofit.create(CartaoService.class);
                        Call<String> call = cartaoService.gerarQrCode("true");
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {

                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "Foram carregados mais 50 QrCodes no banco de dados", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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
                    case R.id.minha_conta_admin:
                        Toast.makeText(getApplicationContext(), "Você já está nessa opção", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.localizacao_nikko:
                        intent = new Intent(getApplicationContext(), AdminActivity.class);
                        intent.putExtra("id_usuario", finalId_usuario);
                        startActivity(intent);
                        break;
                    case R.id.agenda_nikko:
                        intent = new Intent(getApplicationContext(), AdminActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.relatorio_cartao_fidelidade:
                        intent = new Intent(getApplicationContext(), AdminActivity.class);
                        intent.putExtra("id_usuario", finalId_usuario);
                        startActivity(intent);
                        break;
                    case R.id.relatorio_notificacao:
                        intent = new Intent(getApplicationContext(), AdminActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.relatorio_visu_check:
                        intent = new Intent(getApplicationContext(), AdminActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.relatorio_avaliacao:
                        intent = new Intent(getApplicationContext(), AdminActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.relatorio_sugestao:
                        intent = new Intent(getApplicationContext(), AdminActivity.class);
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
