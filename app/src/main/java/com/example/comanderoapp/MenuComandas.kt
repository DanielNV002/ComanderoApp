package com.example.comanderoapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class MenuComandas : AppCompatActivity() {
    @SuppressLint("WrongViewCast", "MissingInflatedId")
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

        // Listener para "Bebidas"
        imageViewBack.setOnClickListener {
            loadFragment(ComandasFragment())  // Carga el fragmento de Bebidas
        }



    }

    // Método para cargar un fragmento
    private fun loadFragment(fragmentMENU: Fragment) {
        // Reemplaza el fragment en el FrameLayout
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayoutMENU, fragmentMENU) // R.id.frameLayout es el contenedor
        transaction.commit()
    }

}