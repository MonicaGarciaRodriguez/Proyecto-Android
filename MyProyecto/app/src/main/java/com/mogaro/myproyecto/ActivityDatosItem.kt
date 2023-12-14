package com.mogaro.myproyecto
// Monica Garcia Rodriguez
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mogaro.myproyecto.databinding.ActivityDatosItemBinding

class ActivityDatosItem : AppCompatActivity() {
    private lateinit var binding: ActivityDatosItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDatosItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Se recuperan los datos y se asignan al TextView.
        val nombre = intent.getStringExtra(AlumnoAdapter.NOMBRE)
        val fecha = intent.getStringExtra(AlumnoAdapter.EXTRA_FECHA)
        val grupo = intent.getStringExtra(AlumnoAdapter.EXTRA_GRUPO)
        val aula = intent.getStringExtra(AlumnoAdapter.EXTRA_AULA)
        val ciclo = intent.getStringExtra(AlumnoAdapter.EXTRA_CICLO)
        val modalidad = intent.getStringExtra(AlumnoAdapter.EXTRA_MODALIDAD)
       // val id = intent.getStringExtra(AlumnoAdapter.id)

        binding.txtNombre.text =  "$nombre"
        binding.txtFecha.text =  fecha
        binding.txtGrupo.text = "Grupo $grupo"
        binding.txtAula.text = "Aula $aula"
        binding.txtCiclo.text = "$ciclo($modalidad)"
    }
}