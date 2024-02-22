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

public class DadosFirestoreExterno {

    public interface FirestoreDataExternoListener {
        void onDataLoaded(byte[] data);
    }

    public static void fetchFirestoreDataExt(DadosFirestoreExterno.FirestoreDataExternoListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("eventosExt")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<EventExtData> eventExtDataList = new ArrayList<>();
                    AtomicInteger count = new AtomicInteger(0);

                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String eventoExtId = documentSnapshot.getString("eventosExtId");

                        db.collection("infoEventosExt").document(eventoExtId)
                                .get()
                                .addOnCompleteListener(infoEventosTask -> {
                                    if (infoEventosTask.isSuccessful()) {
                                        DocumentSnapshot infoEventoSnapshot = infoEventosTask.getResult();
                                        if (infoEventoSnapshot.exists()) {
                                            EventExtData eventExtData = new EventExtData();
                                            eventExtData.nomedoEventoExterno = documentSnapshot.getString("nomedoEventoExterno");
                                            eventExtData.dataInicialEventoExterno = documentSnapshot.getString("dataInicialEventoExterno");
                                            eventExtData.dataFinalEventoExterno = documentSnapshot.getString("dataFinalEventoExterno");
                                            eventExtData.horaInicialEventoExterno = documentSnapshot.getString("horaInicialEventoExterno");
                                            eventExtData.horaFinalEventoExterno = documentSnapshot.getString("horaFinalEventoExterno");
                                            eventExtData.localEventoExterno = documentSnapshot.getString("localEventoExterno");
                                            eventExtData.descricaoEventoExterno = documentSnapshot.getString("descricaoEventoExterno");
                                            eventExtData.observacoesEventoExterno = infoEventoSnapshot.getString("observacoesEventoExterno");
                                            eventExtData.participantesEventoExterno = infoEventoSnapshot.getString("participantesEventoExterno");
                                            eventExtData.setorResponsavelEventoExterno = infoEventoSnapshot.getString("setorResponsavelEventoExterno");

                                            eventExtDataList.add(eventExtData);

                                            count.incrementAndGet();
                                            if (count.get() == queryDocumentSnapshots.size()) {
                                                createXLSXFileExt(eventExtDataList, listener);
                                            }
                                        } else {
                                            // Documento info_eventos não existe
                                            // Aqui você pode decidir como lidar com essa situação
                                            Log.d("DadosFirestoreExterno", "Documento info_eventos não existe para o evento com ID: " + eventoExtId);
                                        }
                                    } else {
                                        // Erro ao obter dados do info_eventos
                                        // Aqui você pode decidir como lidar com essa situação
                                        Log.e("DadosFirestoreExterno", "Erro ao obter dados do info_eventos: " + infoEventosTask.getException());
                                    }
                                });
                    }
                });
    }

    private static void createXLSXFileExt(List<DadosFirestoreExterno.EventExtData> eventExtDataList, DadosFirestoreExterno.FirestoreDataExternoListener listener) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Relatório de Eventos Externo");

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

            for (int i = 0; i < eventExtDataList.size(); i++) {
                DadosFirestoreExterno.EventExtData eventData = eventExtDataList.get(i);
                Row dataRow = sheet.createRow(i + 1);
                dataRow.createCell(0).setCellValue(eventData.nomedoEventoExterno);
                dataRow.createCell(1).setCellValue(eventData.dataInicialEventoExterno);
                dataRow.createCell(2).setCellValue(eventData.dataFinalEventoExterno);
                dataRow.createCell(3).setCellValue(eventData.horaInicialEventoExterno);
                dataRow.createCell(4).setCellValue(eventData.horaFinalEventoExterno);
                dataRow.createCell(5).setCellValue(eventData.localEventoExterno);
                dataRow.createCell(6).setCellValue(eventData.descricaoEventoExterno);
                dataRow.createCell(7).setCellValue(eventData.observacoesEventoExterno);
                dataRow.createCell(8).setCellValue(eventData.participantesEventoExterno);
                dataRow.createCell(9).setCellValue(eventData.setorResponsavelEventoExterno);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            listener.onDataLoaded(outputStream.toByteArray());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Lidar com erros aqui
            Log.e("DadosFirestoreExterno", "Erro ao criar o arquivo XLSX: " + e.getMessage());
        }
    }

    private static class EventExtData {
        String nomedoEventoExterno;
        String localEventoExterno;
        String horaInicialEventoExterno;
        String horaFinalEventoExterno;
        String descricaoEventoExterno;
        String dataInicialEventoExterno;
        String dataFinalEventoExterno;
        String observacoesEventoExterno;
        String participantesEventoExterno;
        String setorResponsavelEventoExterno;
    }
}
