package com.xpf.p2p.widget.randomLayout

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class ShakeListener(private val mContext: Context) : SensorEventListener {

    private var mSensorMgr: SensorManager? = null
    private var mLastX = -1.0f
    private var mLastY = -1.0f
    private var mLastZ = -1.0f
    private var mLastTime: Long = 0
    private var mShakeListener: OnShakeListener? = null
    private var mShakeCount = 0
    private var mLastShake: Long = 0
    private var mLastForce: Long = 0

    init {
        resume()
    }

    fun setOnShakeListener(listener: OnShakeListener?) {
        mShakeListener = listener
    }

    fun resume() {
        mSensorMgr = mContext.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
            ?: throw UnsupportedOperationException("Sensors not supported")

        val supported = mSensorMgr!!.registerListener(
            this, mSensorMgr!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_UI
        )
        if (!supported) {
            mSensorMgr!!.unregisterListener(this)
        }
    }

    fun pause() {
        if (mSensorMgr != null) {
            mSensorMgr!!.unregisterListener(this)
            mSensorMgr = null
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        Log.e("TAG", "accuracy:$accuracy")
    }

    override fun onSensorChanged(event: SensorEvent) {
        Log.i("TAG", "x:${event.values[SensorManager.DATA_X]}  y:${event.values[SensorManager.DATA_Y]}  z:${event.values[SensorManager.DATA_Z]}")

        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val now = System.currentTimeMillis()

        if (now - mLastForce > SHAKE_TIMEOUT) {
            mShakeCount = 0
        }

        if (now - mLastTime > TIME_THRESHOLD) {
            val diff = now - mLastTime
            val speed = Math.abs(
                event.values[SensorManager.DATA_X] + event.values[SensorManager.DATA_Y] +
                        event.values[SensorManager.DATA_Z] - mLastX - mLastY - mLastZ
            ) / diff * 10000
            if (speed > FORCE_THRESHOLD) {
                if (++mShakeCount >= SHAKE_COUNT && now - mLastShake > SHAKE_DURATION) {
                    mLastShake = now
                    mShakeCount = 0
                    mShakeListener?.onShake()
                }
                mLastForce = now
            }
            mLastTime = now
            mLastX = event.values[SensorManager.DATA_X]
            mLastY = event.values[SensorManager.DATA_Y]
            mLastZ = event.values[SensorManager.DATA_Z]
        }
    }

    interface OnShakeListener {
        fun onShake()
    }

    companion object {
        private const val FORCE_THRESHOLD = 250
        private const val TIME_THRESHOLD = 100
        private const val SHAKE_TIMEOUT = 500
        private const val SHAKE_DURATION = 1000
        private const val SHAKE_COUNT = 2
    }
}
