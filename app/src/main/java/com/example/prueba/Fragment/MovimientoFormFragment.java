package com.example.prueba.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.prueba.DAO.CategoriaDAO;
import com.example.prueba.DAO.MovimientoDAO;
import com.example.prueba.R;
import com.example.prueba.clases.Categoria;
import com.example.prueba.clases.Movimiento;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MovimientoFormFragment extends Fragment {

    private MovimientoDAO movimientoDAO;
    private CategoriaDAO categoriaDAO;

    private long movimientoId = -1;
    private Movimiento movimientoActual;
    private final Calendar fechaSeleccionada = Calendar.getInstance();

    private TextInputEditText inputImporte;
    private TextInputEditText inputDescripcion;
    private Button btnSeleccionarFecha;
    private Spinner spinnerCategoria;
    private Button btnBorrar;

    private List<Categoria> categorias = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movimiento_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movimientoDAO = new MovimientoDAO(requireContext());
        categoriaDAO = new CategoriaDAO(requireContext());

        inputImporte = view.findViewById(R.id.inputImporte);
        inputDescripcion = view.findViewById(R.id.inputDescripcion);
        btnSeleccionarFecha = view.findViewById(R.id.btnSeleccionarFecha);
        spinnerCategoria = view.findViewById(R.id.spinnerCategoria);
        Button btnGuardar = view.findViewById(R.id.btnGuardar);
        btnBorrar = view.findViewById(R.id.btnBorrar);

        view.findViewById(R.id.btnVolver).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        actualizarTextoFecha();
        cargarCategorias();

        if (getArguments() != null) {
            movimientoId = getArguments().getLong("movimientoId", -1);
        }

        if (movimientoId != -1) {
            cargarMovimientoExistente();
            btnBorrar.setVisibility(View.VISIBLE);
        }

        btnSeleccionarFecha.setOnClickListener(v -> mostrarDatePicker());
        btnGuardar.setOnClickListener(v -> guardarMovimiento());
        btnBorrar.setOnClickListener(v -> borrarMovimiento());
    }

    private void cargarMovimientoExistente() {
        movimientoActual = movimientoDAO.getMovimientoById(movimientoId);
        if (movimientoActual == null) return;

        inputImporte.setText(String.valueOf(movimientoActual.getImporte()));
        inputDescripcion.setText(movimientoActual.getDescripcion());

        fechaSeleccionada.setTimeInMillis(movimientoActual.getFecha());
        actualizarTextoFecha();

        seleccionarCategoriaEnSpinner(movimientoActual.getCategoriaId());
    }

    private void cargarCategorias() {
        categorias = categoriaDAO.getAllCategorias();

        List<String> nombres = new ArrayList<>();
        for (Categoria categoria : categorias) {
            String etiqueta = categoria.getNombre() + " (" +
                    ("INGRESO".equals(categoria.getTipo()) ? "Ingreso" : "Gasto") + ")";
            nombres.add(etiqueta);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, nombres);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);
    }

    private void seleccionarCategoriaEnSpinner(long categoriaId) {
        for (int i = 0; i < categorias.size(); i++) {
            if (categorias.get(i).getId() == categoriaId) {
                spinnerCategoria.setSelection(i);
                return;
            }
        }
    }

    private void mostrarDatePicker() {
        new DatePickerDialog(requireContext(), (datePicker, anio, mes, dia) -> {
            fechaSeleccionada.set(anio, mes, dia);
            actualizarTextoFecha();
        },
                fechaSeleccionada.get(Calendar.YEAR),
                fechaSeleccionada.get(Calendar.MONTH),
                fechaSeleccionada.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void actualizarTextoFecha() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        btnSeleccionarFecha.setText(formato.format(fechaSeleccionada.getTime()));
    }

    private void guardarMovimiento() {
        String textoImporte = inputImporte.getText() != null ? inputImporte.getText().toString().trim() : "";
        if (textoImporte.isEmpty()) {
            inputImporte.setError("Introduce un importe");
            inputImporte.requestFocus();
            return;
        }

        double importe;
        try {
            importe = Double.parseDouble(textoImporte);
        } catch (NumberFormatException e) {
            inputImporte.setError("Importe no válido");
            return;
        }

        if (categorias.isEmpty()) {
            Toast.makeText(requireContext(),
                    "Primero crea al menos una categoría", Toast.LENGTH_SHORT).show();
            return;
        }

        int posicionSeleccionada = spinnerCategoria.getSelectedItemPosition();
        Categoria categoriaElegida = categorias.get(posicionSeleccionada);

        String descripcion = inputDescripcion.getText() != null ? inputDescripcion.getText().toString().trim() : "";

        if (movimientoId == -1) {
            Movimiento nuevo = new Movimiento(importe, fechaSeleccionada.getTimeInMillis(),
                    descripcion, categoriaElegida.getTipo(), categoriaElegida.getId());
            movimientoDAO.createMovimiento(nuevo);
        } else {
            movimientoActual.setImporte(importe);
            movimientoActual.setFecha(fechaSeleccionada.getTimeInMillis());
            movimientoActual.setDescripcion(descripcion);
            movimientoActual.setTipo(categoriaElegida.getTipo());
            movimientoActual.setCategoriaId(categoriaElegida.getId());
            movimientoDAO.updateMovimiento(movimientoActual);
        }

        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void borrarMovimiento() {
        movimientoDAO.removeMovimiento(movimientoId);
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}