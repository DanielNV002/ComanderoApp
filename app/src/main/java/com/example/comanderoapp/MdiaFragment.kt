package com.example.comanderoapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class MdiaFragment : Fragment() {

    private lateinit var mdiaContainer: FrameLayout
    private lateinit var viewModel: ViewModelPedidos

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout para este fragmento
        val rootView = inflater.inflate(R.layout.fragment_mdia, container, false)

        // Obtén el contenedor donde agregarás las filas dinámicamente
        mdiaContainer = rootView.findViewById(R.id.productosContainer)

        // Obtén el ViewModel compartido
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelPedidos::class.java)

        val btnMas1 = rootView.findViewById<TextView>(R.id.textView2)
        val btnMas2 = rootView.findViewById<TextView>(R.id.textView4)
        val btnMas3 = rootView.findViewById<TextView>(R.id.textView6)
        val btnMas4 = rootView.findViewById<TextView>(R.id.textView8)

        // Modificar los botones para que envíen el productoId en lugar del nombre
        btnMas1.setOnClickListener {
            val productoId = obtenerProductoId("sabores")
            agregarLinea(productoId) // Pasar productoId, no el nombre
        }
        btnMas2.setOnClickListener {
            val productoId = obtenerProductoId("abuela")
            agregarLinea(productoId) // Pasar productoId, no el nombre
        }
        btnMas3.setOnClickListener {
            val productoId = obtenerProductoId("estacional")
            agregarLinea(productoId) // Pasar productoId, no el nombre
        }
        btnMas4.setOnClickListener {
            val productoId = obtenerProductoId("gourmet")
            agregarLinea(productoId) // Pasar productoId, no el nombre
        }

        return rootView
    }

    // Función para obtener el productoId correspondiente al nombre
    @SuppressLint("Range")
    fun obtenerProductoId(nombreProducto: String): Int {
        val db = DataBaseHelper(requireContext())
        val query = "SELECT productoId FROM producto WHERE nombre = ?"
        val cursor = db.readableDatabase.rawQuery(query, arrayOf(nombreProducto))
        var productoId = -1
        if (cursor.moveToFirst()) {
            productoId = cursor.getInt(cursor.getColumnIndex("productoId"))
        }
        cursor.close()
        db.close()
        return productoId
    }

    // Función para añadir una nueva línea de FrameLayout
    fun agregarLinea(productoId: Int) {
        // Agregar la bebida al ViewModel usando el productoId
        viewModel.agregarProducto(productoId)
    }
}
