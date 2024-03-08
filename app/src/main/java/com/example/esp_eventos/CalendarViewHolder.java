package com.example.esp_eventos;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public final TextView dayOfMonth;
    private final CalendarAdapter.OnItemListener onItemListener;
    View eventIndicator, eventExtIndicator;
    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener) {
        super(itemView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
        eventIndicator = itemView.findViewById(R.id.eventIndicator);
        eventExtIndicator = itemView.findViewById(R.id.eventExtIndicator);

        itemView.setOnClickListener(v -> {
            if (onItemListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemListener.onItemClick(position, (String) dayOfMonth.getText());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        onItemListener.onItemClick(getAdapterPosition(), (String) dayOfMonth.getText());
    }
}
