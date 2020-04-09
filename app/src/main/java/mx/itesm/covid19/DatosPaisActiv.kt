package mx.itesm.covid19

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.BitmapRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.activity_datos_pais.*
import org.json.JSONObject
import java.security.KeyStore

class DatosPaisActiv : AppCompatActivity() {
    var entries=ArrayList<Entry>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_pais)
        val nombre = intent.getStringExtra("PAIS")
        tvNombrePais.text = nombre;


        descargarDatosPais(nombre)
        descargarDatosGraficar(nombre)
        crearGrafica()
    }

    private fun descargarDatosPais(nombrePais: String?) {
        val direccion = "https://corona.lmao.ninja/countries/$nombrePais"
        AndroidNetworking.get(direccion)
            .build()
            .getAsJSONObject(object: JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val casos = response?.get("cases")
                    tvCasosPais.text = "$casos"
                    val recuperados = response?.get("recovered")
                    tvRecuperadosPais.text = "$recuperados"
                    val decesos = response?.get("deaths")
                    tvDecesosPais.text = "$decesos"
                    val dInfo= response?.get("countryInfo") as JSONObject
                    val dirBandera= dInfo.get("flag") as String?
                    descargarBandera(dirBandera)
                }
                override fun onError(anError: ANError?) {
                    Toast.makeText(this@DatosPaisActiv,
                        "Error: $anError", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun descargarBandera(dirBandera: String?) {
        if (dirBandera!= null){
            AndroidNetworking.get(dirBandera).build().getAsBitmap(
                object: BitmapRequestListener{
                    override fun onResponse(response: Bitmap?) {
                        imageBanderaView.setImageBitmap(response)
                    }

                    override fun onError(anError: ANError?) {
                        Toast.makeText(this@DatosPaisActiv,"Error: $anError",Toast.LENGTH_LONG).show()
                    }
            })
        }

    }

    private fun descargarDatosGraficar(nombrePais: String?) {
        val direccion = "https://corona.lmao.ninja/v2/historical/$nombrePais"
        AndroidNetworking.get(direccion)
            .build()
            .getAsJSONObject(object: JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val timeline = response?.get("timeline") as JSONObject?
                    val casos = timeline?.get("cases") as JSONObject
                    var indice = 0f
                    for (fecha in casos.keys()){
                        val valor=casos.get(fecha)as Int
                        if (valor>0){
                            val entrada= Entry(indice,valor+0f)
                            entries.add(entrada)
                            indice = indice.inc()
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
    }

    private fun crearGrafica() {
        val datos = LineDataSet(entries, "Personas")
        datos.setDrawValues(true)
        datos.lineWidth = 3f
        lineChart.data = LineData(datos)
        lineChart.description.text = "COVID-19"
        lineChart.animateX(1800, Easing.EaseInOutSine)
    }

}