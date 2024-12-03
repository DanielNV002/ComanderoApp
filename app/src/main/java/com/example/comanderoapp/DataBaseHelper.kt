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
        db!!.execSQL(ordenCreacionTrabajador)

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
                preciopedido REAL NOT NULL,
                numeroMesa INTEGER NOT NULL,
                FOREIGN KEY (numeroMesa) REFERENCES mesa(numero)
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

        // Insertar productos iniciales
        insertarProductosIniciales(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
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
        datos.put("dni", dni)
        datos.put("nombre", nombre)

        val db = this.writableDatabase
        db.insert("trabajador", null, datos)
        db.close()
    }

    /**
     * Comprueba si un DNI ya existe en la base de datos.
     */
    fun comprobarDniExistente(dni: String): Boolean {
        val db = readableDatabase
        val query = "SELECT 1 FROM trabajador WHERE dni = ?"
        val cursor = db.rawQuery(query, arrayOf(dni))
        val existe = cursor.moveToFirst()
        cursor.close()
        db.close()
        return existe
    }

    /**
     * Obtiene un trabajador por su DNI.
     */
    fun obtenerTrabajadorPorDni(dni: String): Cursor? {
        val db = this.readableDatabase
        val query = "SELECT * FROM trabajador WHERE dni = ?"
        return db.rawQuery(query, arrayOf(dni))
    }

    /**
     * Obtiene todas la comandas
     */
    fun obtenerComandas(): Cursor? {
        val db = this.readableDatabase
        val query = """
        SELECT producto.tipoproducto, producto.nombre, almacena.cantidad 
        FROM almacena 
        INNER JOIN producto 
        ON almacena.productoId = producto.productoId;
    """
        return db.rawQuery(query, null)
    }

    /**
     * Inserta productos iniciales en la tabla producto.
     */
    private fun insertarProductosIniciales(db: SQLiteDatabase) {
        val productos = listOf(
            // Bebidas
            Triple("bebidas", "agua", 1.0),
            Triple("bebidas", "cerveza", 1.5),
            Triple("bebidas", "cervezasin", 1.5),
            Triple("bebidas", "refresco", 2.0),
            // Entrantes
            Triple("entrantes", "tablaChacinas", 5.5),
            Triple("entrantes", "tablaQuesos", 5.5),
            Triple("entrantes", "pinchoTortilla", 4.5),
            Triple("entrantes", "croquetasPuchero", 6.0),
            // Plato 1
            Triple("plato1", "risottoSetas", 7.5),
            Triple("plato1", "macarrones", 6.5),
            Triple("plato1", "fideua", 7.0),
            Triple("plato1", "potaje", 8.0),
            // Plato 2
            Triple("plato2", "albondigas", 5.5),
            Triple("plato2", "arrozCarrilleras", 9.0),
            Triple("plato2", "empanadillas", 7.0),
            Triple("plato2", "guisoMarisco", 10.0),
            // Postres
            Triple("postres", "tartaQueso", 4.5),
            Triple("postres", "coulant", 4.5),
            Triple("postres", "redVelvet", 3.5),
            Triple("postres", "tocinoCielo", 4.0),
            // Infantil
            Triple("infantil", "pechugaPollo", 5.5),
            Triple("infantil", "hamburguesa", 6.0),
            Triple("infantil", "nuggets", 5.0),
            Triple("infantil", "perrito", 5.5),
            // Vinos
            Triple("vinos", "tinto", 3.5),
            Triple("vinos", "blanco", 3.0),
            Triple("vinos", "rosado", 3.5),
            Triple("vinos", "espumoso", 4.0),
            // Menú del día
            Triple("menudeldia", "sabores", 12.0),
            Triple("menudeldia", "abuela", 13.0),
            Triple("menudeldia", "estacional", 11.0),
            Triple("menudeldia", "gourmet", 15.0)
        )

        db.beginTransaction()
        try {
            for (producto in productos) {
                val valores = ContentValues().apply {
                    put("tipoproducto", producto.first)
                    put("nombre", producto.second)
                    put("precio", producto.third)
                }
                db.insert("producto", null, valores)
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
    }
}
