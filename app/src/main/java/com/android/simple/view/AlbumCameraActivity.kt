package com.android.simple.view

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.android.simple.R
import com.android.simple.databinding.ActivityAlbumCameraBinding
import com.github.hueyra.base.view.BaseBindingActivity
import com.github.hueyra.mediax.MediaX
import com.github.hueyra.mediax.entity.LocalMedia
import com.permissionx.guolindev.PermissionX

/**
 * Created by zhujun
 * Date : 2022-03-29
 * Desc : _
 */
class AlbumCameraActivity : BaseBindingActivity<ActivityAlbumCameraBinding>(),
    View.OnClickListener {

    private val mSb = StringBuffer()

    @SuppressLint("SetTextI18n")
    private val mLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            val list: List<LocalMedia>? =
                MediaX.obtainResult(activityResult.resultCode, activityResult.data)
            mSb.setLength(0)
            if (list != null) {
                mSb.append("result不为空").append("\n")
                for (i in list.indices) {
                    mSb.append("------list of $i-----").append("\n")
                    mSb.append("realPath -> ${list[i].realPath}").append("\n")
                    mSb.append("cutPath -> ${list[i].cutPath}").append("\n")
                }
            } else {
                mSb.append("result为空")
            }
            mBinding.result.text = mSb.toString()
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSimpleTitleView(mBinding.stvTitle)
        findViewById<View>(R.id.camera_btn_both).setOnClickListener(this)
        findViewById<View>(R.id.camera_btn_img).setOnClickListener(this)
        findViewById<View>(R.id.camera_btn_img_crop).setOnClickListener(this)
        findViewById<View>(R.id.camera_btn_vdo).setOnClickListener(this)
        findViewById<View>(R.id.album_btn_both).setOnClickListener { openAlbumBoth() }
        findViewById<View>(R.id.album_btn_img).setOnClickListener { openAlbumOnlyImage() }
    }

    override fun onClick(v: View) {
        val mediaXBuilder = MediaX.Builder()
        when (v.id) {
            R.id.camera_btn_both -> mediaXBuilder.both()
            R.id.camera_btn_img -> mediaXBuilder.onlyImage()
            R.id.camera_btn_img_crop -> mediaXBuilder.onlyImage().cropImage()
            R.id.camera_btn_vdo -> mediaXBuilder.onlyVideo()
        }

        val mediaX = mediaXBuilder.build()

        PermissionX.init(this).permissions(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
        )
            .request { allGranted: Boolean, _: List<String?>?, _: List<String?>? ->
                if (allGranted) {
                    mLauncher.launch(mediaX.newIntent4Camera(this))
                    mediaX.openWithDefaultAnim(this)
                } else {
                    Toast.makeText(this, "请授权", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun openAlbumBoth() {
        PermissionX.init(this).permissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
            .request { allGranted: Boolean, _: List<String?>?, _: List<String?>? ->
                if (allGranted) {
                    val mediaX = MediaX.Builder()
                        .both()
                        .maxSelect(4)
                        .build()
                    mLauncher.launch(mediaX.newIntent4Album(this))
                    mediaX.openWithDefaultAnim(this)

                } else {
                    Toast.makeText(this, "请授权", Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun openAlbumOnlyImage() {
        PermissionX.init(this).permissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
            .request { allGranted: Boolean, _: List<String?>?, _: List<String?>? ->
                if (allGranted) {
                    val mediaX = MediaX.Builder().singleCropIMG().build()
                    mLauncher.launch(mediaX.newIntent4Album(this))
                    mediaX.openWithDefaultAnim(this)
                } else {
                    Toast.makeText(this, "请授权", Toast.LENGTH_SHORT).show()
                }
            }


    }
}