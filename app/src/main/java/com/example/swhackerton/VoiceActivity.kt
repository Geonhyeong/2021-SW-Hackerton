package com.example.swhackerton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.swhackerton.databinding.ActivityVoiceBinding
import org.webrtc.SurfaceViewRenderer

class VoiceActivity : AppCompatActivity() {
    lateinit var binding : ActivityVoiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var surfaceRendererArray:Array<SurfaceViewRenderer>

        binding = DataBindingUtil.setContentView( this, R.layout.activity_voice)
        surfaceRendererArray = arrayOf(
            binding.surfRendererLocal,
            binding.surfRendererRemote1,
            binding.surfRendererRemote2,
            binding.surfRendererRemote3,
            binding.surfRendererRemote4,
            binding.surfRendererRemote5
        )

        var availableView:Array<Boolean>


        setContentView(R.layout.activity_voice)
    }
}