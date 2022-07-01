package com.dev0.sensors

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import java.util.*
import kotlin.math.abs

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor
    private lateinit var sensor2: Sensor

    private val aVal = FloatArray(3)
    private val mVal = FloatArray(3)

    private val rotation = FloatArray(9)
    private val angles = FloatArray(3)

    private lateinit var xText: TextView
    private lateinit var yText: TextView
    private lateinit var zText: TextView

    private lateinit var oText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        xText = findViewById<TextView>(R.id.xTextView)
        yText = findViewById<TextView>(R.id.yTextView)
        zText = findViewById<TextView>(R.id.zTextView)

        oText = findViewById<TextView>(R.id.oTextView)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensor2 = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }


    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, sensor2, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(p0.values, 0, aVal, 0, aVal.size)
        } else if (p0.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(p0.values, 0, mVal, 0, mVal.size)
        }

        SensorManager.getRotationMatrix(rotation,null, aVal, mVal)
        SensorManager.getOrientation(rotation, angles)

        val x = Math.toDegrees(angles[0].toDouble()).toInt()
        val y = Math.toDegrees(angles[1].toDouble()).toInt()
        val z = Math.toDegrees(angles[2].toDouble()).toInt()


        xText.text = "x: $x°"
        yText.text = "y: $y°"
        zText.text = "z: $z°"



        if(x>0 && abs(y) < 45) oText.text = "Horizontal 1"
        else if(x<0 && abs(y) < 45) oText.text = "Horizontal 2"
        else if(y<0) oText.text = "Vertical 1"
        else if(y>0) oText.text = "Vertical 2"



    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
}