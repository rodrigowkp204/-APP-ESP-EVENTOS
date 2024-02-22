package com.example.esp_eventos;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;

public class EventDecorator extends RecyclerView.ItemDecoration {

    private final int eventColor;
    private final HashSet<Integer> datesWithEvents;

    public EventDecorator(int eventColor, HashSet<Integer> datesWithEvents) {
        this.eventColor = eventColor;
        this.datesWithEvents = datesWithEvents;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        // Verifique se a posição do dia atual tem um evento
        int dayOfMonth = parent.getChildAdapterPosition(view) + 1; // posição do filho + 1 = dia do mês
        if (datesWithEvents.contains(dayOfMonth)) {
            // Se houver um evento, adicione um espaçamento para baixo para a bolinha
            outRect.bottom = 20; // Espaçamento de 20 pixels (ajuste conforme necessário)
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

        // Desenhe a bolinha para os dias com eventos
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = parent.getChildAt(i);
            int dayOfMonth = parent.getChildAdapterPosition(view) + 1; // posição do filho + 1 = dia do mês
            if (datesWithEvents.contains(dayOfMonth)) {
                drawEventIndicator(c, view);
            }
        }
    }

    private void drawEventIndicator(Canvas canvas, View view) {
        // Determine as coordenadas para desenhar a bolinha
        int radius = 8; // Raio da bolinha (ajuste conforme necessário)
        int cx = view.getRight() - radius * 3; // Coordenada x do centro da bolinha
        int cy = view.getBottom() + radius * 2; // Coordenada y do centro da bolinha

        // Desenhe a bolinha
        Paint paint = new Paint();
        paint.setColor(eventColor);
        canvas.drawCircle(cx, cy, radius, paint);
    }
}