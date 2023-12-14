package com.mogaro.myproyecto
// Monica Garcia Rodriguez
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mogaro.myproyecto.databinding.FragmentPrincipalBinding
import java.io.IOException
import java.io.OutputStreamWriter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_Principal.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_Principal : Fragment() {
    private lateinit var binding: FragmentPrincipalBinding
    private lateinit var db: FirebaseFirestore

    private var datosfecha: String = ""
    private var datosedad: String = ""
    private var datosgrupo: String = ""
    private var datosaula: String = ""
    private var datosciclo: String = ""
    private var datosmodalidad: String = ""

    //pasamos el codigo de activityMain
    companion object {
        const val NOMBRE = "myNombre"

        //recogemos datos de la segunda actividad
        const val EXTRA_FECHA = "myFecha"
        const val EXTRA_EDAD = "myEdad"
        const val EXTRA_GRUPO = "myGrupo"
        const val EXTRA_AULA = "myAula"
        const val EXTRA_CICLO = "myCiclo"
        const val EXTRA_MODALIDAD = "myModalidad"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment_Principal.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_Principal().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private val REQUEST_CODE = 1234



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {

        // Inflate the layout for this fragment
        binding = FragmentPrincipalBinding.inflate(inflater)

        binding.btLeerDatos.setOnClickListener {
            lanzarSegundaActividad() }

        binding.bObtener.setOnClickListener {
            obtenerDatos() }

        binding.bGuardar.setOnClickListener {
            guardardatos("¿Desea guardar la información obtenida?") }

        return binding.root
    }

    private fun guardardatos(message: String) {
        val builder = AlertDialog.Builder(context)
        // Se crea el AlertDialog.
        builder.apply {
            // Se asigna un título.
            setTitle("Añadir")
            // Se asgina el cuerpo del mensaje.
            setMessage(message)
            // Se define el comportamiento de los botones.
            setPositiveButton(
                android.R.string.ok,
                DialogInterface.OnClickListener(function = actionButton)
            )
            setNegativeButton(android.R.string.cancel) { _, _ ->}
            // Se muestra el AlertDialog.
            builder.show()
        }

    }
    private  val actionButton = { dialog: DialogInterface, which: Int ->
        // Obtenemos la instancia a la BD
        db = FirebaseFirestore.getInstance()
        // Obtenemos la colección con la que deseamos trabajar
        val AlumCollection: CollectionReference = db.collection("Alumnos")
        val alum = hashMapOf(
                "nombre" to binding.etPersonName.text.toString(),
                "fechanac" to datosfecha,
                "modalidad" to datosmodalidad,
                "grupo" to datosgrupo,
                "aula" to datosaula,
                "ciclo" to datosciclo
        )
        // Se añade el documento sin indicar ID, dejando que Firebase genere el ID
        // al añadir el documento. Para esta acción se recomienda add().
        AlumCollection.add(alum)
            // Respuesta si ha sido correcto.
            .addOnSuccessListener {
                Snackbar.make(binding.root, "Guardado Correctamente", Snackbar.LENGTH_LONG).show()
            }
            // Respuesta si se produce un fallo.
            .addOnFailureListener { e ->
                Snackbar.make(binding.root, e.message.toString(), Snackbar.LENGTH_LONG).show()
            }

        reiniciarapp()
    }

    private fun reiniciarapp() {
        binding.tvFechaNacimiento.setText("Fecha nacimiento: ")
        binding.tvModalidad.setText("Modalidad:")
        binding.tvCiclo.setText("Ciclo:")
        binding.tvEdad.setText("")
        binding.tvAula.setText("")
        binding.tvGrupo.setText("")
        binding.etPersonName.setText("")
        binding.bGuardar.isEnabled = false
        binding.btLeerDatos.isEnabled = false
    }
    private fun lanzarSegundaActividad() {

       if ( binding.etPersonName.text.toString().isEmpty()) {
            Toast.makeText(context, "Introduzca su nombre", Toast.LENGTH_SHORT).show()
        } else {
            // Se crea un objeto de tipo Intent
            val myIntent = Intent(context, ActivityLeerdatos::class.java).apply {
                // Se añade la información a pasar por clave-valor
                putExtra(NOMBRE, binding.etPersonName.text.toString())
            }
            getResult.launch(myIntent)
        }

    }
    //recogemos informacion de la actividad leer datos
    private val getResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.bObtener.isEnabled = true

            datosfecha = it.data?.getStringExtra(EXTRA_FECHA) ?: ""
            datosedad = it.data?.getStringExtra(EXTRA_EDAD) ?: ""
            datosgrupo = it.data?.getStringExtra(EXTRA_GRUPO) ?: ""
            datosaula = it.data?.getStringExtra(EXTRA_AULA) ?: ""
            datosciclo = it.data?.getStringExtra(EXTRA_CICLO) ?: ""
            datosmodalidad = it.data?.getStringExtra(EXTRA_MODALIDAD) ?: ""

            //mostramos en los text view la informacion de la fecha, modalidad y ciclo
            binding.tvFechaNacimiento.text = "Fecha nacimiento:  $datosfecha"
            binding.tvModalidad.text =  "Modalidad:  $datosmodalidad"
            binding.tvCiclo.text =  "Ciclo:  $datosciclo"

        }
    }
    //mostramos la informacion edad, grupo y aula al pulsar obtener datos
    private fun obtenerDatos() {
        binding.bGuardar.isEnabled = true

        binding.tvEdad.text =  "Edad: $datosedad"
        binding.tvGrupo.text = "Grupo: $datosgrupo"
        binding.tvAula.text =  "Aula: $datosaula"
    }
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

}