package com.example.esp_eventos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Eventos> eventosList;
    private Context context;

    public EventAdapter(List<Eventos> eventosList, Context context) {
        this.eventosList = eventosList;
        this.context = context;
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
        holder.bind(evento);

        // Verifica se há mais de um evento no mesmo dia
        if (eventosList.size() > 1) {
            // Ajusta a altura do item para um valor menor
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            layoutParams.height = dpToPx(175); // Defina a altura desejada em dp
            holder.itemView.setLayoutParams(layoutParams);
        } else {
            // Mantém a altura padrão do item
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.itemView.setLayoutParams(layoutParams);
        }
    }

    @Override
    public int getItemCount() {
        return eventosList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public TextView textViewDescription;
        public TextView textViewDate;
        public TextView textViewFinalDate;
        public TextView textViewLocal;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textView_event_title);
            textViewDescription = itemView.findViewById(R.id.textView_event_description);
            textViewDate = itemView.findViewById(R.id.textView_event_date);
            textViewFinalDate = itemView.findViewById(R.id.textView_event_final_date);
            textViewLocal = itemView.findViewById(R.id.textView_event_local);
        }

        public void bind(Eventos evento) {
            textViewTitle.setText(evento.getnomedoEvento());
            textViewDescription.setText(evento.getdescricaodaAtividade());
            textViewDate.setText(evento.getdatadoEvento());
            textViewFinalDate.setText(evento.getDatadoEventoFinal());
            textViewLocal.setText(evento.getlocaldoEvento());
        }
    }
    // Método auxiliar para converter dp para pixels
    private int dpToPx(int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
