package com.example.comanderoapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider

class MenuComandas : AppCompatActivity() {
    @SuppressLint("WrongViewCast", "MissingInflatedId", "CutPasteId", "Range", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_comandas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadFragment(ComandasFragment())

        // Encuentra el btnBack
        val imageViewBack = findViewById<ImageView>(R.id.btnBack)
        imageViewBack.setOnClickListener {
            // Buscar el fragmento con el tag correcto
            val fragment = supportFragmentManager.findFragmentByTag(ComandasFragment::class.java.simpleName)

            Log.i("FragmentCheck", "Comprobando fragmento: ${fragment != null} y esVisible: ${fragment?.isVisible}")

            if (fragment != null && fragment.isVisible) {
                val intent = Intent(this, MesasActivity::class.java)
                startActivity(intent)
            } else {
                loadFragmentBack(ComandasFragment())
            }
        }

        // Encuentra el btnEdit
        val imageViewEdit = findViewById<ImageView>(R.id.btnEdit)
        imageViewEdit.setOnClickListener {
            // Buscar el fragmento con el tag correcto
            supportFragmentManager.findFragmentByTag(ComandasFragment::class.java.simpleName)

            loadFragment(ListaComanda())  // Carga el fragmento ListaComanda
        }

// Encuentra el btnSend
        val imageViewSend = findViewById<ImageView>(R.id.btnSend)
        imageViewSend.setOnClickListener {

            Log.i("btnSend", "Comanda Enviada")

            // Crear la comanda con precio 0 y numeroMesa 0
            val db = DataBaseHelper(this)
            val ordenCreacionComanda = """
        INSERT INTO comanda (preciopedido, numeroMesa)
        VALUES (0, 0)
    """.trimIndent()

            db.writableDatabase.execSQL(ordenCreacionComanda)

            // Obtener el último codigocomanda generado (última fila insertada)
            val query = "SELECT last_insert_rowid() AS codigocomanda"
            val cursor = db.readableDatabase.rawQuery(query, null)
            var codigoComanda = -1
            if (cursor.moveToFirst()) {
                codigoComanda = cursor.getInt(cursor.getColumnIndex("codigocomanda"))
            }
            cursor.close()

            // Obtener los productos seleccionados (suponiendo que los IDs de los productos están en el ViewModel)
            val viewModel = ViewModelProvider(this).get(ViewModelPedidos::class.java)
            val listaProductos = viewModel.productosList.value // Asumiendo que esta lista contiene los productoId's seleccionados

            if (codigoComanda != -1 && listaProductos != null) {
                // Insertar o actualizar cada producto en la tabla almacena
                for (productoId in listaProductos) {
                    // Verificar si el producto ya existe en la tabla almacena con el mismo codigocomanda
                    val queryExistencia = """
                SELECT cantidad FROM almacena WHERE productoId = ? AND codigocomanda = ?
            """.trimIndent()

                    val cursorExistente = db.readableDatabase.rawQuery(queryExistencia, arrayOf(productoId.toString(), codigoComanda.toString()))

                    if (cursorExistente.moveToFirst()) {
                        // Si ya existe, actualizar la cantidad
                        val cantidadExistente = cursorExistente.getInt(cursorExistente.getColumnIndex("cantidad"))
                        val nuevaCantidad = cantidadExistente + 1  // Incrementa la cantidad en 1 (puedes modificarlo)

                        val ordenActualizacion = """
                    UPDATE almacena SET cantidad = ? WHERE productoId = ? AND codigocomanda = ?
                """.trimIndent()

                        val stmtActualizar = db.writableDatabase.compileStatement(ordenActualizacion)
                        stmtActualizar.bindLong(1, nuevaCantidad.toLong()) // nueva cantidad
                        stmtActualizar.bindLong(2, productoId.toLong()) // productoId
                        stmtActualizar.bindLong(3, codigoComanda.toLong()) // codigocomanda
                        stmtActualizar.executeUpdateDelete()

                        Log.i("Comanda", "Cantidad del producto $productoId actualizada.")
                    } else {
                        // Si no existe, insertar el producto con cantidad 1
                        val cantidad = 1
                        val ordenInsercionAlmacena = """
                    INSERT INTO almacena (productoId, codigocomanda, cantidad)
                    VALUES (?, ?, ?)
                """.trimIndent()

                        val stmtInsertar = db.writableDatabase.compileStatement(ordenInsercionAlmacena)
                        stmtInsertar.bindLong(1, productoId.toLong()) // productoId
                        stmtInsertar.bindLong(2, codigoComanda.toLong()) // codigocomanda
                        stmtInsertar.bindLong(3, cantidad.toLong()) // cantidad
                        stmtInsertar.executeInsert()

                        Log.i("Comanda", "Producto $productoId añadido a almacena.")
                    }
                    cursorExistente.close()
                }
                Log.i("Comanda", "Comanda creada y productos añadidos/actualizados en la tabla almacena.")
            }
            loadFragment(ComandasFragment())
            viewModel.productosList.value?.clear()  // Limpia la lista
            db.close()
        }

        // Coge el numero de la mesa y lo muestra en la pantalla de camarero
        val mesa = findViewById<TextView>(R.id.textViewNMesaLabel)
        val terraza = findViewById<TextView>(R.id.textViewNMesaLabel)

        // Obtener el número de mesa del Intent
        val numeroMesa = intent.getIntExtra("numeroMesa", -1)

        Log.i("nMesa", numeroMesa.toString())

        // Si el número de mesa es válido (no es -1), entonces actualizamos el TextView
        if (numeroMesa != -1) {
            // Comprobamos cuál de los dos TextViews utilizar
            if (mesa.text.isEmpty()) {
                // Si el TextView 'mesa' está vacío, usamos este para mostrar el número de mesa
                mesa.text = numeroMesa.toString()
            } else {
                // Si el TextView 'terraza' está vacío, usamos este para mostrar el número de mesa
                terraza.text = numeroMesa.toString()
            }
        }

        // Coge el nombre del dni del trabajador
        var nombreDni = findViewById<TextView>(R.id.textViewAtendidoPorLabel)
        val dni = intent.getStringExtra("dni") // Obtén el DNI desde el Intent
        val db = DataBaseHelper(this) // Inicializa tu helper de base de datos

        // Verifica si el dni no es nulo
        if (dni != null) {
            val query = "SELECT nombre FROM trabajador WHERE dni = ?"
            val cursor = db.readableDatabase.rawQuery(query, arrayOf(dni)) // Ejecuta la consulta

            // Comprobar si se obtuvo algún resultado
            if (cursor.moveToFirst()) {
                // Obtén el nombre del cursor
                val nombre = cursor.getString(cursor.getColumnIndex("nombre"))

                // Establece el nombre en el TextView
                val nombreDni = findViewById<TextView>(R.id.textViewAtendidoPorLabel)
                nombreDni.text = nombre
            } else {
                // Si no se encuentra el trabajador, puedes mostrar un mensaje o dejar el TextView vacío
                nombreDni.text = "Trabajador no encontrado"
            }

            cursor.close() // Cierra el cursor
            db.close() // Cierra la base de datos
        } else {
            Log.e("Error", "DNI no proporcionado")
        }
    }

    // Metodo para cargar un fragmento
    private fun loadFragment(fragmentMENU: Fragment) {
        // Reemplaza el fragment en el FrameLayout
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayoutMENU, fragmentMENU) // R.id.frameLayout es el contenedor
        transaction.commit()
    }

    private fun loadFragmentBack(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayoutMENU, fragment, ComandasFragment::class.java.simpleName) // Usamos el tag
        transaction.addToBackStack(ComandasFragment::class.java.simpleName) // Aseguramos que se agregue a la pila
        transaction.commit()
    }
}