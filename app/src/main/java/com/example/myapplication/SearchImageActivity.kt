package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.get
import com.example.myapplication.databinding.ActivitySearchImageBinding
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_search_image.*
import kotlinx.android.synthetic.main.activity_search_image.bottomNavigationView
import kotlinx.android.synthetic.main.activity_search_image.fab
import java.io.File
import java.util.Locale

class SearchImageActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //bottom navigation
        binding = ActivitySearchImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavigationView.background = null
        bottomNavigationView.menu[2].isEnabled = false
        //bottom navigation//selected
        bottomNavigationView.selectedItemId = R.id.miSearch
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.miHome -> startActivity(Intent(this, MainActivity::class.java))
                R.id.miProfile -> startActivity(Intent(this, ViewPersonalActivity::class.java))
                R.id.miSearch -> startActivity(Intent(this, SearchImageActivity::class.java))
                R.id.miSettings -> startActivity(Intent(this, SettingActivity::class.java))
            }
            true
        }

        fab.setOnClickListener {
            startActivity(Intent(this, AddPhotoActivity::class.java))
        }



        //get image from firebase
        binding.getImage.setOnClickListener{
            val imageName = binding.etImageId.text.toString()
            val storageRef = FirebaseStorage.getInstance().reference.child("uploads/$imageName.jpg")

            val localfile = File.createTempFile("tempImage","jpg")
            storageRef.getFile(localfile).addOnSuccessListener {

                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                binding.imageview.setImageBitmap(bitmap)

            }.addOnFailureListener{

                Toast.makeText(this,"Failed to retrieve the image", Toast.LENGTH_SHORT).show()

            }
        }

        //upload image from firebase to cloud
        shareImage.setOnClickListener{
            val myIntent = Intent(Intent.ACTION_SEND)
            myIntent.type = "type/palin"
            val shareBody = "body"
            val shareSub = "subject"
            myIntent.putExtra(Intent.EXTRA_SUBJECT,shareBody)
            myIntent.putExtra(Intent.EXTRA_TEXT,shareSub)
            startActivity(Intent.createChooser(myIntent,"Share your picture"))
        }

        //speak to text
        speakText.setOnClickListener{
            askSpeechInput()
        }
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            etImageId.setText(result?.get(0).toString())
        }
    }

    private fun askSpeechInput() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)){
            Toast.makeText(this,"Speech recognition not available",Toast.LENGTH_SHORT).show()
        } else{
            val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault())
            i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say the picture you want to find")
            resultLauncher.launch(i)
        }
    }
}