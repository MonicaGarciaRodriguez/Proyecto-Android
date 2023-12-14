package com.mogaro.myproyecto
// Monica Garcia Rodriguez
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mogaro.myproyecto.databinding.FragmentHistoricoBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_Historico.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_Historico : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentHistoricoBinding
    private lateinit var myAlumnoadapter: AlumnoAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflater.inflate(R.layout.fragment__historico, container, false)
        binding = FragmentHistoricoBinding.inflate(inflater)
        recyclerView = binding.myRvAlumno.findViewById(R.id.my_rv_alumno)
        // Obtenemos la instancia a la BD
        db = FirebaseFirestore.getInstance()
        return (binding.root)
    }

    override fun onResume() {
        super.onResume()
        getAlumnos()
    }
    private fun getAlumnos() {
        // Obtenemos la colección con la que deseamos trabajar
        val alumCollection: CollectionReference = db.collection("Alumnos")
        // Obtiene todos los documentos de una colección (sin escucha).
        alumCollection.get().apply {
            addOnSuccessListener {
                val itemAlumno: MutableList<MyAlumno> = arrayListOf()
                for (document in it) {
                    itemAlumno.add(
                        MyAlumno(
                            document.id,
                            document["aula"].toString(),
                            document["ciclo"].toString(),
                            document["fechanac"].toString(),
                            document["grupo"].toString(),
                            document["modalidad"].toString(),
                            document["nombre"].toString()
                        )
                    )
                }
                // Esta opción a TRUE significa que el RV tendrá
                // hijos del mismo tamaño, optimiza su creación.
                binding.myRvAlumno.setHasFixedSize(true)
                // Se indica el contexto para RV en forma de lista.
                binding.myRvAlumno.layoutManager = LinearLayoutManager(context)
                // Se genera el adapter.
               myAlumnoadapter = AlumnoAdapter(itemAlumno, requireContext(), alumCollection)
                // Se asigna el adapter al RV.
                binding.myRvAlumno.adapter = myAlumnoadapter
                }

                addOnFailureListener { exception ->
                    Log.d("DOC", "Error durante la recogida de documentos: ", exception)
                }
            }
        }

        companion object {
            /**
             * Use this factory method to create a new instance of
             * this fragment using the provided parameters.
             *
             * @param param1 Parameter 1.
             * @param param2 Parameter 2.
             * @return A new instance of fragment Fragment_Historico.
             */
            // TODO: Rename and change types and number of parameters
            @JvmStatic
            fun newInstance(param1: String, param2: String) =
                Fragment_Historico().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
        }
}




