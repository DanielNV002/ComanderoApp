package com.example.comanderoapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, "basedatoscomandero.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        // Crear tabla trabajador
        val ordenCreacionTrabajador = """
            CREATE TABLE trabajador (
                posicion INTEGER PRIMARY KEY AUTOINCREMENT, 
                dni TEXT NOT NULL UNIQUE,
                nombre TEXT NOT NULL
            )
        """.trimIndent()

        db!!.execSQL(ordenCreacionTrabajador) // Ejecutar la instrucción de creación

        // Crear tabla mesa
        val ordenCreacionMesa = """
            CREATE TABLE mesa (
                numero INTEGER PRIMARY KEY,
                tipo_mesa TEXT NOT NULL CHECK (tipo_mesa IN ('sala', 'terraza'))
            )
        """.trimIndent()

        db.execSQL(ordenCreacionMesa)

        // Crear tabla comanda
        val ordenCreacionComanda = """
            CREATE TABLE comanda (
                codigocomanda INTEGER PRIMARY KEY AUTOINCREMENT,
                preciopedido REAL NOT NULL
            )
        """.trimIndent()

        db.execSQL(ordenCreacionComanda)

        // Crear tabla producto
        val ordenCreacionProducto = """
            CREATE TABLE producto (
                productoId INTEGER PRIMARY KEY AUTOINCREMENT,
                tipoproducto TEXT NOT NULL CHECK (tipoproducto IN ('bebidas', 'entrantes', 'plato1', 'plato2', 'postres', 'infantil', 'vinos', 'menudeldia')),
                nombre TEXT NOT NULL,
                precio REAL NOT NULL
            )
        """.trimIndent()

        db.execSQL(ordenCreacionProducto)

        // Crear tabla almacena
        val ordenCreacionAlmacena = """
            CREATE TABLE almacena (
                productoId INTEGER NOT NULL,
                codigocomanda INTEGER NOT NULL,
                cantidad INTEGER NOT NULL CHECK (cantidad > 0),
                PRIMARY KEY (productoId, codigocomanda),
                FOREIGN KEY (productoId) REFERENCES producto(productoId),
                FOREIGN KEY (codigocomanda) REFERENCES comanda(codigocomanda)
            )
        """.trimIndent()

        db.execSQL(ordenCreacionAlmacena)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Borrar tablas si ya existen y recrearlas
        db!!.execSQL("DROP TABLE IF EXISTS trabajador")
        db.execSQL("DROP TABLE IF EXISTS mesa")
        db.execSQL("DROP TABLE IF EXISTS comanda")
        db.execSQL("DROP TABLE IF EXISTS producto")
        db.execSQL("DROP TABLE IF EXISTS almacena")
        onCreate(db)
    }

    /**
     * Función para añadir un trabajador a la base de datos.
     */
    fun anadirTrabajador(dni: String, nombre: String) {
        val datos = ContentValues()
        datos.put("dni", dni)        // Agregar DNI
        datos.put("nombre", nombre) // Agregar nombre

        val db = this.writableDatabase
        db.insert("trabajador", null, datos) // Insertar datos en la tabla trabajador
        db.close() // Cerrar conexión
    }

    /**
     * Función para obtener un trabajador por DNI.
     */
    fun obtenerTrabajadorPorDni(dni: String): Cursor? {
        val db = this.readableDatabase
        val query = "SELECT * FROM trabajador WHERE dni = ?"
        return db.rawQuery(query, arrayOf(dni))
    }
}
