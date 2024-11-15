package com.example.comanderoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SesionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sesion)

        // Inicializar los EditText
        val etNombre: EditText = findViewById(R.id.et_nombre) // EditText para el nombre
        val etDni: EditText = findViewById(R.id.et_dni) // EditText para el DNI

        // Inicializar los botones
        val btnSiguiente: Button = findViewById(R.id.btn_siguiente)
        val btnRegistrar: Button = findViewById(R.id.btn_registrar)
        val btnSalirSesion: Button = findViewById(R.id.salir_sesion)

        // Acción al pulsar "Siguiente"
        btnSiguiente.setOnClickListener {
            // Redirigir al MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Acción al pulsar "Registrar"
        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val dni = etDni.text.toString()

            if (nombre.isNotEmpty() && dni.isNotEmpty()) {
                // Aquí puedes añadir el código para registrar al trabajador en la base de datos

                Toast.makeText(this, "Usuario registrado: $nombre", Toast.LENGTH_SHORT).show()

                // Redirigir al MainActivity después de registrar
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }

        // Acción al pulsar "Salir" para cerrar la aplicación
        btnSalirSesion.setOnClickListener {
            finishAffinity() // Cierra todas las actividades y finaliza la aplicación
        }
    }
}



