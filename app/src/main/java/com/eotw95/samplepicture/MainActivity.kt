package com.eotw95.samplepicture

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.eotw95.samplepicture.ui.theme.SamplePictureTheme

class MainActivity : ComponentActivity() {
    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        dispatchTakePictureIntent()

        /**
         * 標準ギャラリーから画像を選択する
         */
        var imageUri by mutableStateOf<Uri?>(null)
//        val launcher = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
//            // Todo: handle image uri
//            imageUri = it
//        }
//        launcher.launch(arrayOf("image/*"))

        /**
         * 写真選択ツールから画像を選択する
         */
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { tmpUri ->
            tmpUri?.let {
                imageUri = it
            }
        }
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        setContent {
            SamplePictureTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (imageUri != null) {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 150.dp),
                            content = {
                                items(3) {
                                    Log.d("MainActivity", "imageUri=$imageUri")
                                    Image(
                                        painter = rememberAsyncImagePainter(model = imageUri),
                                        contentDescription = null,
                                        // 画像のサイズを指定しないと、大きすぎて表示できない
                                        modifier = Modifier.size(width = 150.dp, height = 150.dp)
                                    )
                                }
                            }
                        )
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