package com.example.patrykbolozmarcincisek.morfingapp
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import com.morphing.Morphing
import kotlinx.coroutines.experimental.async


class MainActivity : AppCompatActivity() {


    private var libraryButton: Button? = null
    private var choosePointsButton: Button? = null
    private var selectedImages = mutableListOf<Image>()
    lateinit var morpher: Morphing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getReferenceToControls()
    }

    fun onSelectedLibrary(view: View) {
        openPhotoLibrary()
    }

    fun onChoosePoints(view: View) {
        openImageViewer()
    }

    fun getReferenceToControls() {
        libraryButton = findViewById(R.id.goToLibrary) as Button
        choosePointsButton = findViewById(R.id.choosePoints) as Button

    }


    private fun openPhotoLibrary() {
        ImagePicker.create(this).start()
    }

    private fun openImageViewer() {
        if (selectedImages.isEmpty() || selectedImages.count() == 1) {
            Toast.makeText(applicationContext,"Please select photos", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, GestureImageView::class.java)
            intent.putExtra("imageUrl", selectedImages[0].path)
            intent.putExtra("imageUrl2", selectedImages[1].path)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            selectedImages = ImagePicker.getImages(data)
            morpher = Morphing(getImageForUrl(selectedImages[0].path), getImageForUrl(selectedImages[1].path))


        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun getImageForUrl(path: String): Bitmap {
        val bitmap = BitmapFactory.decodeFile(path)
        return bitmap
    }



}
