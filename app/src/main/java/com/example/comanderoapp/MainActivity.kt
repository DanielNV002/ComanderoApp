package com.example.comanderoapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TableLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.comanderoapp.R.*
import com.example.comanderoapp.R.id.*

class MainActivity : AppCompatActivity() {

    var tlFactura:TableLayout?=null

    val facturaPrueba = ArrayList<FacturaComanda>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        tlFactura=findViewById(R.id.TlRowFactura)


        llenarFactura()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun llenarFactura(){
        // val conecta = SQLlite(this,"commandero", null, 1)
        // de error por que la base de datos no esta relizada,
        // COMPLETAR BASE DE DATOS DESPUES DE REALIZAR LA LOGICA DE LA FACTURA

        facturaPrueba.add( FacturaComanda("Cola Cola", 1, 0.9F))
        facturaPrueba.add( FacturaComanda("Doritos", 2, 1.10F))


            /* val fila = bbdd.rawquery("select nombreproducto, cantidad, preciounitario, preciofinal from productos", null)
            *fila.moveToFirst()
            * */

            for(factura in facturaPrueba){

            //Este chorizo "Infla" de datos la tabla, y no va a para hasta mientras sigan
            val Registrar = LayoutInflater.from(this).inflate(R.layout.gestion_factura, null, false)

            //se buscan lod datos a nuestra tabla
            val textViewNombreProducto = Registrar.findViewById<View>(R.id.textViewNombreProducto) as TextView
            val textViewCantidad = Registrar.findViewById<View>(R.id.textViewCantidad) as TextView
            val TextViewPrecioUnitario = Registrar.findViewById<View>(R.id.TextViewPrecioUnitario) as TextView
            val TextViewPrecioTotal= Registrar.findViewById<View>(R.id.TextViewPrecioFinal) as TextView

            //ahora se muestran en texto (de momento da error por que no hay variable "fila"
            textViewNombreProducto.setText(factura.producto)
            textViewCantidad.setText(factura.cantidad)
            TextViewPrecioUnitario.setText(factura.precioUnidad.toString())
            TextViewPrecioTotal.setText(factura.precioTotal.toString())
            tlFactura?.addView(Registrar)
            }

    }



}