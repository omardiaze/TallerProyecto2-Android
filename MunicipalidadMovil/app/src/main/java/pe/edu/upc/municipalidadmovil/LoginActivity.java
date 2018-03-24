package pe.edu.upc.municipalidadmovil;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pe.edu.upc.municipalidadmovil.modelo.Usuario;
import pe.edu.upc.municipalidadmovil.servicios.usuario.UsuarioInsertarService;

public class LoginActivity extends AppCompatActivity {

    EditText txtCorreo;
    EditText txtPassowrd;
    Button btnIngresar;
    Button btnRegistrar;

    private static  String TAG = "Log APP:";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtCorreo = (EditText) findViewById(R.id.txtCorreoLogin);
        txtPassowrd = (EditText) findViewById(R.id.txtPasswordLogin);
        btnIngresar = (Button) findViewById(R.id.btnIngresarLogin);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrarLogin);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent iNuevoUsuario = new Intent(LoginActivity.this, NuevoUsuarioActivity.class) ;
              startActivity(iNuevoUsuario);

            }
        });

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidarUsuario();
            }
        });
    }


    private void ValidarUsuario() {
        if (txtCorreo.getText().toString().isEmpty()){
            Toast.makeText(this,"Ingrese correo",Toast.LENGTH_SHORT).show();
            txtCorreo.requestFocus();
            return;
        }
        if (txtPassowrd.getText().toString().isEmpty()){
            Toast.makeText(this,"Ingrese password",Toast.LENGTH_SHORT).show();
            txtPassowrd.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(txtCorreo.getText().toString(), txtPassowrd.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, "Autentificación fallida",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        FirebaseUser user = mAuth.getCurrentUser();
                        SiguienteUI(user);
                    }
                });

    }

    private void SiguienteUI(FirebaseUser user){
        if(user != null){
            Intent iconIntent = new Intent(this, MainQuejasActivity.class);
            this.startActivity(iconIntent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        SiguienteUI(currentUser);
    }
}
