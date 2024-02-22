package com.example.esp_eventos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

public class CalendarIntegration {

    private final Calendar service;


    public CalendarIntegration(Context context) {
        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                        context, Collections.singleton(CalendarScopes.CALENDAR))
                .setBackOff(new ExponentialBackOff()); // Opcional, para tratamento de erros
        this.service = new Calendar.Builder(
                AndroidHttp.newCompatibleTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName("com.example.esp_eventos")
                .build();
    }

    public void createEvent(String title, String description, String location,
                            String dataEventoInicial, String dataEventoFinal,
                            String horaEventoInicial, String horaEventoFinal) {
        // Converter os campos de data e hora para objetos DateTime
        DateTime startDateTime = convertToDateTime(dataEventoInicial, horaEventoInicial);
        DateTime endDateTime = convertToDateTime(dataEventoFinal, horaEventoFinal);

        Event event = new Event()
                .setSummary(title)
                .setDescription(description)
                .setLocation(location);

        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone(TimeZone.getDefault().getID());
        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone(TimeZone.getDefault().getID());
        event.setEnd(end);

        try {
            Event createdEvent = service.events().insert("primary", event).execute();
            Log.d("CalendarIntegration", "Event created: " + createdEvent.getId());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("CalendarIntegration", "Error creating event: " + e.getMessage());
        }
    }

    private DateTime convertToDateTime(String date, String time) {
        try {
            String dateTimeString = date + " " + time;
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date parsedDate = dateFormat.parse(dateTimeString);

            return new DateTime(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Ou tratar o erro de outra forma, conforme necessário
        }
    }

}
