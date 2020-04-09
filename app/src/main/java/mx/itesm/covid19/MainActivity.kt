package mx.itesm.covid19

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity(), ListenerRecycler{

    var adaptadorPais : AdaptadorPais? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidNetworking.initialize(applicationContext)
        descargarInformacion()
        configurarRecycler()

    }
    private fun descargarInformacion(){
        val direccion = "https://corona.lmao.ninja/countries?sort=country"
        AndroidNetworking.get(direccion).build().getAsJSONArray(object : JSONArrayRequestListener{
            override fun onResponse(response: JSONArray?) {
                if(response!=null){
                    var arrPaises= mutableListOf<Pais>()
                    for (i in 0 until response.length()){
                        val dPais= response.get(i) as JSONObject
                        val nombre = dPais.getString("country")
                        val casos = dPais.getInt("cases")
                        arrPaises.add(0, Pais(nombre,casos))
                    }
                    adaptadorPais?.arrPaises=arrPaises.toTypedArray()
                    adaptadorPais?.notifyDataSetChanged()

                }

            }

            override fun onError(anError: ANError?) {
                Toast.makeText(this@MainActivity,"Error $anError",Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun configurarRecycler() {
        val layout = LinearLayoutManager(this)
        layout.orientation=LinearLayoutManager.VERTICAL
        recyclerView.layoutManager=layout

        adaptadorPais=AdaptadorPais(this, Pais.arrPaises)
        adaptadorPais?.listener = this
        recyclerView.adapter=adaptadorPais

        val divisor = DividerItemDecoration(this, layout.orientation)
        recyclerView.addItemDecoration(divisor)
    }

    override fun itemClicked(position: Int) {
        //lanzar segunda pantalla
        val intDatosPais= Intent(this,DatosPaisActiv::class.java)
        val nombre= adaptadorPais?.arrPaises?.get(position)?.nombre
        intDatosPais.putExtra("PAIS",nombre)
        startActivity(intDatosPais)
    }
    //RecyclerView
}
