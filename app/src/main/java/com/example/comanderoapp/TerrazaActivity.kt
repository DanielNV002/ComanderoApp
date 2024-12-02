package com.example.comanderoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TerrazaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_terraza)

        // Manejo de insets para el diseño Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configura el botón "TERRAZA" para navegar a SalaActivity
        val terrazaButton: Button = findViewById(R.id.terrazaButton)
        terrazaButton.setOnClickListener {
            val intent = Intent(this, MesasActivity::class.java)
            startActivity(intent)
        }

        // Configura el botón "BACK" para regresar a MainActivity
        val atrasButton: Button = findViewById(R.id.atras)
        atrasButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Cierra MesasActivity para evitar apilar actividades
        }

        // Suponiendo que tienes 10 ImageViews (msala1, msala2, ..., msala10)
        val terrazas = arrayOf(
            R.id.mterraza10, R.id.mterraza11, R.id.mterraza12, R.id.mterraza13, R.id.mterraza14,
            R.id.mterraza15, R.id.mterraza16, R.id.mterraza17, R.id.mterraza18
        )

// Recorremos el array de identificadores
        for (i in terrazas.indices) {
            val mesa = findViewById<ImageView>(terrazas[i])
            mesa?.setOnClickListener {
                val numeroTerraza = i + 10 // Sumar 1 para que el número de la mesa empiece en 1
                val intent = Intent(this, MenuComandas::class.java)
                intent.putExtra("numeroMesa",numeroTerraza)
                startActivity(intent)
            }
        }
    }
}

