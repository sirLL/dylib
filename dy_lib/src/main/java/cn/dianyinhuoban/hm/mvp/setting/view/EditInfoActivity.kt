package cn.dianyinhuoban.hm.mvp.setting.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.setting.contract.ProfileContract
import cn.dianyinhuoban.hm.mvp.setting.presenter.ProfilePresenter
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.dy_activity_edit_info.*

class EditInfoActivity : BaseActivity<ProfilePresenter>(), ProfileContract.View {

    var reqType: Int = 0;

    companion object {
        const val REQ_TYPE_NICK: Int = 1
        const val REQ_TYPE_TEAM: Int = 2

        fun open(context: Activity) {
            var intent = Intent(context, EditInfoActivity::class.java)
            context.startActivity(intent)
        }

        fun open(context: Activity,reqCode: Int, reqType: Int) {
            var intent = Intent(context, EditInfoActivity::class.java)
            intent.putExtra("Type", reqType)
            context.startActivityForResult(intent, reqCode)
        }

        fun open(context: Activity,reqCode: Int, reqType: Int, reqData: String) {
            var intent = Intent(context, EditInfoActivity::class.java)
            intent.putExtra("Type", reqType)
            intent.putExtra("Data", reqData)
            context.startActivityForResult(intent, reqCode)
        }

        fun open(fragment: Fragment,reqCode: Int, reqType: Int) {
            var intent = Intent(fragment.requireContext(), EditInfoActivity::class.java)
            intent.putExtra("Type", reqType)
            fragment.startActivityForResult(intent, reqCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_edit_info)
        setTitle("修改昵称")
        reqType = intent.getIntExtra("Type",0)


        ed_name.setText(if (intent.getStringExtra("Data") == null) "" else intent.getStringExtra("Data").toString())
        
        when(reqType) {
            REQ_TYPE_NICK -> {
                setTitle("修改昵称")
                tv_name.text = "昵称可以是中英文、数字的任意组合，30天只能修改一次哦。"
            }
            REQ_TYPE_TEAM -> {
                setTitle("修改团队名")
                tv_name.text = "团队名称可以是中英文、数字的任意组合，30天只能修改一次哦。"
            }
        }


        setRightButtonText("保存") {
            doSubmitInfo()
        }

        ed_name.requestFocus()

    }

    private fun doSubmitInfo() {
        var editContent = ed_name.text.toString().trim();
        if (editContent.isEmpty()) {
            showToast("请输入你要修改的值")
            return
        }

        when(reqType) {
            REQ_TYPE_TEAM -> {
                mPresenter.updateProfile("","","","",editContent)
            }

            REQ_TYPE_NICK -> {
                mPresenter.updateProfile(editContent,"","","","")
            }
        }

    }

    override fun getPresenter(): ProfilePresenter {
        return ProfilePresenter(this)
    }

    override fun onUpdateSuccess() {
        super.onUpdateSuccess()
        showToast("修改成功")
        var intent = Intent()
        intent.putExtra("data", ed_name.text.toString().trim())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}