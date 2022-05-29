package com.thorhelgen.picturedownload

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.view.KeyEvent
import android.widget.Toast
import com.thorhelgen.picturedownload.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private var loadedImgBitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(layoutMain.root)

        // Restore image if it was saved
        if (savedInstanceState != null) {
            loadedImgBitmap = decodeImage(
                savedInstanceState.getString(
                    getString(R.string.picture_URL_bundle_key)
                )
            );
            layoutMain.loadedPictureView.setImageBitmap(loadedImgBitmap)
        }

        layoutMain.pictureURLEdit.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {

                // Declaring executor to parse the URL
                val executor = Executors.newSingleThreadExecutor()

                // Once the executor parses the URL
                // and receives the image, handler will load it
                // in the ImageView
                val handler = Handler(Looper.getMainLooper())

                // Only for Background process (can take time depending on the Internet speed)
                executor.execute {

                    // Tries to get the image and post it in the ImageView
                    // with the help of Handler
                    try {
                        val inputStream = URL(layoutMain.pictureURLEdit.text.toString()).openStream()
                        loadedImgBitmap = BitmapFactory.decodeStream(inputStream)

                        // Only for making changes in UI
                        handler.post {
                            layoutMain.loadedPictureView.setImageBitmap(loadedImgBitmap)
                        }
                    }

                    // If the URL does not point to
                    // image or any other kind of failure
                    catch (e: Exception) {
                        handler.post {
                            Toast.makeText(v.context, "URL is wrong!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save image bitmap encoded in Base64
        if (loadedImgBitmap != null) {
            outState.putString(
                getString(R.string.picture_URL_bundle_key),
                encodeImage(loadedImgBitmap!!)
            )
        }
    }

    private fun encodeImage(bm: Bitmap): String? {
        val byteArrayOS = ByteArrayOutputStream()
        // Compress without quality loss
        bm.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS)
        val byteArray = byteArrayOS.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun decodeImage(str: String?): Bitmap? {
        if (str == null) {
            return null
        }
        val byteArray = Base64.decode(str, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}