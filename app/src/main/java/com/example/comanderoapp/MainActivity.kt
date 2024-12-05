package com.example.comanderoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar el helper de base de datos
        dbHelper = DataBaseHelper(this)

        // Referencias a los botones y el campo de texto
        val dniEditText: EditText = findViewById(R.id.nombre) // Campo para DNI
        val camareroButton: Button = findViewById(R.id.camarero)
        val cocinaButton: Button = findViewById(R.id.cocina)
        val barraButton: Button = findViewById(R.id.barra)
        val salirButton: Button = findViewById(R.id.salir)

        // Configuración de los botones
        camareroButton.setOnClickListener {
            val dni = dniEditText.text.toString().trim().uppercase() // Convertir DNI a mayúsculas

            if (dbHelper.comprobarDniExistente(dni)) {
                val intent = Intent(this, MesasActivity::class.java)
                intent.putExtra("dni",dni)
                startActivity(intent)
            } else {
                mostrarToastDniNoValido()
            }
        }

        cocinaButton.setOnClickListener {
            val dni = dniEditText.text.toString().trim().uppercase() // Convertir DNI a mayúsculas
            if (dbHelper.comprobarDniExistente(dni)) {
                val intent = Intent(this, Cocina::class.java)  // Navegar a la actividad Cocina
                startActivity(intent)
            } else {
                mostrarToastDniNoValido()
            }
        }


        barraButton.setOnClickListener {
            val dni = dniEditText.text.toString().trim().uppercase()  // Convertir DNI a mayúsculas
            if (dbHelper.comprobarDniExistente(dni)) {
                val intent = Intent(this, Factura::class.java)  // Navegar a la actividad Factura
                startActivity(intent)
            } else {
                mostrarToastDniNoValido()
            }
        }

        salirButton.setOnClickListener {
            val intent = Intent(this, SesionActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun mostrarToastDniNoValido() {
        Toast.makeText(this, "DNI no registrado. Acceso denegado.", Toast.LENGTH_SHORT).show()
    }
}
