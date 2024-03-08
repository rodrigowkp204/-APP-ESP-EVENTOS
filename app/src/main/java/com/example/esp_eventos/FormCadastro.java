package com.example.esp_eventos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FormCadastro extends AppCompatActivity {

    ImageView imageViewbackFormCadastro;
    private TextView txt_admin;
    private Button bt_cadastrar;
    String[] mensagens = {"Preencha todos os campos", "Cadastro realizado com sucesso"};
    String usuarioID;
    private boolean isPasswordVisible = false;
    TextInputEditText passwordEditTextNovoUsuario, emailEditTextNovoUsuario, coordEditTextNovoUsuario, nomeEditTextNovoUsuario;
    CheckBox adminToggleCheckBoxFormCadastro;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);

        getSupportActionBar().hide();
        IniciarComponentes();

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.azul1));

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        final CheckBox passwordToggleCheckBoxFormCadastro = findViewById(R.id.passwordToggleCheckBoxFormCadastro);

        passwordToggleCheckBoxFormCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPasswordVisible = !isPasswordVisible;
                passwordEditTextNovoUsuario.setTransformationMethod(isPasswordVisible ? null : new PasswordTransformationMethod());
                passwordEditTextNovoUsuario.setSelection(passwordEditTextNovoUsuario.getText().length());
            }
        });

        bt_cadastrar.setOnClickListener(v -> {

            String nome = Objects.requireNonNull(nomeEditTextNovoUsuario.getText()).toString();
            String email = String.valueOf(emailEditTextNovoUsuario.getText());
            String senha = String.valueOf(passwordEditTextNovoUsuario.getText());
            String cord = Objects.requireNonNull(coordEditTextNovoUsuario.getText()).toString();

            bt_cadastrar.setEnabled(false);

            if(nome.isEmpty()){

                nomeEditTextNovoUsuario.setError("O campo não pode estar vazio");
                bt_cadastrar.setEnabled(true);
            }else if(cord.isEmpty()){

                coordEditTextNovoUsuario.setError("O campo não pode estar vazio");
                bt_cadastrar.setEnabled(true);
            }else if(email.isEmpty()){

                emailEditTextNovoUsuario.setError("O campo não pode estar vazio");
                bt_cadastrar.setEnabled(true);
            }else if(senha.isEmpty()){

                passwordEditTextNovoUsuario.setError("O campo não pode estar vazio");
                bt_cadastrar.setEnabled(true);
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                emailEditTextNovoUsuario.setError("E-mail inválido");
                bt_cadastrar.setEnabled(true);
            }
            else{

                CadastrarUsuario(v);
                bt_cadastrar.setEnabled(true);
            }
        });
        imageViewbackFormCadastro.setOnClickListener(v -> {

            FirebaseAuth.getInstance().signOut();
            Intent intentVoltarCadastroUsuario = new Intent(FormCadastro.this, Activity_Splash_Form_Login.class);
            startActivity(intentVoltarCadastroUsuario);
            finish();
        });

            mAuth = FirebaseAuth.getInstance();
            mFirestore = FirebaseFirestore.getInstance();

            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                // Verificar se o usuário atual é um administrador
                String userId = currentUser.getUid();
                mFirestore.collection("users").document(userId).get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentTypeUser = task.getResult();
                                if (documentTypeUser.exists()) {
                                    String userType = documentTypeUser.getString("Tipo");
                                    if ("admin".equals(userType)) {
                                        // Exibir o CheckBox apenas para administradores
                                        adminToggleCheckBoxFormCadastro.setVisibility(View.VISIBLE);
                                        txt_admin.setVisibility(View.VISIBLE);
                                    }
                                }else {
                                    return;
                                }
                            }
                        });
            }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
    private void CadastrarUsuario(View v) {
        String email = emailEditTextNovoUsuario.getText().toString();
        String senha = passwordEditTextNovoUsuario.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                SalvarDadosUsuario(v);
            } else {
                String erro;
                try {
                    throw Objects.requireNonNull(task.getException());
                } catch (FirebaseAuthWeakPasswordException e) {
                    erro = "Digite uma senha com no mínimo 6 caracteres";
                } catch (FirebaseAuthUserCollisionException e) {
                    erro = "Esta conta já foi cadastrada";
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    erro = "E-mail inválido";
                } catch (Exception e) {
                    erro = "Erro ao cadastrar usuário";
                }
                Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }
        });
    }
    private void SalvarDadosUsuario(View s) {
        String nome = nomeEditTextNovoUsuario.getText().toString();
        String coord = coordEditTextNovoUsuario.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("Nome", nome);
        usuarios.put("Coordenação", coord);
        usuarios.put("Tipo", "normal");

        usuarioID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DocumentReference documentReference = db.collection("Info_Usuarios").document(usuarioID);
        documentReference.set(usuarios).addOnSuccessListener(aVoid -> {
            Log.d("db", "Sucesso ao salvar os dados");

            Snackbar snackbar = Snackbar.make(s, mensagens[1], Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();

            Intent intentFCUsuario = new Intent(FormCadastro.this, Activity_Splash_Form_Login.class);
            startActivity(intentFCUsuario);
            finish();

        }).addOnFailureListener(e -> Log.d("db_error", "Erro ao salvar os dados" + e));
    }
    private void IniciarComponentes(){

        //Recuperar os componentes do arquivo XML
        coordEditTextNovoUsuario = findViewById(R.id.edit_coord);
        nomeEditTextNovoUsuario = findViewById(R.id.edit_nome);
        bt_cadastrar = findViewById(R.id.bt_cadastrar);
        imageViewbackFormCadastro = findViewById(R.id.imageViewbackFormCadastro);
        passwordEditTextNovoUsuario = findViewById(R.id.edit_senha_form_cadastro);
        emailEditTextNovoUsuario = findViewById(R.id.edit_email_form_cadastro);
        adminToggleCheckBoxFormCadastro = findViewById(R.id.adminToggleCheckBoxFormCadastro);
        txt_admin = findViewById(R.id.txt_admin);

    }
}