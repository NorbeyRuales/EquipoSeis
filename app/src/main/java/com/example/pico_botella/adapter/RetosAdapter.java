package com.example.pico_botella.adapter;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pico_botella.R;
import com.example.pico_botella.model.Reto;
import java.util.List;

public class RetosAdapter extends RecyclerView.Adapter<RetosAdapter.RetoViewHolder> {

    private List<Reto> listaRetos;
    private OnRetoClickListener listener;

    public interface OnRetoClickListener {
        void onEditClick(Reto reto);
        void onDeleteClick(Reto reto);
    }

    public RetosAdapter(List<Reto> listaRetos, OnRetoClickListener listener) {
        this.listaRetos = listaRetos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RetoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reto, parent, false);
        return new RetoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RetoViewHolder holder, int position) {
        Reto reto = listaRetos.get(position);
        holder.tvDescripcion.setText(reto.getDescripcion());
        
        // Criterio 7: Pequeño delay para dejar ver la animación de touch (ripple)
        holder.btnEdit.setOnClickListener(v -> {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (listener != null) listener.onEditClick(reto);
            }, 200);
        });

        holder.btnDelete.setOnClickListener(v -> {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (listener != null) listener.onDeleteClick(reto);
            }, 200);
        });
    }

    @Override
    public int getItemCount() {
        return listaRetos.size();
    }

    public void setRetos(List<Reto> nuevosRetos) {
        this.listaRetos = nuevosRetos;
        notifyDataSetChanged();
    }

    static class RetoViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescripcion;
        ImageButton btnEdit, btnDelete;

        public RetoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
