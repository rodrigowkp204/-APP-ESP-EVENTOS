package com.example.login;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.esp_eventos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FormCadastroEvento extends BaseActivity {
    private EditText edit_nome_evento,edit_setor_resp, edit_numeros_participantes, edit_desc_atv, edit_obs ;
    String[] locais = {"Isabel dos Santos","Paulo Freire","Multimídia 1","Multimídia 2"};
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterlocais;
    ImageView imageViewData; //Instanciando o que você deseja puxar
    ImageView imageViewoff;  //Instanciando o que você deseja puxar
    ImageView imageViewHora; //Instanciando o que você deseja puxar
    ImageView imageViewHoraFinal; //Instanciando o que você deseja puxar
    ImageView imageViewback; //Instanciando o que você deseja puxar
    @SuppressLint("StaticFieldLeak")
    static EditText  editTextData;
    @SuppressLint("StaticFieldLeak")
    static EditText  editTextData2;
    @SuppressLint("StaticFieldLeak")
    static EditText editTextHoraInicial;
    @SuppressLint("StaticFieldLeak")
    static EditText editTextHoraFinal;
    ScrollView scrollview;


    @SuppressLint({"MissingInflatedId", "CutPasteId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro_evento);

        FirebaseApp.initializeApp(this);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .build();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);

        IniciarComponentes();

        // LIGAÇÃO COM O FRONTEND - RECUPERAR O QUE FOR COLOCADO LA - //
        editTextHoraInicial = findViewById(R.id.edit_horas);
        editTextHoraFinal = findViewById(R.id.edit_horas_final);
        editTextData = findViewById(R.id.edit_data);
        editTextData2 = findViewById(R.id.edit_data);
        imageViewHora = findViewById(R.id.imageViewHora);
        imageViewHoraFinal = findViewById(R.id.imageViewHoraFinal);
        imageViewData = findViewById(R.id.imageViewData);
        imageViewoff = findViewById(R.id.imageViewoff);
        Button bt_cadastrar = findViewById(R.id.bt_cadastrar);
        imageViewback = findViewById(R.id.imageViewback);
        autoCompleteTxt = findViewById(R.id.auto_complete_txt);
        scrollview = findViewById(R.id.scrollView);


        adapterlocais = new ArrayAdapter<>(this, R.layout.lista_item_local, locais);

        autoCompleteTxt.setAdapter(adapterlocais);


        Calendar calendario = Calendar.getInstance(); //Pegar a data do celular
        Integer dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH);
        Integer ano = calendario.get(Calendar.YEAR);
        Integer hora = calendario.get(Calendar.HOUR_OF_DAY);
        Integer minutos = calendario.get(Calendar.MINUTE);

        @SuppressLint("DefaultLocale")
        String dataFormatada = String.format("%02d/%02d/%04d", dia, mes+1, ano);
        editTextData.setText(dataFormatada);
        editTextData2.setText(dataFormatada);
        @SuppressLint("DefaultLocale")
        String horaformatada = String.format("%02d:%02d",hora,minutos);
        editTextHoraInicial.setText(horaformatada);

        imageViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Colocar as ações que você deseja executar quando clicar
                DialogFragment dialogFragment = new DatePicker();
                dialogFragment.show(getSupportFragmentManager(), "data1");
            }
        });

        imageViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Colocar as ações que você deseja executar quando clicar
                DialogFragment dialogFragment = new DatePicker();
                dialogFragment.show(getSupportFragmentManager(), "data2");
            }
        });

        imageViewHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Colocar as ações que você deseja executar quando clicar
                DialogFragment dialogFragment = new TimePicker();
                dialogFragment.show(getSupportFragmentManager(), "hora1");
            }
        });

        imageViewHoraFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragmentFinal = new TimePicker();
                dialogFragmentFinal.show(getSupportFragmentManager(),"hora2");
            }
        });

        imageViewoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(FormCadastroEvento.this, Activity_Splash_Form_Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FormCadastroEvento.this, PaginaPrincipal.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = editTextData2.getText().toString();
                String hora = editTextHoraInicial.getText().toString();
                String horafinal = editTextHoraFinal.getText().toString();
                String nome = edit_nome_evento.getText().toString();
                String local = autoCompleteTxt.getText().toString();
                String descricao = edit_desc_atv.getText().toString();
                String setor = edit_setor_resp.getText().toString();
                String participantes = edit_numeros_participantes.getText().toString();
                String observacoes = edit_obs.getText().toString();

                CollectionReference evento = db.collection("eventos");

                Query query = evento.whereEqualTo("datadoEvento", data).whereEqualTo("localdoEvento", local);

                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean hasConflict = false;
                            boolean isEmptyField = data.isEmpty() || hora.isEmpty() || nome.isEmpty() || setor.isEmpty() || participantes.isEmpty()
                                    || local.isEmpty() || descricao.isEmpty() || observacoes.isEmpty() || horafinal.isEmpty();

                            if (task.getResult().size() > 0) {
                                // Verificar se já existe um evento cadastrado para a mesma data e local
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String existingStartTime = document.getString("horadoEventoInicial");
                                    String existingEndTime = document.getString("horaEventoFinal");

                                    assert existingEndTime != null;
                                    if ((hora.compareTo(existingEndTime) < 0 && horafinal.compareTo(Objects.requireNonNull(existingStartTime)) > 0)
                                            || (hora.compareTo(existingEndTime) > 0 && horafinal.compareTo(Objects.requireNonNull(existingStartTime)) < 0)
                                            || (hora.equals(existingStartTime) && horafinal.equals(existingEndTime))) {
                                        // Há conflito de horário
                                        hasConflict = true;
                                        break;
                                    }
                                }
                            }

                            if (hasConflict) {
                                // Há conflito de horário ou evento já cadastrado para a mesma data e local
                                Toast.makeText(getApplicationContext(), "Já existe um agendamento para essa data e local ou conflito de horário", Toast.LENGTH_SHORT).show();
                            } else if (isEmptyField) {
                                // Verificar se todos os campos estão preenchidos
                                Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                            } else {
                                // Verificar a capacidade máxima de participantes com base no local do evento
                                int capacidadeMaxima = 0; // Capacidade máxima inicializada com 0
                                switch (local) {
                                    case "Isabel dos Santos":
                                        capacidadeMaxima = 80;
                                        break;
                                    case "Paulo Freire":
                                        capacidadeMaxima = 150;
                                        break;
                                    case "Multimídia 1":
                                    case "Multimídia 2":
                                        capacidadeMaxima = 50;
                                        break;
                                }

                                if (Integer.parseInt(participantes) > capacidadeMaxima) {
                                    // Verificar se o número de participantes excede a capacidade máxima
                                    Toast.makeText(getApplicationContext(), "A capacidade máxima para este local é de " + capacidadeMaxima + " participantes", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Todos os campos estão preenchidos, não há conflitos de horário e o número de participantes está dentro da capacidade máxima, pode cadastrar o evento
                                    Map<String, Object> eventos = new HashMap<>();
                                    eventos.put("datadoEvento", data);
                                    eventos.put("horadoEventoInicial", hora);
                                    eventos.put("horaEventoFinal", horafinal);
                                    eventos.put("nomedoEvento", nome);
                                    eventos.put("localdoEvento", local);
                                    eventos.put("descricaodaAtividade", descricao);

                                    evento.add(eventos).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Agendamento cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                                            cadastrarinfos();
                                            Intent intentCE = new Intent(FormCadastroEvento.this, PaginaPrincipal.class);
                                            intentCE.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intentCE);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Erro ao cadastrar agendamento", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Erro ao verificar agendamento", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });

        edit_obs.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v.getId() == R.id.edit_obs){
                    v.getParent().requestDisallowInterceptTouchEvent(true);

                    if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }

                return false;
            }
        });

        edit_desc_atv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v.getId() == R.id.edit_desc_atv){
                    v.getParent().requestDisallowInterceptTouchEvent(true);

                    if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }

                return false;
            }
        });

    }
    @Override
    public void onBackPressed(){
        Toast.makeText(this, "Utilize o botão de voltar no topo da tela", Toast.LENGTH_SHORT).show();
    }
    private void cadastrarinfos() {
        String setor = edit_setor_resp.getText().toString();
        String participantes = edit_numeros_participantes.getText().toString();
        String observacoes = edit_obs.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference info = db.collection("infoEventos");

        Map<String, Object> eventos2 = new HashMap<>();
        eventos2.put("setorResponsavel",setor);
        eventos2.put("numerosdeParticipantes", participantes);
        eventos2.put("observacoes", observacoes);

        info.add(eventos2).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    public static class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){

            Calendar calendario = Calendar.getInstance();
            int dia = calendario.get(Calendar.DAY_OF_MONTH);
            int mes = calendario.get(Calendar.MONTH);
            int ano = calendario.get(Calendar.YEAR);

            return new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, this, ano, mes, dia);
        }
        @Override
        public void onDateSet(android.widget.DatePicker view, int ano, int mes, int dia) {

            String tagClicado = getTag();

            assert tagClicado != null;
            if(tagClicado.equals("data1")){

                @SuppressLint("DefaultLocale")
                String dataFormatada = String.format("%02d/%02d/%04d", dia, mes+1, ano);
                editTextData.setText(dataFormatada);
            } else if(tagClicado.equals("data2")){
                @SuppressLint("DefaultLocale")
                String dataFormatada2 = String.format("%02d/%02d/%04d", dia, mes+1, ano);
                editTextData2.setText(dataFormatada2);
            }

        }
    }
    public static class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){

            Calendar calendario = Calendar.getInstance();
            int hora = calendario.get(Calendar.HOUR_OF_DAY);
            int minutos = calendario.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, this, hora, minutos,true);
        }
        @Override
        public void onTimeSet(android.widget.TimePicker view, int horas, int minutos) {

            String tagClicado = getTag();

            assert tagClicado != null;
            if(tagClicado.equals("hora1")){

                @SuppressLint("DefaultLocale")
                String horaformatada = String.format("%02d:%02d",horas,minutos);

                editTextHoraInicial.setText(horaformatada);

            }else if (tagClicado.equals("hora2")){

                @SuppressLint("DefaultLocale")
                String horaformatadafinal = String.format("%02d:%02d",horas,minutos);

                editTextHoraFinal.setText(horaformatadafinal);
            }
        }
    }
    @SuppressLint("WrongViewCast")
    private void IniciarComponentes(){

        edit_nome_evento = findViewById(R.id.edit_nome_evento); //Recuperar o edit_nome_evento que está na parte do front-end.
        edit_setor_resp = findViewById(R.id.edit_setor_resp); //Recuperar o edit_setor_resp que está na parte do front-end.
        edit_numeros_participantes = findViewById(R.id.edit_numeros_participantes); //Recuperar o edit_numeros_participantes que está na parte do front-end.
        edit_desc_atv = findViewById(R.id.edit_desc_atv);
        edit_obs = findViewById(R.id.edit_obs);

    }

}