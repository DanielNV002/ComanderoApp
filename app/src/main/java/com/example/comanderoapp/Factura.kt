package com.example.comanderoapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Factura : AppCompatActivity() {

    private var tlFactura: TableLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_factura)

        tlFactura = findViewById(R.id.TLFactura)
        val spinnerMesas = findViewById<Spinner>(R.id.spinnerMesas)
        val botonImprimir = findViewById<ImageButton>(R.id.BotonImprimir)
        val botonBack: Button = findViewById(R.id.botonBack)

        // Obtener las mesas con comandas pendientes
        val mesasPendientes = obtenerMesasPendientes()
        Log.d("FacturaActivity", "Mesas pendientes: ${mesasPendientes.joinToString()}")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mesasPendientes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMesas.adapter = adapter

        // Cargar la factura cuando se selecciona una mesa
        spinnerMesas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val mesaSeleccionada = parent.getItemAtPosition(position).toString()
                Log.d("FacturaActivity", "Mesa seleccionada: $mesaSeleccionada")
                llenarFactura(mesaSeleccionada.toInt())
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
                Log.d("FacturaActivity", "Nuevas mesas pendientes: ${nuevasMesas.joinToString()}")
                adapter.clear()
                adapter.addAll(nuevasMesas)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "No hay mesas pendientes", Toast.LENGTH_SHORT).show()
            }
        }

        // Configura el botón "Back" para regresar a MainActivity
        botonBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
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
                val numeroMesa = fila.getInt(0).toString()
                listaMesas.add(numeroMesa)
                Log.d("FacturaActivity", "Mesa pendiente encontrada: $numeroMesa")
            } while (fila.moveToNext())
        } else {
            Log.d("FacturaActivity", "No se encontraron mesas pendientes")
        }

        fila.close()
        bbdd.close()
        return listaMesas
    }

    // Llenar factura con la comanda de la mesa seleccionada
    @SuppressLint("SetTextI18n", "InflateParams")
    fun llenarFactura(numeroMesa: Int) {
        Log.i("muestra"," "+numeroMesa)
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

        tlFactura?.addView(LayoutInflater.from(this).inflate(R.layout.tabla_titulos, null, false))

        if (!fila.moveToFirst()) {
            Log.d("FacturaActivity", "No se encontraron productos para la mesa $numeroMesa")
            Toast.makeText(this, "No se encontraron productos para la mesa seleccionada", Toast.LENGTH_SHORT).show()
        } else {
            do {
                val nombreProducto = fila.getString(fila.getColumnIndexOrThrow("nombreProducto"))
                val cantidad = fila.getInt(fila.getColumnIndexOrThrow("cantidad"))
                val precioUnitario = fila.getDouble(fila.getColumnIndexOrThrow("precioUnitario"))
                val precioFinal = fila.getDouble(fila.getColumnIndexOrThrow("precioFinal"))

                Log.d("FacturaActivity", "Producto: $nombreProducto, Cantidad: $cantidad, Precio: $precioFinal")

                val registrar = LayoutInflater.from(this).inflate(R.layout.gestion_factura, null, false)

                val textViewNombreProducto = registrar.findViewById<TextView>(R.id.textViewNombreProducto)
                val textViewCantidad = registrar.findViewById<TextView>(R.id.textViewCantidad)
                val textViewPrecioUnitario = registrar.findViewById<TextView>(R.id.TextViewPrecioUnitario)
                val textViewPrecioTotal = registrar.findViewById<TextView>(R.id.TextViewPrecioFinal)

                textViewNombreProducto.text = nombreProducto
                textViewCantidad.text = cantidad.toString()
                textViewPrecioUnitario.text = precioUnitario.toString()+"€"
                textViewPrecioTotal.text = precioFinal.toString()+"€"

                precioTotal += precioFinal

                tlFactura?.addView(registrar)

            } while (fila.moveToNext())
        }

        val textViewPrecioTotal = findViewById<TextView>(R.id.textViewPrecioTotal)
        textViewPrecioTotal.text = "TOTAL: $precioTotal€"

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