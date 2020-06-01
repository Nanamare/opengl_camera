package com.pichu.nanamare.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.PopupWindow
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.pichu.nanamare.BR
import com.pichu.nanamare.R
import com.pichu.nanamare.base.ui.BaseActivity
import com.pichu.nanamare.base.ui.SimpleRecyclerView
import com.pichu.nanamare.camera.CameraRenderer
import com.pichu.nanamare.custom.VideoCircleView
import com.pichu.nanamare.databinding.CameraLayoutItemBinding
import com.pichu.nanamare.databinding.MainActivityBinding
import com.pichu.nanamare.databinding.PopupCameraLayoutBinding
import com.pichu.nanamare.dialog.CameraFilterBottomDialog
import com.pichu.nanamare.enums.CameraLayoutType
import com.pichu.nanamare.ext.setOnBlockClick
import com.pichu.nanamare.utils.DisplayUtils
import com.pichu.nanamare.utils.Logger
import com.pichu.nanamare.vm.CameraFilterVieModel
import com.pichu.nanamare.vm.MainViewModel
import com.pichu.nanamare.vm.navigator.CameraFilterNavigator
import com.pichu.nanamare.vm.navigator.MainNavigator
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.dimen
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.FileNotFoundException
import java.io.IOException


class MainActivity : BaseActivity<MainActivityBinding>(R.layout.main_activity), MainNavigator,
    CameraFilterNavigator {

    private val mainViewModel by viewModel<MainViewModel> {
        parametersOf(this)
    }

    private val cameraFilterViewModel by viewModel<CameraFilterVieModel> {
        parametersOf(this)
    }

    private val cameraRenderer by lazy {
        CameraRenderer(
            applicationContext,
            binding.tvSurface,
            cameraFilterViewModel.liveCameraFilter,
            cameraFilterViewModel.liveCameraFilters,
            cameraFilterViewModel.liveFilterId
        )
    }

    private val layoutPopup by lazy { initLayoutPopup() }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.tvSurface.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            cameraRenderer.onSurfaceTextureSizeChanged(null, v.width, v.height)
        }

        binding.run {
            mainVM = mainViewModel
            cameraFilterVM = cameraFilterViewModel
            vcvBtn.setOnRecordListener(object : VideoCircleView.OnRecordListener() {
                override fun onShortClick() {
                    openMediaStore(callback = {
                        cameraRenderer.lockFocus()
                    })
                }

                override fun OnRecordStartClick() {
                    cameraRenderer.startRecordingVideo()
                }

                override fun OnFinish(resultCode: Int) {
                    if (resultCode == 1) {
                        cameraRenderer.stopRecordingVideo()
                    } else {
                        // 녹화는 끝내지만 저장은 하지 않기
                        // cameraRenderer?.stopRecordingVideo()
                    }
                }
            })

            binding.tvSurface.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        cameraRenderer.setSelectedFilter(0)
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        cameraRenderer.setSelectedFilter(cameraFilterViewModel.liveFilterId.value!!)
                        displayFocusAnim(event)
                    }
                }
                true
            }
        }

        cameraRenderer.let {
            it.imageObservable.observeOn(Schedulers.io())
                ?.subscribeOn(Schedulers.io())
                ?.subscribe {
                    if (capture(System.currentTimeMillis().toString())) {
                        showToast("Finished save image")
                    } else {
                        showToast("Failed save image")
                    }
                }?.let {
                    compositeDisposable.add(
                        it
                    )
                }
        }

    }

    override fun onResume() {
        super.onResume()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )
            ) {
                Toast.makeText(this, "Camera access is required.", Toast.LENGTH_SHORT).show()

            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                )
            }
        } else {
            cameraRenderer.startBackgroundThread()
            cameraRenderer.reopenCamera()
        }

    }

    override fun onPause() {
        cameraRenderer.closeCamera()
        cameraRenderer.stopBackgroundThread()
        super.onPause()
    }

    private fun capture(imgName: String): Boolean {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, imgName)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, imgName)
        values.put(MediaStore.Images.Media.DESCRIPTION, imgName)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())

        val imgUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val source = binding.tvSurface.bitmap

        try {
            imgUri?.let {
                if (source != null) {
                    val imageOut = contentResolver.openOutputStream(it)
                    imageOut.use { img ->
                        source.compress(Bitmap.CompressFormat.JPEG, 100, img)
                    }
                } else {
                    contentResolver.delete(it, null, null)
                }
            } ?: throw Exception()
        } catch (ex: FileNotFoundException) {
            Logger.e(TAG, ex)
            return false
        } catch (ex: IOException) {
            Logger.e(TAG, ex)
            return false
        } catch (ex: Exception) {
            Logger.e(TAG, ex)
            return false
        }
        return true
    }

    private fun haveStoragePermission() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PERMISSION_GRANTED

    private fun requestPermission() {
        if (!haveStoragePermission()) {
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(
                this, permissions,
                READ_EXTERNAL_STORAGE_REQUEST
            )
        }
    }

    private fun openMediaStore(callback: () -> Unit) {
        if (haveStoragePermission()) {
            callback.invoke()
        } else {
            requestPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                    cameraRenderer.lockFocus()
                } else {
                    val showRationale =
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    if (showRationale) {

                    } else {

                    }
                }
            }
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                    cameraRenderer.startRenderThread(
                        binding.tvSurface.surfaceTexture,
                        binding.tvSurface.width,
                        binding.tvSurface.height
                    )
                }
            }
        }
    }

    override fun onShowCameraFilter() {
        CameraFilterBottomDialog.getInstance().apply {
            onDismissListener = {

            }
        }.show(supportFragmentManager, CameraFilterBottomDialog.TAG)
    }

    override fun onTurnOnFlash() {
        cameraRenderer.onTurnOnFlash()
    }

    override fun onSwitchCamera() {
        cameraRenderer.onSwitchCamera()
    }

    override fun onChangeLayout(cameraLayoutType: CameraLayoutType) {
        cameraRenderer.run {
            closeCamera()
            stopBackgroundThread()
            defaultUserSelectRatio = when (cameraLayoutType) {
                CameraLayoutType.RESOLUTION_3TO4 -> CameraRenderer.NOT_FULL_SCREEN_RATIO
                CameraLayoutType.RESOLUTION_9TO16 -> CameraRenderer.FULL_SCREEN_RATIO
            }
            startBackgroundThread()
            reopenCamera()
        }
    }

    private fun initLayoutPopup(): PopupWindow {
        val view = LayoutInflater.from(this@MainActivity)
            .inflate(R.layout.popup_camera_layout, null, false)
        val binding: PopupCameraLayoutBinding =
            DataBindingUtil.bind(view)!!

        binding.run {
            val cameraRatio = listOf("3:4", "9:16")
            data = cameraRatio
            cameraFilterViewModel.liveAvailableResolutions.value = cameraRatio
            rvContent.adapter = object :
                SimpleRecyclerView.Adapter<String, CameraLayoutItemBinding>(
                    R.layout.camera_layout_item, BR.data
                ) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): SimpleRecyclerView.ViewHolder<CameraLayoutItemBinding> {
                    return super.onCreateViewHolder(parent, viewType).apply {
                        itemView.setOnBlockClick(View.OnClickListener {
                            cameraFilterViewModel.run {
                                liveSelectResolution.value =
                                    liveAvailableResolutions.value?.get(adapterPosition)
                                when (adapterPosition) {
                                    0 -> onChangeLayout(CameraLayoutType.RESOLUTION_3TO4)
                                    1 -> onChangeLayout(CameraLayoutType.RESOLUTION_9TO16)
                                    else -> onChangeLayout(CameraLayoutType.RESOLUTION_9TO16)
                                }
                            }
                            if (layoutPopup.isShowing) {
                                layoutPopup.dismiss()
                            }
                        })
                    }
                }
            }
        }

        view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )

        view.measuredWidth
        view.measuredHeight

        val popupWindow = PopupWindow(
            view,
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        popupWindow.isFocusable = true
        popupWindow.setBackgroundDrawable(ColorDrawable())
        return popupWindow
    }

    override fun onShowLayoutPopUp() {
        val location = IntArray(2)
        binding.tvLayout.getLocationOnScreen(location)
        layoutPopup.showAtLocation(
            binding.tvLayout,
            Gravity.NO_GRAVITY,
            location[0],
            location[1] + binding.tvLayout.height + dimen(R.dimen.dp12)
        )
    }

    private fun displayFocusAnim(e: MotionEvent) {
        binding.run {
            val dx = (e.x - DisplayUtils.getRefLength(this@MainActivity, 100.0f) / 2).toInt()
            val dy = (e.y - DisplayUtils.getRefLength(this@MainActivity, 100.0f) / 2).toInt()
            val localLayoutParams = ivFocusAnimView.layoutParams as ConstraintLayout.LayoutParams
            localLayoutParams.leftMargin = dx
            localLayoutParams.topMargin = dy
            ivFocusAnimView.layoutParams = localLayoutParams
            ivFocusAnimView.clearAnimation()
            val anim = AnimationUtils.loadAnimation(this@MainActivity, R.anim.anim_camera_focus)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}

                override fun onAnimationEnd(animation: Animation) {
                    ivFocusAnimView.visibility = View.GONE
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            ivFocusAnimView.visibility = View.VISIBLE
            ivFocusAnimView.startAnimation(anim)
        }
    }

    companion object {
        /** The request code for requesting [Manifest.permission.CAMERA] permission. */
        private const val REQUEST_CAMERA_PERMISSION = 0x999

        /** The request code for requesting [Manifest.permission.READ_EXTERNAL_STORAGE] permission. */
        private const val READ_EXTERNAL_STORAGE_REQUEST = 0x1045
    }
}
