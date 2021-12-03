package com.example.hakatonapplication

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AnyRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hakatonapplication.databinding.FragmentNotifBinding
import com.example.hakatonapplication.recycler.CustomRecyclerAdapter
import com.example.hakatonapplication.recycler.ImagesWithDefection

class NotifFragment : Fragment() {

    private var _binding: FragmentNotifBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotifBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        val images = ArrayList<ImagesWithDefection>()

        images.add(ImagesWithDefection(getUriToDrawable(R.drawable.example)))
        images.add(ImagesWithDefection(getUriToDrawable(R.drawable.example)))
        images.add(ImagesWithDefection(getUriToDrawable(R.drawable.example)))

        val adapter = CustomRecyclerAdapter(images)
        recyclerView.adapter = adapter
    }

    private fun getUriToDrawable(
        @AnyRes drawableId: Int
    ): Uri {
        return Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + context?.resources?.getResourcePackageName(drawableId)
                    + '/' + context?.resources?.getResourceTypeName(drawableId)
                    + '/' + context?.resources?.getResourceEntryName(drawableId)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}