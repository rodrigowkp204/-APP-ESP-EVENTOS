package com.example.login;

import android.content.Intent;
import android.os.Bundle;

import com.example.esp_eventos.R;

import java.util.Objects;

public class PhotoDetailsActivity extends BaseActivity2 {
    private int photoId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);

        // Obtenha o photoId da intenção (intent)
        Intent intent = getIntent();
        if (intent != null) {
            photoId = intent.getIntExtra("photoId", 0);
        }
        if (photoId == 1){
            Intent intent2 = new Intent(PhotoDetailsActivity.this, FormCadastroEvento.class);
            Objects.requireNonNull(intent2).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent2);
            finish();
        } else if (photoId == 2) {
            Intent intent3 = new Intent(PhotoDetailsActivity.this, FormVisualizacao.class);
            Objects.requireNonNull(intent3).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent3);
            finish();
        }
        // Faça o que for necessário para exibir os detalhes da foto
        // Você pode usar o photoId para carregar os detalhes da foto do banco de dados ou de outras fontes de dados
    }
}