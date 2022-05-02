package com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.ui.notifications

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.R
import com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var listaIDs = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mostrarDatos()

        binding.insertar.setOnClickListener {
            var subdepartamento = Subdepartamento(requireContext())

            subdepartamento.idedificio = binding.idedificio.text.toString()
            subdepartamento.piso = binding.piso.text.toString()
            subdepartamento.descripcion = binding.descripcion.text.toString()
            subdepartamento.division = binding.division.text.toString()

            val result = subdepartamento.insertar()
            if(result){
                Toast.makeText(requireContext(), "TU SUBDEPARTAMENTO SE INSERTO EXITOSAMENTE",Toast.LENGTH_LONG).show()
                mostrarDatos()
                binding.idedificio.setText("")
                binding.piso.setText("")
                binding.descripcion.setText("")
                binding.division.setText("")
            }else{
                AlertDialog.Builder(requireContext())
                    .setTitle("AVISO DE ERROR")
                    .setMessage("TU SUBDEPARTAMENTO NO SE INSERTO, INSERTE LA DESCRIPCIÓN Y LA DIVISIÓN CORRECTA")
                    .show()
            }
        }

        //BOTON DE BUSCAR-----------------------------------------------
        binding.buscar.setOnClickListener {
            var resultSubdepartamento = ArrayList<Subdepartamento>()
            if(!binding.idedificio.text.isEmpty()){
                resultSubdepartamento = Subdepartamento(requireContext()).buscarSubdepaIDSUBDEPTO(binding.idedificio.text.toString())
            }else if(!binding.descripcion.text.isEmpty()){
                resultSubdepartamento = Subdepartamento(requireContext()).buscarSubdepaDesc(binding.descripcion.text.toString())
            }else if(!binding.division.text.isEmpty()){
                resultSubdepartamento = Subdepartamento(requireContext()).buscarSubdepaDiv(binding.division.text.toString())
            }
            var aviso = ""
            if(resultSubdepartamento.size == 0){
                AlertDialog.Builder(requireContext())
                    .setTitle("AVISO")
                    .setMessage("NO EXISTE TU DATO")
                    .setNeutralButton("Salir"){d,i->}
                    .show()
                return@setOnClickListener
            }
            (0..resultSubdepartamento.size-1).forEach {
                aviso += "ID EDIFICIO: "+resultSubdepartamento[it].idedificio+" PISO: "+resultSubdepartamento[it].piso+
                        " ID AREA: "+resultSubdepartamento[it].idArea+"\n"
            }
            AlertDialog.Builder(requireContext())
                .setTitle("BUSQUEDA")
                .setMessage(aviso)
                .setNeutralButton("SALIR"){d,i->}
                .show()
        }
        return root
    }

    fun mostrarDatos(){
        var lista = Subdepartamento(requireContext()).mostrarAll()
        var informacionSubdepartamento = ArrayList<String>()

        listaIDs.clear()
        (0..lista.size-1).forEach {
            var subdepa = lista.get(it)
            informacionSubdepartamento.add("\nID EDIFICIO: "+subdepa.idedificio+"\nPISO: "+subdepa.piso+"\nID AREA: "+subdepa.idArea+"\n")
            listaIDs.add(subdepa.idsubdepto.toString())
        }
        binding.lista.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, informacionSubdepartamento)

        binding.lista.setOnItemClickListener { adapterView, view, indice, l ->
            val idSub = listaIDs.get(indice)
            val subdepartamento = Subdepartamento(requireContext()).mostrarSubdepartamentoID(idSub)

            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("AVISO")
                .setMessage("¿Que acción deseas hacer?")
                .setNegativeButton("ELIMINAR"){d,i->
                    subdepartamento.eliminar()
                    mostrarDatos()
                }
                .setPositiveButton("ACTUALIZAR"){d,i->
                    var modal = Intent(requireContext(), modal_actualizar_subdepartamento::class.java)

                    modal.putExtra("idsubdepartamento", idSub)
                    startActivity(modal)
                }
                .setNeutralButton("CANCELAR"){d,i->}
                .show()
        }
    }

    override fun onResume() {
        mostrarDatos()
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}