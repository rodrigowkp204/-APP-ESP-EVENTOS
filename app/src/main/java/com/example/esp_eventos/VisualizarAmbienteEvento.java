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

public class VisualizarAmbienteEvento extends BaseActivity {

    ImageView imageViewbackAmbVisu;
    String[] ambientesVisu = {"Interno","Externo"};
    AutoCompleteTextView auto_complete_txt_amb_visu;
    ArrayAdapter<String> adapterambientesVisu;
    Button bt_seguinte_visu;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_ambiente_evento);

        getSupportActionBar().hide();
        IniciarComponentes();

        adapterambientesVisu = new ArrayAdapter<>(this,R.layout.lista_item_local,ambientesVisu);

        auto_complete_txt_amb_visu.setAdapter(adapterambientesVisu);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.azul1));

        imageViewbackAmbVisu.setOnClickListener(v -> {

            Intent intentVoltarVisualizarAmbienteEvento = new Intent(VisualizarAmbienteEvento.this, PaginaPrincipal.class);
            startActivity(intentVoltarVisualizarAmbienteEvento);
            finish();

        });
        bt_seguinte_visu.setOnClickListener(v -> {
            @SuppressLint("CutPasteId") AutoCompleteTextView autoCompleteTextView = findViewById(R.id.auto_complete_txt_amb_visu);
            String selectOption1 = autoCompleteTextView.getText().toString();
            if(selectOption1.equals("Interno")){
                Intent bt_seguinte_visuInterno = new Intent(VisualizarAmbienteEvento.this, FormVisualizacao.class);
                startActivity(bt_seguinte_visuInterno);
                finish();
            } else if (selectOption1.equals("Externo")){
                Intent bt_seguinte_visuExterno = new Intent(VisualizarAmbienteEvento.this, FormVisualizacaoExterno.class);
                startActivity(bt_seguinte_visuExterno);
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
        imageViewbackAmbVisu = findViewById(R.id.imageViewback_Visu_ext);
        auto_complete_txt_amb_visu = findViewById(R.id.auto_complete_txt_amb_visu);
        bt_seguinte_visu = findViewById(R.id.bt_seguinte_visu);
    }

}