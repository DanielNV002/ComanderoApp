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
        val base = DataBaseHelper(context = this)

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
            try {
                val nombre = etNombre.text.toString().trim()
                var dni = etDni.text.toString().trim()

                if (validarNombre(nombre) && validarDNI(dni, base)) {
                    // Convertir DNI a mayúsculas antes de guardarlo
                    dni = dni.uppercase()

                    base.anadirTrabajador(dni, nombre)
                    Toast.makeText(this, "Usuario registrado: $nombre", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace() // Muestra el error en el Logcat
                Toast.makeText(this, "Ocurrió un error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        // Acción al pulsar "Salir" para cerrar la aplicación
        btnSalirSesion.setOnClickListener {
            finishAffinity() // Cierra todas las actividades y finaliza la aplicación
        }
    }

    private fun validarNombre(nombre: String): Boolean {
        return if (nombre.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$"))) {
            true
        } else {
            Toast.makeText(this, "Nombre no válido. Solo se permiten letras y espacios.", Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun validarDNI(dni: String, base: DataBaseHelper): Boolean {
        if (!dni.matches(Regex("^[0-9]{8}[A-Za-z]$"))) {
            Toast.makeText(this, "DNI no válido. Debe tener 8 dígitos y una letra.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (base.comprobarDniExistente(dni.uppercase())) { // Comprobación con la letra en mayúsculas
            Toast.makeText(this, "El DNI ya está registrado en la base de datos.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
