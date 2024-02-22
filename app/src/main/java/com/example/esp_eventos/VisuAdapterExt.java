package com.example.esp_eventos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class VisuAdapterExt extends RecyclerView.Adapter<VisuAdapterExt.MyViewHolder> {
    private List<EventosExterno> listaEventosExt;
    private List<String> listIdsExt;
    private Context context2;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm");

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredListExt(List<EventosExterno> filteredList, List<String> filteredListIdsExt){
        //this.listaEventos.clear();
        this.listaEventosExt = filteredList;
        //this.listIds.clear();
        this.listIdsExt = filteredListIdsExt;
        notifyDataSetChanged();
    }
    private int compareDatesOnly(String date1, String date2) {
        try {
            Date dateObject1 = dateFormat.parse(date1);
            Date dateObject2 = dateFormat.parse(date2);

            return dateObject1.compareTo(dateObject2);
        } catch (ParseException e) {
            Log.e("DateParsingError", "Error parsing dates: " + e.getMessage());
            return 0;
        }
    }
    public void removerEvento(int position) {
        if(position < listaEventosExt.size()){
            listaEventosExt.remove(position);
            notifyItemRemoved(position);
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void ordenarPorDataInicialProxima() {
        listaEventosExt.sort((evento1, evento2) -> -compareDatesOnly(evento1.getDataInicialEventoExterno(), evento2.getDataInicialEventoExterno()));

        notifyDataSetChanged();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void ordenarPorDataInicialPassada() {
        listaEventosExt.sort((evento1, evento2) -> compareDatesOnly(evento1.getDataInicialEventoExterno(), evento2.getDataInicialEventoExterno()));

        notifyDataSetChanged();
    }

    //TESTES DE ORDERNAÇÃO DE EVENTO
    public void ordenarPorEventosHoje() {
        Calendar hoje = Calendar.getInstance();
        hoje.set(Calendar.HOUR_OF_DAY, 0);
        hoje.set(Calendar.MINUTE, 0);
        hoje.set(Calendar.SECOND, 0);
        hoje.set(Calendar.MILLISECOND, 0);

        Calendar amanha = (Calendar) hoje.clone();
        amanha.add(Calendar.DAY_OF_YEAR, 1);

        ordenarPorPeriodo(hoje.getTime(), amanha.getTime());
    }

    public void ordenarPorEventosProximos7Dias() {
        Calendar seteDiasAtras = Calendar.getInstance();
        seteDiasAtras.add(Calendar.DAY_OF_YEAR, 7);

        ordenarPorPeriodo(new Date(), seteDiasAtras.getTime());
    }

    public void ordenarPorEventosProximos30Dias() {
        Calendar trintaDiasAtras = Calendar.getInstance();
        trintaDiasAtras.add(Calendar.DAY_OF_YEAR, 30);

        ordenarPorPeriodo(new Date(), trintaDiasAtras.getTime());
    }

    public void ordenarPorEventosPersonalizados(Date dataInicial, Date dataFinal) {
        ordenarPorPeriodo(dataInicial, dataFinal);
    }
    @SuppressLint("NotifyDataSetChanged")
    private void ordenarPorPeriodo(final Date dataInicial, final Date dataFinal) {
        List<EventosExterno> eventosNoPeriodo = new ArrayList<>();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (EventosExterno eventosExterno : listaEventosExt) {
            try {
                Date dataEvento = dateFormat.parse(eventosExterno.getDataInicialEventoExterno() + " " + eventosExterno.getHoraInicialEventoExterno());
                Date dataEventoFinal = dateFormat.parse(eventosExterno.getDataFinalEventoExterno() + " " + eventosExterno.getHoraFinalEventoExterno());

                // Verifique se a data do evento está dentro do período especificado
                if (dataEvento != null && dataEvento.after(dataInicial) && dataEvento.before(dataFinal)) {
                    eventosNoPeriodo.add(eventosExterno);
                }else if(dataEventoFinal != null && dataEventoFinal.after(dataInicial) && dataEventoFinal.before(dataFinal)){
                    eventosNoPeriodo.add(eventosExterno);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Classifique a lista de eventos no período
        Collections.sort(eventosNoPeriodo, new Comparator<EventosExterno>() {
            @Override
            public int compare(EventosExterno evento1, EventosExterno evento2) {
                Date dataEvento1, dataEvento2;
                try {
                    dataEvento1 = dateFormat.parse(evento1.getDataInicialEventoExterno() + " " + evento1.getHoraInicialEventoExterno());
                    dataEvento2 = dateFormat.parse(evento2.getDataFinalEventoExterno() + " " + evento2.getHoraFinalEventoExterno());
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0; // Trate erros de parse como iguais
                }

                if (dataEvento1 != null && dataEvento2 != null) {
                    return dataEvento1.compareTo(dataEvento2);
                }

                return 0; // Trate datas nulas como iguais
            }
        });

        // Atualize seu RecyclerView com a nova lista de eventos no período
        setListaEventosExt(eventosNoPeriodo);
        notifyDataSetChanged();
    }

    public void setListaEventosExt(List<EventosExterno> filteredListExt){
        this.listaEventosExt = filteredListExt;
    }
    public void setListIds(List<String> listIdsExt) {
        this.listIdsExt = listIdsExt;
    }

    public void ordenarPorDataMaisRecente() {
        Collections.sort(listaEventosExt, (evento1, evento2) -> {
            Timestamp dataAdicaoext1 = evento1.getDataAdicaoext();
            Timestamp dataAdicaoext2 = evento2.getDataAdicaoext();

            if (dataAdicaoext1 != null && dataAdicaoext2 != null) {
                return dataAdicaoext2.compareTo(dataAdicaoext1);
            } else if (dataAdicaoext1 == null && dataAdicaoext2 != null) {
                return 1;
            } else if (dataAdicaoext1 != null && dataAdicaoext2 == null) {
                return -1;
            } else {
                return 0;
            }
        });

        notifyDataSetChanged();
    }

    public VisuAdapterExt(List<EventosExterno> listaEventosExt, List<String> listIdsExt){
        this.listaEventosExt = listaEventosExt;
        this.listIdsExt = listIdsExt;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context2 = parent.getContext();
        View itemListaExt = LayoutInflater.from(context2).inflate(R.layout.lista_eventos_externos,parent, false);
        return new MyViewHolder(itemListaExt);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,@SuppressLint("RecyclerView") int position) {

        if(listaEventosExt.isEmpty() || position >= listaEventosExt.size()){
            return;
        }

        EventosExterno eventosExterno = listaEventosExt.get(position);
        String eventoExtId = listIdsExt.get(position);

        holder.nomeext.setText(eventosExterno.getNomedoEventoExterno());
        holder.descricaoext.setText(eventosExterno.getDescricaoEventoExterno());
        holder.datainicialext.setText(eventosExterno.getDataInicialEventoExterno());
        holder.datafinalext.setText(eventosExterno.getDataFinalEventoExterno());
        holder.horarioinicialext.setText(eventosExterno.getHoraInicialEventoExterno());
        holder.horariofinalext.setText(eventosExterno.getHoraFinalEventoExterno());
        holder.localext.setText(eventosExterno.getLocalEventoExterno());
        holder.itemView.setOnClickListener(v -> {
            DocumentReference infoEventoExtRef = FirebaseFirestore.getInstance().collection("infoEventosExt").document(eventoExtId);
            infoEventoExtRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot infoEventoExtSnapshot = task.getResult();
                    if (infoEventoExtSnapshot.exists()) {
                        String observacoesEventoExterno = infoEventoExtSnapshot.getString("observacoesEventoExterno");
                        String participantesEventoExterno = infoEventoExtSnapshot.getString("participantesEventoExterno");
                        String setorResponsavelEventoExterno = infoEventoExtSnapshot.getString("setorResponsavelEventoExterno");
                        String equipamentosEventoExterno = infoEventoExtSnapshot.getString("equipamentosEventoExterno");
                        String quaisequipamentosEventoExterno = infoEventoExtSnapshot.getString("quaisequipamentosEventoExterno");
                        String temTransmissaoEventoExterno = infoEventoExtSnapshot.getString("temtransmissaoEventoExterno");
                        String transmissaoEventoExterno = infoEventoExtSnapshot.getString("temTransmissaoTipoEventoExterno");
                        String gravacaoEventoExterno = infoEventoExtSnapshot.getString("temGravacaoTipoEventoExterno");
                        String ambosEventoExterno = infoEventoExtSnapshot.getString("temAmbosTipoEventoExterno");
                        String presencialEventoExterno = infoEventoExtSnapshot.getString("presencialEventoExterno");
                        String hibridoEventoExterno = infoEventoExtSnapshot.getString("hibridoEventoExterno");
                        String onlineEventoExterno = infoEventoExtSnapshot.getString("onlineEventoExterno");
                        String linkTransmissaoEventoExterno = infoEventoExtSnapshot.getString("linkTransmissaoEventoExterno");

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseUser currentUser = mAuth.getCurrentUser();

                        if (currentUser != null) {
                            // Verificar se o usuário atual é um administrador
                            String userId = currentUser.getUid();
                            db.collection("Info_Usuarios").document(userId).get()
                                    .addOnCompleteListener(task45 -> {
                                        if (task45.isSuccessful()) {
                                            DocumentSnapshot document = task45.getResult();
                                            if (document.exists()) {
                                                String userType = document.getString("Tipo");
                                                if ("admin".equals(userType)) {
                                                    // Habilitar o CheckBox para administradores
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(context2,R.style.AlertDialogTheme);
                                                    View view = LayoutInflater.from(context2).inflate(
                                                            R.layout.layout_admin_informacao_evento_dialog,null);
                                                    builder.setView(view);
                                                    ((TextView) view.findViewById(R.id.textTitleAlertDialog)).setText(eventosExterno.getNomedoEventoExterno());
                                                    ((TextView) view.findViewById(R.id.textMessage)).setText(
                                                            "Descrição da Atividade: " + eventosExterno.getDescricaoEventoExterno() +
                                                            "\n\nLocal de Evento: " + eventosExterno.getLocalEventoExterno() +
                                                            "\n\nHora Inicial: " + eventosExterno.getHoraInicialEventoExterno() +
                                                            "\n\nHora Final: " + eventosExterno.getHoraFinalEventoExterno() +
                                                            "\n\nData Inicial: " + eventosExterno.getDataInicialEventoExterno() +
                                                            "\n\nData Final: " + eventosExterno.getDataFinalEventoExterno() +
                                                            "\n\nObservações: " + observacoesEventoExterno +
                                                            "\n\nNúmero de Participantes: " + participantesEventoExterno +
                                                            "\n\nSetor Responsável: " + setorResponsavelEventoExterno);
                                                    if(Objects.equals(equipamentosEventoExterno, "Sim")){
                                                        ((TextView) view.findViewById(R.id.textMessage1)).setText(
                                                                "\n\nEquipamentos de áudio de vídeo: " + equipamentosEventoExterno +
                                                                "\n\nEquipamentos: " + quaisequipamentosEventoExterno);
                                                    } else{
                                                            ((TextView) view.findViewById(R.id.textMessage1)).setText(
                                                                    "\n\nEquipamentos de áudio de vídeo: " + "Nao");
                                                    }
                                                    if(Objects.equals(temTransmissaoEventoExterno, "Sim") && Objects.equals(transmissaoEventoExterno, "Sim")){
                                                        ((TextView) view.findViewById(R.id.textMessage2)).setText(
                                                                "\n\nHaverá transmissão/ gravação: " + temTransmissaoEventoExterno +
                                                                "\n\nTransmissão: " + transmissaoEventoExterno);
                                                    } else {
                                                        if (Objects.equals(temTransmissaoEventoExterno, "Sim") && Objects.equals(gravacaoEventoExterno, "Sim")){
                                                            ((TextView) view.findViewById(R.id.textMessage2)).setText(
                                                                    "\n\nHaverá transmissão/ gravação: " + temTransmissaoEventoExterno +
                                                                    "\n\nGravação: " + gravacaoEventoExterno);
                                                        } else{
                                                            if (Objects.equals(temTransmissaoEventoExterno, "Sim") && Objects.equals(ambosEventoExterno, "Sim")){
                                                                ((TextView) view.findViewById(R.id.textMessage2)).setText(
                                                                        "\n\nHaverá transmissão/ gravação: " + temTransmissaoEventoExterno +
                                                                        "\n\nTransmissão e gravação: " + ambosEventoExterno);
                                                            } else{
                                                                ((TextView) view.findViewById(R.id.textMessage2)).setText(
                                                                        "\n\nHaverá transmissão/ gravação: " + temTransmissaoEventoExterno);
                                                            }
                                                        }
                                                    }
                                                    if(Objects.equals(presencialEventoExterno, "Sim")){
                                                        ((TextView) view.findViewById(R.id.textMessage3)).setText(
                                                                "\n\nPresencial: " + presencialEventoExterno);
                                                    } else {
                                                        if (Objects.equals(hibridoEventoExterno, "Sim") && Objects.equals(linkTransmissaoEventoExterno, "Sim")) {
                                                            ((TextView) view.findViewById(R.id.textMessage3)).setText(
                                                                    "\n\nHibrído: " + hibridoEventoExterno +
                                                                    "\n\nVai ter link: " + linkTransmissaoEventoExterno);
                                                        } else {
                                                            if (Objects.equals(onlineEventoExterno, "Sim") && Objects.equals(linkTransmissaoEventoExterno, "Sim")) {
                                                                ((TextView) view.findViewById(R.id.textMessage3)).setText(
                                                                        "\n\nOnline: " + onlineEventoExterno +
                                                                        "\n\nVai ter link: " + linkTransmissaoEventoExterno);
                                                            }
                                                        }
                                                    }
                                                    ((Button) view.findViewById(R.id.buttoneditar)).setText("Editar");
                                                    ((Button) view.findViewById(R.id.buttonexcluir)).setText("Excluir");
                                                    ((Button) view.findViewById(R.id.buttonvoltar)).setText("Voltar");

                                                    final AlertDialog alertDialog = builder.create();

                                                    view.findViewById(R.id.buttonvoltar).setOnClickListener(view13 -> alertDialog.dismiss());

                                                    view.findViewById(R.id.buttonexcluir).setOnClickListener(view12 -> {

                                                        alertDialog.dismiss();

                                                        AlertDialog.Builder builderExcluir = new AlertDialog.Builder(context2,R.style.AlertDialogTheme);
                                                        View viewExcluir = LayoutInflater.from(context2).inflate(
                                                                R.layout.layout_alerta_dialog,null);

                                                        builderExcluir.setView(viewExcluir);
                                                        ((TextView) viewExcluir.findViewById(R.id.textTitleAlertDialog)).setText("Aviso");
                                                        ((TextView) viewExcluir.findViewById(R.id.textMessage)).setText("Você deseja realmente excluir esse evento ?");
                                                        ((Button) viewExcluir.findViewById(R.id.buttonYes)).setText("Sim");
                                                        ((Button) viewExcluir.findViewById(R.id.buttonNo)).setText("Não");
                                                        ((ImageView) viewExcluir.findViewById(R.id.imageIcon)).setImageResource(R.drawable.aviso);

                                                        final AlertDialog alertDialog1 = builderExcluir.create();

                                                        viewExcluir.findViewById(R.id.buttonYes).setOnClickListener(view15 -> {
                                                            alertDialog1.dismiss();
                                                            EventosExterno eventosExterno1 = listaEventosExt.get(holder.getAdapterPosition());
                                                            String eventosId = eventosExterno1.geteventosExtId();
                                                            Log.d("Excluir", "Excluir evento com o eventoId: " + eventosId);
                                                            excluirEvento(eventosId,position);
                                                        });

                                                        viewExcluir.findViewById(R.id.buttonNo).setOnClickListener(view14 -> {
                                                            alertDialog1.dismiss();
                                                            alertDialog.show();

                                                        });

                                                        if (alertDialog1.getWindow() != null){
                                                            alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                                                        }
                                                        alertDialog1.show();
                                                    });

                                                    view.findViewById(R.id.buttoneditar).setOnClickListener(view1 -> {
                                                        EventosExterno eventosExtern = listaEventosExt.get(holder.getAdapterPosition());
                                                        String eventosExtId = eventosExtern.geteventosExtId();

                                                        Intent intentEdicaoFormExt = new Intent(v.getContext(), FormCadastroEventoExterno.class);
                                                        intentEdicaoFormExt.putExtra("eventosExtId", eventosExtId);
                                                        Log.d("Debug 2", "Nenhum documento encontrado com o eventoId: " + eventosExtId);
                                                        v.getContext().startActivity(intentEdicaoFormExt);
                                                    });

                                                    if (alertDialog.getWindow() != null){
                                                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                                                    }
                                                    alertDialog.show();
                                                } else{
                                                    //add aqui os normais
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(context2,R.style.AlertDialogTheme);
                                                    View view = LayoutInflater.from(context2).inflate(
                                                            R.layout.layout_informacao_evento_dialog,null);
                                                    builder.setView(view);
                                                    ((TextView) view.findViewById(R.id.textTitleAlertDialog)).setText(eventosExterno.getNomedoEventoExterno());
                                                    ((TextView) view.findViewById(R.id.textMessage)).setText(
                                                            "Descrição da Atividade: " + eventosExterno.getDescricaoEventoExterno() +
                                                            "\n\nLocal de Evento: " + eventosExterno.getLocalEventoExterno() +
                                                            "\n\nHora Inicial: " + eventosExterno.getHoraInicialEventoExterno() +
                                                            "\n\nHora Final: " + eventosExterno.getHoraFinalEventoExterno() +
                                                            "\n\nData Inicial: " + eventosExterno.getDataInicialEventoExterno() +
                                                            "\n\nData Final: " + eventosExterno.getDataFinalEventoExterno() +
                                                            "\n\nObservações: " + observacoesEventoExterno +
                                                            "\n\nNúmero de Participantes: " + participantesEventoExterno +
                                                            "\n\nSetor Responsável: " + setorResponsavelEventoExterno);

                                                    if(Objects.equals(equipamentosEventoExterno, "Sim")){
                                                        ((TextView) view.findViewById(R.id.textMessage1)).setText(
                                                                "\n\nEquipamentos de áudio de vídeo: " + equipamentosEventoExterno +
                                                                        "\n\nEquipamentos: " + quaisequipamentosEventoExterno);
                                                    } else{
                                                        ((TextView) view.findViewById(R.id.textMessage1)).setText(
                                                                "\n\nEquipamentos de áudio de vídeo: " + "Nao");
                                                    }
                                                    if(Objects.equals(temTransmissaoEventoExterno, "Sim") && Objects.equals(transmissaoEventoExterno, "Sim")){
                                                        ((TextView) view.findViewById(R.id.textMessage2)).setText(
                                                                "\n\nHaverá transmissão/ gravação: " + temTransmissaoEventoExterno +
                                                                "\n\nTransmissão: " + transmissaoEventoExterno);
                                                    } else {
                                                        if (Objects.equals(temTransmissaoEventoExterno, "Sim") && Objects.equals(gravacaoEventoExterno, "Sim")){
                                                            ((TextView) view.findViewById(R.id.textMessage2)).setText(
                                                                    "\n\nHaverá transmissão/ gravação: " + temTransmissaoEventoExterno +
                                                                    "\n\nGravação: " + gravacaoEventoExterno);
                                                        } else{
                                                            if (Objects.equals(temTransmissaoEventoExterno, "Sim") && Objects.equals(ambosEventoExterno, "Sim")){
                                                                ((TextView) view.findViewById(R.id.textMessage2)).setText(
                                                                        "\n\nHaverá transmissão/ gravação: " + temTransmissaoEventoExterno +
                                                                        "\n\nTransmissão e gravação: " + ambosEventoExterno);
                                                            } else{
                                                                ((TextView) view.findViewById(R.id.textMessage2)).setText(
                                                                        "\n\nHaverá transmissão/ gravação: " + temTransmissaoEventoExterno);
                                                            }
                                                        }
                                                    }
                                                    if(Objects.equals(presencialEventoExterno, "Sim")){
                                                        ((TextView) view.findViewById(R.id.textMessage3)).setText(
                                                                "\n\nPresencial: " + presencialEventoExterno);
                                                    } else {
                                                        if (Objects.equals(hibridoEventoExterno, "Sim") && Objects.equals(linkTransmissaoEventoExterno, "Sim")) {
                                                            ((TextView) view.findViewById(R.id.textMessage3)).setText(
                                                                    "\n\nHibrído: " + hibridoEventoExterno +
                                                                    "\n\nVai ter link: " + linkTransmissaoEventoExterno);
                                                        } else {
                                                            if (Objects.equals(onlineEventoExterno, "Sim") && Objects.equals(linkTransmissaoEventoExterno, "Sim")) {
                                                                ((TextView) view.findViewById(R.id.textMessage3)).setText(
                                                                        "\n\nOnline: " + onlineEventoExterno +
                                                                        "\n\nVai ter link: " + linkTransmissaoEventoExterno);
                                                            }
                                                        }
                                                    }

                                                    ((Button) view.findViewById(R.id.buttonAction)).setText("Voltar");

                                                    final AlertDialog alertDialog = builder.create();

                                                    view.findViewById(R.id.buttonAction).setOnClickListener(view13 -> alertDialog.dismiss());

                                                    if (alertDialog.getWindow() != null){
                                                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                                                    }
                                                    alertDialog.show();
                                                }
                                            }
                                        }
                                    });
                        }

                    } else {
                        Log.d("FormVisualizacao", "O documento infoEventos não existe para o evento com ID: " + listIdsExt);
                    }
                } else {
                    Log.e("FormVisualizacao", "Erro ao obter dados do infoEventos: " + task.getException());
                }
            });
        });
        // Converte as datas e horários em strings para objetos Date
        Date dataAtual = new Date();
        Date dataInicioEvento = null;
        Date dataFimEvento = null;
        try {
            dataInicioEvento = dateFormat.parse(eventosExterno.getDataInicialEventoExterno());
            dataFimEvento = dateFormat.parse(eventosExterno.getDataFinalEventoExterno());
            Date horaInicio = horaFormat.parse(eventosExterno.getHoraInicialEventoExterno());
            Date horaFim = horaFormat.parse(eventosExterno.getHoraFinalEventoExterno());

            dataInicioEvento = sumTimeToDate(dataInicioEvento, horaInicio);
            dataFimEvento = sumTimeToDate(dataFimEvento, horaFim);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dataInicioEvento != null && dataFimEvento != null) {
            if (dataAtual.after(dataInicioEvento) && dataAtual.before(dataFimEvento)) {
                holder.tagAovivoExt.setVisibility(View.VISIBLE); // Mostra a tag "Ao Vivo"
                holder.tagExpiradoExt.setVisibility(View.GONE); // Esconde a tag "Expirado"
                holder.tagEmBreveExt.setVisibility(View.GONE); // Esconde a tag "Em breve"
            } else if (dataAtual.after(dataFimEvento)) {
                holder.tagExpiradoExt.setVisibility(View.VISIBLE); // Mostra a tag "Expirado"
                holder.tagAovivoExt.setVisibility(View.GONE); // Esconde a tag "Ao Vivo"
                holder.tagEmBreveExt.setVisibility(View.GONE); // Esconde a tag "Em breve"
            } else {
                holder.tagEmBreveExt.setVisibility(View.VISIBLE); // Mostra a tag "Em breve"
                holder.tagAovivoExt.setVisibility(View.GONE); // Esconde a tag "Ao Vivo"
                holder.tagExpiradoExt.setVisibility(View.GONE); // Esconde a tag "Expirado"
            }
        }

    }
    private Date sumTimeToDate(Date date, Date time) {
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);

        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(time);

        calendarDate.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
        calendarDate.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));

        return calendarDate.getTime();
    }
    private void excluirEvento(String eventId, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Primeiro, exclua o documento da coleção "eventos"
        Query query = db.collection("eventosExt").whereEqualTo("eventosExtId", eventId);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                    documentSnapshot.getReference().delete()
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Debug", "Evento excluído com sucesso da coleção 'eventos'");

                                // Em seguida, exclua o documento correspondente da coleção "infoEventosExt"
                                DocumentReference infoEventoRef = db.collection("infoEventosExt").document(eventId);
                                infoEventoRef.delete()
                                        .addOnSuccessListener(aVoid1 -> {
                                            Log.d("Debug", "Evento excluído com sucesso da coleção 'infoEventos'");

                                            VisuAdapterExt.this.removerEvento(position);
                                            // Exiba uma mensagem de sucesso para o usuário
                                            //Toast.makeText(this, "Evento excluído com sucesso!", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("FormCadastroEvento", "Erro ao excluir evento da coleção 'infoEventos'", e);
                                            //showErrorAlertDilog(mensagens[0], "Erro ao excluir o evento");
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Log.e("FormCadastroEvento", "Erro ao excluir evento da coleção 'eventos'", e);
                                //showErrorAlertDilog(mensagens[0], "Erro ao excluir o evento");
                            });
                } else {
                    Log.d("Debug", "Nenhum documento encontrado com o eventoId: " + eventId);
                }
            } else {
                Log.e("FormCadastroEvento", "Erro ao buscar o evento para exclusão", task.getException());
                //showErrorAlertDilog(mensagens[0], "Erro ao excluir o evento");
            }
        });
    }
    @Override
    public int getItemCount() {
        return listaEventosExt.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private final TextView nomeext;
        private final TextView descricaoext;
        private final TextView datainicialext;
        private final TextView datafinalext;
        private final  TextView horarioinicialext;
        private final TextView horariofinalext;
        private final TextView  localext;
        private final TextView tagExpiradoExt;
        private final TextView tagEmBreveExt;
        private final TextView tagAovivoExt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            nomeext = itemView.findViewById(R.id.txtnomeext);
            descricaoext = itemView.findViewById(R.id.txtdescricaoext);
            datainicialext = itemView.findViewById(R.id.txtdatainicialext);
            datafinalext = itemView.findViewById(R.id.txtdatafinalext);
            horarioinicialext = itemView.findViewById(R.id.txthorarioinicialext);
            horariofinalext = itemView.findViewById(R.id.txthorariofinalext);
            localext = itemView.findViewById(R.id.txtlocalext);
            tagExpiradoExt = itemView.findViewById(R.id.tagExpiradoExt);
            tagEmBreveExt = itemView.findViewById(R.id.tagEmBreveExt);
            tagAovivoExt = itemView.findViewById(R.id.tagAovivoExt);
        }
    }
}
