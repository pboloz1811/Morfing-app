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
import android.os.Handler
import android.widget.ImageView
import android.view.animation.AnimationUtils
import android.os.AsyncTask
import android.os.Build
import android.annotation.TargetApi
import android.widget.ProgressBar
import java.util.logging.Logger


class MainActivity : AppCompatActivity() {
    private var libraryButton: Button? = null
    private var choosePointsButton: Button? = null
    private var startButton: Button? = null
    private var resultImageView: ImageView? = null
    private var progressBar: ProgressBar? = null
    private var selectedImages = mutableListOf<Image>()
    private var imagesBitmap = mutableListOf<Bitmap>()
    lateinit var morpher: Morphing
    lateinit var morpher1: Morphing
    lateinit var morpher2: Morphing
    lateinit var morpher3: Morphing
    var isActivityEnabled: Boolean? = null
    var listMorphers: ArrayList<Morphing> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getReferenceToControls()
    }

    fun getReferenceToControls() {
        libraryButton = findViewById(R.id.goToLibrary) as Button
        choosePointsButton = findViewById(R.id.choosePoints) as Button
        startButton = findViewById(R.id.start) as Button
        resultImageView = findViewById(R.id.resultImageView) as ImageView
        progressBar = findViewById(R.id.progressBar) as ProgressBar
    }

    /* BUTTON 1 */
    fun onSelectedLibrary(view: View) {
        openPhotoLibrary()
    }
    private fun openPhotoLibrary() {
        ImagePicker.create(this).start()
    }

    /* BUTTON 2 */
    fun onChoosePoints(view: View) {
        openImageViewer()
    }
    private fun openImageViewer() {
        isActivityEnabled = true
        if (selectedImages.isEmpty() || selectedImages.count() == 1) {
            Toast.makeText(applicationContext,"Please select photos", Toast.LENGTH_SHORT).show()

        } else {
            val intent = Intent(this, GestureImageView::class.java)
            intent.putExtra("imageUrl", selectedImages[0].path)
            intent.putExtra("imageUrl2", selectedImages[1].path)

            if(isActivityEnabled as Boolean) {
                startActivity(intent)
            }
            else {
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            selectedImages = ImagePicker.getImages(data)
            imagesBitmap.add(BitmapFactory.decodeFile(selectedImages[0].path))
            imagesBitmap.add(BitmapFactory.decodeFile(selectedImages[1].path))
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getImageForUrl(path: String): Bitmap {
        val bitmap = BitmapFactory.decodeFile(path)
        return bitmap
    }

    fun onStartMorphing(view: View) {

        createMorphers()

        for(i in 0..listMorphers.size-1) {
            listMorphers[i].start()
            listMorphers[i].join()
        }

        for(i in 0..listMorphers.size-1) {
            imagesBitmap.add(i+1, listMorphers[i].finalImageBitmap)
        }

        nextImage()
    }

    private fun createMorphers() {
        for(i in 1..4) {
            listMorphers.add(Morphing(getImageForUrl(selectedImages[0].path), getImageForUrl(selectedImages[1].path), 0.2*i))
        }
    }

    private var currentIndex: Int = 0
    private var startIndex: Int = 0
    private var endIndex: Int = 5


    fun nextImage() {
        resultImageView?.setImageBitmap(imagesBitmap[currentIndex])
        currentIndex++
        Handler().postDelayed(Runnable {
            if (currentIndex > endIndex) {
                currentIndex--
                previousImage()
            } else {
                nextImage()
            }
        }, 100)

    }

    fun previousImage() {
        resultImageView?.setImageBitmap(imagesBitmap[currentIndex])
        currentIndex--
        Handler().postDelayed(Runnable {
            if (currentIndex < startIndex) {
                currentIndex++
                nextImage()
            } else {
                previousImage()
            }
        }, 100)
    }

}
