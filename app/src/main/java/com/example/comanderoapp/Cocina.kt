package com.example.comanderoapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
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
        //para que cada vez que de la vuelta el movil no recarge más de una vez el fragmente
        if(savedInstanceState == null){
            val bundle = bundleOf(
                TIPO_BUNDLE to "Bebidas",
                NUMERO_BUNDLE to "1")
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<Comanda>(R.id.fragmentBebidas, args = bundle)
            }
        }
        if(savedInstanceState == null){
            val bundle = bundleOf(
                TIPO_BUNDLE to "Comidas",
                NUMERO_BUNDLE to "1")
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<Comanda>(R.id.fragmentComidas, args = bundle)
            }
        }
    }

    fun recargarPedido(){
        //Funcion que recarga las tablas

    }
}