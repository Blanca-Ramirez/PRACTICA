package com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.ui.dashboard

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.BaseDatos
import com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.MainActivity
import com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.R
import com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    var listaIDs = ArrayList<String>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mostrarDatos()

        //BOTON DE INSERTAR UN AREA
        binding.insertar.setOnClickListener {
            var area = Area(requireContext())

            area.descripcion = binding.descripcion.text.toString()
            area.division = binding.division.text.toString()
            area.cantidad_empleados = binding.cantidadEmpleados.text.toString().toInt()
            //System.out.println("SI ENTRO")

            val resultado = area.insertar()
            //System.out.println(resultado)
            if (resultado){
                Toast.makeText(requireContext(),"TU AREA SE INSERTO EXITOSAMENTE", Toast.LENGTH_LONG)
                    .show()
                mostrarDatos()
                //LIMPIAMOS LAS CAJAS DE TEXTO
                binding.descripcion.setText("")
                binding.division.setText("")
                binding.cantidadEmpleados.setText("")
            }else{
                AlertDialog.Builder(requireContext())
                    .setTitle("ERROR")
                    .setMessage("NO SE PUDO INSERTAR TU AREA")
                    .show()
            }
        }

        //PROGRAMAMOS LA BUSQUEDA
        binding.buscar.setOnClickListener {
            if(!binding.descripcion.text.toString().equals("")){
                val area = Area(requireContext()).buscarAreaPorDescripcion(binding.descripcion.toString())
                binding.lista.adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1,area)
            }
            if(!binding.division.text.toString().equals("")){
                val area = Area(requireContext()).buscarAreaPorDivision(binding.division.toString())
                binding.lista.adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1,area)
            }

            //VERIFICAMOS QUE SI MANDEN DATOS A CONSULTAR.
            if(binding.descripcion.text.toString().equals("")&&binding.division.text.toString().equals("")) {
                androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("AVISO")
                    .setMessage("TIENES QUE PONER ALGUN DATO, PARA PODER CONSULTAR")
                    .setNeutralButton("ACEPTAR"){d,i->}
                    .show()
            }

        }


        //AQUI MOSTRAMOS TODOS LOS REGISTROS ACTUALES.
        binding.mostrar.setOnClickListener {
            mostrarDatos()
            //LIMPIAMOS LAS CAJAS DE TEXTO
            binding.descripcion.setText("")
            binding.division.setText("")
            binding.cantidadEmpleados.setText("")
        }
        return root
    }

    //PARA MOSTRAR DATOS EN EL LISTVIEW
    fun mostrarDatos(){
        var listaArea = Area(requireContext()).mostrarAll()
        var descripcionArea = ArrayList<String>()
        var divisionArea = ArrayList<String>()
        var cantidadEmpleadosArea = ArrayList<Int>()
        var cad = ArrayList<String>()
        var cad2 = ""

        listaIDs.clear()
        (0..listaArea.size-1).forEach{
            val au = listaArea.get(it)
            descripcionArea.add(au.descripcion)
            divisionArea.add(au.division)
            cantidadEmpleadosArea.add(au.cantidad_empleados)
            listaIDs.add(au.idarea.toString())
            cad2="ID AREA:"+listaIDs[it]+" \nDescripción:  "+descripcionArea[it]+" \nDivisión: "+divisionArea[it]+" \n# Empleados: "+cantidadEmpleadosArea[it]
            cad.add(cad2)
        }
        binding.lista.adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1,cad)
        binding.lista.setOnItemClickListener{adapterView, view, indice, l ->
            val idArea = listaIDs.get(indice)
            val area = Area(requireContext()).buscarAreaPorID(idArea)

            AlertDialog.Builder(requireContext())
                .setTitle("AVISO")
                .setMessage("¿Que accion deseas hacer?")
                .setNegativeButton("ELIMINAR"){d,i->
                    area.eliminar(idArea)
                    mostrarDatos()
                }
                .setPositiveButton("ACTUALIZAR"){d,i->
                    var modal = Intent(requireContext(),ModalActualizar::class.java)
                    modal.putExtra("idarea",idArea)
                    startActivity(modal)
                }
                .setNeutralButton("Cancelar"){d,i->}
                .show()
        }
    }


    override fun onResume(){
        mostrarDatos()
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}