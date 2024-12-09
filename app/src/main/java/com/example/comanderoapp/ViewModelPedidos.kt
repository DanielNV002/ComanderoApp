package com.example.comanderoapp

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelPedidos : ViewModel() {
    val productosList: MutableLiveData<MutableList<Int>> = MutableLiveData(mutableListOf()) // Ahora es una lista de ids

    fun agregarProducto(productoId: Int) {
        val listaActual = productosList.value ?: mutableListOf()
        listaActual.add(productoId)
        productosList.value = listaActual
        Log.i("Lista", "Se ha añadido el producto con ID: $productoId")
    }

    fun eliminarProducto(productoId: Int) {
        val listaActual = productosList.value ?: mutableListOf()
        listaActual.remove(productoId)
        productosList.value = listaActual
    }

    // Obtener el nombre del producto a partir de su ID
    @SuppressLint("Range")
    fun obtenerNombreProducto(productoId: Int, context: Context): String {
        val db = DataBaseHelper(context)
        val query = "SELECT nombre FROM producto WHERE productoId = ?"
        val cursor = db.readableDatabase.rawQuery(query, arrayOf(productoId.toString()))
        var nombreProducto = ""
        if (cursor.moveToFirst()) {
            nombreProducto = cursor.getString(cursor.getColumnIndex("nombre"))
        }
        cursor.close()
        db.close()
        return nombreProducto
    }
}
