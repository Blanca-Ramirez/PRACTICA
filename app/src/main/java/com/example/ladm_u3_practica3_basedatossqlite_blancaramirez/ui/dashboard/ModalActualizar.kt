package com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.R
import com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.databinding.ActivityModalActualizarBinding

class ModalActualizar : AppCompatActivity() {
    lateinit var binding: ActivityModalActualizarBinding
    var IDAREAS = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModalActualizarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        IDAREAS = this.intent.extras!!.getString("idarea")!!
        var area = Area(this).buscarAreaPorID(IDAREAS)

        binding.descripcion.setText(area.descripcion)
        binding.division.setText(area.division)
        binding.cantidadEmpleados.setText(area.cantidad_empleados.toString())

        binding.actualizar.setOnClickListener {
            var area = Area(this)
            area.descripcion = binding.descripcion.text.toString()
            area.division = binding.division.text.toString()
            area.cantidad_empleados = binding.cantidadEmpleados.text.toString().toInt()

            val respuesta = area.actualizar(IDAREAS)
            if (respuesta){
                Toast.makeText(this,"ACTUALIZACION EXITOSA!", Toast.LENGTH_LONG)
                    .show()
                binding.descripcion.setText("")
                binding.division.setText("")
                binding.cantidadEmpleados.setText("")
            }else{
                AlertDialog.Builder(this)
                    .setTitle("AVISO")
                    .setTitle("NO SE PUDO ACTUALIZAR")
                    .show()
            }
        }

        //BOTON PARA VOLVER
        binding.volver.setOnClickListener {
            finish()
        }
    }
}