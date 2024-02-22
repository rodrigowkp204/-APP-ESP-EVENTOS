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

public class VisuAdapter extends RecyclerView.Adapter<VisuAdapter.MyViewHolder> {

    private List<Eventos> listaEventos;
    private List<String> listIds;
    private Context context;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm");

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredList(List<Eventos> filteredList, List<String> filteredListIds){
        this.listaEventos = filteredList;
        this.listIds = filteredListIds;
        notifyDataSetChanged();
    }
    public VisuAdapter(List<Eventos> listaEventos, List<String> listIds) {
        this.listaEventos = listaEventos;
        this.listIds = listIds;
    }
    public void removerEvento(int position) {
        if(position < listaEventos.size()){
            listaEventos.remove(position);
            notifyItemRemoved(position);
        }
    }
    @SuppressLint("NotifyDataSetChanged")

    public void ordenarPorDataMaisRecente() {
        listaEventos.sort((evento1, evento2) -> {
            Timestamp dataAdicao1 = evento1.getDataAdicao();
            Timestamp dataAdicao2 = evento2.getDataAdicao();

            if (dataAdicao1 != null && dataAdicao2 != null) {
                return dataAdicao2.compareTo(dataAdicao1);
            } else if (dataAdicao1 == null && dataAdicao2 != null) {
                return 1;
            } else if (dataAdicao1 != null && dataAdicao2 == null) {
                return -1;
            } else {
                return 0;
            }
        });
        setListaEventos(listaEventos);
        setListIds(listIds);
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
    @SuppressLint("NotifyDataSetChanged")
    public void ordenarPorDataInicialProxima() {
        listaEventos.sort((evento1, evento2) -> -compareDatesOnly(evento1.getdatadoEvento(), evento2.getdatadoEvento()));

        notifyDataSetChanged();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void ordenarPorDataInicialPassada() {
        listaEventos.sort((evento1, evento2) -> compareDatesOnly(evento1.getdatadoEvento(), evento2.getdatadoEvento()));

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
        List<Eventos> eventosNoPeriodo = new ArrayList<>();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (Eventos evento : listaEventos) {
            try {
                Date dataEvento = dateFormat.parse(evento.getdatadoEvento() + " " + evento.getHoradoEventoInicial());
                Date dataEventoFinal = dateFormat.parse(evento.getDatadoEventoFinal() + " " + evento.getHoraEventoFinal());

                // Verifique se a data do evento está dentro do período especificado
                if (dataEvento != null && dataEvento.after(dataInicial) && dataEvento.before(dataFinal)) {
                    eventosNoPeriodo.add(evento);
                }else if(dataEventoFinal != null && dataEventoFinal.after(dataInicial) && dataEventoFinal.before(dataFinal)){
                    eventosNoPeriodo.add(evento);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Classifique a lista de eventos no período
        Collections.sort(eventosNoPeriodo, new Comparator<Eventos>() {
            @Override
            public int compare(Eventos evento1, Eventos evento2) {
                Date dataEvento1, dataEvento2;
                try {
                    dataEvento1 = dateFormat.parse(evento1.getdatadoEvento() + " " + evento1.getHoradoEventoInicial());
                    dataEvento2 = dateFormat.parse(evento2.getDatadoEventoFinal() + " " + evento2.getHoraEventoFinal());
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
        setListaEventos(eventosNoPeriodo);
        notifyDataSetChanged();
    }

    public void setListaEventos(List<Eventos> listaEventos) {
        this.listaEventos = listaEventos;
    }
    public void setListIds(List<String> listIds) {
        this.listIds = listIds;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        View itemLista = LayoutInflater.from(context).inflate(R.layout.lista_eventos_1, parent, false);
        return new MyViewHolder(itemLista);
    }

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if(listaEventos.isEmpty() || position >= listaEventos.size()){
            return;
        }

        Eventos evento = listaEventos.get(position);
        String eventoId = listIds.get(position);
        String localdoevento = evento.getlocaldoEvento();

        if(localdoevento != null){
            switch (localdoevento) {
                case "Isabel dos Santos": {
                    Log.d("TAG", "Local do evento: Isabel dos Santos");
                    ImageView imageView = holder.itemView.findViewById(R.id.imglocal);
                    imageView.setImageResource(R.drawable.izabelmini);
                    break;
                }
                case "Paulo Freire": {
                    Log.d("TAG", "Local do evento: Paulo Freire");
                    ImageView imageView = holder.itemView.findViewById(R.id.imglocal);
                    imageView.setImageResource(R.drawable.paulomini);
                    break;
                }
                case "Multimídia 1": {
                    Log.d("TAG", "Local do evento: Multimídia 1");
                    ImageView imageView = holder.itemView.findViewById(R.id.imglocal);
                    imageView.setImageResource(R.drawable.multi1mini);
                    break;
                }
                case "Multimídia 2": {
                    Log.d("TAG", "Local do evento: Multimídia 2");
                    ImageView imageView = holder.itemView.findViewById(R.id.imglocal);
                    imageView.setImageResource(R.drawable.multi2mini);
                    break;
                }
                case "Multimídia 3": {
                    Log.d("TAG", "Local do evento: Multimídia 3");
                    ImageView imageView = holder.itemView.findViewById(R.id.imglocal);
                    imageView.setImageResource(R.drawable.multi2mini);
                    break;
                }
                case "Tecnologias Educacionais": {
                    Log.d("TAG", "Local do evento: Tecnologias Educacionais");
                    ImageView imageView = holder.itemView.findViewById(R.id.imglocal);
                    imageView.setImageResource(R.drawable.tecnologias_educacionais);
                    break;
                }
                case "Educação e Saúde": {
                    Log.d("TAG", "Local do evento: Educação e Saúde");
                    ImageView imageView = holder.itemView.findViewById(R.id.imglocal);
                    imageView.setImageResource(R.drawable.educacao_e_saude);
                    break;
                }
                case "Diretoria": {
                    Log.d("TAG", "Local do evento: Diretoria");
                    ImageView imageView = holder.itemView.findViewById(R.id.imglocal);
                    imageView.setImageResource(R.drawable.diretoria);
                    break;
                }
                case "Pesquisa e Desenvolvimento": {
                    Log.d("TAG", "Local do evento: Pesquisa e Desenvolvimento");
                    ImageView imageView = holder.itemView.findViewById(R.id.imglocal);
                    imageView.setImageResource(R.drawable.pesquisa_e_desenvolvimento);
                    break;
                }
                case "Estágio": {
                    Log.d("TAG", "Local do evento: Estágio");
                    ImageView imageView = holder.itemView.findViewById(R.id.imglocal);
                    imageView.setImageResource(R.drawable.estagio);
                    break;
                }
                case "Núcleo de Planejamento": {
                    Log.d("TAG", "Local do evento: Núcleo de Planejamento");
                    ImageView imageView = holder.itemView.findViewById(R.id.imglocal);
                    imageView.setImageResource(R.drawable.nucleo_de_planejamento);
                    break;
                }
                case "Sala de Reunião": {
                    Log.d("TAG", "Local do evento: Sala de Reunião");
                    ImageView imageView = holder.itemView.findViewById(R.id.imglocal);
                    imageView.setImageResource(R.drawable.sala_de_reuniao);
                    break;
                }
            }
        }
        holder.nome.setText(evento.getnomedoEvento());
        holder.data.setText(evento.getdatadoEvento());
        holder.datafinal.setText(evento.getDatadoEventoFinal());
        holder.horario.setText(evento.getHoradoEventoInicial());
        holder.horafinal.setText(evento.getHoraEventoFinal());
        holder.local.setText(evento.getlocaldoEvento());
        holder.desc.setText(evento.getdescricaodaAtividade());
        holder.itemView.setOnClickListener(v -> {
            DocumentReference infoEventoRef = FirebaseFirestore.getInstance().collection("infoEventos").document(eventoId);
            infoEventoRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot infoEventoSnapshot = task.getResult();
                    if (infoEventoSnapshot.exists()) {
                        String observacoes = infoEventoSnapshot.getString("observacoes");
                        String participantes = infoEventoSnapshot.getString("numerosdeParticipantes");
                        String setor = infoEventoSnapshot.getString("setorResponsavel");
                        String temTransmissao = infoEventoSnapshot.getString("temtransmissao");
                        String transmissao = infoEventoSnapshot.getString("temTransmissaoTipo");
                        String gravacao = infoEventoSnapshot.getString("temGravacaoTipo");
                        String ambos = infoEventoSnapshot.getString("temAmbosTipo");
                        String presencial = infoEventoSnapshot.getString("presencial");
                        String hibrido = infoEventoSnapshot.getString("hibrido");
                        String online = infoEventoSnapshot.getString("online");
                        String linkTransmissao = infoEventoSnapshot.getString("linkTransmissao");

                        Log.d("EVENTOID", "Registro do evento Id " + eventoId);

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
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogTheme);
                                                    View view = LayoutInflater.from(context).inflate(
                                                            R.layout.layout_admin_informacao_evento_dialog,null);
                                                    builder.setView(view);
                                                    ((TextView) view.findViewById(R.id.textTitleAlertDialog)).setText(evento.getnomedoEvento());
                                                    ((TextView) view.findViewById(R.id.textMessage)).setText(
                                                            "Descrição da Atividade: " + evento.getdescricaodaAtividade() +
                                                            "\n\nLocal de Evento: " + evento.getlocaldoEvento() +
                                                            "\n\nHora Inicial: " + evento.getHoradoEventoInicial() +
                                                            "\n\nHora Final: " + evento.getHoraEventoFinal() +
                                                            "\n\nData Inicial: " + evento.getdatadoEvento() +
                                                            "\n\nData Final: " + evento.getDatadoEventoFinal() +
                                                            "\n\nObservações: " + evento.getObservacoes() +
                                                            "\n\nNúmero de Participantes: " + evento.getNumerosdeParticipantes() +
                                                            "\n\nSetor Responsável: " + evento.getSetorResponsavel());
                                                    if(evento.getEquipamentos().equals("Sim")){
                                                        ((TextView) view.findViewById(R.id.textMessage1)).setText(
                                                                "\n\nEquipamentos de áudio de vídeo: " + evento.getEquipamentos() +
                                                                "\n\nEquipamentos: " + evento.getQuaisequipamentos());
                                                    } else{
                                                            ((TextView) view.findViewById(R.id.textMessage1)).setText(
                                                                    "\n\nEquipamentos de áudio de vídeo: " + "Nao" );
                                                    }

                                                    if(Objects.equals(temTransmissao, "Sim") && Objects.equals(transmissao, "Sim")){
                                                        ((TextView) view.findViewById(R.id.textMessage2)).setText(
                                                                "\n\nHaverá transmissão/ gravação: " + temTransmissao +
                                                                "\n\nTransmissão: " + transmissao);
                                                    } else {
                                                        if (Objects.equals(temTransmissao, "Sim") && Objects.equals(gravacao, "Sim")){
                                                            ((TextView) view.findViewById(R.id.textMessage2)).setText(
                                                                    "\n\nHaverá transmissão/ gravação: " + temTransmissao +
                                                                    "\n\nGravação: " + gravacao);
                                                        } else{
                                                            if (Objects.equals(temTransmissao, "Sim") && Objects.equals(ambos, "Sim")){
                                                                ((TextView) view.findViewById(R.id.textMessage2)).setText(
                                                                        "\n\nHaverá transmissão/ gravação: " + temTransmissao +
                                                                        "\n\nTransmissão e gravação: " + ambos);
                                                            } else{
                                                                ((TextView) view.findViewById(R.id.textMessage2)).setText(
                                                                        "\n\nHaverá transmissão/ gravação: " + temTransmissao);
                                                            }
                                                        }
                                                    }
                                                    if(Objects.equals(presencial, "Sim")){
                                                        ((TextView) view.findViewById(R.id.textMessage3)).setText(
                                                                "\n\nPresencial: " + presencial);
                                                    } else {
                                                        if (Objects.equals(hibrido, "Sim") && Objects.equals(linkTransmissao, "Sim")) {
                                                            ((TextView) view.findViewById(R.id.textMessage3)).setText(
                                                                "\n\nHibrído: " + hibrido +
                                                                "\n\nVai ter link: " + linkTransmissao);
                                                    } else {
                                                        if (Objects.equals(online, "Sim") && Objects.equals(linkTransmissao, "Sim")) {
                                                         ((TextView) view.findViewById(R.id.textMessage3)).setText(
                                                                 "\n\nOnline: " + online +
                                                                 "\n\nVai ter link: " + linkTransmissao);
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

                                                        AlertDialog.Builder builderExcluir = new AlertDialog.Builder(context,R.style.AlertDialogTheme);
                                                        View viewExcluir = LayoutInflater.from(context).inflate(
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
                                                            Eventos event = listaEventos.get(holder.getAdapterPosition());
                                                            String eventosId = event.geteventosId();
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
                                                        Eventos event = listaEventos.get(holder.getAdapterPosition());
                                                        String eventosId = event.geteventosId();

                                                        Intent intentEdicaoForm = new Intent(v.getContext(), FormCadastroEvento.class);
                                                        intentEdicaoForm.putExtra("eventosId", eventosId);
                                                        Log.d("Debug 2", "Nenhum documento encontrado com o eventoId: " + eventosId);
                                                        v.getContext().startActivity(intentEdicaoForm);
                                                    });

                                                    if (alertDialog.getWindow() != null){
                                                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                                                    }
                                                    alertDialog.show();
                                                } else{
                                                    //add aqui os normais
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogTheme);
                                                    View view = LayoutInflater.from(context).inflate(
                                                            R.layout.layout_informacao_evento_dialog,null);
                                                    builder.setView(view);
                                                    ((TextView) view.findViewById(R.id.textTitleAlertDialog)).setText(evento.getnomedoEvento());
                                                    ((TextView) view.findViewById(R.id.textMessage)).setText(
                                                            "Descrição da Atividade: " + evento.getdescricaodaAtividade() +
                                                            "\n\nLocal de Evento: " + evento.getlocaldoEvento() +
                                                            "\n\nHora Inicial: " + evento.getHoradoEventoInicial() +
                                                            "\n\nHora Final: " + evento.getHoraEventoFinal() +
                                                            "\n\nData Inicial: " + evento.getdatadoEvento() +
                                                            "\n\nData Final: " + evento.getDatadoEventoFinal() +
                                                            "\n\nObservações: " + evento.getObservacoes() +
                                                            "\n\nNúmero de Participantes: " + evento.getNumerosdeParticipantes() +
                                                            "\n\nSetor Responsável: " + evento.getSetorResponsavel());

                                                    if(evento.getEquipamentos().equals("Sim")){
                                                        ((TextView) view.findViewById(R.id.textMessage1)).setText(
                                                                "\n\nEquipamentos de áudio de vídeo: " + evento.getEquipamentos() +
                                                                        "\n\nEquipamentos: " + evento.getQuaisequipamentos());
                                                    } else{
                                                        ((TextView) view.findViewById(R.id.textMessage1)).setText(
                                                                "\n\nEquipamentos de áudio de vídeo: " + "Nao" );
                                                    }
                                                    if(Objects.equals(temTransmissao, "Sim") && Objects.equals(transmissao, "Sim")){
                                                        ((TextView) view.findViewById(R.id.textMessage2)).setText(
                                                                "\n\nHaverá transmissão/ gravação: " + temTransmissao +
                                                                "\n\nTransmissão: " + transmissao);
                                                    } else {
                                                        if (Objects.equals(temTransmissao, "Sim") && Objects.equals(gravacao, "Sim")){
                                                        ((TextView) view.findViewById(R.id.textMessage2)).setText(
                                                                "\n\nHaverá transmissão/ gravação: " + temTransmissao +
                                                                "\n\nGravação: " + gravacao);
                                                        } else{
                                                            if (Objects.equals(temTransmissao, "Sim") && Objects.equals(ambos, "Sim")){
                                                                ((TextView) view.findViewById(R.id.textMessage2)).setText(
                                                                        "\n\nHaverá transmissão/ gravação: " + temTransmissao +
                                                                        "\n\nTransmissão e gravação: " + ambos);
                                                            } else{
                                                                ((TextView) view.findViewById(R.id.textMessage2)).setText(
                                                                        "\n\nHaverá transmissão/ gravação: " + temTransmissao);
                                                            }
                                                        }
                                                    }
                                                    if(Objects.equals(presencial, "Sim")){
                                                        ((TextView) view.findViewById(R.id.textMessage3)).setText(
                                                                "\n\nPresencial: " + presencial);
                                                    } else {
                                                        if (Objects.equals(hibrido, "Sim") && Objects.equals(linkTransmissao, "Sim")) {
                                                            ((TextView) view.findViewById(R.id.textMessage3)).setText(
                                                                    "\n\nHibrído: " + hibrido +
                                                                    "\n\nVai ter link: " + linkTransmissao);
                                                        } else {
                                                            if (Objects.equals(online, "Sim") && Objects.equals(linkTransmissao, "Sim")) {
                                                                ((TextView) view.findViewById(R.id.textMessage3)).setText(
                                                                        "\n\nOnline: " + online +
                                                                        "\n\nVai ter link: " + linkTransmissao);
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
                        Log.d("FormVisualizacao", "O documento infoEventos não existe para o evento com ID: " + eventoId);
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
            dataInicioEvento = dateFormat.parse(evento.getdatadoEvento());
            dataFimEvento = dateFormat.parse(evento.getDatadoEventoFinal());
            Date horaInicio = horaFormat.parse(evento.getHoradoEventoInicial());
            Date horaFim = horaFormat.parse(evento.getHoraEventoFinal());

            dataInicioEvento = sumTimeToDate(dataInicioEvento, horaInicio);
            dataFimEvento = sumTimeToDate(dataFimEvento, horaFim);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dataInicioEvento != null && dataFimEvento != null) {
            if (dataAtual.after(dataInicioEvento) && dataAtual.before(dataFimEvento)) {
                holder.tagAovivo.setVisibility(View.VISIBLE); // Mostra a tag "Ao Vivo"
                holder.tagExpirado.setVisibility(View.GONE); // Esconde a tag "Expirado"
                holder.tagEmBreve.setVisibility(View.GONE); // Esconde a tag "Em breve"
            } else if (dataAtual.after(dataFimEvento)) {
                holder.tagExpirado.setVisibility(View.VISIBLE); // Mostra a tag "Expirado"
                holder.tagAovivo.setVisibility(View.GONE); // Esconde a tag "Ao Vivo"
                holder.tagEmBreve.setVisibility(View.GONE); // Esconde a tag "Em breve"
            } else {
                holder.tagEmBreve.setVisibility(View.VISIBLE); // Mostra a tag "Em breve"
                holder.tagAovivo.setVisibility(View.GONE); // Esconde a tag "Ao Vivo"
                holder.tagExpirado.setVisibility(View.GONE); // Esconde a tag "Expirado"
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
        Query query = db.collection("eventos").whereEqualTo("eventosId", eventId);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                    documentSnapshot.getReference().delete()
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Debug", "Evento excluído com sucesso da coleção 'eventos'");

                                // Em seguida, exclua o documento correspondente da coleção "infoEventos"
                                DocumentReference infoEventoRef = db.collection("infoEventos").document(eventId);
                                infoEventoRef.delete()
                                        .addOnSuccessListener(aVoid1 -> {
                                            Log.d("Debug", "Evento excluído com sucesso da coleção 'infoEventos'");

                                            VisuAdapter.this.removerEvento(position);
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
        return listaEventos.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView nome;
        private final TextView desc;
        private final TextView data;
        private final TextView datafinal;
        private final TextView horario;
        private final TextView horafinal;
        private final TextView local;
        private final TextView layout;
        private final TextView tagExpirado;
        private final TextView tagEmBreve;
        private final TextView tagAovivo;

         public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.txtnome);
            desc = itemView.findViewById(R.id.txtdescricao);
            data = itemView.findViewById(R.id.txtdata1);
            datafinal = itemView.findViewById(R.id.txtdata2);
            horario = itemView.findViewById(R.id.txthorario1);
            horafinal = itemView.findViewById(R.id.txthorario2);
            local = itemView.findViewById(R.id.txtlocal1);
            layout = itemView.findViewById(R.id.layoutDialogContainer);
            tagExpirado = itemView.findViewById(R.id.tagExpirado);
            tagEmBreve = itemView.findViewById(R.id.tagEmBreve);
            tagAovivo = itemView.findViewById(R.id.tagAovivo);

        }
    }
}
