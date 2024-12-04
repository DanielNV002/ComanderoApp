package com.example.comanderoapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelPedidos : ViewModel() {
    val productosList: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())

    fun agregarProducto(producto: String) {
        val listaActual = productosList.value ?: mutableListOf()
        listaActual.add(producto)
        productosList.value = listaActual
        Log.i("Lista","Se ha añadido $producto")
    }

    fun eliminarProducto(producto: String) {
        val listaActual = productosList.value ?: mutableListOf()
        listaActual.remove(producto)
        productosList.value = listaActual
    }
}