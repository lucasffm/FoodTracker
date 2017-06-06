package br.com.projetofinal.foodtracker.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.projetofinal.foodtracker.R;
import br.com.projetofinal.foodtracker.interfaces.AgendaService;
import br.com.projetofinal.foodtracker.interfaces.PessoaService;
import br.com.projetofinal.foodtracker.modelo.Agenda;
import br.com.projetofinal.foodtracker.modelo.Carro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AgendaActivity extends AppCompatActivity {
    private Double longitude, latitude;
    private EditText edtHoraInicio, edtHoraFim, edtData;
    private Button adicionarAgenda;
    private Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            latitude = bundle.getDouble("lat");
            longitude = bundle.getDouble("long");
        }
        adicionarAgenda = (Button) findViewById(R.id.btn_cadastrar_agenda);
        edtHoraInicio = (EditText) findViewById(R.id.edt_hora_inicio);
        edtHoraFim = (EditText) findViewById(R.id.edt_hora_fim);
        edtData = (EditText) findViewById(R.id.edt_data_agenda);


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, i);
                myCalendar.set(Calendar.MONTH, i1);
                myCalendar.set(Calendar.DAY_OF_MONTH, i2);
                updateLabel();
            }
        };

        edtData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AgendaActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        adicionarAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtData.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Preencha o campo data", Toast.LENGTH_SHORT).show();
                }
                else if(edtHoraInicio.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Preencha o campo hora inicio", Toast.LENGTH_SHORT).show();
                }else if(edtHoraFim.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Preencha o campo hora fim", Toast.LENGTH_SHORT).show();
                }else{
                    inserirAgenda();
                }
            }
        });



    }
    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edtData.setText(sdf.format(myCalendar.getTime()));
    }

    private void inserirAgenda(){

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formata_hora = new SimpleDateFormat("HH:mm");
        String dataSt = edtData.getText().toString();
        String horaIncioST = edtHoraInicio.getText().toString();
        String horaFimST = edtHoraFim.getText().toString();
        Date data = null;
        Date hora_inicio = null;
        Date hora_fim = null;
        try {
            data = formatter.parse(dataSt);
            hora_inicio = formata_hora.parse(horaIncioST);
            hora_fim = formata_hora.parse(horaFimST);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AgendaService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AgendaService agendaService = retrofit.create(AgendaService.class);
        Agenda agenda = new Agenda();
        Carro carro = new Carro();
        carro.setId_carro(1);
        agenda.setLatitude(latitude);
        agenda.setLongitude(longitude);
        agenda.setCarro(carro);
        agenda.setData(data);
        agenda.setHora_inicio(hora_inicio);
        agenda.setHora_fim(hora_fim);

        Call<Boolean> ag = agendaService.inserirAgenda(agenda);
        ag.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body() == true) {
                        Toast.makeText(getApplicationContext(), "Registrado com sucesso", Toast.LENGTH_SHORT).show();
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
}