package com.example.soundhtml

import android.app.Activity
import android.app.SearchManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.media.MediaPlayer
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.soundhtml.ml.MobilenetV110224Quant
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class MainActivity : AppCompatActivity() {

    lateinit var select_image_button : Button
    lateinit var make_prediction : Button
    lateinit var img_view : ImageView
    lateinit var text_view : TextView
    lateinit var bitmap: Bitmap
    lateinit var camerabtn : Button
    private lateinit var webview : Button

    lateinit var question : Button

    lateinit var videoz : Button

    public fun checkandGetpermissions(){
        if(checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 100)
        }
        else{
            Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 100){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        select_image_button = findViewById(R.id.buttonfolder)
        make_prediction = findViewById(R.id.buttoninternet)
        img_view = findViewById(R.id.imageView2)
        text_view = findViewById(R.id.textView)
        camerabtn = findViewById<Button>(R.id.buttoncamera)
        webview = findViewById(R.id.buttonotto)
        question = findViewById(R.id.buttonquestion)

        videoz = findViewById(R.id.buttonvideo)

        // DS plan to load a default html on create
        val myWebView: WebView = findViewById(R.id.webview)
        myWebView.loadUrl("file:///android_asset/homepage2.html")
        myWebView.setBackgroundColor(0);

        // handling permissions
        checkandGetpermissions()

        val labels = application.assets.open("labels.txt").bufferedReader().use { it.readText() }.split("\n")

        select_image_button.setOnClickListener(View.OnClickListener {
            Log.d("mssg", "button pressed")
            var intent : Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"

            startActivityForResult(intent, 250)

            // might play sound on click
            var mediaPlayer = MediaPlayer.create(this, R.raw.cinema)
            mediaPlayer.start() // no need to call prepare(); create() does that for you


        })

// DS somehow this icon sends to open a new web page from a static address
        videoz.setOnClickListener(View.OnClickListener {
            val myWebLink = Intent(Intent.ACTION_VIEW)
            myWebLink.data = Uri.parse("https://www.youtube.com/shorts/nJaJNGTYHdU")
            startActivity(myWebLink)

            // might play sound on click
            var mediaPlayer = MediaPlayer.create(this, R.raw.drums)
            mediaPlayer.start() // no need to call prepare(); create() does that for you

        })

        // DS this icon opens instructions on how to use in the webview
        question.setOnClickListener(View.OnClickListener {
            val myWebView: WebView = findViewById(R.id.webview)
            myWebView.loadUrl("file:///android_asset/instructions.html")

            var mediaPlayer = MediaPlayer.create(this, R.raw.snap2)
            mediaPlayer.start() // no need to call prepare(); create() does that for you

        })


        make_prediction.setOnClickListener(View.OnClickListener {
            var resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
            val model = MobilenetV110224Quant.newInstance(this)

            var tbuffer = TensorImage.fromBitmap(resized)
            var byteBuffer = tbuffer.buffer

            // might play sound on click
            var mediaPlayer = MediaPlayer.create(this, R.raw.lasers)
            mediaPlayer.start() // no need to call prepare(); create() does that for you

// Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
            inputFeature0.loadBuffer(byteBuffer)

// Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            var max = getMax(outputFeature0.floatArray)

            text_view.setText(labels[max])


            //Intent intent = new Intent (Intent.ACTION_WEB_SEARCH);
            //intent.putExtra(SearchManager.QUERY, query);
            //startActivity(intent);


           // val intent = Intent(Intent.ACTION_WEB_SEARCH)
            //val keyword = "upcycled pallet board"
            //intent.putExtra(SearchManager.QUERY, keyword)
            //startActivity(intent)

            val q: String = text_view.getText().toString()
            val keyword = q + " upcycled image"
            val intent = Intent(Intent.ACTION_WEB_SEARCH)
            intent.putExtra(SearchManager.QUERY, keyword)
            startActivity(intent)


// Releases model resources if no longer used.
            model.close()
        })





// this code runs ML from the OTTO's idea button and then returns the object label to the query
        // the query then looks for an html file store in assets to open


        webview.setOnClickListener(View.OnClickListener {
            var resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
            val model = MobilenetV110224Quant.newInstance(this)

            var tbuffer = TensorImage.fromBitmap(resized)
            var byteBuffer = tbuffer.buffer

// Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
            inputFeature0.loadBuffer(byteBuffer)

// Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            var max = getMax(outputFeature0.floatArray)

            text_view.setText(labels[max])

           // webview.setOnClickListener(View.OnClickListener {


            // might play sound on click
            var mediaPlayer = MediaPlayer.create(this, R.raw.brown)
            mediaPlayer.start() // no need to call prepare(); create() does that for you



                val myWebView: WebView = findViewById(R.id.webview)
                val q: String = text_view.getText().toString()
            myWebView.loadUrl("file:///android_asset/" + q + ".html")
           // myWebView.loadUrl("file:///android_asset/" + q + ".txt")
            myWebView.setBackgroundColor(0);

            //DS attempt to add a web page that the app defaults to,
            // if there is no Otto choice web page

         //   myWebView.setWebViewClient(new MyWebVewClient()
        //    {
             //   public void onReceivedError(WebView webview, int i, String s, String s1)
             //   {
                    // mWebView.loadUrl("file:///android_asset/canoe.html");
             //   }
         //   }
            // );



// Releases model resources if no longer used.
            model.close()
        })





//})

        camerabtn.setOnClickListener(View.OnClickListener {
            var camera : Intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(camera, 200)

            // might play sound on click
            var mediaPlayer = MediaPlayer.create(this, R.raw.arcade)
            mediaPlayer.start() // no need to call prepare(); create() does that for you
        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 250){
            img_view.setImageURI(data?.data)

            var uri : Uri ?= data?.data
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        }
        else if(requestCode == 200 && resultCode == Activity.RESULT_OK){
            bitmap = data?.extras?.get("data") as Bitmap
            img_view.setImageBitmap(bitmap)
        }

    }

    fun getMax(arr:FloatArray) : Int{
        var ind = 0;
        var min = 0.0f;

        for(i in 0..1000)
        {
            if(arr[i] > min)
            {
                min = arr[i]
                ind = i;
            }
        }
        return ind
    }


      //  setContentView(R.layout.select_otto_idea);

        // WebView mWebView;
        // mWebView = (WebView) findViewById (R.id.select_otto_idea);

        // WebSettings webSettings = mWebView . getSettings ();
      //  webSettings.setJavaScriptEnabled(true);

        // mWebView.loadUrl("file:///android_asset/index.html");

    // select_otto_idea.setOnClickListener(View.OnClickListener {
       // val myWebView: WebView = findViewById(R.id.webview)
     //   myWebView.loadUrl("file://android_assets/html.htm")
    }

// val myWebView: WebView = findViewById(R.id.webview)
// myWebView.loadUrl("http://www.example.com")


