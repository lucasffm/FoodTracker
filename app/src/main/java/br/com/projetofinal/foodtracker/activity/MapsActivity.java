package br.com.projetofinal.foodtracker.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.projetofinal.foodtracker.R;

import static br.com.projetofinal.foodtracker.R.id.map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Intent intent;
    private NavigationView navigationView;
    private int id_usuario;
    private Double longitude = 0.0d , latitude = 0.0d;
    FloatingActionButton fabConsultaAgenda, fabAdicionaAgenda;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Bundle extras = getIntent().getExtras();
        if (extras.getInt("id_usuario") != 0) {
            id_usuario = extras.getInt("id_usuario");
        }

        fabConsultaAgenda = (FloatingActionButton) findViewById(R.id.fab_consultar_agenda);
        fabConsultaAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), ConsultaAgendaActivity.class);
                startActivity(intent);

            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);


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
                switch (item.getItemId()) {
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
                        intent = new Intent(getApplicationContext(), RelatorioAvaliacaoActivity.class);
                        intent.putExtra("id_usuario", finalId_usuario);
                        startActivity(intent);
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


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            mMap = googleMap;
            mMap.setOnMapClickListener(this);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
        }catch (SecurityException ex){
            Log.e("MapsActivity", "erro no mapa", ex);
        }
        // Add a marker in brasilia and move the camera
        LatLng brasilia = new LatLng(-15.76817880231238,-47.88227632641792);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(brasilia));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        fabAdicionaAgenda = (FloatingActionButton) findViewById(R.id.fab_agenda);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            int marker_count=0;
            MarkerOptions marker = new MarkerOptions();
            @Override
            public void onMapClick(LatLng point) {
                if (marker_count < 1) {
                    marker_count += 1;
                    marker.position(new LatLng(point.latitude, point.longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker));
                    mMap.addMarker(marker);
                    latitude = point.latitude;
                    longitude = point.longitude;
                    mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                        @Override
                        public void onMapLongClick(LatLng latLng) {
                            mMap.clear();
                            Toast.makeText(getApplicationContext(), "Marcardor Removido.",
                                    Toast.LENGTH_LONG).show();
                            latitude = 0.0d;
                            longitude = 0.0d;
                            marker_count = 0;
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(), "Só é permitido a seleção de um marcador, faça um toque longo no mapa para retirar o marcador atual",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        fabAdicionaAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), AgendaActivity.class);
                Bundle extras = new Bundle();
                extras.putDouble("long", longitude);
                extras.putDouble("lat", latitude);
                intent.putExtras(extras);
                if(latitude == 0.0d && longitude == 0.0d){
                    Toast.makeText(getApplicationContext(), "Selecione um marcador no mapa para adicionar na agenda.", Toast.LENGTH_SHORT).show();
                }else{
                    startActivity(intent);
                }

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


    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(getApplicationContext(), "Coordenadas: " + latLng.toString(), Toast.LENGTH_SHORT).show();
    }
}
