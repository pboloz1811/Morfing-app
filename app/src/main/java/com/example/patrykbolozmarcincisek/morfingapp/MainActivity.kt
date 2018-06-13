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
import android.content.DialogInterface
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.widget.ImageView
import com.shared.logger.Logger
import kotlinx.coroutines.experimental.async
import android.view.animation.AnimationUtils
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationSet
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.DecelerateInterpolator
import kotlin.concurrent.thread
import android.os.AsyncTask
import android.os.Build
import android.annotation.TargetApi
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_main.view.*


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
        morpher1 = Morphing(getImageForUrl(selectedImages[0].path), getImageForUrl(selectedImages[1].path), 0.6)
        morpher2 = Morphing(getImageForUrl(selectedImages[0].path), getImageForUrl(selectedImages[1].path), 0.8)
        imagesBitmap.add(1, morpher.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get())
        imagesBitmap.add(2, morpher1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get())
        imagesBitmap.add(3, morpher2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get())

        nextImage()

    }

    /* DIALOG BOX */
    fun dialogBox() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("TEST")
        alertDialogBuilder.setPositiveButton("Ok",
                DialogInterface.OnClickListener { arg0, arg1 -> })
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


    private fun animate(imageView: ImageView?, images: MutableList<Bitmap>, imageIndex: Int, forever: Boolean) {

        //imageView <-- The View which displays the images
        //images[] <-- Holds R references to the images to display
        //imageIndex <-- index of the first image to show in images[]
        //forever <-- If equals true then after the last image it starts all over again with the first image resulting in an infinite loop. You have been warned.

        val fadeInDuration = 500 // Configure time values here
        val timeBetween = 3000
        val fadeOutDuration = 1000

        imageView?.visibility = View.INVISIBLE    //Visible or invisible by default - this will apply when the animation ends
        imageView?.setImageBitmap(images[imageIndex])

        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator() // add this
        fadeIn.duration = fadeInDuration.toLong()

        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.interpolator = AccelerateInterpolator() // and this
        fadeOut.startOffset = (fadeInDuration + timeBetween).toLong()
        fadeOut.duration = fadeOutDuration.toLong()

        val animation = AnimationSet(false) // change to false
        animation.addAnimation(fadeIn)
        animation.addAnimation(fadeOut)
        animation.repeatCount = 1
        imageView?.animation = animation

        animation.setAnimationListener(object : AnimationListener {
            override fun onAnimationEnd(animation: Animation) {
                if (images.size - 1 > imageIndex) {
                    animate(imageView, images, imageIndex + 1, forever) //Calls itself until it gets to the end of the array
                } else {
                    if (forever == true) {
                        animate(imageView, images, 0, forever)  //Calls itself to start the animation all over again in a loop if forever = true
                    }
                }
            }

            override fun onAnimationRepeat(animation: Animation) {
                // TODO Auto-generated method stub
            }

            override fun onAnimationStart(animation: Animation) {
                // TODO Auto-generated method stub
            }
        })
    }



    private var currentIndex: Int = 0
    private var startIndex: Int = 0
    private var endIndex: Int = 4


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
        }, 500) // here 1000(1 second) interval to change from current  to next image

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
                previousImage() // here 1000(1 second) interval to change from current  to previous image
            }
        }, 500)

    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB) // API 11
    fun startMyTask(asyncTask: AsyncTask<*, *, *>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        else
            asyncTask.execute()
    }



}
