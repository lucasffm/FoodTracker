package br.com.projetofinal.foodtracker.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.projetofinal.foodtracker.R;
import br.com.projetofinal.foodtracker.dec.AgendaDec;
import br.com.projetofinal.foodtracker.interfaces.AgendaService;
import br.com.projetofinal.foodtracker.modelo.Agenda;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AtualizaAgendaActivity extends AppCompatActivity {

    private EditText edtHoraInicio, edtHoraFim, edtData;
    private Button atualizarAgenda;
    private Calendar myCalendar = Calendar.getInstance();
    private int id_agenda;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualiza_agenda);
        id_agenda = 0;
        Bundle extras = getIntent().getExtras();
        if(extras.getInt("id_agenda") != 0)
        {
            id_agenda = extras.getInt("id_agenda");
        }

        recuperaAgenda();

        atualizarAgenda = (Button) findViewById(R.id.btn_atualiza_agenda);
        edtHoraInicio = (EditText) findViewById(R.id.edt_atualiza_hora_inicio);
        edtHoraFim = (EditText) findViewById(R.id.edt_atualiza_hora_fim);
        edtData = (EditText) findViewById(R.id.edt_atualiza_data_agenda);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, i);
                myCalendar.set(Calendar.MONTH, i1);
                myCalendar.set(Calendar.DAY_OF_MONTH, i2);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                edtData.setText(sdf.format(myCalendar.getTime()));
            }
        };

        edtData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AtualizaAgendaActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        atualizarAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtData.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Por favor insira uma data", Toast.LENGTH_SHORT).show();
                }else if(edtHoraInicio.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Por favor insira a hora de inicio do evento", Toast.LENGTH_SHORT).show();
                }else if (edtHoraFim.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Por favor insira a hora do fim do evento", Toast.LENGTH_SHORT).show();
                }else{
                    atualizarAgenda();
                }

            }
        });

    }


    public void atualizarAgenda(){



        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formata_hora = new SimpleDateFormat("HH:mm");
        String dataSt = edtData.getText().toString();
        String horaIncioST = edtHoraInicio.getText().toString();
        String horaFimST = edtHoraFim.getText().toString();
        Date data;
        Date hora_inicio;
        Date hora_fim;
        try {

            data = formatter.parse(dataSt);
            hora_inicio = formata_hora.parse(horaIncioST);
            hora_fim = formata_hora.parse(horaFimST);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AgendaService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            AgendaService service = retrofit.create(AgendaService.class);
            Agenda agenda = new Agenda();
            agenda.setId_agenda(id_agenda);
            agenda.setData(data);
            agenda.setHora_inicio(hora_inicio);
            agenda.setHora_fim(hora_fim);
            Call<Boolean> agendaAG = service.atualizaAgenda(agenda);
            agendaAG.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful()){
                        if(response.body() == true){
                            Toast.makeText(getApplicationContext(), "Atualizado com sucesso", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "Ocorreu algum erro, tente novamente.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Erro " + t.getCause(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }




    }

    public void recuperaAgenda(){
        dialog = new ProgressDialog(AtualizaAgendaActivity.this);
        dialog.setMessage("Carregando...");
        dialog.setCancelable(false);
        dialog.show();
        Gson g = new GsonBuilder().registerTypeAdapter(Agenda.class, new AgendaDec()).create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AgendaService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(g))
                .build();
        AgendaService agendaService = retrofit.create(AgendaService.class);
        Call<Agenda> call = agendaService.getAgendaID(id_agenda);
        call.enqueue(new Callback<Agenda>() {
            @Override
            public void onResponse(Call<Agenda> call, Response<Agenda> response) {
                if(response.isSuccessful()){
                    dialog.dismiss();
                    Agenda ag = response.body();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date data = ag.getData();
                    String dataString = formatter.format(data);

                    SimpleDateFormat formata_hora = new SimpleDateFormat("HH:mm");
                    String hora_inicio = formata_hora.format(ag.getHora_inicio());
                    String hora_fim = formata_hora.format(ag.getHora_fim());

                    edtHoraInicio.setText(hora_inicio);
                    edtHoraFim.setText(hora_fim);
                    edtData.setText(dataString);
                }else{
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Ocorreu algum erro, tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Agenda> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Ocorreu algum erro, tente novamente mais tarde.", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
