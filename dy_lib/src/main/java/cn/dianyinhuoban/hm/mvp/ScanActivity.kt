package cn.dianyinhuoban.hm.mvp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import cn.bingoogolapple.qrcode.core.QRCodeView
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.util.CoilEngine
import com.gyf.immersionbar.ImmersionBar
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.dy_activity_scan.*

class ScanActivity : BaseActivity<IPresenter?>(), QRCodeView.Delegate {

    companion object {
        fun openScan(activity: AppCompatActivity, requestCode: Int) {
            activity.startActivityForResult(Intent(activity, ScanActivity::class.java), requestCode)
        }

        fun openScan(fragment: Fragment, requestCode: Int) {
            fragment.startActivityForResult(
                Intent(fragment.context, ScanActivity::class.java),
                requestCode
            )
        }

        fun getScanResult(data: Intent?): String {
            var scanResult: String? = null
            data?.let {
                val bundle = data.extras
                scanResult = bundle?.getString("scanResult", "")
            }
            return if (scanResult == null) {
                ""
            } else {
                scanResult!!
            }
        }
    }

    override fun isDarkModeEnable(): Boolean {
        return false
    }

    override fun getStatusBarColor(): Int {
        return R.color.dy_base_color_transparent
    }

    override fun getToolbarColor(): Int {
        return Color.TRANSPARENT
    }

    override fun getBackButtonIcon(): Int {
        return R.drawable.dy_base_ic_back_white
    }

    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun getRootView(): Int {
        return R.layout.dy_activity_scan_root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_scan)
        setupStatusBar()
        zxingview.setDelegate(ScanActivity@ this)
        setTitle("扫一扫", Color.WHITE)
        tv_gallery.setOnClickListener {
            openGallery()
        }
    }

    override fun initStatusBar() {

    }

    private fun setupStatusBar() {
        ImmersionBar.with(this)
            .autoDarkModeEnable(isDarkModeEnable)
            .autoStatusBarDarkModeEnable(isDarkModeEnable)
            .statusBarColor(statusBarColor)
            .flymeOSStatusBarFontColor(statusBarColor)
            .titleBarMarginTop(mToolbar)
            .init()
    }

    override fun onStart() {
        super.onStart()
        val rxPermissions = RxPermissions(ScanActivity@ this)
        rxPermissions.request(
            Manifest.permission.CAMERA
        ).subscribe { aBoolean: Boolean ->
            if (aBoolean) {
                zxingview.startCamera()
                zxingview.startSpotAndShowRect()
            } else {
                showToast("你尚未开启相机权限")
                finish()
            }
        }

    }

    override fun onStop() {
        zxingview.stopCamera()
        super.onStop()
    }

    override fun onDestroy() {
        zxingview.onDestroy()
        super.onDestroy()
    }

    override fun onScanQRCodeSuccess(result: String?) {
        if (TextUtils.isEmpty(result)) {
            showToast("未发现二维码")
            zxingview.startSpot()
            return
        }
        vibrate()
        val bundle = Bundle()
        bundle.putString("scanResult", result)
        val intent = Intent()
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
        var tipText: String = zxingview.scanBoxView.tipText
        val ambientBrightnessTip = "\n环境过暗，请打开闪光灯"
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                zxingview.scanBoxView.tipText = tipText + ambientBrightnessTip
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip))
                zxingview.scanBoxView.tipText = tipText
            }
        }
    }

    override fun onScanQRCodeOpenCameraError() {
        showToast("打开相机出错")
    }

    private fun vibrate() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(200)
    }

    private fun openGallery() {
        PictureSelector.create(this)
            .openGallery(PictureMimeType.ofImage())
            .imageEngine(CoilEngine.instance)
            .selectionMode(PictureConfig.SINGLE)
            .isCompress(false)
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: MutableList<LocalMedia>?) {
                    result?.let {
                        if (it.isNotEmpty()) {
                            val localMedia = it[0]
                            localMedia?.let { media ->
                                handleGalleryResult(media)
                            }
                        }
                    }
                }

                override fun onCancel() {

                }
            })
    }

    private fun handleGalleryResult(localMedia: LocalMedia) {
        var path = localMedia.path
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!TextUtils.isEmpty(localMedia.androidQToPath)) {
                path = localMedia.androidQToPath
            }
        }
        zxingview.decodeQRCode(path)
    }


}