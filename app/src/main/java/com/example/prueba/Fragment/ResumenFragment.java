package com.example.prueba.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prueba.Adapter.MovimientoAdapter;
import com.example.prueba.DAO.MovimientoDAO;
import com.example.prueba.R;
import com.example.prueba.clases.Movimiento;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ResumenFragment extends Fragment {

    private MovimientoDAO movimientoDAO;
    private MovimientoAdapter adapter;

    private TextView txtMesActual;
    private TextView txtIngresos;
    private TextView txtGastos;
    private TextView txtBalance;

    private final Calendar mesSeleccionado = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_resumen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movimientoDAO = new MovimientoDAO(requireContext());

        txtMesActual = view.findViewById(R.id.txtMesActual);
        txtIngresos = view.findViewById(R.id.txtIngresos);
        txtGastos = view.findViewById(R.id.txtGastos);
        txtBalance = view.findViewById(R.id.txtBalance);

        RecyclerView recyclerMovimientos = view.findViewById(R.id.recyclerMovimientos);
        recyclerMovimientos.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new MovimientoAdapter(new ArrayList<>(), new MovimientoAdapter.OnMovimientoActionListener() {
            @Override
            public void onEditar(Movimiento movimiento) {
                abrirFormulario(movimiento.getId());
            }

            @Override
            public void onBorrar(Movimiento movimiento) {
                confirmarBorrado(movimiento);
            }
        });
        recyclerMovimientos.setAdapter(adapter);

        view.findViewById(R.id.fabAgregarMovimiento).setOnClickListener(v -> abrirFormulario(-1));

        ImageButton btnMesAnterior = view.findViewById(R.id.btnMesAnterior);
        ImageButton btnMesSiguiente = view.findViewById(R.id.btnMesSiguiente);

        btnMesAnterior.setOnClickListener(v -> {
            mesSeleccionado.add(Calendar.MONTH, -1);
            cargarDatosDelMes();
        });

        btnMesSiguiente.setOnClickListener(v -> {
            mesSeleccionado.add(Calendar.MONTH, 1);
            cargarDatosDelMes();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarDatosDelMes();
    }

    private void confirmarBorrado(Movimiento movimiento) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Eliminar movimiento")
                .setMessage("¿Seguro que quieres eliminarlo?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    movimientoDAO.removeMovimiento(movimiento.getId());
                    cargarDatosDelMes();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
    private void cargarDatosDelMes() {
        long[] rango = obtenerRangoDelMes();
        long inicio = rango[0];
        long fin = rango[1];

        List<Movimiento> movimientos = movimientoDAO.getMovimientosPorMes(inicio, fin);
        adapter.actualizarLista(movimientos);

        double ingresos = movimientoDAO.getTotalIngresos(inicio, fin);
        double gastos = movimientoDAO.getTotalGastos(inicio, fin);
        double balance = ingresos - gastos;

        txtIngresos.setText(String.format(Locale.getDefault(), "%.2f €", ingresos));
        txtGastos.setText(String.format(Locale.getDefault(), "%.2f €", gastos));
        txtBalance.setText(String.format(Locale.getDefault(), "%.2f €", balance));

        SimpleDateFormat formato = new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES"));
        String nombreMes = formato.format(mesSeleccionado.getTime());
        txtMesActual.setText(capitalizar(nombreMes));
    }

    // Devuelve {inicioDelMes, finDelMes} en milisegundos
    private long[] obtenerRangoDelMes() {
        Calendar inicio = (Calendar) mesSeleccionado.clone();
        inicio.set(Calendar.DAY_OF_MONTH, 1);
        inicio.set(Calendar.HOUR_OF_DAY, 0);
        inicio.set(Calendar.MINUTE, 0);
        inicio.set(Calendar.SECOND, 0);
        inicio.set(Calendar.MILLISECOND, 0);

        Calendar fin = (Calendar) inicio.clone();
        fin.set(Calendar.DAY_OF_MONTH, inicio.getActualMaximum(Calendar.DAY_OF_MONTH));
        fin.set(Calendar.HOUR_OF_DAY, 23);
        fin.set(Calendar.MINUTE, 59);
        fin.set(Calendar.SECOND, 59);
        fin.set(Calendar.MILLISECOND, 999);

        return new long[]{inicio.getTimeInMillis(), fin.getTimeInMillis()};
    }

    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }

    private void abrirFormulario(long movimientoId) {
        MovimientoFormFragment formFragment = new MovimientoFormFragment();

        Bundle args = new Bundle();
        args.putLong("movimientoId", movimientoId);
        formFragment.setArguments(args);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, formFragment)
                .addToBackStack(null)
                .commit();
    }
}