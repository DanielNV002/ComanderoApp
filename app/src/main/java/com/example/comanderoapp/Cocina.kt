package com.example.comanderoapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Cocina : AppCompatActivity() {
    //ARRAYLIST auxiliares mientras no esta la DB
    val arrayB = ArrayList<Productos>()
    val arrayP = ArrayList<Productos>()
    //Tables de cada tipo de producto bebidas y primeros
    var tlBebidas:TableLayout?=null
    var tlPrimeros:TableLayout?=null
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
        //Busacamos el elemento en el layout y ecribimos los datos
        tlBebidas=findViewById(R.id.tlBebidas)
        tlPrimeros=findViewById(R.id.tlPrimeros)
        sacarRecibos()
    }

    fun sacarRecibos(){
        //porbisional hasta BD
        arrayB.add(Productos("Vino tinto", 4))
        arrayB.add(Productos("Cerveza", 9))
        arrayP.add(Productos("Carrilada con castañas", 2))
        arrayP.add(Productos("Choco a la plancha", 3))
        //pasamos por todas las bebidas
        for (producto in arrayB) {
            //sacamos la variable de las filas de las tablas
            val registro = LayoutInflater.from(this).inflate(R.layout.item_table_layout_pn,null,false)
            //literalmente es una fila con puntos no hay más
            val puntos = LayoutInflater.from(this).inflate(R.layout.item_table_layout_puntos,null,false)
            //cojemos los dos registros de cada TextView
            val nombre = registro.findViewById<View>(R.id.producto) as TextView
            val cantidad = registro.findViewById<View>(R.id.cantidad) as TextView
            //metemos los valores a cada TextView
            nombre.setText(producto.nombre);
            val numero = "x"+producto.cantidad.toString()
            cantidad.setText(numero)
            //metmos las dos filas en la tabla
            tlBebidas?.addView(registro)
            tlBebidas?.addView(puntos)
        }
        //el mismo codigo que el de arriba simplemnte es otra tabla
        for(producto in arrayP){
            val registro = LayoutInflater.from(this).inflate(R.layout.item_table_layout_pn,null,false)
            val puntos = LayoutInflater.from(this).inflate(R.layout.item_table_layout_puntos,null,false)

            val nombre = registro.findViewById<View>(R.id.producto) as TextView
            val cantidad = registro.findViewById<View>(R.id.cantidad) as TextView

            nombre.setText(producto.nombre)
            val numero = "x"+producto.cantidad.toString()
            cantidad.setText(numero)

            tlPrimeros?.addView(registro)
            tlPrimeros?.addView(puntos)
        }
    }

    fun recargarPedido(){
        //Funcion que recarga las tablas
        tlBebidas?.removeAllViews()
        tlPrimeros?.removeAllViews()
        sacarRecibos()
    }
}