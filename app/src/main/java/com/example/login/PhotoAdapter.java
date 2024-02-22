package com.example.login;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.esp_eventos.R;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
    private final List<Photos> photoList;
    public PhotoAdapter(List<Photos> photoList) {
        this.photoList = photoList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar o layout do item da grade de fotos
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Photos photoItem = photoList.get(position);

        // Carregar a imagem e o texto para o ImageView e TextView, respectivamente.
        holder.imageView.setImageResource(photoItem.getImgResId());
        holder.textView.setText(photoItem.getTxt());

        // Definir um ouvinte de clique para o item da grade de fotos
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lidar com o clique da foto
                // Neste exemplo, vamos abrir uma nova página (activity) com base na foto clicada

                Intent intent = new Intent(v.getContext(), PhotoDetailsActivity.class); //Observação nessa página de PhotoDetailsActivity, por que no voltar vai pra página preta.
                intent.putExtra("photoId", photoItem.getPhotoId());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                v.getContext().startActivity(intent);
                ((PaginaPrincipal)v.getContext()).finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //REFERÊNCIA A LISTA_ITEM.XML
            imageView = itemView.findViewById(R.id.imgServicos);
            textView = itemView.findViewById(R.id.txtServico);
        }
    }
}
