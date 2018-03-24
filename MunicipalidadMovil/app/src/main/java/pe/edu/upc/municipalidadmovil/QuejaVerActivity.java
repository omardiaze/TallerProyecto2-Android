package pe.edu.upc.municipalidadmovil;

import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class QuejaVerActivity extends AppCompatActivity {

    TextView lblTipo;

    TextView lblDescripcion;
    TextView lblEstado;
    TextView lblFecReg;
    ImageView imgFoto;
    String IdQueja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queja_ver);

        lblTipo = (TextView)findViewById(R.id.lblTipoD);
        lblDescripcion = (TextView)findViewById(R.id.lblDescripcionD);
        lblEstado = (TextView)findViewById(R.id.lblEstadoD);
        lblFecReg = (TextView)findViewById(R.id.lblFechaD);
        imgFoto = (ImageView)findViewById(R.id.imgFotoD);

        lblTipo.setText(getIntent().getExtras().getString("pTipo"));
        lblDescripcion.setText(getIntent().getExtras().getString("pDescripcion"));
        lblEstado.setText(getIntent().getExtras().getString("pEstado"));
        lblFecReg.setText(getIntent().getExtras().getString("pFechaReg"));
        IdQueja = getIntent().getExtras().getString("pIdQueja");

        Picasso.with(imgFoto.getContext()).
                load(getIntent().getExtras().getString("pImagen")).into(imgFoto);
/*

        Uri imgUri = Uri.parse(getIntent().getExtras().getString("pImagen"));

        Glide.with(imgFoto.getContext())
                .load(imgUri)
                .fitCenter()
                .centerCrop()
                .into(imgFoto);

        imgFoto.setImageURI(imgUri);*/

    }





}
