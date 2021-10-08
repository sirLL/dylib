package cn.dianyinhuoban.hm.mvp.pk.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.PkMember
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_pk_list.*

class PkListActivity : BaseActivity<IPresenter?>() {

    var mSelectType: String = PkMemberFragment.TYPE_PERSONAL

    companion object {

        const val REQ_CODE_SEARCH = 1;

        fun open(context: Context,reqCode: Int,selectType: String) {
            var intent = Intent(context, PkListActivity::class.java)
            intent.putExtra("type", selectType)
            (context as Activity).startActivityForResult(intent, reqCode)
        }

        fun open(context: Fragment,reqCode: Int,selectType: String) {
            var intent = Intent(context.requireContext(), PkListActivity::class.java)
            intent.putExtra("type", selectType)
            context.startActivityForResult(intent,reqCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pk_list)
        setTitle("选择PK对象")

        mSelectType = intent.getStringExtra("type").toString()

        el_search_btn.setOnClickListener {
            PkSearchListActivity.open(PkListActivity@ this, REQ_CODE_SEARCH,mSelectType)
            overridePendingTransition(R.anim.dy_fade_in, R.anim.dy_fade_out)
        }

        supportFragmentManager
            .beginTransaction()
            .add(
                R.id.el_pk_member_container,
                PkMemberFragment.newInstance(mSelectType), ""
            )
            .commit()
    }

    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_SEARCH && resultCode == Activity.RESULT_OK) {
            var pkMember = data?.getParcelableExtra<PkMember>("data")

            if (pkMember != null) {
                confirmSelectPkMember(pkMember)
            }

        }
    }

    public fun confirmSelectPkMember(data: PkMember) {
        var returnIntent = Intent()
        returnIntent.putExtra("data",data)
        setResult(Activity.RESULT_OK,returnIntent)
        finish()
    }
}