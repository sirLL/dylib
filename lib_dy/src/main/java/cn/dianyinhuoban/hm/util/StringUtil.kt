package cn.dianyinhuoban.hm.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService


class StringUtil {
    companion object {
        fun getBankCardEndNo(bankCardNo: String?): String {
            bankCardNo?.let {
                return if (it.length > 4) {
                    it.substring(bankCardNo.length - 4)
                } else {
                    it
                }
            }
            return "--"
        }

        fun copyString(context: Context, content: String) {
            val cm: ClipboardManager? = context.getSystemService(Context.CLIPBOARD_SERVICE)
                    as ClipboardManager?
            val mClipData: ClipData = ClipData.newPlainText("Label", content)
            cm?.setPrimaryClip(mClipData)
        }
    }


}