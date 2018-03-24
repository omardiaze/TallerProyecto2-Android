package pe.edu.upc.municipalidadmovil.adaptador;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import pe.edu.upc.municipalidadmovil.QuejaVerActivity;
import pe.edu.upc.municipalidadmovil.R;
import pe.edu.upc.municipalidadmovil.modelo.Queja;

/**
 * Created by USUARIO on 17/02/2018.
 */

public class QuejaAdapter extends RecyclerView.Adapter<QuejaAdapter.QuejaViewHolder>  {
    public QuejaAdapter(List<Queja> items) {
        this.items = items;
    }

    private List<Queja> items;

    public List<Queja> getItems() {
        return items;
    }

    public void setItems(List<Queja> items) {
        this.items = items;
    }

    public static class QuejaViewHolder extends RecyclerView.ViewHolder {
        public CardView cvQueja;
        public ImageView imgFotoC;
        public TextView lblTipoC;
        public TextView lblEstadoC;
        public TextView lblFechaC;

        public QuejaViewHolder(View v) {
            super(v);
            cvQueja = (CardView) v.findViewById(R.id.cvQueja);
            imgFotoC = (ImageView) v.findViewById(R.id.imgFotoC);
            lblTipoC = (TextView) v.findViewById(R.id.lblTipoC);
            lblEstadoC = (TextView) v.findViewById(R.id.lblEstadoC);
            lblFechaC = (TextView) v.findViewById(R.id.lblFechaC);
        }
    }



    @Override
    public QuejaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.queja_card, parent, false);
        return new QuejaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(QuejaViewHolder holder, final int i) {

        //Uri imgUri = Uri.parse(items.get(i).getImagen());
/*        Glide.with(holder.imgFotoC.getContext())
                .load(Uri.parse(items.get(i).getImagen()))
                .fitCenter()
                .centerCrop()
                .into(holder.imgFotoC);

        holder.imgFotoC.setImageURI(Uri.parse(items.get(i).getImagen()));
*/
        Picasso.with(holder.imgFotoC.getContext()).
                load(items.get(i).getImagen()).into(holder.imgFotoC);


        holder.lblTipoC.setText("Tipo: " +items.get(i).getTipo());
        holder.lblEstadoC.setText(items.get(i).getEstado());
        holder.lblFechaC.setText("Fecha registro: " + items.get(i).getFecreg());

        holder.cvQueja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("pIdQueja", String.valueOf(items.get(i).getId()));
                bundle.putString("pCorreo", String.valueOf(items.get(i).getCorreo()));
                bundle.putString("pDescripcion", String.valueOf(items.get(i).getDescripcion()));
                bundle.putString("pEstado", String.valueOf(items.get(i).getEstado()));
                bundle.putString("pFechaReg", String.valueOf(items.get(i).getFecreg()));
                bundle.putString("pImagen", String.valueOf(items.get(i).getImagen()));
                bundle.putString("pTipo", String.valueOf(items.get(i).getTipo()));

                Intent iconIntent = new Intent(view.getContext(), QuejaVerActivity.class);
                iconIntent.putExtras(bundle);
                view.getContext().startActivity(iconIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getItems().size();
    }


}
