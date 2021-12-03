package com.example.hakatonapplication.ui.home

import android.R
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alexvas.rtsp.demo.ui.live.HomeViewModel
import com.example.hakatonapplication.databinding.FragmentHomeBinding
import com.example.hakatonapplication.decode.Rtsp
import com.example.hakatonapplication.socket.BaseSocket
import com.example.hakatonapplication.socket.ResponseMessage
import com.example.hakatonapplication.socket.SocketCallback
import com.example.hakatonapplication.socket.SocketFactory
import com.google.gson.Gson
import kotlinx.coroutines.*
import androidx.lifecycle.Observer

class HomeFragment : Fragment(), SocketCallback {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private var socket: BaseSocket? = null
    private val gson = Gson()
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private var rtsp1: Rtsp? = null
    private var btnStartStop1: Button? = null
    private var progressBar1: View? = null

    private var rtsp2: Rtsp? = null
    private var btnStartStop2: Button? = null
    private var progressBar2: View? = null

    private var rtsp3: Rtsp? = null
    private var btnStartStop3: Button? = null
    private var progressBar3: View? = null

    private var rtsp4: Rtsp? = null
    private var btnStartStop4: Button? = null
    private var progressBar4: View? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        socket = SocketFactory.createSocket(gson)
        socket?.setupAddress()
        socket?.listener = this
        socket?.setupSocket()
        socket?.connectAndListen()

        val u = binding.surfaceViewExample.background
        var korutine: Job? = null

        binding.buttonStartStopRtsp1.setOnClickListener {
            if (binding.buttonStartStopRtsp1.text == "Start RTSP 1")
            {
                binding.surfaceView1.background = u
                binding.buttonStartStopRtsp1.text = "Stop RTSP 1"
                korutine = scope.launch {
                    while (true) {
                        homeViewModel.count.value = (25..30).random()
                        delay(500)
                    }
                }
            }
            else
            {
                binding.surfaceView1.setBackgroundResource(R.drawable.dark_header)
                binding.buttonStartStopRtsp1.text = "Start RTSP 1"
                binding.percent1.text = "0%"
                korutine?.cancel()
            }
        }

        homeViewModel.count.observe(viewLifecycleOwner, Observer {
                it ->
            binding.percent1.text = it.toString() + "%"

        })


//        val surfaceView1: SurfaceView = binding.surfaceView1
//        progressBar1 = binding.progressBar1
//        btnStartStop1 = binding.buttonStartStopRtsp1
//        rtsp1 = Rtsp(
//            homeViewModel.rtspPassword1.value,
//            homeViewModel.rtspUsername1.value,
//            homeViewModel.rtspRequest1.value,
//            btnStartStop1,
//            surfaceView1,
//            progressBar1
//        )


        val surfaceView2 = binding.surfaceView2
        progressBar2 = binding.progressBar2
        btnStartStop2 = binding.buttonStartStopRtsp2
        rtsp2 = Rtsp(
            homeViewModel.rtspPassword2.value,
            homeViewModel.rtspUsername2.value,
            homeViewModel.rtspRequest2.value,
            btnStartStop2,
            surfaceView2,
            progressBar2
        )


        val surfaceView3: SurfaceView = binding.surfaceView3
        progressBar3 = binding.progressBar3
        btnStartStop3 = binding.buttonStartStopRtsp3
        rtsp3 = Rtsp(
            homeViewModel.rtspPassword3.value,
            homeViewModel.rtspUsername3.value,
            homeViewModel.rtspRequest3.value,
            btnStartStop3,
            surfaceView3,
            progressBar3
        )


        val surfaceView4: SurfaceView = binding.surfaceView4
        progressBar4 = binding.progressBar4
        btnStartStop4 = binding.buttonStartStopRtsp4
        rtsp4 = Rtsp(
            homeViewModel.rtspPassword4.value,
            homeViewModel.rtspUsername4.value,
            homeViewModel.rtspRequest4.value,
            btnStartStop4,
            surfaceView4,
            progressBar4
        )
    }

    override fun onStart() {
        super.onStart()
        homeViewModel.loadParams1(context)
        homeViewModel.loadParams2(context)
        homeViewModel.loadParams3(context)
        homeViewModel.loadParams4(context)
    }


    override fun onStop() {
        super.onStop()
        homeViewModel.saveParams1(context)
        homeViewModel.saveParams2(context)
        homeViewModel.saveParams3(context)
        homeViewModel.saveParams4(context)
        rtsp1?.onRtspClientStopped()
        rtsp2?.onRtspClientStopped()
        rtsp3?.onRtspClientStopped()
        rtsp4?.onRtspClientStopped()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun connected() {
        socket?.sendMessage()
    }

    override fun receivedMessage(responseMessage: ResponseMessage) {
        scope.launch {
            binding.percent1.text = responseMessage.data?.first.toString() + "%"
            binding.percent2.text = responseMessage.data?.second.toString() + "%"
            binding.percent3.text = responseMessage.data?.third.toString() + "%"
            binding.percent4.text = responseMessage.data?.fourth.toString() + "%"
            delay(1500)
            socket?.sendMessage()
        }
    }
}