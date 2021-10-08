package cn.dianyinhuoban.hm.mvp.pk.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_pk_search.*

class PkSearchListActivity : BaseActivity<IPresenter?>() {

    var mSelectType: String = PkMemberFragment.TYPE_PERSONAL

    companion object {

        fun open(context: Context, reqCode: Int, selectType: String) {
            var intent = Intent(context, PkSearchListActivity::class.java)
            intent.putExtra("type", selectType)
            (context as Activity).startActivityForResult(intent, reqCode)
        }
    }

    override fun toolbarIsEnable(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pk_search)

        mSelectType = intent.getStringExtra("type").toString()

        supportFragmentManager
            .beginTransaction()
            .add(
                R.id.el_pk_member_container,
                PkMemberFragment.newInstance(mSelectType), PkMemberFragment.TAG
            )
            .commit()

        ed_search_content.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    val fragment =
                        supportFragmentManager.findFragmentByTag(PkMemberFragment.TAG) as PkMemberFragment
                    fragment.doSearch(s.toString().trim())

                    img_close.visibility = View.VISIBLE
                } else {
                    img_close.visibility = View.INVISIBLE
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        img_close.setOnClickListener {
            ed_search_content.text.clear()
        }

        tv_cancel.setOnClickListener {
            closeActivity()
        }

        ed_search_content.requestFocus()

//        var inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//
//        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

    }

    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun onBackPressed() {
        super.onBackPressed()
        closeActivity()
    }

    private fun closeActivity() {
        finish()
        overridePendingTransition(R.anim.dy_fade_out,R.anim.dy_fade_out2)
    }
}