package com.example.comanderoapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider

class ListaComanda : Fragment() {

    private lateinit var viewModel: ViewModelPedidos
    private lateinit var productosContainer: FrameLayout

    // Mapa para hacer seguimiento de las líneas de productos y sus cantidades
    private val productosMap = mutableMapOf<Int, FrameLayout>()

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

        // Observa los cambios en la lista de productos
        viewModel.productosList.observe(viewLifecycleOwner) { listaProductos ->
            // Limpia el contenedor
            productosContainer.removeAllViews()

            // Crea un mapa para contar las ocurrencias de cada producto
            val contadorProductos: MutableMap<Int, Int> = mutableMapOf()

            // Agrega cada producto al contenedor con su nombre
            for (productoId in listaProductos) {
                val nombreProducto = viewModel.obtenerNombreProducto(productoId, requireContext())

                // Actualiza el contador para este producto
                contadorProductos[productoId] = contadorProductos.getOrDefault(productoId, 0) + 1

                // Asigna la cantidad
                val cantidadProducto = "x${contadorProductos[productoId]}"

                // Agrega o actualiza la línea para este producto
                agregarLinea(productoId, nombreProducto, cantidadProducto)
            }
        }

        return rootView
    }

    // Variable para calcular el desplazamiento vertical
    private var currentTopMargin = 0

    // Función para agregar o actualizar una línea de objeto en el contenedor
    private fun agregarLinea(productoId: Int, nombreProducto: String, cantidadProducto: String) {
        // Verifica si ya existe una línea para este producto
        val existingFrameLayout = productosMap[productoId]

        if (existingFrameLayout != null) {
            // Si ya existe, actualiza la cantidad
            val textViewCantidad = existingFrameLayout.getChildAt(2) as TextView
            textViewCantidad.text = cantidadProducto
        } else {
            // Si no existe, crea una nueva línea
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
                // Elimina la bebida de la lista usando el productoId
                viewModel.eliminarProducto(productoId,requireContext())
                // Decrementar el topMargin después de, eliminar
                // No decrementamos de inmediato, sino cuando se ha eliminado el item visualmente
                currentTopMargin -= resources.getDimensionPixelSize(R.dimen.frame_height)

                // Actualizamos el contenedor para que los elementos se reposicionen correctamente
                actualizarPosiciones()
            }
        }

            // Añadimos los TextView dentro del nuevo FrameLayout
            newFrameLayout.addView(textViewNombre)
            newFrameLayout.addView(textViewEliminar)
            newFrameLayout.addView(textViewCantidad)

            // Añadimos el nuevo FrameLayout al contenedor
            productosContainer.addView(newFrameLayout)

            // Incrementamos el topMargin para la siguiente bebida
            currentTopMargin += resources.getDimensionPixelSize(R.dimen.frame_height)

            // Guardamos la referencia del FrameLayout en el mapa
            productosMap[productoId] = newFrameLayout
        }
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

    private fun reloadFragment() {
        val transaction = parentFragmentManager.beginTransaction() // Usamos parentFragmentManager si está dentro de un Fragment
        transaction.replace(R.id.listaComandaFragment, this)  // R.id.fragment_container es el contenedor del fragmento
        transaction.commit()
    }
}
