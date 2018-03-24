package pe.edu.upc.municipalidadmovil.servicios.queja;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import pe.edu.upc.municipalidadmovil.adaptador.QuejaAdapter;
import pe.edu.upc.municipalidadmovil.modelo.Queja;
import pe.edu.upc.municipalidadmovil.utils.Utilitario;

/**
 * Created by USUARIO on 17/02/2018.
 */

public class QuejaConsultarService extends AsyncTask<Void,Void,String> {

    private List<Queja> httpList;
    private RecyclerView httpRecycler;
    private RecyclerView.Adapter httpAdapter;
    private Context httpContext;
    private String correo;
    private String estado;

    ProgressDialog progressDialog;

    public QuejaConsultarService(List<Queja> httpList, RecyclerView httpRecycler, RecyclerView.Adapter httpAdapter, Context httpContext, String correo, String estado) {
        this.httpList = httpList;
        this.httpRecycler = httpRecycler;
        this.httpAdapter = httpAdapter;
        this.httpContext = httpContext;
        this.correo = correo;
        this.estado = estado;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        progressDialog = ProgressDialog.show(httpContext, "downloading","please wait");

    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = null;

        try {
            String wsUrl = "http://34.208.77.240/municipalidadservice/QuejaService.svc/queja/" + correo + "/"+ estado;
            URL url = new URL(wsUrl);

            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            result = Utilitario.inputStreamToString(in);

        }catch (Exception e){
            e.printStackTrace();
        }
        return  result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();

        try{
            JSONArray jsonArray = new JSONArray(s);
            for ( int i = 0; i<jsonArray.length(); i++ ){
                JSONObject jsonobject = jsonArray.getJSONObject(i);

                String icorreo = jsonobject.getString("Correo");
                String idescripcion = jsonobject.getString("Descripcion");
                String iestado = jsonobject.getString("Estado");
                String iFecha = jsonobject.getString("Fecha");
                String iImagen = jsonobject.getString("Imagen");
                String iDireccion = jsonobject.getString("Direccion");
                String iLatitud = jsonobject.getString("Latitud");
                String iLongitud = jsonobject.getString("Longitud");
                int iId =  Integer.parseInt(jsonobject.getString("Id"));
                String iTipo = jsonobject.getString("Tipo");

                this.httpList.add(new Queja(iId,iTipo,idescripcion,iImagen,icorreo,iFecha,iestado,iDireccion,iLatitud,iLongitud));
            }
            httpAdapter = new QuejaAdapter(this.httpList);
            httpRecycler.setAdapter(this.httpAdapter);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
