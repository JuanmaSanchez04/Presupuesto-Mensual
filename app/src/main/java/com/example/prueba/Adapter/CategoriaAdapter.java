package com.example.prueba.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prueba.R;
import com.example.prueba.clases.Categoria;

import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {

    public interface OnCategoriaActionListener {
        void onEditar(Categoria categoria);
        void onBorrar(Categoria categoria);
    }

    private List<Categoria> categorias;
    private final OnCategoriaActionListener listener;

    public CategoriaAdapter(List<Categoria> categorias, OnCategoriaActionListener listener) {
        this.categorias = categorias;
        this.listener = listener;
    }

    public void actualizarLista(List<Categoria> nuevaLista) {
        this.categorias = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categoria, parent, false);
        return new CategoriaViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        Categoria categoria = categorias.get(position);
        holder.txtNombre.setText(categoria.getNombre());

        boolean esIngreso = "INGRESO".equals(categoria.getTipo());
        holder.txtTipo.setText(esIngreso ? "Ingreso" : "Gasto");

        int color = holder.itemView.getContext().getColor(esIngreso ? R.color.ingreso : R.color.gasto);
        holder.franjaTipo.setBackgroundColor(color);

        holder.btnEditar.setOnClickListener(v -> listener.onEditar(categoria));
        holder.btnBorrar.setOnClickListener(v -> listener.onBorrar(categoria));
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    static class CategoriaViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre;
        TextView txtTipo;
        ImageButton btnEditar;
        ImageButton btnBorrar;
        View franjaTipo;

        CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtTipo = itemView.findViewById(R.id.txtTipo);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnBorrar = itemView.findViewById(R.id.btnBorrar);
            franjaTipo = itemView.findViewById(R.id.franjaTipo);
        }
    }
}