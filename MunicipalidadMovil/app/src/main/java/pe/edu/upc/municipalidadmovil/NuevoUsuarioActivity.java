package pe.edu.upc.municipalidadmovil;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import pe.edu.upc.municipalidadmovil.modelo.Usuario;
import pe.edu.upc.municipalidadmovil.servicios.usuario.UsuarioInsertarService;

public class NuevoUsuarioActivity extends AppCompatActivity {

    EditText txtNombre ;
    EditText txtApePat ;
    EditText txtApeMat ;
    EditText txtDni ;
    EditText txtFecNac ;
    EditText txtCorreo ;
    EditText txtPassword1;
    ImageView imgFotoUsuarioN;
    Button btnGuardar;
    Button btnTomarFotoUsuarioN;
    Button btnGaleriaUsuarioN;
    Bitmap imageBitmap;
  
    Spinner spnSexo;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;

    String[] sexolist= {"Másculino","Femenino"};
    String imageFileName;
    private static  String TAG = "Log APP:";
    ProgressDialog progressDialog;

    private static final String DATA_DIRECTORY = Environment.getExternalStorageDirectory().getPath();
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int GALLERY_INTENT = 2;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_usuario);

        txtNombre = (EditText) findViewById(R.id.txtNombreN);
        txtApePat = (EditText) findViewById(R.id.txtApePatN);
        txtApeMat = (EditText) findViewById(R.id.txtApeMatN);
        txtDni = (EditText) findViewById(R.id.txtDniN);
        txtFecNac = (EditText) findViewById(R.id.txtFecNacN);
        txtCorreo = (EditText) findViewById(R.id.txtCorreoN);
        txtPassword1 = (EditText) findViewById(R.id.txtPassword1N);
        imgFotoUsuarioN = (ImageView) findViewById(R.id.imgFotoUsuarioN);
        btnGuardar = (Button) findViewById(R.id.btnGuardarN);
        btnGaleriaUsuarioN = (Button) findViewById(R.id.btnGaleriaUsuarioN);
        btnTomarFotoUsuarioN = (Button) findViewById(R.id.btnTomarFotoUsuarioN);
        spnSexo = (Spinner) findViewById(R.id.spnSexo);
        spnSexo.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sexolist));

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(this);

        btnTomarFotoUsuarioN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    TomarFoto();
            }
        });

        btnGaleriaUsuarioN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeleccionarFoto();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GuardarCuenta();
            }
        });
    }

    private void TomarFoto() {
        int permission1 = ActivityCompat.checkSelfPermission(NuevoUsuarioActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(NuevoUsuarioActivity.this, android.Manifest.permission.CAMERA);
        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    NuevoUsuarioActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        if(permission2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    NuevoUsuarioActivity.this, new String[]{android.Manifest.permission.CAMERA},
                    CAMERA_REQUEST_CODE);
        }

        if (permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
            imageFileName = "PIC_" + timeStamp + ".jpg";

            if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                Uri photoURI =  FileProvider.getUriForFile(NuevoUsuarioActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile());

                takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoURI);
            }else{
                Uri uri = Uri.parse("file:///sdcard/" + imageFileName);
                takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
            }

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
        String vFileName = "PIC_" + timeStamp + ".jpg";
        File file = new File(DATA_DIRECTORY, vFileName);
        imageFileName = file.getAbsolutePath();
        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            String pPath = "";
            if(!imageFileName.equals("")) {
                if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    pPath = imageFileName;
                }else {
                    File file = new File(DATA_DIRECTORY, imageFileName);
                    pPath = file.getAbsolutePath();
                }
            }else{
                Uri selectedImageUri = data.getData();
                String uriImg = getRealPathFromURI(selectedImageUri);
                pPath = uriImg;
            }

            try {
                imageBitmap = BitmapFactory.decodeFile(pPath);
                imgFotoUsuarioN.setImageBitmap(imageBitmap);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else  if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            Uri uri = data.getData();
            String uriImg = getRealPathFromURI(uri);
            try {
                imageBitmap = BitmapFactory.decodeFile(uriImg);
                imgFotoUsuarioN.setImageURI(uri);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = getApplicationContext().getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void SeleccionarFoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_INTENT);
    }

    private void GuardarCuenta() {
        String nom = txtNombre.getText().toString();
        String apepat = txtApePat.getText().toString();
        String apemat = txtApeMat.getText().toString();
        String dni = txtDni.getText().toString();
        String fecnac = txtFecNac.getText().toString();
        String correo = txtCorreo.getText().toString();
        String pass1 = txtPassword1.getText().toString();
        String sexo="";
        String img = "";

        if (imageBitmap == null){
            Toast.makeText(this,"Ingrese una foto", Toast.LENGTH_SHORT).show();
            return;
        }

        if (spnSexo.getSelectedItem().toString() == "Femenino"){
            sexo = "F";
        }
        else {
            sexo = "M";
        }

        if (nom.isEmpty()){
            Toast.makeText(this,"Ingrese su nombre", Toast.LENGTH_SHORT).show();
            return;
        }

        if (apepat.isEmpty()){
            Toast.makeText(this, "Ingrese su apellido paterno",Toast.LENGTH_SHORT).show();
            return;
        }

        if (apemat.isEmpty()){
            Toast.makeText(this, "Ingrese su apellido materno",Toast.LENGTH_SHORT).show();
            return;
        }

        if (dni.isEmpty()){
            Toast.makeText(this, "Ingrese su dni",Toast.LENGTH_SHORT).show();
            return;
        }

        if (fecnac.isEmpty()){
            Toast.makeText(this, "Ingrese su fecha de nacimiento",Toast.LENGTH_SHORT).show();
            return;
        }

        if (correo.isEmpty()){
            Toast.makeText(this,"Ingrese su correo", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pass1.isEmpty()){
            Toast.makeText(this, "Ingrese password",Toast.LENGTH_SHORT).show();
            return;
        }

        final Usuario usuario = new Usuario(0,nom,apepat,apemat,dni,"","","",fecnac,sexo, correo,img);

        progressDialog.setTitle("Grabando..");
        progressDialog.setMessage("Creando usuario FireBase");
        progressDialog.setCancelable(false);
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(correo, pass1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser fb = mAuth.getCurrentUser();
                            if (fb != null ){
                                SubirFoto(fb, usuario);
                            }
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(NuevoUsuarioActivity.this,"Autentificación fallida",
                                    Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.getException().toString());
                        }
                    }
                });
    }

    private void SubirFoto(final FirebaseUser fr, final Usuario usu){

        progressDialog.setTitle("Grabando..");
        progressDialog.setMessage("Subiendo imagen a Firebase");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String namefile = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
        namefile = "PIC_" + namefile + ".jpg";

        StorageReference mountainsRef = mStorage.child(namefile);
        StorageReference mountainImagesRef = mStorage.child(("images/"+ namefile));

        imgFotoUsuarioN.setDrawingCacheEnabled(true);
        imgFotoUsuarioN.buildDrawingCache();
        Bitmap bitmap = imgFotoUsuarioN.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                progressDialog.dismiss();
                Toast.makeText(NuevoUsuarioActivity.this,"Subida de imagen fallida",
                        Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                usu.setImagen(downloadUrl.toString());

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName((usu.getNombre() + " " + usu.getApePat()))
                        .setPhotoUri(downloadUrl)
                        .build();

                fr.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User profile updated.");
                                    GuardarUsuarioBd(usu);
                                }
                            }
                        });
            }
        });

    }

    private void GuardarUsuarioBd(Usuario usu){
        UsuarioInsertarService service = new UsuarioInsertarService(usu,NuevoUsuarioActivity.this);
        service.execute();
    }

}
