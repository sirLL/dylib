package cn.dianyinhuoban.hm.mvp.machine.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class MemberPickerActivity : BaseActivity<IPresenter?>() {
    private var mTitle: String = "选择人员"
    private var mCheckedID: String = "-1"

    companion object {
        fun openMemberPicker(
            activity: Activity,
            checkedID: String,
            title: String,
            requestCode: Int
        ) {
            val bundle = Bundle()
            bundle.putString("checkedID", checkedID)
            bundle.putString("title", title)
            val intent = Intent(activity, MemberPickerActivity::class.java)
            intent.putExtras(intent)
            activity.startActivityForResult(intent, requestCode)
            activity.overridePendingTransition(R.anim.anim_act_bottom_in, R.anim.fade_out)
        }

        fun openMemberPicker(
            fragment: Fragment,
            checkedID: String,
            title: String,
            requestCode: Int
        ) {
            val bundle = Bundle()
            bundle.putString("checkedID", checkedID)
            bundle.putString("title", title)
            val intent = Intent(fragment.context, MemberPickerActivity::class.java)
            intent.putExtras(intent)
            fragment.startActivityForResult(intent, requestCode)
        }
    }

    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun toolbarIsEnable(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_picker)
        setTitle(mTitle)
        supportFragmentManager.beginTransaction()
            .add(R.id.fl_container, MemberPickerFragment.newInstance(mCheckedID)).commit()
    }

    override fun handleIntent(bundle: Bundle?) {
        super.handleIntent(bundle)
        if (bundle != null) {
            mCheckedID = bundle.getString("", "-1")
            mTitle = bundle.getString("title", "选择人员")
        }
    }
}