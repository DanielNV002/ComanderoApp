package com.example.comanderoapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class PplatoFragment : Fragment() {

    private lateinit var pplatoContainer: FrameLayout
    private lateinit var viewModel: ViewModelPedidos

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout para este fragmento
        val rootView = inflater.inflate(R.layout.fragment_pplato, container, false)

        // Obtén el contenedor donde agregarás las filas dinámicamente
        pplatoContainer = rootView.findViewById(R.id.productosContainer)

        // Obtén el ViewModel compartido
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelPedidos::class.java)

        val btnMas1 = rootView.findViewById<TextView>(R.id.textView2)
        val btnMas2 = rootView.findViewById<TextView>(R.id.textView4)
        val btnMas3 = rootView.findViewById<TextView>(R.id.textView6)
        val btnMas4 = rootView.findViewById<TextView>(R.id.textView8)

        // Configura los clics en los botones de añadir
        btnMas1.setOnClickListener {
            agregarLinea("RISSOTO CON SETAS")
        }
        btnMas2.setOnClickListener {
            agregarLinea("MACARRONES GORGONZOLA")
        }
        btnMas3.setOnClickListener {
            agregarLinea("FIDEUÁ DE PAVO")
        }
        btnMas4.setOnClickListener {
            agregarLinea("POTAJE CON CALAMAR")
        }

        return rootView
    }

    // Función para añadir una nueva línea de FrameLayout
    private fun agregarLinea(nombreProducto: String) {
        // Agregar la bebida al ViewModel
        viewModel.agregarProducto(nombreProducto)

        // Aquí seguirías con la lógica para agregar la línea en el fragmento actual (BebidasFragment),
        // pero el ViewModel mantiene la lista de bebidas.
    }
}