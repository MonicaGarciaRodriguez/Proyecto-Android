package com.mogaro.myproyecto
// Monica Garcia Rodriguez
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.text.Editable
import android.util.Log
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mogaro.myproyecto.databinding.ItemAlumnoListBinding

//al adapter le paso mi Firebase
class AlumnoAdapter(
    AlumnoList: MutableList<MyAlumno>,
    context: Context,
    alumCollection: CollectionReference
):
    RecyclerView.Adapter<AlumnoAdapter.MyViewHolder>() {
    private var myAlumno: MutableList<MyAlumno>
    private var myContext: Context
    //menu actionMode
    private var actionMode: ActionMode? = null


    //recogemos datos para pasar a la actividad datos item
    companion object {
        var id = "myId"
        const val NOMBRE = "myNombre"
        const val EXTRA_FECHA = "myFecha"
        const val EXTRA_CICLO = "myCiclo"
        const val EXTRA_MODALIDAD = "myModalidad"
        const val EXTRA_GRUPO = "myGrupo"
        const val EXTRA_AULA = "myAula"
    }

    //constructor
    init {
        myAlumno = AlumnoList
        myContext = context
    }

    // Es el encargado de devolver el ViewHolder ya configurado
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            MyViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_alumno_list, parent, false)
        return MyViewHolder(view)
    }

    //crea a apatir del layout un elemento de la class MyViewHolder
    //enlazo los datos en onBindViewHolder que sirve para ver los elementos que va a tener mi vista
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = myAlumno.get(position)
        holder.bind(item, myContext)
    }

    override fun getItemCount(): Int {
        return myAlumno.size
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Se usa View Binding para localizar los elementos en la vista.
        // Evitamos de esa forma la utilización de findViewById
        private val binding = ItemAlumnoListBinding.bind(view)
        fun bind(itemAlumno: MyAlumno, context: Context) {
            binding.tvId.text = itemAlumno.id
            binding.tvAula.text = itemAlumno.aula
            binding.tvCiclo.text = itemAlumno.ciclo
            binding.tvFecha.text = itemAlumno.fechanac
            binding.tvGrupo.text = itemAlumno.grupo
            binding.tvModalidad.text = itemAlumno.modalidad
            binding.tvNombre.text = itemAlumno.nombre
            //un click llama a la actividad datos item para mostrar la información del item
            itemView.setOnClickListener {
                val myIntent = Intent(context, ActivityDatosItem::class.java).apply {
                    // Se añade la información a pasar
                    putExtra(id, binding.tvId.text)
                    putExtra(NOMBRE, binding.tvNombre.text)
                    putExtra(EXTRA_FECHA, binding.tvFecha.text)
                    putExtra(EXTRA_GRUPO, binding.tvGrupo.text)
                    putExtra(EXTRA_AULA, binding.tvAula.text)
                    putExtra(EXTRA_CICLO, binding.tvCiclo.text)
                    putExtra(EXTRA_MODALIDAD, binding.tvModalidad.text)
                }
                context.startActivity(myIntent)
            }
            //Definimos el código a ejecutar si se realiza un click largo en el item
            //Se abre el menú Action Mode
            itemView.setOnLongClickListener {
                when (actionMode) {
                    null -> {
                        //Se lanza el action Mode
                        actionMode = it.startActionMode(actionModeCallBack)
                        it.isSelected = true
                        true
                    }

                    else -> false
                }
                return@setOnLongClickListener true
            }
        }

        //Accion contextual
        private val actionModeCallBack = object  : ActionMode.Callback {
            //Método llamado al selección una opcion del menu
            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return when (item!!.itemId) {
                    //opcion eliminar
                    R.id.optionDelete -> {
                        var builder = AlertDialog.Builder(myContext)
                        builder.apply {
                            setTitle("Atencion!!!")
                            setMessage("¿Desea eliminar este alumno?")
                            setPositiveButton(
                                android.R.string.ok,
                                DialogInterface.OnClickListener(function = actionButton())
                            )
                            setNegativeButton(android.R.string.cancel) { _, _ -> }
                            // Se muestra el AlertDialog.
                            builder.show()
                        }
                        mode!!.finish()
                        return true
                    }
                    R.id.optionDetalles -> {
                        val myIntent = Intent(myContext, ActivityDatosItem::class.java).apply {
                            // Se añade la información a pasar
                            putExtra(NOMBRE, binding.tvNombre.text)
                            putExtra(EXTRA_FECHA, binding.tvFecha.text)
                            putExtra(EXTRA_GRUPO, binding.tvGrupo.text)
                            putExtra(EXTRA_AULA, binding.tvAula.text)
                            putExtra(EXTRA_CICLO, binding.tvCiclo.text)
                            putExtra(EXTRA_MODALIDAD, binding.tvModalidad.text)
                        }
                        myContext.startActivity(myIntent)
                        return true
                    }
                    R.id.optionEditar -> {
                        val myIntent = Intent(myContext, ActivityEditarItem::class.java).apply {
                            // Se añade la información a pasar
                            putExtra(id, binding.tvId.text)
                            putExtra(NOMBRE, binding.tvNombre.text)
                            putExtra(EXTRA_FECHA, binding.tvFecha.text)
                            putExtra(EXTRA_GRUPO, binding.tvGrupo.text)
                            putExtra(EXTRA_AULA, binding.tvAula.text)
                            putExtra(EXTRA_CICLO, binding.tvCiclo.text)
                            putExtra(EXTRA_MODALIDAD, binding.tvModalidad.text)
                        }
                        myContext.startActivity(myIntent)
                        return true
                    }
                    else -> false
                }
            }

            fun actionButton() = { dialog: DialogInterface, which: Int ->
                // Obtenemos la instancia a la BD
                var db = FirebaseFirestore.getInstance()
                // Obtenemos la colección con la que deseamos trabajar
                val AlumCollection: CollectionReference = db.collection("Alumnos")
                val alum = AlumCollection.document(binding.tvId.text.toString())
                alum.delete()
                    .addOnSuccessListener {
                        Log.d("DOC_UPD", "Documento eliminado correctamente")
                    }
                    .addOnFailureListener { e ->
                        Log.w("DOC_UPD", "Error al eliminar el documento", e)
                    }
                myAlumno.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
                notifyDataSetChanged()

            }
            //llamado cuando al crear el modo accion a traves de startActionMode()
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                //val inflater = mActivity?.menuInflater
                //Asi no necesito la activity
                val inflater = mode?.menuInflater
                inflater?.inflate(R.menu.action_mode_menu, menu)
                return true
            }
            //Se llama cada vez que el modo accion se muestra, siempre
            //despues de onCreateActionMode()
            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }
            //Se llama cuando el usuario sale del modo de accion
            override fun onDestroyActionMode(mode: ActionMode?) {
                actionMode = null
            }
        }

    }
}


