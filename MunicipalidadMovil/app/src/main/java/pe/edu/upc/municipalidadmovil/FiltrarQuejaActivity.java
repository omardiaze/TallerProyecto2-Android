package pe.edu.upc.municipalidadmovil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class FiltrarQuejaActivity extends AppCompatActivity {

    String[] tipolist= {"Todos","Pendiente","Asignado","Concluido"};
    Button btnConsultar;
    Spinner spnEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrar_queja);

        btnConsultar= (Button) findViewById(R.id.btnConsultar);
        spnEstado = (Spinner) findViewById(R.id.spnEstadoQueja);
        spnEstado.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tipolist));

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Consultar();
            }
        });
    }

    private void Consultar() {

        String estado;

        if (spnEstado.getSelectedItem().toString() =="Todos"){
            estado="T";
        }
        else if (spnEstado.getSelectedItem().toString() =="Pendiente"){
            estado="P";
        }
        else if (spnEstado.getSelectedItem().toString() =="Asignado"){
            estado="A";
        }
        else {
            estado="C";
        }

        Bundle bundle = new Bundle();
        bundle.putString("pEstado", estado);

        Intent iconIntent = new Intent(FiltrarQuejaActivity.this, MainQuejasActivity.class);
        iconIntent.putExtras(bundle);
        startActivity(iconIntent);

    }
}
