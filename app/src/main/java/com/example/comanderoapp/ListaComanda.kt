package com.example.comanderoapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class ListaComanda : Fragment() {

    private lateinit var viewModel: ViewModelPedidos
    private lateinit var productosContainer: FrameLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout para este fragmento
        val rootView = inflater.inflate(R.layout.fragment_lista_comanda, container, false)

        // Obtén el contenedor donde agregarás las filas dinámicamente
        productosContainer = rootView.findViewById(R.id.fragmentPedidos)

        // Obtén el ViewModel compartido
        viewModel = ViewModelProvider(requireActivity())[ViewModelPedidos::class.java]

        // Observa los cambios en la lista de bebidas
        viewModel.productosList.observe(viewLifecycleOwner) { listaProductos ->
            // Limpia el contenedor
            productosContainer.removeAllViews()

            // Agrega cada bebida al contenedor
            for (producto in listaProductos) {
                agregarLinea(producto)
            }
        }

        return rootView
    }

    // Variable para calcular el desplazamiento vertical
    private var currentTopMargin = 0

    // Función para agregar una línea de bebida al contenedor
    private fun agregarLinea(nombreProducto: String) {
        val newFrameLayout = FrameLayout(requireContext())
        newFrameLayout.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            resources.getDimensionPixelSize(R.dimen.frame_height) // Usamos una altura fija
        ).apply {
            // Establecer un margen superior dinámico
            topMargin = currentTopMargin
        }
        newFrameLayout.setBackgroundResource(R.drawable.border)

        val textViewNombre = TextView(requireContext()).apply {
            text = nombreProducto
            textSize = 23f
            gravity = Gravity.CENTER_VERTICAL
            // Usamos FrameLayout.LayoutParams con márgenes para la posición
            layoutParams = FrameLayout.LayoutParams(
                1000, // Ancho fijo
                FrameLayout.LayoutParams.MATCH_PARENT // Altura
            ).apply {
                // Márgenes para la posición
                setMargins(100, 0, 0, 0)
            }
        }

        val textViewEliminar = TextView(requireContext()).apply {
            text = "-"
            textSize = 50f
            gravity = Gravity.CENTER
            layoutParams = FrameLayout.LayoutParams(
                70, // Ancho fijo
                FrameLayout.LayoutParams.MATCH_PARENT // Altura
            ).apply {
                // Márgenes para la posición
                setMargins(900, 0, 0, 0)
            }
            setOnClickListener {
                // Elimina la bebida de la lista
                viewModel.eliminarProducto(nombreProducto)
                // Decrementar el topMargin después de eliminar
                // No decrementamos de inmediato, sino cuando se ha eliminado el item visualmente
                currentTopMargin -= resources.getDimensionPixelSize(R.dimen.frame_height)

                // Actualizamos el contenedor para que los elementos se reposicionen correctamente
                actualizarPosiciones()
            }
        }

        // Añadimos los TextView dentro del nuevo FrameLayout
        newFrameLayout.addView(textViewNombre)
        newFrameLayout.addView(textViewEliminar)

        // Añadimos el nuevo FrameLayout al contenedor
        productosContainer.addView(newFrameLayout)

        // Incrementamos el topMargin para la siguiente bebida
        currentTopMargin += resources.getDimensionPixelSize(R.dimen.frame_height)
    }

    // Método para actualizar la posición de los FrameLayouts después de eliminar un elemento
    private fun actualizarPosiciones() {
        // Iteramos sobre todos los FrameLayouts y les ajustamos el marginTop
        for (i in 0 until productosContainer.childCount) {
            val child = productosContainer.getChildAt(i)
            val params = child.layoutParams as FrameLayout.LayoutParams
            // Ajustamos el margen superior de cada item según su posición en la lista
            params.topMargin = i * resources.getDimensionPixelSize(R.dimen.frame_height)
            child.layoutParams = params // Aplicamos el nuevo margen
        }
    }
}