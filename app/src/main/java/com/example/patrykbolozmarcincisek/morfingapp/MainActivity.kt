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

        morpher = Morphing(getImageForUrl(selectedImages[0].path), getImageForUrl(selectedImages[1].path), 0.2)
        morpher1 = Morphing(getImageForUrl(selectedImages[0].path), getImageForUrl(selectedImages[1].path), 0.4)
        morpher2 = Morphing(getImageForUrl(selectedImages[0].path), getImageForUrl(selectedImages[1].path), 0.6)
        morpher3 = Morphing(getImageForUrl(selectedImages[0].path), getImageForUrl(selectedImages[1].path), 0.8)

        morpher.start()
        morpher1.start()
        morpher2.start()
        morpher3.start()

        morpher.join()
        morpher1.join()
        morpher2.join()
        morpher3.join()


        imagesBitmap.add(1, morpher.finalImageBitmap)
        imagesBitmap.add(2, morpher1.finalImageBitmap)
        imagesBitmap.add(3, morpher2.finalImageBitmap)
        imagesBitmap.add(4, morpher3.finalImageBitmap)

        nextImage()
    }


    // MARK : Animation

    private var currentIndex: Int = 0
    private var startIndex: Int = 0
    private var endIndex: Int = 5


    fun nextImage() {
        resultImageView?.setImageBitmap(imagesBitmap[currentIndex])
        val rotateimage = AnimationUtils.loadAnimation(this, R.anim.abc_fade_in)
        resultImageView?.startAnimation(rotateimage)
        currentIndex++
        Handler().postDelayed(Runnable {
            if (currentIndex > endIndex) {
                currentIndex--
                previousImage()
            } else {
                nextImage()
            }
        }, 500)

    }

    fun previousImage() {
        resultImageView?.setImageBitmap(imagesBitmap[currentIndex])
        val rotateimage = AnimationUtils.loadAnimation(this, R.anim.abc_fade_in)
        resultImageView?.startAnimation(rotateimage)
        currentIndex--
        Handler().postDelayed(Runnable {
            if (currentIndex < startIndex) {
                currentIndex++
                nextImage()
            } else {
                previousImage()
            }
        }, 500)
    }

}
