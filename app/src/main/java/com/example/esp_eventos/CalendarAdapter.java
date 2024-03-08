package com.example.esp_eventos;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{

    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;
    private LocalDate selectedDate;
    private Map<LocalDate, Boolean> eventsMap = new HashMap<>();
    private FirebaseFirestore firestore;
    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener, LocalDate initialDate){
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
        this.selectedDate = initialDate; // Adicione isso
    }

    public void setSelectedDate(LocalDate date) {
        this.selectedDate = date;
    }
    public void setEventsMap(Map<LocalDate, Boolean> eventsMap) {
        this.eventsMap = eventsMap;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell,parent,false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.1666666);
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        String dayString = daysOfMonth.get(position);
        holder.dayOfMonth.setText(dayString);

        // Limpar o círculo indicativo de todos os dias
        holder.dayOfMonth.setBackgroundResource(0);

        if (!dayString.isEmpty()) {
            try {
                int day = Integer.parseInt(dayString);
                LocalDate date = LocalDate.of(selectedDate.getYear(), selectedDate.getMonthValue(), day);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String formattedDate = date.format(formatter);

                // Configure o listener de clique para o dia
                holder.itemView.setOnClickListener(v -> {
                    if (onItemListener != null) {
                        onItemListener.onDayClick(date);
                    }
                });

                firestore = FirebaseFirestore.getInstance();
                // Consultar o Firestore para verificar se há eventos para a data atual
                firestore.collection("eventos")
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            // Iterar sobre os documentos retornados e verificar se algum evento ocorre nesta data
                            boolean hasEvent = false;
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Eventos evento = documentSnapshot.toObject(Eventos.class);
                                if (isEventInIntermediateDays(evento, formattedDate)) {
                                    // Marcar que há pelo menos um evento interno neste dia
                                    hasEvent = true;
                                    break; // Sai do loop assim que encontrar um evento
                                }
                            }
                            // Se houver pelo menos um evento interno neste dia, definir o indicador de evento interno como visível
                            if (hasEvent) {
                                holder.eventIndicator.setVisibility(View.VISIBLE);
                            } else {
                                // Se não houver eventos internos neste dia, esconder o indicador de evento interno
                                holder.eventIndicator.setVisibility(View.GONE);
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Lidar com falhas na consulta ao Firestore
                            Log.e("Firestore", "Erro ao verificar eventos", e);
                        });

                // Consultar o Firestore para verificar se há eventos externos para a data atual
                firestore.collection("eventosExt")
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            // Iterar sobre os documentos retornados e verificar se algum evento externo ocorre nesta data
                            boolean hasExternalEvent = false;
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                EventosExterno eventosExterno = documentSnapshot.toObject(EventosExterno.class);
                                if (isEventExtInIntermediateDays(eventosExterno, formattedDate)) {
                                    // Marcar que há pelo menos um evento externo neste dia
                                    hasExternalEvent = true;
                                    break; // Sai do loop assim que encontrar um evento externo
                                }
                            }
                            // Se houver pelo menos um evento externo neste dia, definir o indicador de evento externo como visível
                            if (hasExternalEvent) {
                                holder.eventExtIndicator.setVisibility(View.VISIBLE);
                            } else {
                                // Se não houver eventos externos neste dia, esconder o indicador de evento externo
                                holder.eventExtIndicator.setVisibility(View.GONE);
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Lidar com falhas na consulta ao Firestore para eventos externos
                            Log.e("Firestore", "Erro ao verificar eventos externos", e);
                        });

                // Definir estilo especial para o texto do dia selecionado
                if (date.equals(selectedDate)) {
                    // Se o dia for o dia selecionado, aplique o estilo desejado
                    holder.dayOfMonth.setBackgroundResource(R.drawable.selected_day_circle);
                    holder.dayOfMonth.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
                } else {
                    // Se não for o dia selecionado, mantenha o estilo padrão
                    holder.dayOfMonth.setBackgroundResource(0);
                }

            } catch (NumberFormatException e) {
                Log.e("CalendarAdapter", "Erro ao converter string para LocalDate", e);
            }
        }
    }



    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public interface OnItemListener{
        void onItemClick(int position, String dayText);
        void onDayClick(LocalDate date);

    }
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
}
