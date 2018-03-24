package pe.edu.upc.municipalidadmovil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.municipalidadmovil.adaptador.QuejaAdapter;
import pe.edu.upc.municipalidadmovil.modelo.Queja;
import pe.edu.upc.municipalidadmovil.servicios.queja.QuejaConsultarService;

public class MainQuejasActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<Queja> Quejas = new ArrayList<>();
    RecyclerView.LayoutManager quejasLayoutManager;
    RecyclerView quejaRecyclerView;
    QuejaAdapter quejaAdapter;
    private FirebaseAuth mAuth;
    FloatingActionButton fab;
    View v;
    TextView txtNombreMenu;
    ImageView imgFotoMenu;
    String estadoQ="T";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quejas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        quejasLayoutManager = new LinearLayoutManager(this);
        quejaRecyclerView = (RecyclerView) findViewById(R.id.rvQuejas);
        quejaRecyclerView.setLayoutManager(quejasLayoutManager);
        mAuth = FirebaseAuth.getInstance();

        fab = (FloatingActionButton) findViewById(R.id.fab_Registrar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent iconIntent = new Intent(MainQuejasActivity.this, RegistrarQuejaActivity.class);
                startActivity(iconIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        txtNombreMenu = (TextView) hView.findViewById(R.id.txtNombreMenu);
        imgFotoMenu = (ImageView) hView.findViewById(R.id.imgMenu);
        //estadoQ = getIntent().getExtras().getString("pEstado");

        FirebaseUser user = mAuth.getCurrentUser();
        txtNombreMenu.setText(user.getEmail().toString());

        Picasso.with(hView.getContext()).
                load(user.getPhotoUrl()).into(imgFotoMenu);

      /*  Glide.with(hView.getContext())
                .load(user.getPhotoUrl())
                .fitCenter()
                .centerCrop()
                .into(imgFotoMenu);

        imgFotoMenu.setImageURI(user.getPhotoUrl());*/
        navigationView.setNavigationItemSelectedListener(this);

        ConsultarQuejas();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void ConsultarQuejas(){
        FirebaseUser user = mAuth.getCurrentUser();
        QuejaConsultarService serv = new QuejaConsultarService(Quejas,quejaRecyclerView,quejaAdapter,MainQuejasActivity.this, user.getEmail().toString(),estadoQ);
        serv.execute();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_consultar) {
            Intent iconIntent = new Intent(this, MainQuejasActivity.class);
            this.startActivity(iconIntent);

        } else if (id == R.id.nav_Registrar) {
            Intent iconIntent = new Intent(this, RegistrarQuejaActivity.class);
            this.startActivity(iconIntent);

        } else if (id == R.id.nav_salir) {
            FirebaseAuth.getInstance().signOut();

            Intent iconIntent = new Intent(this, LoginActivity.class);
            this.startActivity(iconIntent);
        }
        else if (id == R.id.nav_municipalidad) {
            Intent iconIntent = new Intent(this, MapaVerActivity.class);
            this.startActivity(iconIntent);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
