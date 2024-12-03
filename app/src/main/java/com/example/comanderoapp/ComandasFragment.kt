package com.example.comanderoapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class ComandasFragment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout para este fragmento (fragment_comandas.xml)
        val rootView = inflater.inflate(R.layout.fragment_comandas, container, false)

        // Encuentra los ImageView dentro de rootView (el layout del fragmento)
        val imageViewBebidas = rootView.findViewById<ImageView>(R.id.imageViewBebidas)
        val imageViewEntrantes = rootView.findViewById<ImageView>(R.id.imageViewEntrantes)
        val imageViewPplato = rootView.findViewById<ImageView>(R.id.imageViewPplato)
        val imageViewSplato = rootView.findViewById<ImageView>(R.id.imageViewSplato)
        val imageViewPostres = rootView.findViewById<ImageView>(R.id.imageViewPostres)
        val imageViewInfantil = rootView.findViewById<ImageView>(R.id.imageViewInfantil)
        val imageViewVinos = rootView.findViewById<ImageView>(R.id.imageViewVinos)
        val imageViewMdia = rootView.findViewById<ImageView>(R.id.imageViewMdia)

        // Configura el OnClickListener para los ImageViews en rootView
        imageViewBebidas.setOnClickListener {
            loadFragment(BebidasFragment())  // Carga el fragmento de Bebidas
        }

        imageViewEntrantes.setOnClickListener {
            loadFragment(EntrantesFragment())  // Carga el fragmento de Entrantes
        }

        imageViewPplato.setOnClickListener {
            loadFragment(PplatoFragment())  // Carga el fragmento del primer plato
        }

        imageViewSplato.setOnClickListener {
            loadFragment(SplatoFragment())  // Carga el fragmento del segundo plato
        }

        imageViewPostres.setOnClickListener {
            loadFragment(PostresFragment())  // Carga el fragmento del postres
        }

        imageViewInfantil.setOnClickListener {
            loadFragment(InfantilFragment())  // Carga el fragmento del menu infantil
        }

        imageViewVinos.setOnClickListener {
            loadFragment(VinosFragment())  // Carga el fragmento de los vinos
        }

        imageViewMdia.setOnClickListener {
            loadFragment(MdiaFragment())  // Carga el fragmento del menu del dia
        }

        // Devuelve solo el rootView del fragmento, que es donde se deben manejar las interacciones.
        return rootView
    }

    // Método para cargar un fragmento
    private fun loadFragment(fragmentMENU: Fragment) {
        // Usar supportFragmentManager si el fragmento es el fragmento principal de la actividad
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayoutMENU, fragmentMENU) // R.id.frameLayoutMENU es el contenedor
        transaction.commit()
    }
}
