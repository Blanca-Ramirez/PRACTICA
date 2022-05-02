package com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.ui.notifications

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException
import com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.BaseDatos
import com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.ui.dashboard.Area

class Subdepartamento(este: Context) {
    var idsubdepto = 0
    var idedificio = ""
    var piso=""
    var idArea = 0
    var descripcion = ""
    var division = ""
    private var este = este
    //UNA VARIABLE GLOBAL PARA PODER USARLA EN TODAS LAS FUNCIONES
    val basedatos = BaseDatos(este,"BD_SUBDEPARTAMENTO_AREA", null,1)
    private var err=""

    //FUNCION PARA INSERTAR UN SUBDEPARTAMENTO.
    fun insertar(): Boolean{
        err = ""
        try {
            val tabla = basedatos.writableDatabase
            var datos = ContentValues()

            var CONSULTASELECT = "SELECT IDAREA FROM AREA WHERE DESCRIPCION = ? AND DIVISION = ?"
            var cursor = tabla.rawQuery(CONSULTASELECT, arrayOf(descripcion,division))
            if(cursor.moveToFirst()){
                idArea = cursor.getInt(0)
            }else {
                return false
            }
            datos.put("IDEDIFICIO",idedificio)
            datos.put("PISO",piso)
            datos.put("IDAREA",idArea)

            var resultado = tabla.insert("SUBDEPARTAMENTO","IDSUBDEPTO",datos)
            if (resultado == -1L){
                return false
            }
        }catch (err: SQLiteException){
            this.err = err.message!!
            return false
        }finally {
            basedatos.close()
        }
        return true
    }

    // MOSTRAR TODOOOS
    fun mostrarAll() : ArrayList<Subdepartamento> {
        var arreglo = ArrayList<Subdepartamento>()
        err = ""
        try {
            val tabla = basedatos.readableDatabase
            var CONSULTASELECT = "SELECT * FROM SUBDEPARTAMENTO"

            var cursor = tabla.rawQuery(CONSULTASELECT, null)
            if (cursor.moveToFirst()){
                do {
                    var subdepartamento = Subdepartamento(este)
                    subdepartamento.idsubdepto = cursor.getInt(0)
                    subdepartamento.idedificio = cursor.getString(1)
                    subdepartamento.piso = cursor.getString(2)
                    subdepartamento.idArea = cursor.getInt(3)
                    arreglo.add(subdepartamento)
                }while (cursor.moveToNext())
            }
        } catch (err: SQLiteException){
            this.err = err.message!!
        } finally {
            basedatos.close()
        }
        return arreglo
    }

    //FUNCION PARA MOSTRAR UN SUBDEPARTAMENTO POR ID
    fun mostrarSubdepartamentoID(idsubdepto: String): Subdepartamento{
        var subdepartamento = Subdepartamento(este)
        err=""
        try {
            val tabla = basedatos.readableDatabase
            val CONSULTASELECT = "SELECT * FROM SUBDEPARTAMENTO WHERE IDSUBDEPTO = ?"
            var cursor = tabla.rawQuery(CONSULTASELECT, arrayOf(idsubdepto))

            if (cursor.moveToFirst()){
                subdepartamento.idsubdepto = cursor.getInt(0)
                subdepartamento.idedificio = cursor.getString(1)
                subdepartamento.piso = cursor.getString(2)
                subdepartamento.idArea = cursor.getInt(3)
                var departamento = Area(este).buscarAreaPorID(cursor.getInt(3).toString())
                subdepartamento.descripcion = departamento.descripcion
                subdepartamento.division = departamento.division
            }
        }catch (err: SQLiteException){
            this.err = err.message!!
        }finally {
            basedatos.close()
        }
        return subdepartamento
    }

    //FUNCION PARA ELIMINAR UN SUBDEPARTAMENTO.
    fun eliminar(): Boolean{
        err = ""
        try {
            val tabla = basedatos.writableDatabase
            val eliminacion = tabla.delete("SUBDEPARTAMENTO","IDSUBDEPTO=?", arrayOf(idsubdepto.toString()))
            if (eliminacion==0){
                return false
            }
        }catch (err: SQLiteException){
            this.err = err.message!!
        }finally {
            basedatos.close()
        }
        return true
    }

    //FUNCION PARA ACTUALIZAR EL SUBDEPARTAMENTO
    fun actualizar(): Boolean{
        err = ""
        try {
            val tabla = basedatos.writableDatabase
            val actualizacion = ContentValues()

            var CONSULTA = "SELECT IDAREA FROM AREA WHERE DESCRIPCION=? AND DIVISION=?"

            var cursor = tabla.rawQuery(CONSULTA, arrayOf(descripcion,division))
            if (cursor.moveToFirst()){
                idArea = cursor.getInt(0)
            }else{
                return false
            }

            actualizacion.put("IDEDIFICIO",idedificio)
            actualizacion.put("PISO",piso)
            actualizacion.put("IDAREA",idArea)

            val resultado = tabla.update("SUBDEPARTAMENTO",actualizacion,"IDSUBDEPTO=?", arrayOf(idsubdepto.toString()))

            if (resultado==0){
                return false
            }
        }catch (err: SQLiteException){
            this.err = err.message!!
        }finally {
            basedatos.close()
        }
        return true
    }

    //---------------------BUSQUEDAS---------------------------------
    //FUNCION BUSQUEDA POR ID EDIFICIO
    fun buscarSubdepaIDSUBDEPTO(idedificio: String):ArrayList<Subdepartamento>{
        var capsula = ArrayList<Subdepartamento>()
        err=""
        try {
            val tabla = basedatos.readableDatabase
            val CONSULTA = "SELECT * FROM SUBDEPARTAMENTO WHERE IDEDIFICIO=?"

            var cursor = tabla.rawQuery(CONSULTA, arrayOf(idedificio))

            if (cursor.moveToFirst()) {
                do {
                    var subdepartamento = Subdepartamento(este)
                    subdepartamento.idsubdepto = cursor.getInt(0)
                    subdepartamento.idedificio = cursor.getString(1)
                    subdepartamento.piso = cursor.getString(2)
                    subdepartamento.idArea = cursor.getInt(3)
                    capsula.add(subdepartamento)
                } while (cursor.moveToNext())
            }
        }catch (err: SQLiteException){
            this.err = err.message!!
        }finally {
            basedatos.close()
        }
        return capsula
    }

    //FUNCION BUSQUEDA POR DESCRIPCION
    fun buscarSubdepaDesc(descripcion: String): ArrayList<Subdepartamento>{
        var capsula = ArrayList<Subdepartamento>()
        err = ""

        try{
            val tabla = basedatos.readableDatabase
            var CONSULTA = "SELECT * FROM SUBDEPARTAMENTO WHERE IDAREA =(SELECT IDAREA FROM AREA WHERE DESCRIPCION=? ) "

            var cursor = tabla.rawQuery(CONSULTA, arrayOf(descripcion))
            if(cursor.moveToFirst()){
                do{
                    var subdepartamento = Subdepartamento(este)
                    subdepartamento.idsubdepto = cursor.getInt(0)
                    subdepartamento.idedificio = cursor.getString(1)
                    subdepartamento.piso = cursor.getString(2)
                    subdepartamento.idArea = cursor.getInt(3)
                    capsula.add(subdepartamento)
                }while (cursor.moveToNext())
            }
        }catch (err: SQLiteException){
            this.err = err.message!!
        }finally {
            basedatos.close()
        }
        return capsula
    }

    //FUNCION BUSQUEDA POR DIVISION
    fun buscarSubdepaDiv(division: String): ArrayList<Subdepartamento>{
        var capsula = ArrayList<Subdepartamento>()
        err = ""

        try {
            val tabla = basedatos.readableDatabase
            var CONSULTA = "SELECT * FROM SUBDEPARTAMENTO WHERE IDAREA=(SELECT IDAREA FROM AREA WHERE DIVISION=?)"
            var cursor = tabla.rawQuery(CONSULTA, arrayOf(division))
            if(cursor.moveToFirst()){
                do {
                    var subdepartamento = Subdepartamento(este)
                    subdepartamento.idsubdepto = cursor.getInt(0)
                    subdepartamento.idedificio = cursor.getString(1)
                    subdepartamento.piso = cursor.getString(2)
                    subdepartamento.idArea = cursor.getInt(3)
                    capsula.add(subdepartamento)
                }while (cursor.moveToNext())
            }
        }catch (err: SQLiteException){
            this.err = err.message!!
        }finally {
            basedatos.close()
        }
        return capsula
    }

}