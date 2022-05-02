package com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.ui.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.databinding.ActivityModalActualizarBinding
import com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.databinding.ActivityModalActualizarSubdepartamentoBinding

class modal_actualizar_subdepartamento : AppCompatActivity() {
    lateinit var binding: ActivityModalActualizarSubdepartamentoBinding
    var idsub = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModalActualizarSubdepartamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idsub = this.intent.extras!!.getString("idsubdepartamento")!!
        val subdepartamento = Subdepartamento(this).mostrarSubdepartamentoID(idsub)

        binding.idedificio.setText(subdepartamento.idedificio)
        binding.piso.setText(subdepartamento.piso)
        binding.descripcion.setText(subdepartamento.descripcion)
        binding.division.setText(subdepartamento.division)

        //PROGRAMACION DE BOTON DE ACTUALIZAR
        binding.actualizar.setOnClickListener {
            var subdepartamento = Subdepartamento(this)

            subdepartamento.idsubdepto = idsub.toInt()
            subdepartamento.idedificio = binding.idedificio.text.toString()
            subdepartamento.piso = binding.piso.text.toString()
            subdepartamento.descripcion = binding.descripcion.text.toString()
            subdepartamento.division = binding.division.text.toString()

            val resultado = subdepartamento.actualizar()

            if (resultado){
                Toast.makeText(this,"TU SUBDEPARTAMENTO SE ACTUALIZO", Toast.LENGTH_LONG).show()
                binding.idedificio.setText("")
                binding.piso.setText("")
                binding.descripcion.setText("")
                binding.division.setText("")
            }else{
                AlertDialog.Builder(this)
                    .setTitle("AVISO")
                    .setMessage("NO SE PUDO ACTUALIZAR TU SUBDEPARTAMENTO")
                    .show()
            }
        }
        binding.volver.setOnClickListener {
            finish()
        }
    }
}