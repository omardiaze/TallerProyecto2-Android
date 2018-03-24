package pe.edu.upc.municipalidadmovil.servicios.usuario;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import pe.edu.upc.municipalidadmovil.MainQuejasActivity;
import pe.edu.upc.municipalidadmovil.NuevoUsuarioActivity;
import pe.edu.upc.municipalidadmovil.modelo.Usuario;
import pe.edu.upc.municipalidadmovil.utils.Utilitario;

/**
 * Created by usuario on 16/02/2018.
 */

public class UsuarioInsertarService extends AsyncTask<Void,Void,String> {

    private Usuario usuario;
    private Context httpContext;
    ProgressDialog progressDialog;

    public UsuarioInsertarService(Usuario usuario, Context httpContext) {
        this.usuario = usuario;
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
        String jsonRequest = gson.toJson(usuario);
        byte[] postdata = jsonRequest.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postdata.length;

        try {
            String wsUrl = "http://34.208.240.207/municipalidadservice/usuarioService.svc/usuario";

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
