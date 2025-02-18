package com.example.comanderoapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast;
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

        //obtener numero de mesa del Intent
        val numeroMesa = intent.getIntExtra("numeroMesa", -1) // -1 es un valor por defecto si no se encuentra el extra

        if (numeroMesa == -1){
            Toast.makeText(this, "No se encontro la mesa", Toast.LENGTH_SHORT).show()

            finish() // Cierra la actividad si no hay número de mesa
        }
        // Mostrar el número de mesa en el TextView
        val textViewNumeroMesa = findViewById<TextView>(R.id.textViewNumeroMesa)
        textViewNumeroMesa.text = numeroMesa.toString()



        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_factura)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets




        }



        tlFactura=findViewById(R.id.TLFactura)


        // Configurar el botón de imprimir para restablecer el estado
        val botonImprimir = findViewById<ImageButton>(R.id.BotonImprimir)
        botonImprimir.setOnClickListener {
            // Limpia el contenido del TableLayout
            resetFactura()
            val toast = Toast.makeText(this, "comanda impresa", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0) // Cambia la posición
            toast.show()
        }

        llenarFactura()

        // Configura el botón "back" para regresar a TerrazaActivity
        val buttonVolver: Button = findViewById(R.id.buttonVolver)
        buttonVolver.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
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
        INNER JOIN comanda c ON a.codigocomanda = c.codigocomanda
        WHERE c.numeroMesa = ? and c.pagado = 0
    """

        val fila = bbdd.rawQuery(query, null)
        var precioTotal = 0.0

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

                //sumar precio total
                precioTotal += fila.getDouble(fila.getColumnIndexOrThrow("precioFinal"))

                // Agregar la vista inflada a la tabla
                tlFactura?.addView(registrar)

            } while (fila.moveToNext())
        }

        //mostrar precio total
        val TextViewPrecioTotal = findViewById<TextView>(R.id.TextViewPrecioFinal)
            TextViewPrecioTotal.text = "TOTAK: $precioTotal #"

        // Cerrar el cursor y la conexión a la base de datos
        fila.close()
        bbdd.close()
    }

    fun seleccionarNumeroMesa(){ //vacio de mo
        val conecta = DataBaseHelper(this)
        val bbdd=conecta.readableDatabase



    }


    fun estaPagado(){
        val conecta = DataBaseHelper(this)
        val bbdd=conecta.readableDatabase



    }

    // Método para restablecer el estado del TableLayout
    fun resetFactura() {
        tlFactura?.removeAllViews() // Elimina todas las filas del TableLayout

    }
 }
