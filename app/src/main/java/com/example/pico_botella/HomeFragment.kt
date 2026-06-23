package com.example.pico_botella

import android.content.ActivityNotFoundException
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pico_botella.databinding.FragmentHomeBinding
import com.example.pico_botella.dialog.RetoDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var heartbeatAnim: Animation
    private var spinMediaPlayer: MediaPlayer? = null
    private var isGameRunning = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as? MainActivity

        binding.toolbar.btnAudio.setOnClickListener {
            mainActivity?.toggleMusic()
            updateAudioIcon()
        }

        binding.toolbar.btnInstructions.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_instruccionesFragment)
        }

        binding.toolbar.btnStar.setOnClickListener {
            rateApp()
        }

        binding.toolbar.btnShare.setOnClickListener {
            shareApp()
        }

        binding.toolbar.btnAddChallenge.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_retosFragment)
        }

        startHeartbeatAnimation()

        binding.btnStartGame.setOnClickListener {
            if (!isGameRunning) {
                binding.btnStartGame.clearAnimation()
                girarBotella()
            }
        }
        
        updateAudioIcon()
    }

    private fun updateAudioIcon() {
        val mainActivity = activity as? MainActivity ?: return
        if (mainActivity.isPlaying) {
            binding.toolbar.btnAudio.setImageResource(android.R.drawable.ic_lock_silent_mode_off)
        } else {
            binding.toolbar.btnAudio.setImageResource(android.R.drawable.ic_lock_silent_mode)
        }
    }

    private fun girarBotella() {
        isGameRunning = true
        binding.btnStartGame.isEnabled = false
        
        val mainActivity = activity as? MainActivity
        if (mainActivity?.mediaPlayer?.isPlaying == true) {
            mainActivity.mediaPlayer.pause()
        }

        try {
            val resId = resources.getIdentifier("giro_botella", "raw", requireContext().packageName)
            if (resId != 0) {
                spinMediaPlayer = MediaPlayer.create(context, resId)
                spinMediaPlayer?.start()
            }
        } catch (e: Exception) {}

        val spinDuration = Random.nextLong(3000, 5001)
        val extraRotation = Random.nextInt(1080, 2160).toFloat()
        val targetRotation = binding.ivBottle.rotation + extraRotation

        binding.ivBottle.animate()
            .rotation(targetRotation)
            .setDuration(spinDuration)
            .setInterpolator(DecelerateInterpolator())
            .withEndAction {
                stopSpinSound()
                startPostSpinCountdown()
            }
            .start()
    }

    private fun stopSpinSound() {
        spinMediaPlayer?.stop()
        spinMediaPlayer?.release()
        spinMediaPlayer = null
    }

    private fun startPostSpinCountdown() {
        binding.tvCountdown.visibility = View.VISIBLE
        val handler = Handler(Looper.getMainLooper())
        
        var count = 3
        val runnable = object : Runnable {
            override fun run() {
                if (count >= 0) {
                    binding.tvCountdown.text = count.toString()
                    count--
                    handler.postDelayed(this, 1000)
                } else {
                    binding.tvCountdown.visibility = View.GONE
                    openHU12Integration()
                }
            }
        }
        handler.post(runnable)
    }

    private fun openHU12Integration() {
        val dialog = RetoDialogFragment.newInstance()
        dialog.setOnDismissListener {
            resetGameState()
        }
        dialog.show(parentFragmentManager, RetoDialogFragment.TAG)
    }

    private fun resetGameState() {
        isGameRunning = false
        binding.btnStartGame.isEnabled = true
        binding.btnStartGame.startAnimation(heartbeatAnim)
        
        val mainActivity = activity as? MainActivity
        if (mainActivity?.isPlaying == true) {
            mainActivity.mediaPlayer.start()
        }
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            val shareMessage = "App Pico Botella\n\nSolo los valientes lo juegan !!\n\nhttps://play.google.com/store/apps/details?id=com.nequi.MobileApp"
            putExtra(Intent.EXTRA_TEXT, shareMessage)
        }
        startActivity(Intent.createChooser(shareIntent, "Compartir usando:"))
    }

    private fun rateApp() {
        val packageName = "com.nequi.MobileApp"
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
            startActivity(intent)
        }
    }

    private fun startHeartbeatAnimation() {
        heartbeatAnim = AnimationUtils.loadAnimation(context, R.anim.heartbeat)
        binding.btnStartGame.startAnimation(heartbeatAnim)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        stopSpinSound()
    }
}
