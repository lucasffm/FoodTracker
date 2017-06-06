package br.com.projetofinal.foodtracker.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.projetofinal.foodtracker.R;
import br.com.projetofinal.foodtracker.dec.AgendaDec;
import br.com.projetofinal.foodtracker.dec.GraficoAvaliacaoDec;
import br.com.projetofinal.foodtracker.interfaces.AgendaService;
import br.com.projetofinal.foodtracker.interfaces.RelatorioService;
import br.com.projetofinal.foodtracker.modelo.Agenda;
import br.com.projetofinal.foodtracker.modelo.GraficoAvaliacao;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RelatorioAvaliacaoActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private PieChart pieChartAvaliacao;
    private int id_usuario, notaPositiva, notaNegativa, notaImparcial, totalAvaliacoes;
    private Float percentPositiva, percentNegativa, percentImparcial;
    private Intent intent;
    private Calendar myCalendar = Calendar.getInstance();
    private EditText edtData;
    private Button btnGerar;
    private ProgressDialog dialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_avaliacao);
        Bundle extras = getIntent().getExtras();
        if(extras.getInt("id_usuario") != 0)
        {
            id_usuario = extras.getInt("id_usuario");
        }
        edtData = (EditText) findViewById(R.id.edt_data_relatorio_avaliacao);
        btnGerar = (Button) findViewById(R.id.btn_gerar_relatorio_avaliacao);

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
                new DatePickerDialog(RelatorioAvaliacaoActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        pieChartAvaliacao = (PieChart) findViewById(R.id.piechart_avaliacao);

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
                        intent = new Intent(getApplicationContext(), AdminActivity.class);
                        intent.putExtra("id_usuario", finalId_usuario);
                        startActivity(intent);
                        break;
                    case R.id.agenda_nikko:
                        intent = new Intent(getApplicationContext(), MapsActivity.class);
                        intent.putExtra("id_usuario", finalId_usuario);
                        startActivity(intent);
                        break;
                    case R.id.relatorio_cartao_fidelidade:
                        Toast.makeText(getApplicationContext(), "Opção desativada", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.relatorio_notificacao:
                        Toast.makeText(getApplicationContext(), "Opção desativada", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.relatorio_visu_check:
                        Toast.makeText(getApplicationContext(), "Opção desativada", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.relatorio_avaliacao:
                        Toast.makeText(getApplicationContext(), "Você já está nesta opção.", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.relatorio_sugestao:
                        Toast.makeText(getApplicationContext(), "Opção desativada", Toast.LENGTH_SHORT).show();
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




        btnGerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtData.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Preencha o campo data", Toast.LENGTH_SHORT).show();
                }else{
                    String date = edtData.getText().toString();
                    recuperaNotas(date);
                }
            }
        });
    }

    private void gerarRelatorio() {
        float[] yData = {percentPositiva, percentNegativa, percentImparcial};
        String[] xData = {"Positiva", "Negativa", "Imparcial"};
        Description desc = new Description();
        desc.setText("");
        pieChartAvaliacao.setRotationEnabled(true);
        pieChartAvaliacao.setHoleRadius(33f);
        pieChartAvaliacao.setDescription(desc);
        pieChartAvaliacao.setRotationEnabled(true);
        pieChartAvaliacao.setTransparentCircleAlpha(0);
        pieChartAvaliacao.setCenterText("Avaliações do dia");
        pieChartAvaliacao.setCenterTextSize(10);
        addDataSet(yData, xData);

    }

    private void addDataSet(float[] yData, String[] xData){


        List<PieEntry> entries = new ArrayList<>();

        for (int i = 0;i < yData.length;i++){
            for (i = 0;i < xData.length;i++){
                entries.add(new PieEntry(yData[i], yData[i] + "% " + xData[i].toUpperCase()));
            }
        }


        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setSliceSpace(1);
        pieDataSet.setValueTextColor(12);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GREEN);
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.GRAY);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);
        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend = pieChartAvaliacao.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setEnabled(true);
        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChartAvaliacao.setData(pieData);
        pieChartAvaliacao.invalidate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void recuperaNotas(String date){
        dialog = new ProgressDialog(RelatorioAvaliacaoActivity.this);
        dialog.setMessage("Recuperando notas...");
        dialog.setCancelable(true);
        dialog.show();
        String dia = date.substring(0,2);
        String mes = date.substring(3,5);
        String ano = date.substring(6);
        String DataFormatada = (ano + "-" + mes + "-" + dia);
        Gson g = new GsonBuilder().registerTypeAdapter(GraficoAvaliacao.class, new GraficoAvaliacaoDec()).create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RelatorioService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(g))
                .build();
        RelatorioService service = retrofit.create(RelatorioService.class);
        Call<GraficoAvaliacao> avaliacaoCall = service.getRelatorioAvaliacao(DataFormatada);
        avaliacaoCall.enqueue(new Callback<GraficoAvaliacao>() {
            @Override
            public void onResponse(Call<GraficoAvaliacao> call, Response<GraficoAvaliacao> response) {
                if(response.isSuccessful()){
                    GraficoAvaliacao graficoAvaliacao = response.body();
                    notaPositiva = graficoAvaliacao.getNotaPositiva();
                    notaNegativa = graficoAvaliacao.getNotaNegativa();
                    notaImparcial = graficoAvaliacao.getNotaImparcial();
                    totalAvaliacoes = notaPositiva + notaNegativa + notaImparcial;
                    if(totalAvaliacoes == 0){
                        Toast.makeText(getApplicationContext(), "Não há avaliações nesta data.", Toast.LENGTH_SHORT).show();
                    }else{
                        percentPositiva = Float.valueOf((notaPositiva * 100)/totalAvaliacoes);
                        percentNegativa = Float.valueOf((notaNegativa * 100)/totalAvaliacoes);
                        percentImparcial = Float.valueOf((notaImparcial * 100)/totalAvaliacoes);
                        gerarRelatorio();
                        Log.i("LOGNOTA", percentPositiva + "%" + percentNegativa + "%" + percentImparcial + "%");
                    }
                    dialog.dismiss();
                }else{
                    Log.i("LOGNOTA", response.message());
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<GraficoAvaliacao> call, Throwable t) {

            }
        });

    }

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edtData.setText(sdf.format(myCalendar.getTime()));
    }


}
