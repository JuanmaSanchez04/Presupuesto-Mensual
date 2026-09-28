package com.example.prueba.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prueba.R;
import com.example.prueba.clases.Movimiento;

import java.util.List;
import java.util.Locale;

public class MovimientoAdapter extends RecyclerView.Adapter<MovimientoAdapter.MovimientoViewHolder> {

    public interface OnMovimientoActionListener {
        void onEditar(Movimiento movimiento);
        void onBorrar(Movimiento movimiento);
    }

    private List<Movimiento> movimientos;
    private final OnMovimientoActionListener listener;

    public MovimientoAdapter(List<Movimiento> movimientos, OnMovimientoActionListener listener) {
        this.movimientos = movimientos;
        this.listener = listener;
    }

    public void actualizarLista(List<Movimiento> nuevaLista) {
        this.movimientos = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovimientoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movimiento, parent, false);
        return new MovimientoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull MovimientoViewHolder holder, int position) {
        Movimiento movimiento = movimientos.get(position);
        android.content.Context contexto = holder.itemView.getContext();

        String descripcion = movimiento.getDescripcion();
        holder.txtDescripcion.setText(descripcion != null && !descripcion.isEmpty()
                ? descripcion : "(Sin descripción)");

        boolean esIngreso = "INGRESO".equals(movimiento.getTipo());
        String signo = esIngreso ? "+" : "-";
        holder.txtImporte.setText(String.format(Locale.getDefault(), "%s%.2f €", signo, movimiento.getImporte()));

        int color = contexto.getColor(esIngreso ? R.color.ingreso : R.color.gasto);
        holder.txtImporte.setTextColor(color);
        holder.franjaTipo.setBackgroundColor(color);

        holder.btnEditar.setOnClickListener(v -> listener.onEditar(movimiento));
        holder.btnBorrar.setOnClickListener(v -> listener.onBorrar(movimiento));
    }

    @Override
    public int getItemCount() {
        return movimientos.size();
    }

    static class MovimientoViewHolder extends RecyclerView.ViewHolder {
        TextView txtDescripcion;
        TextView txtImporte;
        ImageButton btnEditar;
        ImageButton btnBorrar;
        View franjaTipo;

        MovimientoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            txtImporte = itemView.findViewById(R.id.txtImporte);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnBorrar = itemView.findViewById(R.id.btnBorrar);
            franjaTipo = itemView.findViewById(R.id.franjaTipo);
        }
    }
}