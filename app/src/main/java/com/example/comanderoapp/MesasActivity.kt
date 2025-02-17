package com.example.comanderoapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

var dni: String?=null

class MesasActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mesas)

        dni = intent.getStringExtra("dni")

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
            intent.putExtra("dni", dni)
            startActivity(intent)
        }

        // Configura el botón "BACK" para regresar a MainActivity
        val atrasButton: Button = findViewById(R.id.atras)
        atrasButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("dni", dni)
            startActivity(intent)
            finish() // Cierra SalaActivity para evitar apilar actividades
        }

// Suponiendo que tienes 10 ImageViews (msala1, msala2, ..., msala10)
        val mesas = arrayOf(
            R.id.msala1, R.id.msala2, R.id.msala3, R.id.msala4, R.id.msala5,
            R.id.msala6, R.id.msala7, R.id.msala8, R.id.msala9,
        )
// Recorremos el array de identificadores
        for (i in mesas.indices) {
            val mesa = findViewById<ImageView>(mesas[i])
            mesa?.setOnClickListener {
                val numeroMesa = i + 1 // Sumar 1 para que el número de la mesa empiece en 1
                val intent = Intent(this, MenuComandas::class.java)
                intent.putExtra("numeroMesa",numeroMesa)
                intent.putExtra("dni",dni)
                startActivity(intent)
            }
        }
    }
}
