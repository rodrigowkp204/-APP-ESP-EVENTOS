package com.example.esp_eventos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class AmbienteEvento extends BaseActivity {
    ImageView imageViewbackAmb;
    String[] ambientes = {"Interno","Externo"};
    AutoCompleteTextView auto_complete_txt_amb;
    ArrayAdapter<String> adapterambientes;
    Button bt_seguinte;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambiente_evento);

        getSupportActionBar().hide();
        IniciarComponentes();

        adapterambientes = new ArrayAdapter<>(this,R.layout.lista_item_local, ambientes);
        auto_complete_txt_amb.setAdapter(adapterambientes);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.azul1));

        imageViewbackAmb.setOnClickListener(v -> {

            Intent intentVoltarAmbienteEvento = new Intent(AmbienteEvento.this, PaginaPrincipal.class);
            startActivity(intentVoltarAmbienteEvento);
            finish();
        });

        bt_seguinte.setOnClickListener(v -> {
            AutoCompleteTextView autoCompleteTextView = findViewById(R.id.auto_complete_txt_amb);
            String selectOption = autoCompleteTextView.getText().toString();
            bt_seguinte.setEnabled(false);
            if (selectOption.equals("Interno")) {
                Intent bt_internoAmbienteEvento = new Intent(AmbienteEvento.this, FormCadastroEvento.class);
                startActivity(bt_internoAmbienteEvento);
                finish();
            } else if (selectOption.equals("Externo")) {
                Intent bt_externoAmbienteEvento = new Intent(AmbienteEvento.this, FormCadastroEventoExterno.class);
                startActivity(bt_externoAmbienteEvento);
                finish();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onBackPressed(){
        Toast.makeText(this, "Utilize o botão de voltar no topo da tela", Toast.LENGTH_SHORT).show();
    }
    private void IniciarComponentes(){
        imageViewbackAmb = findViewById(R.id.imageViewback_ext);
        auto_complete_txt_amb = findViewById(R.id.auto_complete_txt_amb);
        bt_seguinte = findViewById(R.id.bt_seguinte);
    }
}