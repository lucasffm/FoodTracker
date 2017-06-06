package br.com.projetofinal.foodtracker.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.projetofinal.foodtracker.R;
import br.com.projetofinal.foodtracker.dec.AgendaDec;
import br.com.projetofinal.foodtracker.dec.AtendimentoDec;
import br.com.projetofinal.foodtracker.interfaces.AgendaService;
import br.com.projetofinal.foodtracker.interfaces.AtendimentoService;
import br.com.projetofinal.foodtracker.modelo.Agenda;
import br.com.projetofinal.foodtracker.modelo.Atendimento;
import br.com.projetofinal.foodtracker.modelo.Carro;
import br.com.projetofinal.foodtracker.modelo.Pessoa;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static br.com.projetofinal.foodtracker.R.id.mapAtual;

public class LocalizacaoAtualActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private GoogleMap mMap;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private Double longitude, latitude;
    private LocationManager locationManager;
    private Location locationNikko, locationGps;
    private Criteria criteria;
    private String provider;
    private Intent intent;
    Date horaInicio, horaFim, hora_sistema;
    private ProgressDialog dialog;
    private LatLng locAtual;
    private FloatingActionButton realizarCheckInBtn;
    private int id_usuario, id_agenda;
    private Boolean check;
    private Location mLastLocation;
    // Cliente do google para interagir com GoogleAPI
    private GoogleApiClient mGoogleApiClient;
    private AlertDialog alerta;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacao_atual);
        id_usuario = 0;
        Bundle extras = getIntent().getExtras();
        if (extras.getInt("id_usuario") != 0) {
            id_usuario = extras.getInt("id_usuario");
        }


        pesquisarAtendimento(id_usuario);




        buildGoogleApiClient();
        criteria = new Criteria();
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager.getBestProvider(criteria, true);
        provider = locationManager.getBestProvider(criteria, true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(mapAtual);
        mapFragment.getMapAsync(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_usuario);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        final int finalId_usuario = id_usuario;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.minha_conta:
                        intent = new Intent(getApplicationContext(), PrincipalActivity.class);
                        intent.putExtra("id_usuario", finalId_usuario);
                        startActivity(intent);
                    case R.id.localizacao_nikko:
                        Toast.makeText(getApplicationContext(), "Você já está nessa opção", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.agenda_nikko:
                        intent = new Intent(getApplicationContext(), AgendaUsuarioActivity.class);
                        intent.putExtra("id_usuario", finalId_usuario);
                        startActivity(intent);
                        break;
                    case R.id.cartao_fidelidade:
                        intent = new Intent(getApplicationContext(), CartaoFidelidadeActivity.class);
                        intent.putExtra("id_usuario", finalId_usuario);
                        startActivity(intent);
                        break;
                    case R.id.sugestoes:
                        intent = new Intent(getApplicationContext(), SugestaoActivity.class);
                        intent.putExtra("id_usuario", finalId_usuario);
                        startActivity(intent);
                        break;
                    case R.id.avaliação:
                        intent = new Intent(getApplicationContext(), AvaliacaoActivity.class);
                        intent.putExtra("id_usuario", finalId_usuario);
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void getLocation() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            locationGps = new Location(provider);
            locationGps.setLatitude(latitude);
            locationGps.setLongitude(longitude);


        } else {
            Toast.makeText(getApplicationContext(), "Não foi possivel obter suas coordenadas.", Toast.LENGTH_SHORT).show();
            realizarCheckInBtn.hide();

        }
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        realizarCheckInBtn = (FloatingActionButton) findViewById(R.id.fab_checkin);
        try {
            dialog = new ProgressDialog(LocalizacaoAtualActivity.this);
            dialog.setMessage("Carregando...");
            dialog.setCancelable(false);
            dialog.show();
            Gson g = new GsonBuilder().registerTypeAdapter(Agenda.class, new AgendaDec()).create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AgendaService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(g))
                    .build();
            AgendaService agendaService = retrofit.create(AgendaService.class);
            Call<Agenda> call = agendaService.getAgendaData();
            call.enqueue(new Callback<Agenda>() {
                @Override
                public void onResponse(Call<Agenda> call, Response<Agenda> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            Agenda ag = response.body();

                            id_agenda = ag.getId_agenda();

                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            Date data = ag.getData();
                            String dataString = formatter.format(data);

                            SimpleDateFormat formata_hora = new SimpleDateFormat("HH:mm");
                            String hora_inicioStr = formata_hora.format(ag.getHora_inicio());
                            String hora_fimStr = formata_hora.format(ag.getHora_fim());
                            Date dateSistema = new Date();
                            String hora_sistemaStr = formata_hora.format(dateSistema);

                            Log.i("HORA", hora_inicioStr);
                            Log.i("HORA", hora_fimStr);
                            Log.i("HORA", hora_sistemaStr);
                            try {
                                horaInicio = formata_hora.parse(hora_inicioStr);
                                horaFim = formata_hora.parse(hora_fimStr);
                                hora_sistema = formata_hora.parse(hora_sistemaStr);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            latitude = ag.getLatitude();
                            longitude = ag.getLongitude();
                            locAtual = new LatLng(latitude, longitude);

                            if(hora_sistema.after(horaInicio) && hora_sistema.before(horaFim)){
                                mMap = googleMap;
                                mMap.getUiSettings().setZoomControlsEnabled(true);
                                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                // Instantiates a new CircleOptions object and defines the center and radius
                                CircleOptions circleOptions = new CircleOptions()
                                        .center(locAtual)
                                        .radius(30);
                                // In meters
                                // Get back the mutable Circle

                                Circle circle = mMap.addCircle(circleOptions);
                                circle.setFillColor(R.color.colorRadius);
                                mMap.setMyLocationEnabled(true);
                                MarkerOptions marker = new MarkerOptions().position(locAtual).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(locAtual));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(19));
                                mMap.addMarker(marker);
                                dialog.dismiss();
                                realizarCheckInBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        getLocation();
                                        locationNikko = new Location(provider);
                                        locationNikko.setLatitude(latitude);
                                        locationNikko.setLongitude(longitude);
                                        Float distancia = locationGps.distanceTo(locationNikko);


                                        if (locationGps != null && locationNikko != null) {
                                            if (distancia <= 30) {
                                                if(id_agenda != 0 && id_usuario != 0){
                                                    realizarCheckIn(check, id_usuario);
                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Você está muito longe do nikko temaki", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Erro ao recuperar as coordenadas", Toast.LENGTH_SHORT).show();
                                            return;
                                        }


                                    }
                                });
                            }else{
                                Toast.makeText(getApplicationContext(), "Nikko temaki ainda não está estacionado.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "O Nikko temaki não possui agenda para hoje.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }


                    } else {
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

        } catch (SecurityException ex) {
            Log.e("MapsActivity", "erro no mapa", ex);
        }

    }

    private void pesquisarAtendimento(int idPessoa){
        Gson g = new GsonBuilder().registerTypeAdapter(Atendimento.class, new AtendimentoDec()).create();
        Retrofit retrofitBuscar = new Retrofit.Builder()
                .baseUrl(AtendimentoService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(g))
                .build();
        AtendimentoService service = retrofitBuscar.create(AtendimentoService.class);
        Call<Boolean> call = service.checkAgenda(id_usuario);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    boolean resposta = response.body();
                    if (resposta){
                        check = true;
                    }else{
                        check = false;
                    }
                }
                else{
                    check = false;
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });


    }

    private void realizarCheckIn(final boolean check, final int idPessoa) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atendimento");
        builder.setMessage("Você está sendo atendido no nikko temaki?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                if(check == true){
                    Toast.makeText(getApplicationContext(), "Você já efetuou um check-in no evento de hoje", Toast.LENGTH_SHORT).show();
                    realizarCheckInBtn.hide();
                    return;
                }else{

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(AtendimentoService.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    AtendimentoService atendimentoService = retrofit.create(AtendimentoService.class);
                    Atendimento atendimento = new Atendimento();
                    Pessoa pessoa = new Pessoa();
                    Carro carro = new Carro();
                    pessoa.setId_pessoa(idPessoa);
                    carro.setId_carro(1);
                    atendimento.setPessoa(pessoa);
                    atendimento.setCarro(carro);

                    Call<Boolean> at = atendimentoService.inserirAtendimento(atendimento);
                    at.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.isSuccessful()) {
                                if (response.body() == true) {
                                    Toast.makeText(getApplicationContext(), "Check-in realizado com sucesso.", Toast.LENGTH_SHORT).show();
                                    realizarCheckInBtn.hide();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Erro na conexão com o servidor.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {

                        }
                    });

                }
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "Check-in cancelado.", Toast.LENGTH_SHORT).show();
                return;
            }
        });
        alerta = builder.create();
        alerta.show();


    }


    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }


    @Override
    public void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("CONERROR", "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("LocalizacaoAtual Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
}