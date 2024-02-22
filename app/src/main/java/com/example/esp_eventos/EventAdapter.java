package com.example.esp_eventos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Eventos> eventosList;

    public EventAdapter(List<Eventos> eventosList) {
        this.eventosList = eventosList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.imte_event, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Eventos evento = eventosList.get(position);
        holder.textViewTitle.setText(evento.getnomedoEvento());
        holder.textViewDescription.setText(evento.getdescricaodaAtividade());
        holder.textViewDate.setText(evento.getdatadoEvento());
    }

    @Override
    public int getItemCount() {
        return eventosList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public TextView textViewDescription;
        public TextView textViewDate;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textView_event_title);
            textViewDescription = itemView.findViewById(R.id.textView_event_description);
            textViewDate = itemView.findViewById(R.id.textView_event_date);
        }
    }
}
