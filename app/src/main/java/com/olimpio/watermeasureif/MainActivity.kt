package com.olimpio.watermeasureif

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.measure_item.*
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var client: MqttAndroidClient

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = MeasureListAdapter(measures(), this)

        val recycleListView = rv_measures as RecyclerView
        recycleListView.layoutManager = LinearLayoutManager(this)
        recycleListView.adapter = adapter

        val clientId: String = MqttClient.generateClientId()
        client = MqttAndroidClient(applicationContext, Constants.MQQTHOST, clientId)

        val options = MqttConnectOptions().apply {
            userName = Constants.USERNAME
            password = Constants.PASSWORD.toCharArray()
        }

        try {
            val token = client.connect(options)
            token.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Toast.makeText(this@MainActivity, "conectado", Toast.LENGTH_LONG).show()
                    subscription()
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Toast.makeText(this@MainActivity, "falhou", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: MqttException) {
            e.printStackTrace()
        }

        client.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                val temp = message?.payload?.decodeToString()?.subSequence(0,3) as String
                val ph = message?.payload?.decodeToString()?.subSequence(3,5) as String
                adapter.updateList(Measure(getTimeStamp(), temp, ph))
            }
        })
    }

    private fun getTimeStamp() : String {
        val simpleDateformat = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())
        return simpleDateformat.format(Date())
    }

    // for tests
    private fun measures(): ArrayList<Measure> {
        return arrayListOf(
            Measure("02/04/94 10:10", "20C", "2"),
            Measure("02/04/94 10:10", "20C", "2"),
            Measure("02/04/94 10:10", "20C", "2"),
            Measure("02/04/94 10:10", "20C", "2"),
            Measure("02/04/94 10:10", "20C", "2"),
            Measure("02/04/94 10:10", "20C", "2"),
            Measure("02/04/94 10:10", "20C", "2"),
            Measure("02/04/94 10:10", "20C", "2"),
            Measure("02/04/94 10:10", "20C", "2"),
            Measure("02/04/94 10:10", "20C", "2")
        )
    }

    private fun subscription() {
        try {
            // inscrever em determinado topico
            client.subscribe(Constants.TOPIC, 0)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun enviarMensagem() {
        val topic = Constants.TOPIC
        val message = "enviou"
        try {
            client.publish(topic, message.toByteArray(), 0, false)
        } catch (e: MqttException) {
            e.printStackTrace()

        }
    }

    //TODO: persist data!!!
    //notifications!
    //app run in background?
}

