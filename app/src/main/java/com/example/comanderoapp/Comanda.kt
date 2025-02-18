package com.example.comanderoapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast

class Comanda : Fragment() {
    private var tipo: String? = null
    val arrayC = ArrayList<Productos>()
    var tlComanda: TableLayout?=null
    private lateinit var base: DataBaseHelper
    private val elementosSeleccionados = mutableListOf<Pair<Int, Int>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tipo = it.getString(TIPO_BUNDLE)
            Log.i("Comanda", tipo.orEmpty())
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_comanda, container, false)

        base = DataBaseHelper(requireContext())

        val label=view.findViewById<TextView>(R.id.TipoC)
        label.text = tipo
        tlComanda = view.findViewById(R.id.tlComandas)
        sacarRecibos()
        return view
    }

    companion object {
        const val TIPO_BUNDLE = "tipo_bundle"
        const val NUMERO_BUNDLE = "numero_bundle"

        @JvmStatic
        fun newInstance(Tipo: String, Terraza: String) =
            Comanda().apply {
                arguments = Bundle().apply {
                    putString(TIPO_BUNDLE, Tipo)
                    putString(NUMERO_BUNDLE, Terraza)
                }
            }
    }

    fun getElementosSeleccionados(): List<Pair<Int, Int>> = elementosSeleccionados

    fun filtrar(tipoComida : String) : Boolean{
        Log.i("Comandas tipo",tipoComida)
        if (tipo.equals("bebidas",true)) {
            if(tipoComida.equals("bebidas",true)||tipoComida.equals("vinos",true)){
                return true
            }else{
                return false
            }
        }else{
            if(!(tipoComida.equals("bebidas",true)||tipoComida.equals("vinos",true))){
                return true
            }else{
                return false
            }
        }
    }


    fun sacarRecibos() {
        val comanda = base.obtenerComandas()
        tlComanda?.removeAllViews()
        if (comanda != null) {
            Log.d("DB_DEBUG", "Número de filas: ${comanda.count}")
        }
        //pasamos por todas las bebidas
        val index =
            LayoutInflater.from(requireContext()).inflate(R.layout.item_table_layout_pn, tlComanda, false)
        tlComanda?.addView(index)
        Log.i("Comanda","estofunciona")
        if (comanda != null && comanda.moveToFirst()) {
            Log.i("Comanda","funciona BBDD")
            do {
                val hecho = comanda.getInt(5)
                Log.i("Comanda",hecho.toString())
                if(hecho==0) {
                    val tipo = comanda.getString(0)
                    if (filtrar(tipo.orEmpty())) {
                        //sacamos la variable de las filas de las tablas
                        val registro =
                            LayoutInflater.from(requireContext())
                                .inflate(R.layout.item_table_layout_pn, null, false)
                        //literalmente es una fila con puntos no hay más
                        val puntos =
                            LayoutInflater.from(requireContext())
                                .inflate(R.layout.item_table_layout_puntos, null, false)
                        //cojemos los dos registros de cada TextView
                        val tipoT = registro.findViewById<View>(R.id.tipo) as TextView
                        val nombre = registro.findViewById<View>(R.id.producto) as TextView
                        val cantidad = registro.findViewById<View>(R.id.cantidad) as TextView
                        val hecho = registro.findViewById<View>(R.id.hecho) as TextView
                        //metemos los valores a cada TextView
                        tipoT.setText(tipo)
                        nombre.setText(comanda.getString(1))
                        val numero = "x" + comanda.getInt(2)
                        cantidad.setText(numero)
                        hecho.setText("✔")

                        val idComanda = comanda.getInt(3) // ID de la comanda
                        val idProducto = comanda.getInt(4) // ID del producto

                        // Agrega un manejador de clics para marcar el elemento como seleccionado
                        hecho.setOnClickListener {
                            val par = Pair(idComanda, idProducto)
                            if (elementosSeleccionados.contains(par)) {
                                elementosSeleccionados.remove(par)
                                Toast.makeText(
                                    requireContext(),
                                    nombre.text.toString() + " quitada de hecho",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                elementosSeleccionados.add(par)
                                registro.setBackgroundColor(2321)
                                Log.i("Comanda", nombre.text.toString())
                                Toast.makeText(
                                    requireContext(),
                                    nombre.text.toString() + " añadida de hecho",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        //metemos las dos filas en la tabla
                        tlComanda?.addView(registro)
                        tlComanda?.addView(puntos)
                        Log.i("Comanda", nombre.text.toString())
                    }
                }
            }while(comanda.moveToNext())
        }
    }
}