package com.example.prueba.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prueba.Adapter.CategoriaAdapter;
import com.example.prueba.DAO.CategoriaDAO;
import com.example.prueba.R;
import com.example.prueba.clases.Categoria;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CategoriaFragment extends Fragment {

    private RecyclerView recyclerCategorias;
    private CategoriaAdapter adapter;
    private CategoriaDAO categoriaDAO;
    private List<Categoria> todasLasCategorias = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categoria, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoriaDAO = new CategoriaDAO(requireContext());

        recyclerCategorias = view.findViewById(R.id.recyclerCategorias);
        recyclerCategorias.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new CategoriaAdapter(new ArrayList<>(), new CategoriaAdapter.OnCategoriaActionListener() {
            @Override
            public void onEditar(Categoria categoria) {
                abrirFormulario(categoria.getId());
            }

            @Override
            public void onBorrar(Categoria categoria) {
                confirmarBorrado(categoria);
            }
        });
        recyclerCategorias.setAdapter(adapter);

        EditText inputBuscar = view.findViewById(R.id.inputBuscar);
        inputBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrar(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        FloatingActionButton fabAgregar = view.findViewById(R.id.fabAgregar);
        fabAgregar.setOnClickListener(v -> abrirFormulario(-1));
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarCategorias();
    }

    private void cargarCategorias() {
        todasLasCategorias = categoriaDAO.getAllCategorias();
        adapter.actualizarLista(todasLasCategorias);
    }

    private void filtrar(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            adapter.actualizarLista(todasLasCategorias);
            return;
        }

        String textoLower = texto.toLowerCase().trim();
        List<Categoria> resultado = new ArrayList<>();
        for (Categoria categoria : todasLasCategorias) {
            if (categoria.getNombre().toLowerCase().contains(textoLower)) {
                resultado.add(categoria);
            }
        }
        adapter.actualizarLista(resultado);
    }

    private void confirmarBorrado(Categoria categoria) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar categoría")
                .setMessage("¿Seguro que quieres eliminar \"" + categoria.getNombre() + "\"? Se borrarán también sus movimientos asociados.")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    categoriaDAO.removeCategoria(categoria.getId());
                    cargarCategorias();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void abrirFormulario(long categoriaId) {
        CategoriaFormFragment formFragment = new CategoriaFormFragment();

        Bundle args = new Bundle();
        args.putLong("categoriaId", categoriaId);
        formFragment.setArguments(args);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, formFragment)
                .addToBackStack(null)
                .commit();
    }
}