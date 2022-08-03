package com.course.customviewtraining

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.course.customviewtraining.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (Random.nextBoolean()) {
            binding.ctvText.setText(mockedString)
        }
    }
}

private val mockedString: String
    get() = List(10) {
        ('a'..'z').random()
    }
        .joinToString("")
