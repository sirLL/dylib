package cn.dianyinhuoban.hm.util

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
    }
}