package com.mogaro.myproyecto

import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mogaro.myproyecto.AlumnoAdapter.Companion.id
import com.mogaro.myproyecto.databinding.ActivityEditarItemBinding

class ActivityEditarItem : AppCompatActivity() {
    private lateinit var binding: ActivityEditarItemBinding
    private lateinit var myAlumno: AlumnoAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var db: FirebaseFirestore
    val selecCalendar = Calendar.getInstance()
    val dia_hoy  = selecCalendar.get(Calendar.DAY_OF_MONTH)
    val mes_hoy  = selecCalendar.get(Calendar.MONTH)
    val anyo_hoy  = selecCalendar.get(Calendar.YEAR)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarItemBinding.inflate((layoutInflater))
        setContentView(binding.root)

        // Se recuperan los datos y se asignan
        val iditem = intent.getStringExtra(id)
        val nombreitem = intent.getStringExtra(AlumnoAdapter.NOMBRE)
        val fechaitem = intent.getStringExtra(AlumnoAdapter.EXTRA_FECHA)
        binding.etPersonNameItem.setText(nombreitem)
        binding.tvTimeDatePickerItem.text = fechaitem
        binding.txtId.text = iditem

        when (intent.getStringExtra(AlumnoAdapter.EXTRA_MODALIDAD)){
            "Presencial" -> binding.rbPresencialItem.isChecked = true
            "Semipresencial" -> binding.rbSemipresencialItem.isChecked = true
        }
        when (intent.getStringExtra(AlumnoAdapter.EXTRA_CICLO)){
            "Asir" -> binding.rbAsirItem.isChecked = true
            "Daw" -> binding.rbDawItem.isChecked = true
            "Dam" -> binding.rbDamItem.isChecked = true
        }
        binding.tvTimeDatePickerItem.setOnClickListener{
            myDatePicker()
        }
        binding.btAceptarItem.setOnClickListener { editar() }
        binding.btCancelarItem.setOnClickListener { finish() }
    }

    private fun myDatePicker(){
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, anyo,
                                                                   mes, dia ->
            //se asigna la fecha elegida
            selecCalendar.set(anyo, mes, dia)
            binding.tvTimeDatePickerItem.text = "$dia/${mes + 1}/$anyo"

        }
        //la fecha actual
        DatePickerDialog(
            this,
            dateSetListener,
            selecCalendar.get(Calendar.YEAR),
            selecCalendar.get(Calendar.MONTH),
            selecCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun editar() {
        //Chequear el grupo y el aula
        var grupo = ""
        var aula = ""
        var modalidad = ""
        var ciclo = ""
        if (binding.rbPresencialItem.isChecked) {
            modalidad += "Presencial"
            if (binding.rbAsirItem.isChecked) {
                ciclo += "Asir"
                grupo += "B"
                aula += "201"
            } else if (binding.rbDamItem.isChecked) {
                ciclo += "Dam"
                grupo += "K"
                aula += "202"
            } else if (binding.rbDawItem.isChecked) {
                ciclo += "Daw"
                grupo += "W"
                aula += "203"
            }

        } else
            if (binding.rbSemipresencialItem.isChecked) {
                modalidad += "Semipresencial"
                if (binding.rbAsirItem.isChecked) {
                    ciclo += "Asir"
                    grupo += "T"
                    aula += "204"
                } else if (binding.rbDamItem.isChecked) {
                    ciclo += "Dam"
                    grupo += "U"
                    aula += "205"
                } else if (binding.rbDawItem.isChecked) {
                    ciclo += "Daw"
                    grupo += "L"
                    aula += "206"
                }
            }

        // Obtenemos la instancia a la BD
        db = FirebaseFirestore.getInstance()
        // Obtenemos la colección con la que deseamos trabajar
        val AlumCollection: CollectionReference = db.collection("Alumnos")
        val alum = AlumCollection.document(binding.txtId.text.toString())
        val datos = mapOf(
            //"id" to binding.txtId.text,
            "nombre" to binding.etPersonNameItem.text.toString(),
            "fechanac" to binding.tvTimeDatePickerItem.text.toString(),
            "modalidad" to modalidad,
            "grupo" to grupo,
            "aula" to aula,
            "ciclo" to ciclo
        )
        // modificar documento
        alum.update(datos)
            // Respuesta si ha sido correcto.
            .addOnSuccessListener {
                Snackbar.make(binding.root, "Actualizado Correctamente", Snackbar.LENGTH_LONG).show()
            }
            // Respuesta si se produce un fallo.
            .addOnFailureListener { e ->
                Snackbar.make(binding.root, e.message.toString(), Snackbar.LENGTH_LONG).show()
            }
        finish()
    }

}




