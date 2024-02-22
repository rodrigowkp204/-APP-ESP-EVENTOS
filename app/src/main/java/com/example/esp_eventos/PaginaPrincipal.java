package com.example.esp_eventos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.esp_eventos.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PaginaPrincipal extends BaseActivity {
    ImageView imageViewoff, imageViewConfigAdmin;
    TextView textnomeUsuario;
    String usuarioID;
    private static final String CHANNEL_ID = "channel_id";
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 123;
    private static final int REQUEST_CADASTRO_ADMIN = 1;
    private RecyclerView recyclerView;
    private List<Photos> photoList = new ArrayList<>();

    @SuppressLint({"MissingInflatedId", "ObsoleteSdkInt"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);

        // Verifique as permissões antes de tentar abrir o arquivo
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED) {
            openFile();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        getSupportActionBar().hide();

        FirebaseApp.initializeApp(this);
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .build();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);

        IniciarComponentes();

        imageViewoff.setOnClickListener(v -> {

            FirebaseAuth.getInstance().signOut();
            Intent intentDeslogarPaginaPrincipal = new Intent(PaginaPrincipal.this, Activity_Splash_Form_Login.class);
            startActivity(intentDeslogarPaginaPrincipal);
            finish();
        });

        imageViewConfigAdmin.setOnClickListener(vadmin ->{
            //showSortDialog("Opções de administrador");
            Intent intenttestecalendario = new Intent(PaginaPrincipal.this, CalendarViewMonthly.class);
            startActivity(intenttestecalendario);
            finish();
        });

        photoList = getPhotoList();
        PhotoAdapter photoAdapter = new PhotoAdapter(photoList);

        //configurar o recyclerview
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.setHasFixedSize(true); //lista dinamica tera um tamanho fixo
        recyclerView.setAdapter(photoAdapter);

        // IMPLEMENTAR EM TODAS AS PÁGINAS
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.azul1));
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Verificar se o usuário atual é um administrador
            String userId = currentUser.getUid();
            db.collection("Info_Usuarios").document(userId).get()
                    .addOnCompleteListener(task45 -> {
                        if (task45.isSuccessful()) {
                            DocumentSnapshot document = task45.getResult();
                            if (document.exists()) {
                                String userType = document.getString("Tipo");
                                if ("admin".equals(userType)) {
                                    // Habilitar a engrenagem para os admins
                                    imageViewConfigAdmin.setVisibility(View.VISIBLE);

                                } else{
                                    // desabilitar a engrenagem para os usuarios
                                    imageViewConfigAdmin.setVisibility(View.GONE);
                                }
                            }
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        usuarioID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Info_Usuarios").document(usuarioID);
        documentReference.addSnapshotListener((documentSnapshot, error) -> {
            if(documentSnapshot != null){
                textnomeUsuario.setText(Objects.requireNonNull(documentSnapshot.getString("Nome")).toUpperCase());
            }
        });
    }
    public void onBackPressed(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        View view = LayoutInflater.from(this).inflate(
                R.layout.layout_alerta_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitleAlertDialog)).setText("Aviso");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Você deseja realmente sair do aplicativo ?");
        ((Button) view.findViewById(R.id.buttonYes)).setText("Sim");
        ((Button) view.findViewById(R.id.buttonNo)).setText("Não");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.aviso);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });

        view.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

            }
        });

        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
    public void showSortDialog(String titulo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        View view = LayoutInflater.from(this).inflate(
                R.layout.layout_config_admin,(ConstraintLayout)findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        CheckBox relatorioeventosinternos = view.findViewById(R.id.relatorioeventosinternos);
        CheckBox relatorioeventosexternos = view.findViewById(R.id.relatorioeventosexternos);
        ((TextView) view.findViewById(R.id.textTitleAlertDialogAdmin)).setText(titulo);
        ((Button) view.findViewById(R.id.buttonActionAdmin)).setText("Ok");

        //Desabilitar os outros checkboxs e deixando apenas um habilitado.
        relatorioeventosinternos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    relatorioeventosexternos.setChecked(false);

                }
            }
        });
        relatorioeventosexternos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    relatorioeventosinternos.setChecked(false);

                }
            }
        });

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.buttonActionAdmin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

                if(relatorioeventosinternos.isChecked()){
                    generateAndDownloadReport();
                }else if(relatorioeventosexternos.isChecked()){
                    generateAndDownloadReportEventExt();
                }
            }
        });

        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
    private List<Photos> getPhotoList(){

        photoList.add(new Photos(1,R.drawable.logo_calendario_eventos_pagina_inicial,"Agendar Evento"));
        photoList.add(new Photos(2,R.drawable.logo_visualizacao_pagina_inicial, "Visualizar Agendados"));

        return photoList;
    }
    private void openFile() {
        // Verifique se o arquivo existe no diretório adequado
        File reportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "MeuAppRelatorios");
        if (!reportDir.exists()) {
            boolean created = reportDir.mkdirs(); // Crie o diretório se ele não existir
            Log.d("OpenFile", "Diretório criado: " + created);
        }

        File reportFile = new File(reportDir, "report.xlsx");

        if (reportFile.exists()) {
            // Tente abrir o arquivo localmente
            try {
                FileInputStream inputStream = new FileInputStream(reportFile);
                // Leia ou manipule o conteúdo do arquivo aqui
                inputStream.close();

                // Crie a Uri para o FileProvider
                Uri uri = FileProvider.getUriForFile(this, "com.example.esp_eventos.fileprovider", reportFile);

                // Crie a Intent para abrir o arquivo
                Intent intentteste1 = new Intent(Intent.ACTION_VIEW);
                intentteste1.setDataAndType(uri, getContentResolver().getType(uri));
                intentteste1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                // Inicie a atividade para abrir o arquivo
                startActivity(intentteste1);

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("OpenFile", "Erro ao abrir o arquivo: " + e.getMessage());
            }
        } else {
            Log.e("OpenFile", "O arquivo não existe.");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                openFile();
            } else {
                Log.e("Permissions", "Permissão negada.");
            }
        }
    }
    private void createNotificationChannel() {
        CharSequence name = "MyChannel";
        String description = "Description for MyChannel";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
    private void generateAndDownloadReportEventExt() {
        DadosFirestoreExterno.fetchFirestoreDataExt(new DadosFirestoreExterno.FirestoreDataExternoListener() {
            @Override
            public void onDataLoaded(byte[] data) {
                Log.d("GenerateReport", "Dados do Firestore recebidos com sucesso.");

                if (isExternalStorageWritable()) {
                    File reportDir = new File(getExternalFilesDir(null), "MeuAppRelatorios");
                    reportDir.mkdirs();

                    File reportFile = new File(reportDir, "Relatório_Eventos_Externos.xlsx");

                    try (FileOutputStream fos = new FileOutputStream(reportFile)) {
                        fos.write(data);
                        fos.close();

                        Log.d("GenerateReport", "Arquivo Excel criado com sucesso.");

                        createNotificationChannel();
                        showNotification("Relatório Gerado", "Toque para abrir o relatório", reportFile);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("GenerateReport", "Erro ao gerar o relatório: " + e.getMessage());
                        showToast("Erro ao gerar o relatório");
                    }
                } else {
                    showToast("Armazenamento externo não disponível");
                }
            }
        });
    }
    private void generateAndDownloadReport() {
        DadosFirestore.fetchFirestoreData(new DadosFirestore.FirestoreDataListener() {
            @Override
            public void onDataLoaded(byte[] data) {
                Log.d("GenerateReport", "Dados do Firestore recebidos com sucesso.");

                if (isExternalStorageWritable()) {
                    File reportDir = new File(getExternalFilesDir(null), "MeuAppRelatorios");
                    reportDir.mkdirs();

                    File reportFile = new File(reportDir, "Relatório_Eventos_Internos.xlsx");

                    try (FileOutputStream fos = new FileOutputStream(reportFile)) {
                        fos.write(data);
                        fos.close();

                        Log.d("GenerateReport", "Arquivo Excel criado com sucesso.");

                        createNotificationChannel();
                        showNotification("Relatório Gerado", "Toque para abrir o relatório", reportFile);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("GenerateReport", "Erro ao gerar o relatório: " + e.getMessage());
                        showToast("Erro ao gerar o relatório");
                    }
                } else {
                    showToast("Armazenamento externo não disponível");
                }
            }
        });
    }
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
    private void showNotification(String title, String message, File reportFile) {

        Intent intentirparaodiretorio = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(this, "com.example.esp_eventos.fileprovider",reportFile);
        intentirparaodiretorio.setDataAndType(uri, getContentResolver().getType(uri));
        intentirparaodiretorio.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentirparaodiretorio, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_calendario_eventos)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void IniciarComponentes(){
        textnomeUsuario = findViewById(R.id.textnomeUsuario1);
        imageViewoff = findViewById(R.id.imageViewoff);
        recyclerView = findViewById(R.id.recyclerView);
        imageViewConfigAdmin = findViewById(R.id.imageViewConfigAdmin);
    }
    protected void onDestroy(){
        super.onDestroy();

        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(null);
        recyclerView = null;

        photoList.clear();
        photoList = null;

        Log.d("Pagina principal", "Activity finalizada");
    }

}