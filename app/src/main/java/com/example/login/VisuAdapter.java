package com.example.login;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.esp_eventos.R;

import java.util.List;

public class VisuAdapter extends RecyclerView.Adapter<VisuAdapter.MyViewHolder> {

    private List<Eventos> listaEventos;
    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredList(List<Eventos> filteredList){
        this.listaEventos = filteredList;
        notifyDataSetChanged();
    }
    public VisuAdapter(List<Eventos> listaEventos) {
        this.listaEventos = listaEventos;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_eventos_1, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Eventos evento = listaEventos.get(position);
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
            }
        }
        
        holder.nome.setText(evento.getnomedoEvento());
        holder.data.setText(evento.getdatadoEvento());
        holder.horario.setText(evento.getHoradoEventoInicial());
        holder.local.setText(evento.getlocaldoEvento());
        holder.desc.setText(evento.getdescricaodaAtividade());

    }

    @Override
    public int getItemCount() {

        return listaEventos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView nome;
        private final TextView desc;
        private final TextView data;
        private final TextView horario;
        private final TextView local;

         public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            nome = itemView.findViewById(R.id.txtnome);
            desc = itemView.findViewById(R.id.txtdescricao);
            data = itemView.findViewById(R.id.txtdata1);
            horario = itemView.findViewById(R.id.txthorario1);
            local = itemView.findViewById(R.id.txtlocal1);


        }
    }
}
