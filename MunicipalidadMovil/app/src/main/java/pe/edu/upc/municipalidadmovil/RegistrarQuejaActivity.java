package pe.edu.upc.municipalidadmovil;

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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import pe.edu.upc.municipalidadmovil.modelo.Queja;
import pe.edu.upc.municipalidadmovil.servicios.queja.QuejaInsertarService;

public class RegistrarQuejaActivity extends AppCompatActivity {

    Button btnGuardar;
    Button btnCargarFoto;
    Button btnTomarFoto;
    Spinner spnTipoQueja;

    EditText txtDescripcion;
    ImageView imgFoto;
    Bitmap imageBitmap;

    String[] tipolist= {"Pago servicios","Impuestos","Delincuencia","Limpieza"};
    private static  String TAG = "Log APP:";
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    String imageFileName;
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
        setContentView(R.layout.activity_registrar_queja);

        txtDescripcion = (EditText) findViewById(R.id.txtDescQuejaN);
        imgFoto = (ImageView)  findViewById(R.id.imgFotoQuejaN);
        btnGuardar = (Button) findViewById(R.id.btnGuardarQuejaN);
        btnCargarFoto= (Button) findViewById(R.id.btnCargarFotoQuejaN);
        btnTomarFoto= (Button) findViewById(R.id.btnTomarFotoQuejaN);
        spnTipoQueja = (Spinner) findViewById(R.id.spnTipoQuejaN);
        spnTipoQueja.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipolist));

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(this);

        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TomarFoto();
            }
        });

        btnCargarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeleccionarFoto();
            }
        });
        
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GuardarQueja();
            }
        });
    }

    private void TomarFoto() {
        int permission1 = ActivityCompat.checkSelfPermission(RegistrarQuejaActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(RegistrarQuejaActivity.this, android.Manifest.permission.CAMERA);
        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    RegistrarQuejaActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        if(permission2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    RegistrarQuejaActivity.this, new String[]{android.Manifest.permission.CAMERA},
                    CAMERA_REQUEST_CODE);
        }

        if (permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
            imageFileName = "PIC_" + timeStamp + ".jpg";

            if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                Uri photoURI =  FileProvider.getUriForFile(RegistrarQuejaActivity.this,
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
                imgFoto.setImageBitmap(imageBitmap);
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
                imgFoto.setImageURI(uri);

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

    private void GuardarQueja() {

        String desc = txtDescripcion.getText().toString();
        String tipoq;
        String img = "";

        if (imageBitmap == null){
            Toast.makeText(this,"Ingrese una foto", Toast.LENGTH_SHORT).show();
            return;
        }

        if (desc.isEmpty()){
            Toast.makeText(this,"Ingrese su descripción", Toast.LENGTH_SHORT).show();
            return;
        }

        if (spnTipoQueja.getSelectedItem().toString() == "Pago servicios"){
            tipoq = "PAGOS";
        }
        else if (spnTipoQueja.getSelectedItem().toString() == "Impuestos"){
            tipoq = "IMPTO";
        }
        else if (spnTipoQueja.getSelectedItem().toString() == "Delincuencia"){
            tipoq = "DELIN";
        }
        else
        {
            tipoq = "LIMPI";
        }

        FirebaseUser user = mAuth.getCurrentUser();

        Queja item = new Queja(0, tipoq, desc, img,user.getEmail(), "", "P","","","");
        SubirFoto(user,item);
    }

    private void SubirFoto(final FirebaseUser fr, final Queja queja){

        progressDialog.setTitle("Grabando..");
        progressDialog.setMessage("Subiendo imagen a Firebase");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String namefile = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
        namefile = "PIC_" + namefile + ".jpg";

        StorageReference mountainsRef = mStorage.child(namefile);
        StorageReference mountainImagesRef = mStorage.child(("images/"+ namefile));

        imgFoto.setDrawingCacheEnabled(true);
        imgFoto.buildDrawingCache();
        Bitmap bitmap = imgFoto.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                progressDialog.dismiss();
                Toast.makeText(RegistrarQuejaActivity.this,"Subida de imagen fallida",
                        Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                queja.setImagen(downloadUrl.toString());
                progressDialog.dismiss();
                GuardarQuejaBd(queja);

            }
        });

    }

    private void GuardarQuejaBd(Queja queja ){
        QuejaInsertarService service = new QuejaInsertarService(queja,RegistrarQuejaActivity.this);
        service.execute();
    }
}
