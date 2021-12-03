package com.example.hakatonapplication.decode

import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import com.alexvas.rtsp.RtspClient
import com.alexvas.utils.NetUtils
import com.alexvas.utils.VideoCodecUtils
import java.net.Socket
import java.util.concurrent.atomic.AtomicBoolean


private const val DEFAULT_RTSP_PORT = 554
private val TAG: String = Rtsp::class.java.simpleName
private const val DEBUG = true

class Rtsp(
    _rtspPassword: String?,
    _rtspUsername: String?,
    _rtspRequest: String?,
    _btnStartStop: Button? = null,
    _surfaceView: SurfaceView,

    _progressBar: View? = null
) : SurfaceHolder.Callback  {

    private var rtspPassword = _rtspPassword
    private var rtspUsername = _rtspUsername
    private var rtspRequest = _rtspRequest
    private var videoFrameQueue: FrameQueue = FrameQueue()
    private var audioFrameQueue: FrameQueue = FrameQueue()
    private var rtspThread: RtspThread? = null
    private var videoDecodeThread: VideoDecodeThread? = null
    var rtspStopped = AtomicBoolean(true)
    private var btnStartStop = _btnStartStop
    private var surfaceView = _surfaceView
    private var surface: Surface = surfaceView.holder.surface
    private var surfaceWidth: Int = 1920
    private var surfaceHeight: Int = 1080
    private var progressBar = _progressBar
    private var videoMimeType: String = ""
    private var audioMimeType: String = ""
    private var audioSampleRate: Int = 0
    private var audioChannelCount: Int = 0
    private var audioCodecConfig: ByteArray? = null

    init {
        surfaceView.holder.addCallback(this)
        surface = surfaceView.holder.surface
        btnStartStop?.setOnClickListener {
            if (rtspStopped.get()) {
                rtspThread = RtspThread()
                rtspThread?.start()
            } else {
                rtspStopped.set(true)
                rtspThread?.interrupt()
            }
        }
    }


    fun onRtspClientStarted() {
        rtspStopped.set(false)
        btnStartStop?.text = "Stop RTSP"
    }

    fun onRtspClientStopped() {
        rtspStopped.set(true)
        btnStartStop?.text = "Start RTSP"
        videoDecodeThread?.interrupt()
        videoDecodeThread = null
    }

    fun onRtspClientConnected() {
        surface.let {
            videoDecodeThread = VideoDecodeThread(
                it,
                videoMimeType,
                surfaceWidth,
                surfaceHeight,
                videoFrameQueue
            )
            videoDecodeThread?.start()
        }
    }

    inner class RtspThread: Thread() {
        override fun run() {
            Handler(Looper.getMainLooper()).post { onRtspClientStarted() }
            val listener = object: RtspClient.RtspClientListener {
                override fun onRtspDisconnected() {
                    Handler(Looper.getMainLooper()).post {
                        progressBar?.visibility = View.GONE
                    }
                    rtspStopped.set(true)
                }

                override fun onRtspFailed(message: String?) {
                    Handler(Looper.getMainLooper()).post {
                        progressBar?.visibility = View.GONE
                    }
                    rtspStopped.set(true)
                }

                override fun onRtspConnected(sdpInfo: RtspClient.SdpInfo) {
                    Handler(Looper.getMainLooper()).post {
                        var s = ""
                        if (sdpInfo.videoTrack != null)
                            s = "video"
                        if (sdpInfo.audioTrack != null) {
                            if (s.isNotEmpty())
                                s += ", "
                            s += "audio"
                        }
                        progressBar?.visibility = View.GONE
                    }
                    if (sdpInfo.videoTrack != null) {
                        videoFrameQueue.clear()
                        when (sdpInfo.videoTrack?.videoCodec) {
                            RtspClient.VIDEO_CODEC_H264 -> videoMimeType = "video/avc"
                            RtspClient.VIDEO_CODEC_H265 -> videoMimeType = "video/hevc"
                        }
                        when (sdpInfo.audioTrack?.audioCodec) {
                            RtspClient.AUDIO_CODEC_AAC -> audioMimeType = "audio/mp4a-latm"
                        }
                        val sps: ByteArray? = sdpInfo.videoTrack?.sps
                        val pps: ByteArray? = sdpInfo.videoTrack?.pps
                        if (sps != null && pps != null) {
                            val data = ByteArray(sps.size + pps.size)
                            sps.copyInto(data, 0, 0, sps.size)
                            pps.copyInto(data, sps.size, 0, pps.size)
                            videoFrameQueue.push(FrameQueue.Frame(data, 0, data.size, 0))
                        } else {
                            if (DEBUG) Log.d(TAG, "RTSP SPS and PPS NAL units missed in SDP")
                        }
                    }
                    if (sdpInfo.audioTrack != null) {
                        audioFrameQueue.clear()
                        when (sdpInfo.audioTrack?.audioCodec) {
                            RtspClient.AUDIO_CODEC_AAC -> audioMimeType = "audio/mp4a-latm"
                        }
                        audioSampleRate = sdpInfo.audioTrack?.sampleRateHz!!
                        audioChannelCount = sdpInfo.audioTrack?.channels!!
                        audioCodecConfig = sdpInfo.audioTrack?.config
                    }
                    onRtspClientConnected()
                }

                override fun onRtspFailedUnauthorized() {
                    Handler(Looper.getMainLooper()).post {
                        progressBar?.visibility = View.GONE
                    }
                    rtspStopped.set(true)
                }

                override fun onRtspVideoNalUnitReceived(data: ByteArray, offset: Int, length: Int, timestamp: Long) {
                    if (DEBUG) {
                        val nals: ArrayList<VideoCodecUtils.NalUnit> = ArrayList()
                        val numNals = VideoCodecUtils.getH264NalUnits(data, offset, length - 1, nals)
                        val builder = StringBuilder()
                        for (nal in nals) {
                            builder.append(", ")
                            builder.append(VideoCodecUtils.getH264NalUnitTypeString(nal.type))
                            builder.append(" (")
                            builder.append(nal.length)
                            builder.append(" bytes)")
                        }
                        var textNals = builder.toString()
                        if (numNals > 0) {
                            textNals = textNals.substring(2)
                        }
                        Log.i(TAG, "NALs ($numNals): $textNals")
                    }
                    if (length > 0)
                        videoFrameQueue.push(FrameQueue.Frame(data, offset, length, timestamp))
                }

                override fun onRtspAudioSampleReceived(data: ByteArray, offset: Int, length: Int, timestamp: Long) {
                    if (length > 0)
                        audioFrameQueue.push(FrameQueue.Frame(data, offset, length, timestamp))
                }

                override fun onRtspConnecting() {
                    if (DEBUG) Log.v(TAG, "onRtspConnecting()")
                    Handler(Looper.getMainLooper()).post {
                        progressBar?.visibility = View.VISIBLE
                    }
                }
            }
            val uri: Uri = Uri.parse(rtspRequest)
            val port = if (uri.port == -1) DEFAULT_RTSP_PORT else uri.port
            val username = rtspUsername
            val password = rtspPassword
            try {
                if (DEBUG) Log.d(TAG, "Connecting to ${uri.host.toString()}:$port...")

                val socket: Socket = if (uri.scheme?.lowercase() == "rtsps")
                    NetUtils.createSslSocketAndConnect(uri.host.toString(), port, 5000)
                else
                    NetUtils.createSocketAndConnect(uri.host.toString(), port, 5000)

                val rtspClient = RtspClient.Builder(socket, uri.toString(), rtspStopped, listener)
                    .requestVideo(true)
                    .withUserAgent("RTSP test")
                    .withCredentials(username, password)
                    .build()
                rtspClient.execute()

                NetUtils.closeSocket(socket)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Handler(Looper.getMainLooper()).post { onRtspClientStopped() }
        }
    }


    override fun surfaceCreated(holder: SurfaceHolder) {
        if (DEBUG) Log.v(TAG, "surfaceCreated()")
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        surface = holder.surface
        surfaceWidth = width
        surfaceHeight = height
//        if (videoDecodeThread != null) {
//            videoDecodeThread?.interrupt()
//            videoDecodeThread = VideoDecodeThread(surface!!, videoMimeType, width, height, videoFrameQueue)
//            videoDecodeThread?.start()
//        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        videoDecodeThread?.interrupt()
        videoDecodeThread = null
    }
}