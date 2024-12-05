package com.example.comanderoapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Guideline
import androidx.core.os.bundleOf
import androidx.core.view.LayoutInflaterCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.comanderoapp.Comanda.Companion.NUMERO_BUNDLE
import com.example.comanderoapp.Comanda.Companion.TIPO_BUNDLE

class Cocina : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cocina)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scrollView)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // boton para recargar los datos de las mesa
        val boton : Button = findViewById((R.id.recargar))
        boton.setOnClickListener {
            recargarPedido()
        }

        // Configura el botón "BACK" para regresar a MainActivity
        val atrasButton: Button = findViewById(R.id.atras)
        atrasButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Cierra SalaActivity para evitar apilar actividades
        }

        //para que cada vez que de la vuelta el movil no recarge más de una vez el fragmente
        if(savedInstanceState == null){
            val bundle = bundleOf(
                TIPO_BUNDLE to "BEBIDAS")
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<Comanda>(R.id.fragmentBebidas, args = bundle)
            }
        }
        if(savedInstanceState == null){
            val bundle = bundleOf(
                TIPO_BUNDLE to "COMIDAS")
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<Comanda>(R.id.fragmentComidas, args = bundle)
            }
        }
    }

    fun recargarPedido() {
        var base =  DataBaseHelper(this)
        val fragmentBebidas = supportFragmentManager.findFragmentById(R.id.fragmentBebidas) as? Comanda
        val fragmentComidas = supportFragmentManager.findFragmentById(R.id.fragmentComidas) as? Comanda

        val seleccionadosBebidas = fragmentBebidas?.getElementosSeleccionados() ?: emptyList()
        val seleccionadosComidas = fragmentComidas?.getElementosSeleccionados() ?: emptyList()

        val idsAEliminar = seleccionadosBebidas + seleccionadosComidas

        // Elimina de la base de datos
        idsAEliminar.forEach { (idComanda, idProducto) ->
            val num = base.eliminarProducto(idComanda, idProducto)
            Log.i("Comandas",num.toString())
        }

        // Recargar los fragments
        fragmentBebidas?.sacarRecibos()
        fragmentComidas?.sacarRecibos()
    }



}