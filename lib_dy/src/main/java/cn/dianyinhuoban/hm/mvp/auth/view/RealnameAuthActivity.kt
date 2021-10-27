package cn.dianyinhuoban.hm.mvp.auth.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AlertDialog
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.auth.contract.AuthContract
import cn.dianyinhuoban.hm.mvp.auth.presenter.AuthPresenter
import cn.dianyinhuoban.hm.mvp.bean.UploadResultBean
import cn.dianyinhuoban.hm.mvp.upload.FileModel
import cn.dianyinhuoban.hm.util.CoilEngine
import coil.load
import coil.transform.CircleCropTransformation
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.ValidatorUtils
import com.wareroom.lib_http.CustomResourceSubscriber
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider
import kotlinx.android.synthetic.main.dy_activity_realname_auth.*
import java.io.File

class RealnameAuthActivity : BaseActivity<AuthPresenter?>(), AuthContract.View {

    companion object {
        const val RC_UPLOAD_POSITIVE = 1
        const val RC_UPLOAD_NEGATIVE = 2

        fun open(context: Context) {
            var intent = Intent(context, RealnameAuthActivity::class.java)
            context.startActivity(intent)
        }
    }

    private var positiveURL: String? = null
    private var negativeURL: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_realname_auth)
        setTitle("实名认证")
        setupAction()
        tv_realname_next.setOnClickListener {
            submitAuth()
        }
    }

    private fun setupAction() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                setSubmitEnable()
            }

        }
        ed_name.addTextChangedListener(textWatcher)
        ed_id_card.addTextChangedListener(textWatcher)
        iv_positive.setOnClickListener {
            showImageDialog(RC_UPLOAD_POSITIVE)
        }
        iv_negative.setOnClickListener {
            showImageDialog(RC_UPLOAD_NEGATIVE)
        }
    }

    private fun setSubmitEnable() {
        val name = ed_name.text.toString()
        val idCard = ed_id_card.text.toString()
        tv_realname_next.isEnabled =
            name.length >= 2
                    && ValidatorUtils.isIDCard(idCard)
                    && !positiveURL.isNullOrBlank()
                    && !negativeURL.isNullOrBlank()
    }

    override fun getPresenter(): AuthPresenter? {
        return AuthPresenter(this)
    }

    private fun showImageDialog(requestCode: Int) {
        val selectItems = arrayOf("相册", "拍照")
        AlertDialog.Builder(UserProfileActivity@ this)
            .setTitle("选择照片")
            .setItems(
                selectItems
            ) { _, which ->
                when (which) {
                    0 -> {
                        openGallery(requestCode)
                    }

                    1 -> {
                        openCamera(requestCode)
                    }
                }
            }.create().show()
    }

    private fun openGallery(requestCode: Int) {
        PictureSelector
            .create(UserProfileActivity@ this)
            .openGallery(PictureMimeType.ofImage())
            .isEnableCrop(false)
            .selectionMode(PictureConfig.SINGLE)
            .isCompress(true)
            .imageEngine(CoilEngine.instance)
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: MutableList<LocalMedia>?) {
                    if (!result.isNullOrEmpty()) {
                        handleLocalMedia(requestCode, result[0])
                    }
                }

                override fun onCancel() {

                }
            })
    }

    private fun openCamera(requestCode: Int) {
        PictureSelector
            .create(UserProfileActivity@ this)
            .openCamera(PictureMimeType.ofImage())
            .isEnableCrop(false)
            .selectionMode(PictureConfig.SINGLE)
            .isCompress(true)
            .imageEngine(CoilEngine.instance)
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: MutableList<LocalMedia>?) {
                    if (!result.isNullOrEmpty()) {
                        handleLocalMedia(requestCode, result[0])
                    }
                }

                override fun onCancel() {

                }

            })
    }

    private fun handleLocalMedia(requestCode: Int, localMedia: LocalMedia) {
        val path = if (localMedia.compressPath.isNullOrBlank()) {
            localMedia.path
        } else {
            localMedia.compressPath
        }
        doUploadFile(requestCode, path)
    }

    @SuppressLint("CheckResult")
    fun doUploadFile(requestCode: Int, filePath: String) {
        showLoading()
        FileModel()
            .upload(File(filePath))
            .compose(SchedulerProvider.getInstance().applySchedulers())
            .compose(ResponseTransformer.handleResult())
            .subscribeWith(object : CustomResourceSubscriber<UploadResultBean?>() {
                override fun onError(exception: ApiException?) {
                    hideLoading()
                    showToast(exception?.displayMessage)
                }


                override fun onNext(t: UploadResultBean) {
                    super.onNext(t)
                    hideLoading()
                    when (requestCode) {
                        RC_UPLOAD_POSITIVE -> {
                            iv_positive.load(t.url)
                            positiveURL = t.url
                        }
                        RC_UPLOAD_NEGATIVE -> {
                            iv_negative.load(t.url)
                            negativeURL = t.url
                        }
                        else -> {

                        }
                    }
                    setSubmitEnable()
                }
            })
    }

    private fun submitAuth() {
        val name = ed_name.text.toString()
        val idCard = ed_id_card.text.toString()
        mPresenter?.submitAuth(name, idCard, positiveURL ?: "", negativeURL ?: "")
    }

    override fun onSubmitAuthSuccess() {
        showToast("提交成功")
        finish()
    }
}