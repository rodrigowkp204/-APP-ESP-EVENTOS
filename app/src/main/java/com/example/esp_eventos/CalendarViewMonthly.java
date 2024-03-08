package com.example.esp_eventos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarViewMonthly extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView textViewMonthYear, textViewEmptyEvents, textViewTitleEvents;
    private Calendar currentDate;
    private FirebaseFirestore firestore;
    private List<Eventos> eventosList = new ArrayList<>();
    private RecyclerView recyclerView_events;
    private EventAdapter eventAdapter;
    private ImageView imageViewbackPaginaInicial;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view_monthly);

        getSupportActionBar().hide();
        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.azul1));

        // Inicialize o Firestore
        firestore = FirebaseFirestore.getInstance();
        textViewMonthYear = findViewById(R.id.textView_month_year);
        calendarView = findViewById(R.id.calendarView);
        recyclerView_events = findViewById(R.id.recyclerView_events);
        textViewEmptyEvents = findViewById(R.id.textViewEmptyEvents);
        textViewTitleEvents = findViewById(R.id.textViewTitleEvents);
        imageViewbackPaginaInicial = findViewById(R.id.imageViewbackPaginaInicial);

        currentDate = Calendar.getInstance();
        //updateCalendar();

        textViewEmptyEvents.setVisibility(View.VISIBLE);
        textViewEmptyEvents.setText("Selecione uma data para verificar se há evento");

        // Configurar um listener para o CalendarView para responder às seleções de datas
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Atualizar o currentDate para a data selecionada
                currentDate.set(year, month, dayOfMonth);
                //updateCalendar();


                // Formate a data selecionada para o formato desejado
                String selectedDate = formatDate(year, month, dayOfMonth);

                // Consulta ao Firestore para obter os eventos na data selecionada
                getEventsByDate(selectedDate);

                calendarView.invalidate();
            }
        });

        imageViewbackPaginaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenttestecalendario = new Intent(CalendarViewMonthly.this, PaginaPrincipal.class);
                startActivity(intenttestecalendario);
                finish();
            }
        });
    }
    private String formatDate(int year, int month, int dayOfMonth) {
        // Formate a data no formato desejado (exemplo: "06/02/2024")
        return String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth,month + 1,year);
    }
    private void getEventsByDate(String selectedDate) {
        // Referência para a coleção "eventos" no Firestore
        CollectionReference eventosRef = firestore.collection("eventos");

        // Executar a consulta para recuperar todos os eventos que começam antes do dia selecionado e terminam após o dia selecionado
        eventosRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Limpar a lista de eventos
                eventosList.clear();

                // Iterar sobre os documentos retornados e adicionar os eventos à lista
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Eventos evento = documentSnapshot.toObject(Eventos.class);

                    // Verificar se o evento ocorre em algum dia intermediário
                    if (isEventInIntermediateDays(evento, selectedDate)) {
                        eventosList.add(evento);
                    }
                }

                // Verificar se a lista de eventos está vazia
                if (eventosList.isEmpty()) {
                    // Exibir mensagem indicando que não há eventos cadastrados
                    textViewEmptyEvents.setVisibility(View.VISIBLE);
                    textViewTitleEvents.setVisibility(View.GONE);
                    textViewEmptyEvents.setText("Não tem evento cadastrado para esse dia");
                } else {
                    // Esconder a mensagem se houver eventos cadastrados
                    textViewEmptyEvents.setVisibility(View.GONE);
                    textViewTitleEvents.setVisibility(View.VISIBLE);
                    textViewTitleEvents.setText("Agendados");
                }

                // Inicializar o adaptador com a lista de eventos
                EventAdapter eventAdapter = new EventAdapter(eventosList, CalendarViewMonthly.this);

                // Definir um LinearLayoutManager para o RecyclerView
                recyclerView_events.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                // Definir o adaptador para o RecyclerView
                recyclerView_events.setAdapter(eventAdapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Tratar falha na recuperação dos eventos, se necessário
                Log.e("MonthlyCalendarActivity", "Error getting events from Firestore", e);
            }
        });
    }

    // Método auxiliar para verificar se um evento ocorre em algum dia intermediário
    private boolean isEventInIntermediateDays(Eventos evento, String selectedDate) {
        String startDate = evento.getdatadoEvento();
        String endDate = evento.getDatadoEventoFinal();

        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);
            Date selected = dateFormat.parse(selectedDate);

            // Verificar se o evento ocorre em algum dia intermediário entre a data de início e a data de término
            return !start.after(selected) && !end.before(selected);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public void onBackPressed(){
        Toast.makeText(this, "Utilize o botão de voltar no topo da tela", Toast.LENGTH_SHORT).show();
    }
}