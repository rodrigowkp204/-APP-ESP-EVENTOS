package com.example.esp_eventos;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DadosFirestore {

    public interface FirestoreDataListener {
        void onDataLoaded(byte[] data);
    }

    public static void fetchFirestoreData(FirestoreDataListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("eventos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<EventData> eventDataList = new ArrayList<>();
                    AtomicInteger count = new AtomicInteger(0);

                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String eventoId = documentSnapshot.getString("eventosId");

                        db.collection("infoEventos").document(eventoId)
                                .get()
                                .addOnCompleteListener(infoEventosTask -> {
                                    if (infoEventosTask.isSuccessful()) {
                                        DocumentSnapshot infoEventoSnapshot = infoEventosTask.getResult();
                                        if (infoEventoSnapshot.exists()) {
                                            EventData eventData = new EventData();
                                            eventData.nomedoEvento = documentSnapshot.getString("nomedoEvento");
                                            eventData.datadoEvento = documentSnapshot.getString("datadoEvento");
                                            eventData.datadoEventoFinal = documentSnapshot.getString("datadoEventoFinal");
                                            eventData.horadoEventoInicial = documentSnapshot.getString("horadoEventoInicial");
                                            eventData.horaEventoFinal = documentSnapshot.getString("horaEventoFinal");
                                            eventData.localdoEvento = documentSnapshot.getString("localdoEvento");
                                            eventData.descricaodaAtividade = documentSnapshot.getString("descricaodaAtividade");
                                            eventData.observacoes = infoEventoSnapshot.getString("observacoes");
                                            eventData.participantes = infoEventoSnapshot.getString("numerosdeParticipantes");
                                            eventData.setor = infoEventoSnapshot.getString("setorResponsavel");

                                            eventDataList.add(eventData);

                                            count.incrementAndGet();
                                            if (count.get() == queryDocumentSnapshots.size()) {
                                                createXLSXFile(eventDataList, listener);
                                            }
                                        } else {
                                            // Documento info_eventos não existe
                                            // Aqui você pode decidir como lidar com essa situação
                                            Log.d("DadosFirestore", "Documento info_eventos não existe para o evento com ID: " + eventoId);
                                        }
                                    } else {
                                        // Erro ao obter dados do info_eventos
                                        // Aqui você pode decidir como lidar com essa situação
                                        Log.e("DadosFirestore", "Erro ao obter dados do info_eventos: " + infoEventosTask.getException());
                                    }
                                });
                    }
                });
    }

    private static void createXLSXFile(List<EventData> eventDataList, FirestoreDataListener listener) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Relatório de Eventos Internos");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Título do Evento");
            headerRow.createCell(1).setCellValue("Data Inicial do Evento");
            headerRow.createCell(2).setCellValue("Data Final do Evento");
            headerRow.createCell(3).setCellValue("Hora Inicial do Evento");
            headerRow.createCell(4).setCellValue("Hora Final do Evento");
            headerRow.createCell(5).setCellValue("Local do Evento");
            headerRow.createCell(6).setCellValue("Descrição do Evento");
            headerRow.createCell(7).setCellValue("Observações do Evento");
            headerRow.createCell(8).setCellValue("Número de participantes do Evento");
            headerRow.createCell(9).setCellValue("Setor Responsável do Evento");

            for (int i = 0; i < eventDataList.size(); i++) {
                EventData eventData = eventDataList.get(i);
                Row dataRow = sheet.createRow(i + 1);
                dataRow.createCell(0).setCellValue(eventData.nomedoEvento);
                dataRow.createCell(1).setCellValue(eventData.datadoEvento);
                dataRow.createCell(2).setCellValue(eventData.datadoEventoFinal);
                dataRow.createCell(3).setCellValue(eventData.horadoEventoInicial);
                dataRow.createCell(4).setCellValue(eventData.horaEventoFinal);
                dataRow.createCell(5).setCellValue(eventData.localdoEvento);
                dataRow.createCell(6).setCellValue(eventData.descricaodaAtividade);
                dataRow.createCell(7).setCellValue(eventData.observacoes);
                dataRow.createCell(8).setCellValue(eventData.participantes);
                dataRow.createCell(9).setCellValue(eventData.setor);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            listener.onDataLoaded(outputStream.toByteArray());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Lidar com erros aqui
            Log.e("DadosFirestore", "Erro ao criar o arquivo XLSX: " + e.getMessage());
        }
    }

    private static class EventData {
        String nomedoEvento;
        String datadoEvento;
        String datadoEventoFinal;
        String horadoEventoInicial;
        String horaEventoFinal;
        String localdoEvento;
        String descricaodaAtividade;
        String observacoes;
        String participantes;
        String setor;
    }
}
