package com.example.esp_eventos;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Activity_Splash_Form_Login extends AppCompatActivity {

    private TextView text_tela_cadastro,text_redefinir_senha;
    private Button bt_entrar;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    String[] mensagens = {"Preencha todos os campos", "Login efetuado com sucesso"};
    private static final int RC_SIGN_IN = 9001;
    TextInputLayout passwordTextInputLayout;
    TextInputEditText passwordEditText,emailEditText;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_form_login);

        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        IniciarComponentes();

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.azul1));

        text_tela_cadastro.setOnClickListener(v -> {

            Intent intentCadastrarUsuario = new Intent(Activity_Splash_Form_Login.this, FormCadastro.class);
            startActivity(intentCadastrarUsuario);
            finish();
        });


        final CheckBox passwordToggleCheckBox = findViewById(R.id.passwordToggleCheckBox);

        passwordToggleCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPasswordVisible = !isPasswordVisible;
                passwordEditText.setTransformationMethod(isPasswordVisible ? null : new PasswordTransformationMethod());
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        });

        bt_entrar.setOnClickListener(v -> {

            String email = emailEditText.getText().toString();
            String senha = passwordEditText.getText().toString();

            if(email.isEmpty() || senha.isEmpty()){
                Snackbar snackbar = Snackbar.make(v,mensagens[0],Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }else {
                AutenticarUsuario(v);
            }
        });
        text_redefinir_senha.setOnClickListener(v ->{

            Intent redefinir_senha = new Intent(Activity_Splash_Form_Login.this,RedefinirSenha.class);
            startActivity(redefinir_senha);
            finish();

        });
    }
    private void signInWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("298491850974-d0b3qvp92hf2lcf4b1v9rki9f9qnjh17.apps.googleusercontent.com")
                .build();
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("DEBUG", "onActivityResult - requestCode: " + requestCode);
        Log.d("DEBUG", "onActivityResult - resultCode: " + resultCode);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    Log.d("DEBUG", "GoogleSignInAccount: " + account.toString());

                    // Autenticar no Firebase com a conta do Google
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    mAuth.signInWithCredential(credential)
                            .addOnCompleteListener(this, task1 -> {
                                if (task1.isSuccessful()) {
                                    // Sucesso no login com o Google
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Log.d("DEBUG", "FirebaseUser: " + user.toString());
                                    progressBar.setVisibility(View.VISIBLE);
                                    bt_entrar.setEnabled(false);

                                    // Aqui você pode redirecionar para a tela principal após o login
                                    new Handler().postDelayed(this::TelaPrincipal, 1000);
                                } else {
                                    // Falha no login com o Google
                                    Log.d("DEBUG", "Falha no login com o Google: " + task1.getException());
                                    Toast.makeText(this, "Erro ao fazer login com o Google", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            } catch (ApiException e) {
                // Manipule erros aqui
                Log.d("DEBUG", "Erro na ApiException: " + e.toString());
                Toast.makeText(this, "Erro ao fazer login com o Google", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void AutenticarUsuario(View view){

        String email = emailEditText.getText().toString();
        String senha = passwordEditText.getText().toString();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email,senha).addOnCompleteListener(this, task -> {

            if(task.isSuccessful()){
                progressBar.setVisibility(View.VISIBLE);
                bt_entrar.setVisibility(View.INVISIBLE);

                new Handler().postDelayed(Activity_Splash_Form_Login.this::TelaPrincipal, 1000);
            }else {

                if (TextUtils.isEmpty(email) || TextUtils.isDigitsOnly(senha)) {
                    emailEditText.setError("Informe o seu e-mail");
                    passwordEditText.setError("Senha inválida");
                    text_redefinir_senha.setVisibility(View.VISIBLE);

                    return;
                }
                Toast.makeText(Activity_Splash_Form_Login.this, "Erro ao autenticar o usuário",
                        Toast.LENGTH_SHORT).show();
                text_redefinir_senha.setVisibility(View.VISIBLE);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if(usuarioAtual != null){
            TelaPrincipal();
        }
    }
    private void TelaPrincipal(){
        Intent intenttp = new Intent(Activity_Splash_Form_Login.this,PaginaPrincipal.class);
        startActivity(intenttp);
        finish();
    }
    private void IniciarComponentes(){

        //Recuperar os componentes do arquivo XML
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);
        emailEditText = findViewById(R.id.edit_email);
        text_redefinir_senha = findViewById(R.id.text_redefinir_senha);
        passwordEditText = findViewById(R.id.edit_senha);
        passwordTextInputLayout= findViewById(R.id.passwordTextInputLayoutLogin);
        bt_entrar = findViewById(R.id.bt_entrar);
        progressBar = findViewById(R.id.progressbar);
    }
}