package com.asliri.samplesamcloud

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.asliri.samcloud.SamCloudReader
import com.asliri.samcloud.interfaceListener.OnProcessingListener
import com.asliri.samcloud.model.EKtpData
import com.asliri.samplesamcloud.databinding.ActivityReaderIdCardBinding
import com.google.gson.Gson

class SampleReaderActivity : AppCompatActivity(), OnProcessingListener {

    private lateinit var binding: ActivityReaderIdCardBinding
    private lateinit var samCloudReader: SamCloudReader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReaderIdCardBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        samCloudReader = SamCloudReader(
            applicationContext,
            this@SampleReaderActivity,
            lifecycle,
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJYWFgtWFhYLVhYWCIsImF1ZCI6WyJHUk9VUCIsIkFTTElSSSBUZXN0IDIiLCJUZXN0aW5nIFRhcElEIDIiXSwiaXNzIjoiYWJhaC5ldG95Lmthc2VwIiwiZXhwIjoxNzUxNTI2Njc2LCJpYXQiOjE3MjA0MjI2NzYsImp0aSI6IjA4ZmZiYTY4LWUzYWYtNGVmMi04NTUwLWIxMGQ2OTc3Y2QwMyJ9.sEB5kwYmw1lQH5CV1l-czC0MZaWc1w70rXuzD1DA2Wk"
        )
        samCloudReader.addOnProcessingEktpListener(this)

        with(binding) {
            startListener.setOnClickListener {
                samCloudReader.addOnProcessingEktpListener(this@SampleReaderActivity)
            }
            removeListener.setOnClickListener {
                samCloudReader.removeEktpListener()
            }
            statusListener.setOnClickListener {
                val status = samCloudReader.getIsConnected()
                val statusConnected = if (status) "connected" else "disconnected";
                Toast.makeText(
                    this@SampleReaderActivity,
                    "Status e-KTP listener is $statusConnected",
                    Toast.LENGTH_SHORT
                ).show()
            }
            nfcStatus.setOnClickListener {
                val status = samCloudReader.getIsNfcEnabled()
                val statusNfc = if (status) "Enabled" else "Disabled"
                Toast.makeText(
                    this@SampleReaderActivity,
                    "Status Nfc is $statusNfc",
                    Toast.LENGTH_SHORT
                ).show()
            }
            deviceStatus.setOnClickListener {
                val status = samCloudReader.getDeviceIsSupported()
                val isSupportedDevice = if (status) "supported" else "dis supported"
                Toast.makeText(
                    this@SampleReaderActivity,
                    "Device is $isSupportedDevice",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onReceiveData(data: String) = with(binding) {
        runOnUiThread {
            Log.d(TAG, "onReceiveDataEktp: $data")
            val results = Gson().fromJson(data, EKtpData::class.java)
            nik.text = "NIK: ${results.nik}"
            nama.text = "Nama: ${results.nama}"
            golDarah.text = "Golongan Darah: ${results.golDarah}"
            jenisKelamin.text = "Jenis Kelamin: ${results.jenisKelamin}"
            tempatLahir.text = "Tempat Lahir: ${results.tempatLahir}"
            alamat.text = "Alamat: ${results.alamat}"
            kelurahan.text = "Kelurahan: ${results.kelurahan}"
            kecamatan.text = "Kecamatan: ${results.kecamatan}"
            agama.text = "Agama: ${results.agama}"
            statusPerkawinan.text = "Perkawinan: ${results.statusPerkawinan}"
            pekerjaan.text = "Pekerjaan: ${results.pekerjaan}"
            kewarganegaraan.text = "Kewarganegaraan: ${results.kewarganegaraan}"
            berlakuHigga.text = "Berlaku Hingga: ${results.berlakuHingga}"
            rt.text = "RT: ${results.rt}"
            rw.text = "RW: ${results.rw}"
        }
    }

    override fun onReceivePhoto(data: String) = with(binding) {
        runOnUiThread {
            val bytesArray = Base64.decode(data, Base64.DEFAULT)
            imageEktp.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    bytesArray,
                    0,
                    bytesArray.size
                )
            )
        }
    }

    override fun onReceiveSignature(data: String) = with(binding) {
        runOnUiThread {
            val bytesArray = Base64.decode(data, Base64.DEFAULT)
            imageDigitalSignature.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    bytesArray,
                    0,
                    bytesArray.size
                )
            )
        }
    }

    override fun onError(code: Int, message: String) = with(binding) {
        runOnUiThread {
            Toast.makeText(
                applicationContext,
                "Error Code : $code, Message : $message",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onProgressSamCloud(progress: Int) = with(binding) {
        progressBar.setProgress(progress, true)
    }

    override fun onConnectedToSamCloud() = with(binding) {
        runOnUiThread {
            status.text = "CONNECTION STATUS \n CONNECTED"
            Toast.makeText(
                this@SampleReaderActivity,
                "CONNECTION STATUS \n CONNECTED",
                Toast.LENGTH_SHORT
            ).show()
            Log.i(TAG, "onConnectedToSamCloud: connected to sam cloud")
        }
    }

    override fun onDisconnectedToSamCloud() = with(binding) {
        runOnUiThread {
            status.text = "CONNECTION STATUS \n DISCONNECTED"
            Toast.makeText(
                this@SampleReaderActivity,
                "CONNECTION STATUS \n DISCONNECTED",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}