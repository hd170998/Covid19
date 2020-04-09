package mx.itesm.covid19

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.renglon_pais.view.*

class AdaptadorPais (private val contexto: Context, var arrPaises: Array<Pais>) : RecyclerView.Adapter<AdaptadorPais.RenglonPais>() {

    var listener : ListenerRecycler? =null
    inner class RenglonPais (var vistaRenglon: View):RecyclerView.ViewHolder(vistaRenglon){


    }
    override fun getItemCount(): Int {
        return arrPaises.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RenglonPais {
        val vista = LayoutInflater.from(contexto).inflate(R.layout.renglon_pais,parent,false)
        return RenglonPais(vista)
    }

    override fun onBindViewHolder(holder: RenglonPais, position: Int) {
        val pais=arrPaises[position]
        holder.vistaRenglon.tvPais.text = pais.nombre
        holder.vistaRenglon.tvCasos.text=pais.casos.toString()
        holder.vistaRenglon.imgBandera.setImageResource(R.drawable.bandera)

        //EVENTO
        holder.vistaRenglon.setOnClickListener{
            listener?.itemClicked(position)}
    }
}