package cn.dianyinhuoban.hm.widget.dialog

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import cn.dianyinhuoban.hm.R
import com.hjq.toast.ToastUtils
import com.wareroom.lib_base.utils.Base64Utils
import com.wareroom.lib_base.utils.DimensionUtils
import kotlinx.android.synthetic.main.dialog_image_code.*

class ImageCodeDialog : DialogFragment() {

    private var code: String? = null
    private var img: String? = null
    private var mImageCodeCallBack: ImageCodeCallBack? = null

    companion object {
        fun newInstance(): ImageCodeDialog {
            val args = Bundle()
            val fragment = ImageCodeDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.dialog_image_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_cancel.setOnClickListener {
            dismiss()
        }
        tv_sure.setOnClickListener {
            val inputCode = ed_code.text.toString()
            if (inputCode.isEmpty()) {
                ToastUtils.show("请输入验证码")
                return@setOnClickListener
            }

            mImageCodeCallBack?.let { imageCodeCallBack ->
                imageCodeCallBack.getImageCode(code, inputCode)
            }
            dismiss()
        }
        iv_code.setOnClickListener {
            mImageCodeCallBack?.let { imageCodeCallBack ->
                imageCodeCallBack.changeImage()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        val layoutParams = window?.attributes
        layoutParams?.width = (DimensionUtils.getScreenWidth(activity) * 0.8f).toInt()
        window?.attributes = layoutParams

        img?.let {
            if (it.isEmpty()) return
            iv_code?.setImageBitmap(Base64Utils.base64ToBitmap(it))
        }
    }

    fun setImageCodeCallBack(imageCodeCallBack: ImageCodeCallBack) {
        mImageCodeCallBack = imageCodeCallBack
    }

    fun loadImageCode(code: String?, img: String?) {
        img?.let {
            if (it.isEmpty()) return
            this.img = it
            this.code = code
            iv_code?.setImageBitmap(Base64Utils.base64ToBitmap(it))
        }
    }

    interface ImageCodeCallBack {
        fun getImageCode(code: String?, inputCode: String)

        fun changeImage()
    }
}