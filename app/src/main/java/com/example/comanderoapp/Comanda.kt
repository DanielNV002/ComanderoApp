package com.example.comanderoapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TextView

class Comanda : Fragment() {
    private var tipo: String? = null
    private var numero: String? = null
    val arrayC = ArrayList<Productos>()
    var tlComanda: TableLayout?=null
    private lateinit var base: DataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tipo = it.getString(TIPO_BUNDLE)
            numero = it.getString(NUMERO_BUNDLE)
            Log.i("Comanda", tipo.orEmpty()+numero.orEmpty())
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

    fun sacarRecibos() {
        val comanda = base?.obtenerComandas()
        //pasamos por todas las bebidas
        if (comanda != null && comanda.moveToFirst()) {
                do {
                    //sacamos la variable de las filas de las tablas
                    val registro =
                        LayoutInflater.from(requireContext()).inflate(R.layout.item_table_layout_pn, null, false)
                    //literalmente es una fila con puntos no hay más
                    val puntos =
                        LayoutInflater.from(requireContext()).inflate(R.layout.item_table_layout_puntos, null, false)
                    //cojemos los dos registros de cada TextView
                    val tipo = registro.findViewById<View>(R.id.tipo) as TextView
                    val nombre = registro.findViewById<View>(R.id.producto) as TextView
                    val cantidad = registro.findViewById<View>(R.id.cantidad) as TextView
                    //metemos los valores a cada TextView
                    tipo.setText(comanda?.getString(1))
                    nombre.setText(comanda?.getString(2))
                    val numero = "x" + comanda?.getInt(3)
                    cantidad.setText(numero)
                    //metemos las dos filas en la tabla
                    tlComanda?.addView(registro)
                    tlComanda?.addView(puntos)
                    Log.i("Comanda",nombre.text.toString())
                }while(comanda.moveToNext())
        }
    }
}