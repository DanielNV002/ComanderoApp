package com.example.comanderoapp

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TableLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.comanderoapp.R.id.main


class Factura : AppCompatActivity() {
    var tlFactura: TableLayout?=null

    val facturaPrueba = ArrayList<FacturaComanda>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_factura)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }


        tlFactura=findViewById(R.id.TLFactura)


        llenarFactura()
    }

    @SuppressLint("Recycle")
    fun llenarFactura(){
        val conecta = DataBaseHelper(this)
        val bbdd=conecta.readableDatabase

        // Consulta para obtener los datos de los productos en la factura
        val query = """
        SELECT p.nombre AS nombreProducto, a.cantidad, p.precio AS precioUnitario, 
               (a.cantidad * p.precio) AS precioFinal
        FROM almacena a
        INNER JOIN producto p ON a.productoId = p.productoId
    """

        val fila = bbdd.rawQuery(query, null)

        if (fila.moveToFirst()) {
            do {
                // Inflar la vista para cada fila de la factura
                val registrar = LayoutInflater.from(this).inflate(R.layout.gestion_factura, null, false)

                // Referencias a los TextView del layout
                val textViewNombreProducto = registrar.findViewById<TextView>(R.id.textViewNombreProducto)
                val textViewCantidad = registrar.findViewById<TextView>(R.id.textViewCantidad)
                val textViewPrecioUnitario = registrar.findViewById<TextView>(R.id.TextViewPrecioUnitario)
                val textViewPrecioTotal = registrar.findViewById<TextView>(R.id.TextViewPrecioFinal)

                // Asignar los valores desde la base de datos
                textViewNombreProducto.text = fila.getString(fila.getColumnIndexOrThrow("nombreProducto")).toString()
                textViewCantidad.text = fila.getInt(fila.getColumnIndexOrThrow("cantidad")).toString()
                textViewPrecioUnitario.text = fila.getDouble(fila.getColumnIndexOrThrow("precioUnitario")).toString()
                textViewPrecioTotal.text = fila.getDouble(fila.getColumnIndexOrThrow("precioFinal")).toString()

                // Agregar la vista inflada a la tabla
                tlFactura?.addView(registrar)

            } while (fila.moveToNext())
        }

        // Cerrar el cursor y la conexión a la base de datos
        fila.close()
        bbdd.close()
    }


 }
