package com.example.esp_eventos;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarViewMonthly extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView textViewMonthYear;
    private Calendar currentDate;
    private FirebaseFirestore firestore;
    private List<Eventos> eventosList = new ArrayList<>();
    private RecyclerView recyclerView_events;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view_monthly);

        // Inicialize o Firestore
        firestore = FirebaseFirestore.getInstance();
        textViewMonthYear = findViewById(R.id.textView_month_year);
        calendarView = findViewById(R.id.calendarView);
        recyclerView_events = findViewById(R.id.recyclerView_events);

        currentDate = Calendar.getInstance();
        updateCalendar();

        // Configurar um listener para o CalendarView para responder às seleções de datas
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Atualizar o currentDate para a data selecionada
                currentDate.set(year, month, dayOfMonth);
                updateCalendar();

                // Formate a data selecionada para o formato desejado
                String selectedDate = formatDate(year, month, dayOfMonth);

                // Consulta ao Firestore para obter os eventos na data selecionada
                getEventsByDate(selectedDate);
            }
        });

        // Recuperar eventos do Firestore e atualizar a lista de eventos
        // getEventsFromFirestore();
    }

    @SuppressLint("SetTextI18n")
    private void updateCalendar() {
        // Atualizar o mês e o ano exibidos no texto do mês/ano
        textViewMonthYear.setText(currentDate.getDisplayName(Calendar.MONTH, Calendar.LONG, getResources().getConfiguration().locale)
                + " " + currentDate.get(Calendar.YEAR));

        // Aqui você pode adicionar a lógica para recuperar os eventos do Firestore para a data atual
        // e atualizar a lista de eventos abaixo
        // Como exemplo, vamos adicionar alguns eventos fictícios
        //events = new ArrayList<>();
        //events.add(new Event("Evento 1", "Descrição do Evento 1", currentDate.getTime()));
        //events.add(new Event("Evento 2", "Descrição do Evento 2", currentDate.getTime()));
        //events.add(new Event("Evento 3", "Descrição do Evento 3", currentDate.getTime()));

        // Atualiza o adaptador da RecyclerView com a lista de eventos
        //eventAdapter = new EventAdapter(events);
        //recyclerViewEvents.setLayoutManager(new LinearLayoutManager(this));
        //recyclerViewEvents.setAdapter(eventAdapter);

    }

    private String formatDate(int year, int month, int dayOfMonth) {
        // Formate a data no formato desejado (exemplo: "06/02/2024")
        return String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth,month + 1,year);
    }

    private void getEventsFromFirestore() {
        // Referência para a coleção "eventos" no Firestore
        CollectionReference eventosRef = firestore.collection("eventos");

        // Consulta para recuperar os eventos ordenados pela data, por exemplo
        Query query = eventosRef.orderBy("datadoEvento");

        // Executar a consulta
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Limpar a lista de eventos
                eventosList.clear();

                // Iterar sobre os documentos retornados e adicionar os eventos à lista
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Eventos evento = documentSnapshot.toObject(Eventos.class);
                    eventosList.add(evento);
                }

                // Inicializar o adaptador com a lista de eventos
                eventAdapter = new EventAdapter(eventosList);

                // Definir um LinearLayoutManager para o RecyclerView
                recyclerView_events.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                // Definir o adaptador para o RecyclerView
                recyclerView_events.setAdapter(eventAdapter);

                // Notificar o adapter sobre a mudança nos dados
                eventAdapter.notifyDataSetChanged();

                // Notificar o adapter sobre a mudança nos dados
                eventAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Tratar falha na recuperação dos eventos, se necessário
                Log.e("MonthlyCalendarActivity", "Error getting events from Firestore", e);
            }
        });
    }

    private void getEventsByDate(String selectedDate) {
        // Referência para a coleção "eventos" no Firestore
        CollectionReference eventosRef = firestore.collection("eventos");

        // Consulta para recuperar os eventos que ocorrem na data selecionada
        Query query = eventosRef.whereEqualTo("datadoEvento", selectedDate);

        Log.d("DATA", selectedDate);

        // Executar a consulta
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Limpar a lista de eventos
                eventosList.clear();

                // Iterar sobre os documentos retornados e adicionar os eventos à lista
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Eventos evento = documentSnapshot.toObject(Eventos.class);
                    eventosList.add(evento);
                }

                // Inicializar o adaptador com a lista de eventos
                eventAdapter = new EventAdapter(eventosList);

                // Definir um LinearLayoutManager para o RecyclerView
                recyclerView_events.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                // Definir o adaptador para o RecyclerView
                recyclerView_events.setAdapter(eventAdapter);

                // Notificar o adapter sobre a mudança nos dados
                eventAdapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Tratar falha na recuperação dos eventos, se necessário
                Log.e("MonthlyCalendarActivity", "Error getting events from Firestore", e);
            }
        });
    }


}