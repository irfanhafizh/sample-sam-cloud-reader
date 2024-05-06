
# Sam Cloud Reader SDK v0.1.1

Sam Cloud Reader is an android SDK used to extract e-KTP card and get output as JSON, this SDK  
used NFC technology to process .

# Getting started
Fist time when you want to use this sdk in native code please make sure that you has add credential authentication to install sdk.

## installation sdk

To install this sdk please add repository in your ***settings.gradle.kts***.

```settings.gradle.kts  
  
// set credentials private repository android
  

dependencyResolutionManagement {  
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)  
		repositories {  
			google()  
			mavenCentral()  
			maven("https://jitpack.io") {  
				credentials {  
				username = "jp_b8plm0ddtf7a0kbahnn5gs6ft4"  
			}
		}  
	}  
}
```  

after add repositories Sam Cloud, Mqtt Server and RxJava then add dependency into your project dependencies in file ***build.gradle.kts***, then sync

```groovy  
dependencies {
//SDK SAM CLOUD
    implementation("com.github.irfanhafizh:sam-cloud:0.1.1@aar")

//MQTT Server  
    implementation("com.hivemq:hivemq-mqtt-client:1.3.0")
    implementation("com.hivemq:hivemq-mqtt-client-reactor:1.3.0")
// RxJava  
    implementation("io.reactivex.rxjava2:rxjava:2.2.19")
}
```  

## how to use it

_AndroidManifest.xml_
```AndroidManifest.xml
<uses-permission android:name="android.permission.INTERNET" />  
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```  

_Activity class or Fragment class_
```kotlin  
  
class SampleReaderActivity : AppCompatActivity(), OnProcessingListener {  
  
	private lateinit var binding: ActivityReaderIdCardBinding  
	private lateinit var samCloudReader: SamCloudReader  
	  
	// check device has been connected  
	val isConnected: Boolean = samCloudReader.getIsConnected()  
  
	// check if nfc in device is enabled  
	val isNfcEnabled: Boolean = samCloudReader.getIsNfcEnabled()  
  
	// check if device is supported  
	val isDeviceSupported: Boolean = samCloudReader.getDeviceIsSupported()  
  
	// remove listener  
	//samCloudReader.removeEktpListener() 
  
	override fun onCreate(savedInstanceState: Bundle?) {  
		super.onCreate(savedInstanceState)  
		binding = ActivityReaderIdCardBinding.inflate(LayoutInflater.from(this))  
		setContentView(binding.root)    
		samCloudReader = SamCloudReader(  
			applicationContext,  
			this@SampleReaderActivity,  
			lifecycle,  
			""//api key empty string, for next version
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
		loading.visibility = View.VISIBLE  
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
  
	override fun onProgressSamCloud(progress: Int) = with(binding) {
		progressBar.setProgress(progress, true)  
	}
}
}
```  

## Error Code
The Error Code will be displayed along with the error code and message, which you can handle within the  function ***onError***.
```Error Code Constant 
 const val DEVICE_DOES_NOT_SUPPORT_NFC_TECHNOLOGY = 1  
 const val DEVICE_NOT_CONNECTED_TO_SAM_CLOUD = 2  
 const val SAM_CLOUD_NOT_AVAILABLE = 3  
 const val NFC_READER_NOT_ACTIVE = 4  
 const val FAILED_TO_START_NFC_READER = 5  
 const val FAILED_TO_SUBSCRIBE_PUBLISH_SAM_CLOUD = 6  
 const val FAILED_TO_PROCESS_REQUEST = 7  
 const val FAILED_TO_READ_EKTP = 8  
 const val FAILED_TO_DISCONNECT_SERVICES = 9  
 const val FAILED_TO_VALIDATE_API_KEY = 10
```  


## Note
Do more please contact irfan.hafizh@asliri.id