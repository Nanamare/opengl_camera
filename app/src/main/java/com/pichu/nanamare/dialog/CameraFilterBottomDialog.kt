package com.pichu.nanamare.dialog

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.util.forEach
import com.pichu.nanamare.BR
import com.pichu.nanamare.R
import com.pichu.nanamare.base.ui.BaseBottomSheetDialogFragment
import com.pichu.nanamare.base.ui.SimpleRecyclerView
import com.pichu.nanamare.databinding.CameraFilterDialogBinding
import com.pichu.nanamare.databinding.CameraFilterItemBinding
import com.pichu.nanamare.ext.setOnBlockClick
import com.pichu.nanamare.utils.CenterLayoutManager
import com.pichu.nanamare.vm.CameraFilterVieModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf

class CameraFilterBottomDialog :
    BaseBottomSheetDialogFragment<CameraFilterDialogBinding>(R.layout.camera_filter_dialog) {

    override fun getTheme(): Int = R.style.SheetDialog

    private val cameraFilterVieModel by sharedViewModel<CameraFilterVieModel> {
        parametersOf(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            val items = mutableListOf<String>()
            cameraFilterVieModel.liveCameraFilters.value?.forEach { key, value ->
                items.add(value.name)
            }
            data = items

            rvContent.run {
                adapter = object :
                    SimpleRecyclerView.Adapter<String, CameraFilterItemBinding>(
                        R.layout.camera_filter_item, BR.data
                    ) {
                    override fun onCreateViewHolder(
                        parent: ViewGroup,
                        viewType: Int
                    ): SimpleRecyclerView.ViewHolder<CameraFilterItemBinding> {
                        return super.onCreateViewHolder(parent, viewType).apply {
                            itemView.setOnBlockClick(View.OnClickListener {
                                cameraFilterVieModel.run {
                                    liveFilterId.value = adapterPosition
                                    liveCameraFilter.value = liveCameraFilters.value?.get(
                                        adapterPosition
                                    )
                                }
                                onCloseClick()
                            })
                        }
                    }
                }
                layoutManager =
                    CenterLayoutManager(context, CenterLayoutManager.HORIZONTAL, false)
            }


            cameraFilterVieModel.liveFilterId.value?.let {
                rvContent.smoothScrollToPosition(it)
            }

        }

    }

    companion object {
        @JvmStatic
        val TAG: String = CameraFilterBottomDialog::class.java.simpleName
        fun getInstance() = CameraFilterBottomDialog()
    }

}