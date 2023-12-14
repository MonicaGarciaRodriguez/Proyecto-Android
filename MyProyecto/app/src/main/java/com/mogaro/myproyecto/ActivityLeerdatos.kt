package com.mogaro.myproyecto
// Monica Garcia Rodriguez
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mogaro.myproyecto.databinding.ActivityLeerdatosBinding



class ActivityLeerdatos : AppCompatActivity() {
    private lateinit var binding: ActivityLeerdatosBinding
    var edad: Int = 0
    val selecCalendar = Calendar.getInstance()
    val dia_hoy  = selecCalendar.get(Calendar.DAY_OF_MONTH)
    val mes_hoy  = selecCalendar.get(Calendar.MONTH)
    val anyo_hoy  = selecCalendar.get(Calendar.YEAR)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeerdatosBinding.inflate((layoutInflater))
        setContentView(binding.root)

        // Se recuperan los datos y se asignan al TextView.
        val nombre = intent.getStringExtra(Fragment_Principal.NOMBRE)
        binding.txDatos.text = "Los datos del alumno: " + "\n" + nombre

        binding.btAceptar.setOnClickListener {
            mostrarDatos()
        }

        binding.btCancelar.setOnClickListener {
            finish()
        }
        binding.tvTimeDatePicker.setOnClickListener{
            myDatePicker()
        }

    }

    private fun myDatePicker(){
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, anyo,
                                                                   mes, dia ->
            //se asigna la fecha elegida
            selecCalendar.set(anyo, mes, dia)
            binding.tvTimeDatePicker.text = "$dia/${mes + 1}/$anyo"
            calcuaredad(dia, mes, anyo)
        }
        //la fecha actual
        DatePickerDialog(
            this,
            dateSetListener,
            selecCalendar.get(Calendar.YEAR),
            selecCalendar.get(Calendar.MONTH),
            selecCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }

   private fun calcuaredad(dia: Int, mes: Int, anyo: Int) {

           edad = anyo_hoy - anyo
           if (mes > mes_hoy || mes == mes_hoy && dia > dia_hoy)
               edad -= 1
    }

    private fun mostrarDatos() {
            val intentResult: Intent = Intent().apply {
               //Chequear el grupo y el aula
                var grupo: String
                grupo = ""
                var aula: String
                aula = ""
                if (binding.rbPresencial.isChecked) {
                    putExtra(Fragment_Principal.EXTRA_MODALIDAD, binding.rbPresencial.text)
                    if (binding.rbAsir.isChecked) {
                        grupo += "B"
                        aula += "201"
                        putExtra(Fragment_Principal.EXTRA_CICLO, binding.rbAsir.text)
                    } else if (binding.rbDam.isChecked) {
                        grupo += "K"
                        aula += "202"
                        putExtra(Fragment_Principal.EXTRA_CICLO, binding.rbDam.text)
                    } else if (binding.rbDaw.isChecked) {
                        grupo += "W"
                        aula += "203"
                        putExtra(Fragment_Principal.EXTRA_CICLO, binding.rbDaw.text)
                    }

                } else
                if (binding.rbSemipresencial.isChecked) {
                    putExtra(Fragment_Principal.EXTRA_MODALIDAD, binding.rbSemipresencial.text)

                    if (binding.rbAsir.isChecked) {
                        grupo += "T"
                        aula += "204"
                        putExtra(Fragment_Principal.EXTRA_CICLO, binding.rbAsir.text)
                    } else if (binding.rbDam.isChecked) {
                        grupo += "U"
                        aula += "205"
                        putExtra(Fragment_Principal.EXTRA_CICLO, binding.rbDam.text)
                    } else if (binding.rbDaw.isChecked) {
                        grupo += "L"
                        aula += "206"
                        putExtra(Fragment_Principal.EXTRA_CICLO, binding.rbDaw.text)
                    }
                }
                putExtra(Fragment_Principal.EXTRA_AULA, aula)
                putExtra(Fragment_Principal.EXTRA_GRUPO, grupo)
                putExtra(Fragment_Principal.EXTRA_FECHA,
                    binding.tvTimeDatePicker.text.toString())
                putExtra(Fragment_Principal.EXTRA_EDAD, edad.toString())
            }
            setResult(Activity.RESULT_OK, intentResult)
            finish()
    }
}




