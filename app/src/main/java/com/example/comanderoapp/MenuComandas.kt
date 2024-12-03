package com.example.comanderoapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class MenuComandas : AppCompatActivity() {
    @SuppressLint("WrongViewCast", "MissingInflatedId", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_comandas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadFragment(ComandasFragment())

        // Encuentra los TextView
        val imageViewBack = findViewById<ImageView>(R.id.btnBack)
        imageViewBack.setOnClickListener {
            // Buscar el fragmento con el tag correcto
            val fragment = supportFragmentManager.findFragmentByTag(ComandasFragment::class.java.simpleName)

            Log.i("FragmentCheck", "Comprobando fragmento: ${fragment != null} y esVisible: ${fragment?.isVisible}")

            if (fragment != null && fragment.isVisible) {
                val intent = Intent(this, MesasActivity::class.java)
                startActivity(intent)
            } else {
                loadFragment2(ComandasFragment())
            }
        }

        // Encuentra los TextView
        val imageViewEdit = findViewById<ImageView>(R.id.btnEdit)
        imageViewEdit.setOnClickListener {
            // Buscar el fragmento con el tag correcto
            val fragment = supportFragmentManager.findFragmentByTag(ComandasFragment::class.java.simpleName)

            // Si el fragmento no está visible, reemplazamos el fragmento actual con ComandasFragment
            if (fragment == null || !fragment.isVisible) {
                loadFragment(ListaComanda())  // Carga el fragmento
            }
            // Si ya está visible, no necesitamos hacer nada
        }


        val mesa = findViewById<TextView>(R.id.textViewNMesaLabel)
        val terraza = findViewById<TextView>(R.id.textViewNMesaLabel)

        // Obtener el número de mesa del Intent
        val numeroMesa = intent.getIntExtra("numeroMesa", -1)

        Log.i("nMesa", numeroMesa.toString())

        // Si el número de mesa es válido (no es -1), entonces actualizamos el TextView
        if (numeroMesa != -1) {
            // Comprobamos cuál de los dos TextViews utilizar
            if (mesa.text.isEmpty()) {
                // Si el TextView 'mesa' está vacío, usamos este para mostrar el número de mesa
                mesa.text = numeroMesa.toString()
            } else {
                // Si el TextView 'terraza' está vacío, usamos este para mostrar el número de mesa
                terraza.text = numeroMesa.toString()
            }
        }

    }

    // Método para cargar un fragmento
    private fun loadFragment(fragmentMENU: Fragment) {
        // Reemplaza el fragment en el FrameLayout
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayoutMENU, fragmentMENU) // R.id.frameLayout es el contenedor
        transaction.commit()
    }

    private fun loadFragment2(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayoutMENU, fragment, ComandasFragment::class.java.simpleName) // Usamos el tag
        transaction.addToBackStack(ComandasFragment::class.java.simpleName) // Aseguramos que se agregue a la pila
        transaction.commit()
    }
}