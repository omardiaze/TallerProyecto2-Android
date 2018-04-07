package pe.edu.upc.municipalidadmovil.servicios.queja;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
  
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import pe.edu.upc.municipalidadmovil.FiltrarQuejaActivity;
import pe.edu.upc.municipalidadmovil.MainQuejasActivity;
import pe.edu.upc.municipalidadmovil.modelo.Queja;
import pe.edu.upc.municipalidadmovil.utils.Utilitario;

/**
 * Created by USUARIO on 17/02/2018.
 */

public class QuejaInsertarService  extends AsyncTask<Void,Void,String> {
    private Queja queja;
    private Context httpContext;
    ProgressDialog progressDialog;

    public QuejaInsertarService(Queja queja, Context httpContext) {
        this.queja = queja;
        this.httpContext = httpContext;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        progressDialog = ProgressDialog.show(httpContext, "Guardando","Un momento por favor");

    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = null;
        Gson gson = new Gson();
        String jsonRequest = gson.toJson(queja);
        byte[] postdata = jsonRequest.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postdata.length;

        try {
            String wsUrl = "http://34.208.77.240/municipalidadservice/QuejaService.svc/queja";

            URL url = new URL(wsUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            OutputStream os = conn.getOutputStream();
            os.write(jsonRequest.getBytes("UTF-8"));
            os.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            result = Utilitario.inputStreamToString(in);

        }catch (Exception e){
            Log.d("Log APP: ", "Error Service:" + e.getMessage());
        }
        return  result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();

        try{

            if (s.isEmpty()){
                Toast.makeText(httpContext, "Ocurrio un error, por favor contactar al administrador del sistema",Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                Intent iTour = new Intent(httpContext, MainQuejasActivity.class);
                httpContext.startActivity(iTour);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
