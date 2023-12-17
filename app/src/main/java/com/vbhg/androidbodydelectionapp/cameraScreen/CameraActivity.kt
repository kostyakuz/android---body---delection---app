package com.vbhg.androidbodydelectionapp.cameraScreen

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.vbhg.androidbodydelectionapp.R
@ExperimentalGetImage class CameraActivity: AppCompatActivity() {
    private lateinit var cameraFuture: ListenableFuture<ProcessCameraProvider>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        cameraFuture = ProcessCameraProvider.getInstance(this)
        cameraFuture.addListener({
            val cameraProvider = cameraFuture.get()
            setCamera(cameraProvider)
        },ContextCompat.getMainExecutor(this))
    }

    fun setCamera(parametr: ProcessCameraProvider) {
        println("settingCamera")
        val preview = Preview.Builder().build()
        val frameView = findViewById<PreviewView>(R.id.camera_preview)
        preview.setSurfaceProvider(frameView.surfaceProvider)

        var cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        val skeletView = findViewById<SkeletView>(R.id.skelet_view)
        val ghbdtn = FrameAnalyzer(skeletView)
        val collector = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        collector.setAnalyzer(mainExecutor,ghbdtn,)
        parametr.bindToLifecycle(this,cameraSelector,preview,collector)
    }
}
