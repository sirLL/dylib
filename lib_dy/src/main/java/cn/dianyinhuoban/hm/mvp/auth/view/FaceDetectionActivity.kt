package cn.dianyinhuoban.hm.mvp.auth.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.dy_activity_face_detection.*

class FaceDetectionActivity : BaseActivity<IPresenter?>() {

    companion object {

        fun open(context: Context) {
            var intent = Intent(context, FaceDetectionActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_face_detection)
        setTitle("人脸识别")

        tv_start_face.setOnClickListener {
            AuthResultActivity.open(FaceDetectionActivity@ this)
        }

    }

    override fun getPresenter(): IPresenter? {
        return null
    }
}