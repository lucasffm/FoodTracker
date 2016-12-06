package br.com.projetofinal.foodtracker.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.projetofinal.foodtracker.Dec.PessoaDec;
import br.com.projetofinal.foodtracker.R;
import br.com.projetofinal.foodtracker.interfaces.CartaoService;
import br.com.projetofinal.foodtracker.interfaces.PessoaService;
import br.com.projetofinal.foodtracker.mascaras.Mask;
import br.com.projetofinal.foodtracker.modelo.CartaoFidelidade;
import br.com.projetofinal.foodtracker.modelo.Pessoa;
import br.com.projetofinal.foodtracker.modelo.QrCode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PremioActivity extends AppCompatActivity {

    private int id_usuario;
    private TextView textoPremio, dataPremio;
    private Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premio);
        textoPremio = (TextView) findViewById(R.id.txtPremio);
        dataPremio = (TextView) findViewById(R.id.data_premio);
        btnGuardar = (Button) findViewById(R.id.btn_guardar_cupom);

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
                textoPremio.setText("Parabéns " +  p.getNome().toString() + ", você ganhou 10% de desconto no Nikko Temaki, apresente este cupom em até 7 dias e aproveite o melhor food truck oriental do DF.");
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String date = df.format(Calendar.getInstance().getTime());
                dataPremio.setText(date);
            }

            @Override
            public void onFailure(Call<Pessoa> call, Throwable t) {

            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(CartaoService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                CartaoService service = retrofit.create(CartaoService.class);
                CartaoFidelidade cf = new CartaoFidelidade();
                Pessoa p = new Pessoa();
                p.setId_pessoa(id_usuario);
                cf.setStatus("inativo");
                cf.setPessoa(p);
                Call<Boolean> cartao = service.atualizaCartao(cf);
                cartao.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful()){
                            if (response.body() == true){
                                Intent intent = new Intent(PremioActivity.this, CartaoFidelidadeActivity.class);
                                intent.putExtra("id_usuario", id_usuario);
                                startActivity(intent);
                                ActivityCompat.requestPermissions(PremioActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        1);
                                Bitmap bitmap = takeScreenShot(PremioActivity.this);
                                saveBitmap(bitmap);
                                Toast.makeText(getApplicationContext(),"Cupom guardado com sucesso, verifique na sua galeria ou memória interna",Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });

            }
        });

    }

    private static Bitmap takeScreenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();

        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return b;

    }

    public void saveBitmap(Bitmap bitmap) {
        File imagePath = new File(Environment.getExternalStorageDirectory()
                + "/Download/cupom.PNG");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }

    }
}
