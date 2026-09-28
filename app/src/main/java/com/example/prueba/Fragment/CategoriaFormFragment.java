package com.example.prueba.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.prueba.DAO.CategoriaDAO;
import com.example.prueba.R;
import com.example.prueba.clases.Categoria;
import com.google.android.material.textfield.TextInputEditText;

public class CategoriaFormFragment extends Fragment {

    private CategoriaDAO categoriaDAO;
    private long categoriaId = -1;
    private Categoria categoriaActual;

    private TextInputEditText inputNombre;
    private RadioGroup radioGroupTipo;
    private RadioButton radioIngreso;
    private RadioButton radioGasto;
    private Button btnBorrar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categoria_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoriaDAO = new CategoriaDAO(requireContext());

        inputNombre = view.findViewById(R.id.inputNombre);
        radioGroupTipo = view.findViewById(R.id.radioGroupTipo);
        radioIngreso = view.findViewById(R.id.radioIngreso);
        radioGasto = view.findViewById(R.id.radioGasto);
        Button btnGuardar = view.findViewById(R.id.btnGuardar);
        btnBorrar = view.findViewById(R.id.btnBorrar);

        view.findViewById(R.id.btnVolver).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        if (getArguments() != null) {
            categoriaId = getArguments().getLong("categoriaId", -1);
        }

        if (categoriaId != -1) {
            cargarCategoriaExistente();
            btnBorrar.setVisibility(View.VISIBLE);
        } else {
            radioGasto.setChecked(true);
        }

        btnGuardar.setOnClickListener(v -> guardarCategoria());
        btnBorrar.setOnClickListener(v -> confirmarBorrado());
    }


    private void cargarCategoriaExistente() {
        categoriaActual = categoriaDAO.getCategoriaById(categoriaId);
        if (categoriaActual != null) {
            inputNombre.setText(categoriaActual.getNombre());
            if ("INGRESO".equals(categoriaActual.getTipo())) {
                radioIngreso.setChecked(true);
            } else {
                radioGasto.setChecked(true);
            }
        }
    }

    private void guardarCategoria() {
        String nombre = inputNombre.getText() != null ? inputNombre.getText().toString().trim() : "";
        if (nombre.isEmpty()) {
            inputNombre.setError("Este campo es obligatorio");
            inputNombre.requestFocus();
            return;
        }

        String tipo = radioGroupTipo.getCheckedRadioButtonId() == R.id.radioIngreso ? "INGRESO" : "GASTO";

        if (categoriaId == -1) {
            Categoria nueva = new Categoria(nombre, tipo, 0);
            categoriaDAO.createCategoria(nueva);
        } else {
            categoriaActual.setNombre(nombre);
            categoriaActual.setTipo(tipo);
            categoriaDAO.updateCategoria(categoriaActual);
        }

        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void confirmarBorrado() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Eliminar categoría")
                .setMessage("¿Seguro que quieres eliminarla?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    categoriaDAO.removeCategoria(categoriaId);
                    requireActivity().getSupportFragmentManager().popBackStack();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void borrarCategoria(View view) {
        categoriaDAO.removeCategoria(categoriaId);
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}