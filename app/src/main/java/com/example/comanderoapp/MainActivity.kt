package com.example.comanderoapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Configuración de sistema edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configuración del clic para navegar a MesasActivity
        val camareroButton: Button = findViewById(R.id.camarero)
        camareroButton.setOnClickListener {
            val intent = Intent(this, MesasActivity::class.java)
            startActivity(intent)
        }

        // Configura el botón "EXIT" para cerrar la aplicación
        val salirButton: Button = findViewById(R.id.salir)
        salirButton.setOnClickListener {
            finishAffinity() // Cierra todas las actividades y finaliza la aplicación
        }
    }
}