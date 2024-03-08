package com.example.esp_eventos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CustomCalendar extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView, recyclerView;
    private ArrayList<String> daysOfMonth;
    private LocalDate selectedDate;
    private TextView textViewMonthYear, textViewEmptyEvents, textViewTitleEvents;
    private CalendarAdapter calendarAdapter;
    private FirebaseFirestore firestore;
    private List<Eventos> eventosList = new ArrayList<>();
    private RecyclerView recyclerView_events;
    private EventAdapter eventAdapter;
    private ImageView imageViewbackPaginaInicial1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_calendar);
        getSupportActionBar().hide();
        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.azul1));

        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();

        // Inicialize o Firestore
        firestore = FirebaseFirestore.getInstance();
        textViewMonthYear = findViewById(R.id.textView_month_year1);
        recyclerView_events = findViewById(R.id.recyclerView_events1);
        textViewEmptyEvents = findViewById(R.id.textViewEmptyEvents1);
        textViewTitleEvents = findViewById(R.id.textViewTitleEvents1);
        imageViewbackPaginaInicial1 = findViewById(R.id.imageViewbackPaginaInicial1);

        textViewEmptyEvents.setVisibility(View.VISIBLE);
        textViewEmptyEvents.setText("Selecione uma data para verificar se há evento");


        // Carregar eventos do Firestore para o mês atual
        loadEventsFromFirestore(selectedDate);

        imageViewbackPaginaInicial1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenttestecalendario = new Intent(CustomCalendar.this, PaginaPrincipal.class);
                startActivity(intenttestecalendario);
                finish();
            }
        });
    }

    private void setMonthView() {
        monthYearText.setText(monthYearTextFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this, selectedDate);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
              daysInMonthArray.add("");
            }
            else
            {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    private String monthYearTextFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    public void previousMonthAction(View view) {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
        loadEventsFromFirestore(selectedDate);
    }

    public void nextMonthAction(View view) {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
        loadEventsFromFirestore(selectedDate);
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (dayText != null && !dayText.isEmpty()) {
            try {
                int day = Integer.parseInt(dayText);
                // Use selectedDate (que é uma LocalDate) para obter o ano e o mês atuais
                int year = selectedDate.getYear();
                int month = selectedDate.getMonthValue();

                // Agora, construa uma string de data completa
                String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);

                // Consulta ao Firestore para obter os eventos na data selecionada
                getEventsByDate(formattedDate);
            } catch (NumberFormatException e) {
                // O texto do dia não é um número válido
                e.printStackTrace();
            }
        } else {
            // O texto do dia é nulo ou vazio
            // Trate o erro ou mostre uma mensagem para o usuário
        }
    }

    @Override
    public void onDayClick(LocalDate date) {
        selectedDate = date;
        //Log.d("CustomCalendar", "Data selecionada: " + selectedDate.toString()); // Adiciona um log para verificar a data selecionada
        // Use selectedDate para obter o ano e o mês atuais
        int year = selectedDate.getYear();
        int month = selectedDate.getMonthValue();
        int day = selectedDate.getDayOfMonth();

        // Construa uma string de data completa
        String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);

        // Consulta ao Firestore para obter os eventos na data selecionada
        getEventsByDate(formattedDate);
        setMonthView(); // Atualiza o calendário com a nova data selecionada
        loadEventsFromFirestore(selectedDate); // Carrega os eventos para a nova data selecionada
    }
    private void getEventsByDate(String selectedDate) {
        // Limpe a lista de eventos antes de adicionar novos eventos
        eventosList.clear();

        // Referência para a coleção "eventos_internos" no Firestore
        CollectionReference eventosInternosRef = firestore.collection("eventos");

        // Referência para a coleção "eventos_externos" no Firestore
        CollectionReference eventosExternosRef = firestore.collection("eventosExt");

        eventosInternosRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Iterar sobre os documentos retornados e adicionar os eventos internos à lista
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Eventos evento = documentSnapshot.toObject(Eventos.class);

                    // Verificar se o evento ocorre na data selecionada
                    if (isEventInIntermediateDays(evento, selectedDate)) {
                        eventosList.add(evento);
                    }
                }

                // Consultar os eventos externos e adicionar à lista
                eventosExternosRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // Iterar sobre os documentos retornados e adicionar os eventos externos à lista
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            EventosExterno eventoExterno = documentSnapshot.toObject(EventosExterno.class);

                            // Verificar se o evento externo ocorre na data selecionada
                            if (isEventExtInIntermediateDays(eventoExterno, selectedDate)) {
                                // Converta o evento externo para o formato Eventos (se necessário) e adicione à lista
                                Eventos evento = convertToEventos(eventoExterno);
                                eventosList.add(evento);
                            }
                        }

                        // Atualize a interface após adicionar eventos internos e externos
                        updateInterface();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Tratar falha na recuperação dos eventos externos, se necessário
                        Log.e("MonthlyCalendarActivity", "Error getting external events from Firestore", e);
                        // Atualize a interface após uma falha
                        updateInterface();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Tratar falha na recuperação dos eventos internos, se necessário
                Log.e("MonthlyCalendarActivity", "Error getting internal events from Firestore", e);
                // Atualize a interface após uma falha
                updateInterface();
            }
        });
    }
    // Método para converter um evento externo (EventoExterno) para o formato Eventos
    private Eventos convertToEventos(EventosExterno eventoExterno) {
        // Implemente a lógica de conversão aqui
        Eventos evento = new Eventos();
        evento.setnomedoEvento(eventoExterno.getNomedoEventoExterno());
        evento.setdescricaodaAtividade(eventoExterno.getDescricaoEventoExterno());
        evento.setlocaldoEvento(eventoExterno.getLocalEventoExterno());
        evento.setdatadoEvento(eventoExterno.getDataInicialEventoExterno());
        evento.setDatadoEventoFinal(eventoExterno.getDataFinalEventoExterno());
        // Converta outros campos conforme necessário
        return evento;
    }

    // Método para atualizar a interface após a conclusão das consultas de eventos
    @SuppressLint("SetTextI18n")
    private void updateInterface() {
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
        EventAdapter eventAdapter = new EventAdapter(eventosList, CustomCalendar.this);

        // Definir um LinearLayoutManager para o RecyclerView
        recyclerView_events.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        // Definir o adaptador para o RecyclerView
        recyclerView_events.setAdapter(eventAdapter);
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
    private boolean isEventExtInIntermediateDays(EventosExterno eventosExterno, String selectedDate) {
        String startDate = eventosExterno.getDataInicialEventoExterno();
        String endDate = eventosExterno.getDataFinalEventoExterno();

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
    private void loadEventsFromFirestore(LocalDate monthToShow) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        firestore.collection("eventos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Map<LocalDate, Boolean> eventsMap = new HashMap<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String startDateStr = documentSnapshot.getString("datadoEvento");
                        String endDateStr = documentSnapshot.getString("datadoEventoFinal");
                        Log.d("FirestoreEvent", "Evento carregado: Início = " + startDateStr + ", Fim = " + endDateStr);

                        try {
                            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
                            LocalDate endDate = LocalDate.parse(endDateStr, formatter);

                            // Preencha o mapa considerando todos os dias entre startDate e endDate
                            LocalDate date = startDate;
                            while (!date.isAfter(endDate)) {
                                eventsMap.put(date, true);
                                date = date.plusDays(1);
                            }

                        } catch (DateTimeParseException e) {
                            Log.e("Firestore", "Erro ao analisar a data", e);
                        }
                    }

                    // Após preencher o mapa, atualize o calendário com os eventos
                    updateCalendarWithEvents(eventsMap);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Erro ao carregar eventos", e);
                });

    }
    @SuppressLint("NotifyDataSetChanged")
    private void updateCalendarWithEvents(Map<LocalDate, Boolean> eventsMap) {
        // Atualizar o calendário com os eventos carregados
        if (calendarAdapter != null) {

            for (LocalDate date : eventsMap.keySet()) {
                Log.d("UpdateEvents", "Data com evento: " + date.toString() + ", Tem evento: " + eventsMap.get(date));
            }

            calendarAdapter.setEventsMap(eventsMap);
            calendarAdapter.notifyDataSetChanged();
        }
    }
}