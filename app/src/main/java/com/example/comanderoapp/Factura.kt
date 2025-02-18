package com.example.comanderoapp

import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity



class Factura : AppCompatActivity() {
    private var tlFactura: TableLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_factura)

        tlFactura = findViewById(R.id.TLFactura)
        val spinnerMesas = findViewById<Spinner>(R.id.spinnerMesas)
        val botonImprimir = findViewById<ImageButton>(R.id.BotonImprimir)

        // Obtener las mesas con comandas pendientes
        val mesasPendientes = obtenerMesasPendientes()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mesasPendientes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMesas.adapter = adapter

        // Cargar la factura cuando se selecciona una mesa
        spinnerMesas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val mesaSeleccionada = parent.getItemAtPosition(position).toString()
                llenarFactura(mesaSeleccionada.toInt())  // Convertimos el número de mesa a Int
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

                // Manejo del botón Imprimir
                botonImprimir.setOnClickListener {
                val mesaSeleccionada = spinnerMesas.selectedItem?.toString()?.toIntOrNull()
                if (mesaSeleccionada != null) {
                    marcarComandaComoPagada(mesaSeleccionada)
                    tlFactura?.removeAllViews()
                    Toast.makeText(this, "Comanda impresa y marcada como pagada", Toast.LENGTH_SHORT).show()

                // Recargar las mesas disponibles
                val nuevasMesas = obtenerMesasPendientes()
                adapter.clear()
                adapter.addAll(nuevasMesas)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "No hay mesas pendientes", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Obtener mesas con comandas pendientes de pago
    fun obtenerMesasPendientes(): List<String> {
        val conecta = DataBaseHelper(this)
        val bbdd = conecta.readableDatabase
        val listaMesas = mutableListOf<String>()

        val query = "SELECT DISTINCT numeroMesa FROM comanda WHERE pagado = 0"
        val fila = bbdd.rawQuery(query, null)

        if (fila.moveToFirst()) {
            do {
                listaMesas.add(fila.getInt(0).toString())
            } while (fila.moveToNext())
        }

        fila.close()
        bbdd.close()
        return listaMesas
    }

    // Llenar factura con la comanda de la mesa seleccionada
    fun llenarFactura(numeroMesa: Int) {
        val conecta = DataBaseHelper(this)
        val bbdd = conecta.readableDatabase

        val query = """
            SELECT p.nombre AS nombreProducto, a.cantidad, p.precio AS precioUnitario, 
                   (a.cantidad * p.precio) AS precioFinal
            FROM almacena a
            INNER JOIN producto p ON a.productoId = p.productoId
            INNER JOIN comanda c ON a.codigocomanda = c.codigocomanda
            WHERE c.numeroMesa = ? AND c.pagado = 0
        """

        val fila = bbdd.rawQuery(query, arrayOf(numeroMesa.toString()))
        var precioTotal = 0.0

        tlFactura?.removeAllViews()  // Limpiar la factura antes de llenarla

        if (fila.moveToFirst()) {
            do {
                val registrar = LayoutInflater.from(this).inflate(R.layout.gestion_factura, null, false)

                val textViewNombreProducto = registrar.findViewById<TextView>(R.id.textViewNombreProducto)
                val textViewCantidad = registrar.findViewById<TextView>(R.id.textViewCantidad)
                val textViewPrecioUnitario = registrar.findViewById<TextView>(R.id.TextViewPrecioUnitario)
                val textViewPrecioTotal = registrar.findViewById<TextView>(R.id.TextViewPrecioFinal)

                textViewNombreProducto.text = fila.getString(fila.getColumnIndexOrThrow("nombreProducto"))
                textViewCantidad.text = fila.getInt(fila.getColumnIndexOrThrow("cantidad")).toString()
                textViewPrecioUnitario.text = fila.getDouble(fila.getColumnIndexOrThrow("precioUnitario")).toString()
                textViewPrecioTotal.text = fila.getDouble(fila.getColumnIndexOrThrow("precioFinal")).toString()

                precioTotal += fila.getDouble(fila.getColumnIndexOrThrow("precioFinal"))

                tlFactura?.addView(registrar)

            } while (fila.moveToNext())
        }

        val textViewPrecioTotal = findViewById<TextView>(R.id.textViewPrecioTotal)
        textViewPrecioTotal.text = "TOTAL: $precioTotal"

        fila.close()
        bbdd.close()
    }

    // Marcar la comanda de la mesa como pagada
    fun marcarComandaComoPagada(numeroMesa: Int) {
        val conecta = DataBaseHelper(this)
        val bbdd = conecta.writableDatabase

        val valores = ContentValues().apply {
            put("pagado", 1)
        }

        bbdd.update("comanda", valores, "numeroMesa = ? AND pagado = 0", arrayOf(numeroMesa.toString()))
        bbdd.close()
    }
 }
