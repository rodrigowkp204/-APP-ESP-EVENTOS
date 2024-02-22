package com.example.esp_eventos;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomCalendarDayView extends androidx.appcompat.widget.AppCompatTextView {

    private boolean hasEvent;

    public CustomCalendarDayView(Context context) {
        super(context);
        init();
    }

    public CustomCalendarDayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomCalendarDayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Configurações de aparência do TextView, como cor do texto, tamanho da fonte, etc.
        // Aqui você pode personalizar a aparência do dia do calendário

        // Defina o clique do TextView para tratar a seleção do dia
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aqui você pode implementar a lógica para lidar com o clique em um dia do calendário
            }
        });
    }

    public void setHasEvent(boolean hasEvent) {
        this.hasEvent = hasEvent;
        invalidate(); // Força a recriação da visualização
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Se houver um evento para este dia, desenhe uma bolinha abaixo do número
        if (hasEvent) {
            // Desenhe a bolinha
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            int radius = 8; // Raio da bolinha (ajuste conforme necessário)
            int cx = getWidth() / 2; // Coordenada x do centro da bolinha
            int cy = getHeight() + radius * 2; // Coordenada y do centro da bolinha
            canvas.drawCircle(cx, cy, radius, paint);
        }
    }
}
