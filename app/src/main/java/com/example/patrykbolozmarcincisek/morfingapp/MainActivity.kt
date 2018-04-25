package com.example.patrykbolozmarcincisek.morfingapp
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.content.Intent
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image

class MainActivity : AppCompatActivity() {


    private var libraryButton: Button? = null
    private var imageListUrl = mutableListOf<Image>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getReferenceToControls()
    }

    fun onSelectedLibrary(view: View) {
        openPhotoLibrary()
    }

    fun getReferenceToControls() {
        libraryButton = findViewById(R.id.goToLibrary) as Button

    }


    private fun openPhotoLibrary() {
        ImagePicker.create(this).start()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            imageListUrl = ImagePicker.getImages(data)

        }

    }




}
