package com.example.comanderoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TableLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Cocina : AppCompatActivity() {
    var tlProductos:TableLayout?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cocina)
        sacarRecibos()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun sacarRecibos(){
        val array = ArrayList<Productos>();
        array.add(Productos("Vino tinto", 4))
        array.add(Productos("cerveza", 9))
        for (producto in array) {
            val registro = LayoutInflater.from(this).inflate(R.layout.item_table_layout_pn,null,false)
            val nombre = registro.findViewById<View>(R.id.producto) as TextView
            val precio = registro.findViewById<View>(R.id.precio) as TextView
            nombre.setText(producto.nombre);
            precio.setText(producto.cantidad.toString())
            tlProductos?.addView(registro)
        }
    }
}