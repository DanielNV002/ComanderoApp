package com.example.comanderoapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, "basedatoscomandero.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        // Crear tabla trabajador
        val ordenCreacion = """
            CREATE TABLE trabajador (
                posicion INTEGER PRIMARY KEY AUTOINCREMENT, 
                dni TEXT NOT NULL UNIQUE,
                nombre TEXT NOT NULL
            )
        """.trimIndent()

        db!!.execSQL(ordenCreacion) // Ejecutar la instrucción de creación
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Borrar tabla si ya existe y recrearla
        val ordenBorrado = "DROP TABLE IF EXISTS trabajador"
        db!!.execSQL(ordenBorrado)
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

    fun obtenerTrabajadorPorDni(dni: String): Cursor? {
        val db = this.readableDatabase
        val query = "SELECT * FROM trabajador WHERE dni = ?"
        return db.rawQuery(query, arrayOf(dni))
    }


}
