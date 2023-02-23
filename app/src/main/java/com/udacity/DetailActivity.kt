package com.udacity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.udacity.databinding.ActivityDetailBinding
import com.udacity.databinding.ContentDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var contentBinding: ContentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        contentBinding = ContentDetailBinding.bind(binding.content.root)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)


        val fileName = intent.getStringExtra("file_name").toString()
        Log.i("TAG", fileName)
        val downloadStatus = intent.getStringExtra("download_status").toString()

        if (downloadStatus == "Failed") contentBinding.tvStatusContent.setTextColor(Color.RED)

        contentBinding.tvFileNameContent.text = fileName
        contentBinding.tvStatusContent.text = downloadStatus

        contentBinding.btnOk.setOnClickListener {
            finish()
        }
    }

}
