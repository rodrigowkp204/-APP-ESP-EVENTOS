package com.example.esp_eventos;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            signInWithGoogle(); // Se não estiver autenticado, inicie o processo de login via Google
        }

        if (currentUser == null) {
            // Se o usuário não estiver autenticado, redirecione-o para a tela de login
            Intent intentemail = new Intent(this, Activity_Splash_Form_Login.class);
            startActivity(intentemail);
            finish();
        }
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // Verifique o resultado do login via Google aqui
            if (resultCode == RESULT_OK) {
                // O login foi bem-sucedido, continue com a lógica do aplicativo
                Intent intentgoogle = new Intent(this, Activity_Splash_Form_Login.class);
                startActivity(intentgoogle);
                finish();

                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    // O usuário está autenticado, continue com o fluxo do aplicativo
                }
            } else {
                // O login via Google não foi bem-sucedido, você pode tratar o erro aqui
            }
        }
    }
}
