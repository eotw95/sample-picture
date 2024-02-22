package com.eotw95.samplepicture

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import coil.compose.rememberAsyncImagePainter
import com.eotw95.samplepicture.ui.theme.SamplePictureTheme

class MainActivity : ComponentActivity() {
    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        dispatchTakePictureIntent()

        // 標準ギャラリーから画像を選択する
        var imageUri by mutableStateOf<Uri?>(null)
        val launcher = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            // Todo: handle image uri
            imageUri = it
        }
        launcher.launch(arrayOf("image/*"))

        setContent {
            SamplePictureTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (imageUri != null) {
                        Image(painter = rememberAsyncImagePainter(model = imageUri), contentDescription = null)
                    }
                }
            }
        }
    }

    /**
     * dispatchTakePictureIntent
     *
     * 画像を撮影するIntentを起動
     */
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // do nothing
        }
    }
}