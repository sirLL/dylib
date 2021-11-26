package cn.dianyinhuoban.hm.api

interface URLConfig {
    companion object {

        //关于我们
        const val PAGE_ABOUT_US = "news_aboutUs.html"

        //用户协议
        const val PAGE_USER_AGREEMENT = "news_userAgreement.html"

        //网页注册
        const val PAGE_WEB_REGISTER = "http://m.dyhm.shop/signup/"

        //发送短信
        const val URL_SEND_SMS = "sms_aliSms.html"

        //图片验证码
        const val URL_IMAGE_CODE = "com_getVailImg.html"

        //注册
        const val URL_REGISTER = "user_register.html"

        //登录
        const val URL_LOGIN = "user_login.html"

        //首页数据
        const val URL_HOME = "user_home.html"

        const val URL_ADD_BANK = "user_addBank.html"

        const val URL_BANK_LIST = "user_bankList.html"

        const val URL_BANK_SET = "user_bankSetting.html"

        //个人中心
        const val URL_ME = "user_mine.html"

        //我的成员团队成员/个人收益排行
        const val URL_TEAM_MEMBER = "user_myTeam.html"

        //排行榜
        const val URL_RANK = "user_rankList.html"

        //机具产品类别列表
        const val URL_MACHINE_TYPE = "machine_productList.html"

        //修改个人信息
        const val URL_UPDATE_PROFILE = "user_updatePersonal.html"

        //修改头像
        const val URL_UPDATE_AVATAR = "user_updateAvatar.html"

        const val URL_CHANGE_PASSWORD = "user_modifyPassword.html"

        //我的机具列表
        const val URL_MY_MACHINE = "machine_myMachine.html"

        //账单明细 个人收益/团队收益/激活返现/申领奖励
        const val URL_INCOME_DETAIL = "bill_billDetail.html"

        //提现记录
        const val URL_WITHDRAW_RECORD = "user_withdrawLog.html"

        const val URL_PROFILE = "user_memberInfo.html"

        //申领产品列表
        const val URL_SUBMIT_ORDER_PRODUCT = "machine_purchaseProduct.html"

        //发起申领
        const val URL_SUBMIT_ORDER = "machine_purchase.html"

        //添加/修改收货地址
        const val URL_SHIP_ADDRESS = "machine_addAddress.html"

        //团队人员机具数据
        const val URL_TEAM_MACHINE = "machine_teamMachine.html"

        //PK数据
        const val URL_PK_LIST = "peak_peakIndex.html"

        //发起PK
        const val URL_PK = "peak_launchPk.html"

        //机具划拨
        const val URL_SUBMIT_TRANSFER = "machine_allotMachine.html"

        //pk对象搜索
        const val URL_PK_SEARCH = "peak_equalSearch.html"

        //忘记密码
        const val URL_FORGET_PASSWORD = "user_forgetPassword.html"

        //PK回应
        const val URL_PK_RESPONSE = "peak_respondPk.html"

        //划拨记录
        const val URL_TRANSFER_RECORD = "machine_allotLog.html"

        //划拨记录详情
        const val URL_TRANSFER_RECORD_DETAIL = "machine_allotLogDetail.html"

        //机具激活后交易明细
        const val URL_TRADE_RECORD = "machine_machineTransLog.html"

        //地址列表
        const val URL_ADDRESS_LIST = "machine_addressList.html"

        //申领记录
        const val URL_PURCHASE_RECORD = "machine_purchaseLog.html"

        //申领单确认收货
        const val URL_CONFIRM_RECEIPT = "machine_purchaseConfirm.html"

        //上传文件
        const val URL_UPLOAD_FILE = "file_uploadFile.html"

        //通知列表
        const val URL_MESSAGE_LIST = "news_noticeList.html"

        //通知详情
        const val URL_MESSAGE_DETAIL = "news_noticeDetail.html"

        //申请授权书
        const val URL_AUTH_APPLY = "auth_authorizationApply.html"

        //讲武堂
        const val URL_JWT_LIST = "news_wuList.html"


        //申请提现
        const val URL_SUBMIT_WITHDRAW = "user_withdraw.html"

        //删除地址
        const val URL_DELETE_ADDRESS = "machine_delAddress.html"

        //实名认证（姓名身份证二要素）
        const val URL_AUTH_2_INFO = "auth_nameCardValidate.html"

        //获取实名信息
        const val URL_AUTH_RESULT = "auth_getAuthStatus.html"

        //我的成员个人收益排行
        const val URL_MEMBER_RANK = "user_myTeamMemberRank.html"

        //我的客户
        const val URL_MY_CLIENT = "user_myClient.html"

        //公告列表
        const val URL_NOTICE_LIST = "news_bulletinList.html"

        //成员详情
        const val URL_MEMBER_INFO = "user_memberDetail.html"

        //成员
        const val URL_EXIT = "user_logOut.html"

        //系统设置
        const val URL_SYSTEM_SETTING = "user_systemSetting.html"

        //分享
        const val URL_SHARE_IMG = "news_shareBg.html"

        //首页活动弹出图片
        const val URL_DIALOG_BANNER = "news_homePopup.html"

        //首页banner
        const val URL_BANNER_LIST = "news_homeBanner.html"

        //PK记录
        const val URL_PK_RECORD = "peak_peakLog.html"

        //海报分类
        const val URL_POSTER_TYPE = "news_postersCate.html"

        //海报列表
        const val URL_POSTER_LIST = "news_postersList.html"

        //修改支付密码
        const val URL_CHANGE_PAY_PASSWORD = "user_modifyPayPassword.html"

        //忘记支付密码
        const val URL_RESET_PAY_PASSWORD = "user_forgetPayPassword.html"

        //提现手续费
        const val URL_WITHDRAW_FEE = "user_cashFee.html"

        //团队成员新版
        const val URL_MY_TEAM = "user_myTeamNew.html"

        //会员等级
        const val URL_MEMBER_LEVEL_LIST = "user_memberGroup.html"
    }

}