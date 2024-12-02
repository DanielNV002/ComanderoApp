package com.example.comanderoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MesasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mesas)

        // Manejo de insets para el diseño Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configura el botón "SALA" para regresar a TerrazaActivity
        val salaButton: Button = findViewById(R.id.salaButton)
        salaButton.setOnClickListener {
            val intent = Intent(this, TerrazaActivity::class.java)
            startActivity(intent)
        }

        // Configura el botón "BACK" para regresar a MainActivity
        val atrasButton: Button = findViewById(R.id.atras)
        atrasButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Cierra SalaActivity para evitar apilar actividades
        }


    }
}
