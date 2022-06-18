package com.vde.mhvezdekod22re.utils

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vde.mhvezdekod22re.R
import com.vde.mhvezdekod22re.databinding.BottomDialogFilterBinding
import com.vde.mhvezdekod22re.models.Tags
import com.vde.mhvezdekod22re.ui.adapters.TagsAdapter

class DialogFilter (
    val context: Context,
    var tagList: ArrayList<Tags>,
    val result: (returnedList : ArrayList<Tags> )-> Unit
) {
    private val dialog = BottomSheetDialog(context, R.style.CustomBottomSheetDialog)
    private var binding: BottomDialogFilterBinding = BottomDialogFilterBinding.inflate(
        LayoutInflater.from(context)
    )
    private lateinit var adapter: TagsAdapter

    init {
        dialog.setCancelable(false)
        /*b.ok.setOnClickListener {
            dialog.dismissWithAnimation = true
            dialog.dismiss()

        }*/
        setRecyclerView()
        setlistener()
        dialog.setContentView(binding.root)
        dialog.show()

    }


    private fun setRecyclerView(){
        binding.tagsRv.layoutManager = LinearLayoutManager(context)
        binding.tagsRv.addItemDecoration(DividerItemDecoration(binding.tagsRv.context, DividerItemDecoration.VERTICAL))
        adapter = TagsAdapter(tagList){ position, tag->
            tagList[position].isChecked = tag.isChecked
            L.d("tagList = $tagList")
        }
        binding.tagsRv.adapter = adapter
    }

    private fun setlistener(){
        binding.addToBtn.setOnClickListener {
            dialog.dismiss()
            result(tagList)

        }
    }
}