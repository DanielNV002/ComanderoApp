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

class BebidasFragment : Fragment() {

    private lateinit var bebidasContainer: FrameLayout // Este es el contenedor donde se agregarán las filas
    private var idContador = 0 // Para dar un ID único a cada nuevo elemento

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout para este fragmento
        val rootView = inflater.inflate(R.layout.fragment_bebidas, container, false)

        // Obtén el contenedor donde agregarás las filas dinámicamente
        bebidasContainer = rootView.findViewById(R.id.bebidasContainer)

        val btnMas1 = rootView.findViewById<TextView>(R.id.textView2)
        val btnMas2 = rootView.findViewById<TextView>(R.id.textView4)
        val btnMas3 = rootView.findViewById<TextView>(R.id.textView6)
        val btnMas4 = rootView.findViewById<TextView>(R.id.textView8)

        // Configura los clics en los botones de añadir
        btnMas1.setOnClickListener { agregarLinea("Agua") }
        btnMas2.setOnClickListener { agregarLinea("Cerveza") }
        btnMas3.setOnClickListener { agregarLinea("Cerveza Sin") }
        btnMas4.setOnClickListener { agregarLinea("Refresco") }

        return rootView
    }

    // Función para añadir una nueva línea de FrameLayout
    private fun agregarLinea(nombreBebida: String) {
        val newFrameLayout = FrameLayout(requireContext())
        newFrameLayout.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            resources.getDimensionPixelSize(R.dimen.frame_height) // Puedes definir una altura fija
        )
        newFrameLayout.setBackgroundResource(R.drawable.border)

        val textViewNombre = TextView(requireContext()).apply {
            text = nombreBebida
            textSize = 23f
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = FrameLayout.LayoutParams(
                0, FrameLayout.LayoutParams.MATCH_PARENT, 1 // Toma todo el espacio disponible
            )
        }

        val textViewEliminar = TextView(requireContext()).apply {
            text = "-"
            textSize = 50f
            gravity = Gravity.CENTER
            layoutParams = FrameLayout.LayoutParams(
                70, FrameLayout.LayoutParams.MATCH_PARENT
            )
            setOnClickListener {
                // Elimina la fila cuando se hace clic en el '-'
                bebidasContainer.removeView(newFrameLayout)
            }
        }

        // Añadimos los TextView dentro del nuevo FrameLayout
        newFrameLayout.addView(textViewNombre)
        newFrameLayout.addView(textViewEliminar)

        // Asignamos un ID único al FrameLayout para poder manipularlo si es necesario
        newFrameLayout.tag = "linea_$idContador"
        idContador++

        // Añadimos el nuevo FrameLayout al contenedor
        bebidasContainer.addView(newFrameLayout)
    }
}


