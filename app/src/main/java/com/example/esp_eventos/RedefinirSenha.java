package com.example.esp_eventos;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;

public class RedefinirSenha extends AppCompatActivity {

    ImageView imageViewbackRedefinir;
    private TextView edit_email_redefinir;
    private Button bt_redefinir;
    String[] mensagens = {"Algo deu errado","Sucesso"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redefinir_senha);

        getSupportActionBar().hide();
        IniciarComponentes();

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.azul1));

        bt_redefinir.setOnClickListener(v ->{
            String email = edit_email_redefinir.getText().toString();

            if (TextUtils.isEmpty(email)) {
                edit_email_redefinir.setError("Informe seu e-mail");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edit_email_redefinir.setError("E-mail inválido");
            }else {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                showsucessAlertDilog(mensagens[1],"E-mail de redefinição de senha foi enviado para o email: " + email);
                            } else {
                                showErrorAlertDilog(mensagens[0],"Erro ao enviar e-mail de redefinição de senha.");
                            }
                        });
            }
        });

        imageViewbackRedefinir.setOnClickListener(v->{
            Intent voltarparalogin1 = new Intent(RedefinirSenha.this, Activity_Splash_Form_Login.class);
            startActivity(voltarparalogin1);
            finish();
        });
    }
    private void showErrorAlertDilog(String titulo,String erro){

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        View view = LayoutInflater.from(this).inflate(
                R.layout.layout_erro_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitleAlertDialog)).setText(titulo);
        ((TextView) view.findViewById(R.id.textMessage)).setText(erro);
        ((Button) view.findViewById(R.id.buttonAction)).setText("Ok");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.error);

        final AlertDialog alertDialogredefinirerro = builder.create();

        view.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogredefinirerro.dismiss();
            }
        });

        if (alertDialogredefinirerro.getWindow() != null){
            alertDialogredefinirerro.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialogredefinirerro.show();
    }
    private void showsucessAlertDilog(String titulo,String erro){

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        View view = LayoutInflater.from(this).inflate(
                R.layout.layout_sucess_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitleAlertDialog)).setText(titulo);
        ((TextView) view.findViewById(R.id.textMessage)).setText(erro);
        ((Button) view.findViewById(R.id.buttonAction)).setText("Ok");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.done);

        final AlertDialog alertDialogredefinirsucess = builder.create();

        view.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogredefinirsucess.dismiss();
                Intent intentCE = new Intent(RedefinirSenha.this, Activity_Splash_Form_Login.class);
                intentCE.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentCE);
            }
        });

        if (alertDialogredefinirsucess.getWindow() != null){
            alertDialogredefinirsucess.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialogredefinirsucess.show();
    }
    private void IniciarComponentes(){
        //Recuperar os componentes do arquivo XML
        edit_email_redefinir = findViewById(R.id.edit_email_redefinir);
        bt_redefinir = findViewById(R.id.bt_redefinir);
        imageViewbackRedefinir = findViewById(R.id.imageViewbackRedefinir);
    }
}