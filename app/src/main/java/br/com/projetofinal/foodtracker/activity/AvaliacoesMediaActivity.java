package br.com.projetofinal.foodtracker.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import br.com.projetofinal.foodtracker.R;
import br.com.projetofinal.foodtracker.dec.MediaAvaliacaoDec;
import br.com.projetofinal.foodtracker.interfaces.AgendaService;
import br.com.projetofinal.foodtracker.interfaces.AvaliacaoService;
import br.com.projetofinal.foodtracker.modelo.MediaAvaliacao;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AvaliacoesMediaActivity extends AppCompatActivity {
    private TextView lbl_cozinha, lbl_atendimento, lbl_comida, lbl_ambiente;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacoes_media);

        lbl_cozinha = (TextView)findViewById(R.id.lbl_media_cozinha);
        lbl_atendimento = (TextView)findViewById(R.id.lbl_media_atendimento);
        lbl_comida = (TextView)findViewById(R.id.lbl_media_comida);
        lbl_ambiente = (TextView)findViewById(R.id.lbl_media_ambiente);

        dialog = new ProgressDialog(AvaliacoesMediaActivity.this);
        dialog.setMessage("Recuperando notas...");
        dialog.setCancelable(true);
        dialog.show();


        Gson g = new GsonBuilder().registerTypeAdapter(MediaAvaliacao.class, new MediaAvaliacaoDec()).create();
        Retrofit retrofitBuscar = new Retrofit.Builder()
                .baseUrl(AvaliacaoService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(g))
                .build();
        AvaliacaoService service = retrofitBuscar.create(AvaliacaoService.class);
        Call<MediaAvaliacao> call = service.getMediaNotas();
        call.enqueue(new Callback<MediaAvaliacao>() {
            @Override
            public void onResponse(Call<MediaAvaliacao> call, Response<MediaAvaliacao> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        MediaAvaliacao mediaAvaliacao = response.body();
                        lbl_cozinha.setText("Cozinha: " + mediaAvaliacao.getMediaCozinha());
                        lbl_atendimento.setText("Atendimento: " + mediaAvaliacao.getMediaAtendimento());
                        lbl_comida.setText("Comida: " + mediaAvaliacao.getMediaComida());
                        lbl_ambiente.setText("Ambiente: " + mediaAvaliacao.getMediaAmbiente());
                        dialog.dismiss();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Ocorreu algo de errado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<MediaAvaliacao> call, Throwable t) {

            }
        });



    }
}
