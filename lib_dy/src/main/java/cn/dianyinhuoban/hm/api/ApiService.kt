package cn.dianyinhuoban.hm.api

import cn.dianyinhuoban.hm.bean.CustomModel
import cn.dianyinhuoban.hm.mvp.bean.*
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable
import retrofit2.http.*

interface ApiService {

    /**
     * 发送短信
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_SEND_SMS)
    fun sendSMS(
        @Field("phone") phone: String,
        @Field("imgcode") imageCode: String,
        @Field("imgcodekey") imageKey: String,
    ): Observable<Response<EmptyBean?>>

    /**
     * 图形验证码
     */
    @GET(URLConfig.URL_IMAGE_CODE)
    fun fetchImageCode(): Observable<Response<ImageCodeBean?>>

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_REGISTER)
    fun submitRegister(
        @Field("username") userName: String,
        @Field("code") msmCode: String,
        @Field("password") password: String,
        @Field("payPassword") payPassword: String,
        @Field("invite") inviteCode: String,
        @Field("deviceid") deviceID: String,
    ): Observable<Response<UserBean?>>

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_LOGIN)
    fun submitLogin(
        @Field("username") userName: String,
        @Field("password") password: String,
    ): Observable<Response<UserBean?>>

    /**
     * 首页数据
     */
    @GET(URLConfig.URL_HOME)
    fun fetchHomeData(): Observable<Response<HomeDataBean?>>


    /**
     * 添加银行卡
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_ADD_BANK)
    fun addBank(
        @Field("name") name: String,
        @Field("bankName") bankName: String,
        @Field("bankNo") bankNo: String,
        @Field("phone") phone: String,
        @Field("code") code: String,
    ): Observable<Response<EmptyBean?>>


    /**
     * 修改银行卡
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_ADD_BANK)
    fun updateBank(
        @Field("name") name: String,
        @Field("bankName") bankName: String,
        @Field("bankNo") bankNo: String,
        @Field("phone") phone: String,
        @Field("code") code: String,
        @Field("id") id: String,
    ): Observable<Response<EmptyBean?>>


    /**
     * 我的
     */
    @GET(URLConfig.URL_ME)
    fun fetchPersonalData(): Observable<Response<PersonalBean?>>

    /**
     *我的成员团队成员
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_TEAM_MEMBER)
    fun fetchTeamMember(
        @Field("type") type: String,
        @Field("name") keyWords: String,
        @Field("page") page: Int,
    ): Observable<Response<List<TeamMemberBean>?>>

    /**
     * 个人收益排行
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_MEMBER_RANK)
    fun fetchMemberRank(
        @Field("page") page: Int,
    ): Observable<Response<MemberRankBean?>>

    /**
     *排行榜
     * @param type 1个人排行 2 团队
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_RANK)
    fun fetchRank(
        @Field("type") type: String,
        @Field("page") page: Int,
    ): Observable<Response<RankBean?>>

    /**
     * 机具产品类别列表
     */
    @GET(URLConfig.URL_MACHINE_TYPE)
    fun fetchMachineType(): Observable<Response<List<MachineTypeBean>?>>

    /**
     * 我的机具列表
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_MY_MACHINE)
    fun fetchMyMachine(
        @Field("machine_type_id") type: String,
        @Field("act_status") status: String,
        @Field("sn") sn: String,
        @Field("page") page: Int,
    ): Observable<Response<MyMachineBean?>>

    /**
     * 账单明细 个人收益/团队收益/激活返现/申领奖励
     * @param type  1个人收益 2 团队收益 3 激活返现 4 申领奖励
     * @param date  日搜索：2021-07-06 月搜索 2021-07
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_INCOME_DETAIL)
    fun fetchIncomeDetail(
        @Field("type") type: String,
        @Field("date") date: String,
        @Field("page") page: Int,
    ): Observable<Response<IncomeDetailBean?>>

    /**
     * 提现记录
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_WITHDRAW_RECORD)
    fun fetchWithdrawRecord(
        @Field("page") page: Int,
    ): Observable<Response<List<WithdrawRecordBean>?>>

    /**
     * 申领产品列表
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_SUBMIT_ORDER_PRODUCT)
    fun fetchPurchaseProduct(
        @Field("id") typeID: String,
        @Field("page") page: Int,
    ): Observable<Response<List<PurchaseProductBean>?>>

    /**
     * 发起申领
     * @param payType 支付方式 1 余额 2 其他（未定）
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_SUBMIT_ORDER)
    fun submitPurchaseOrder(
        @Field("machineId") productID: String,
        @Field("num") num: String,
        @Field("addressId") addressID: String,
        @Field("payType") payType: String,
        @Field("payPassword") password: String,
    ): Observable<Response<PayInfoBean?>>


    /**
     * 忘记密码
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_FORGET_PASSWORD)
    fun forgetPassword(
        @Field("phone") phone: String,
        @Field("code") code: String,
        @Field("password") password: String,
    ): Observable<Response<EmptyBean?>>

    /**
     * 团队人员机具数据
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_TEAM_MACHINE)
    fun fetchTeamMachine(
        @Field("name") name: String,
        @Field("page") page: Int,
    ): Observable<Response<List<TeamMachineItemBean>?>>

    /**
     * 机具划拨
     * @param isPay 是否付款 1 付款 2 不付款
     * @param transferType 划拨方式 1 选择划拨 2 批量划拨
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_SUBMIT_TRANSFER)
    fun submitMachineTransfer(
        @Field("receiverUid") receiverUID: String,
        @Field("machineTypeId") machineType: String,
        @Field("isPay") isPay: String,
        @Field("price") price: String,
        @Field("machineIds") machineIds: String,
        @Field("type") transferType: String,
        @Field("start") startMachineID: String,
        @Field("end") endMachineID: String,
    ): Observable<Response<EmptyBean?>>

    /**
     * 划拨记录
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_TRANSFER_RECORD)
    fun fetchTransferRecord(
        @Field("page") page: Int,
    ): Observable<Response<List<TransferRecordBean>?>>

    /**
     * 划拨记录详情
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_TRANSFER_RECORD_DETAIL)
    fun fetchTransferRecordDetail(
        @Field("logId") recordID: String,
        @Field("page") page: Int,
    ): Observable<Response<List<TransferRecordDetailBean>?>>

    /**
     * 机具激活后交易明细
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_TRADE_RECORD)
    fun fetchTradeRecord(
        @Field("pos_sn") sn: String,
        @Field("page") page: Int,
    ): Observable<Response<List<MachineTradeRecordBean>?>>

    /**
     *地址列表
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_ADDRESS_LIST)
    fun fetchAddressList(
        @Field("page") page: Int,
    ): Observable<Response<List<AddressBean>?>>

    /**
     * 申领记录
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_PURCHASE_RECORD)
    fun fetchPurchaseRecordList(
        @Field("status") status: String,
        @Field("page") page: Int,
    ): Observable<Response<List<OrderBean>?>>

    /**
     *申领记录详情
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_PURCHASE_RECORD)
    fun fetchPurchaseRecord(
        @Field("id") orderID: String,
    ): Observable<Response<List<OrderBean>?>>

    /**
     * 申领单确认收货
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_CONFIRM_RECEIPT)
    fun submitConfirmReceipt(
        @Field("id") orderID: String,
    ): Observable<Response<EmptyBean?>>

    /**
     * 申请提现
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_SUBMIT_WITHDRAW)
    fun submitWithdraw(
        @Field("bankId") orderID: String,
        @Field("amount") amount: String,
        @Field("payPassword") payPassword: String,
    ): Observable<Response<EmptyBean?>>

    /**
     * 删除地址
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_DELETE_ADDRESS)
    fun deleteAddress(
        @Field("ids") ids: String,
    ): Observable<Response<EmptyBean?>>

    /**
     * 实名认证（姓名身份证二要素）
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_AUTH_2_INFO)
    fun submitAuth(
        @Field("name") name: String,
        @Field("idNum") idCard: String,
    ): Observable<Response<EmptyBean?>>

    /**
     * 获取实名信息
     */
    @GET(URLConfig.URL_AUTH_RESULT)
    fun fetchAuthResult(): Observable<Response<AuthResult?>>

    /**
     * 我的客户
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_MY_CLIENT)
    fun fetchMyClientBean(@Field("page") page: Int): Observable<Response<List<MyClientBean>?>>

    /**
     * 公告列表
     */
    @GET(URLConfig.URL_NOTICE_LIST)
    fun fetchNoticeList(): Observable<Response<List<CustomModel>?>>

    /**
     * 成员详情
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_MEMBER_INFO)
    fun fetchMemberDetail(
        @Field("memberId") memberID: String,
        @Field("month") month: String,
    ): Observable<Response<MemberDetailBean?>>

    /**
     * 系统设置
     */
    @GET(URLConfig.URL_SYSTEM_SETTING)
    fun fetchSystemSetting(): Observable<Response<List<SystemItemBean>?>>

    /**
     * 分享图片
     */
    @GET(URLConfig.URL_SHARE_IMG)
    fun fetchShareImage(): Observable<Response<List<ShareItemBean>?>>

    /**
     * 首页活动弹出图片
     */
    @GET(URLConfig.URL_DIALOG_BANNER)
    fun fetchDialogBanner(): Observable<Response<List<BannerBean>?>>

    /**
     * 首页banner
     */
    @GET(URLConfig.URL_BANNER_LIST)
    fun fetchBannerList(): Observable<Response<List<BannerBean>?>>

    /**
     * 海报分类
     */
    @GET(URLConfig.URL_POSTER_TYPE)
    fun fetchPosterType(): Observable<Response<List<PosterTypeBean>?>>

    /**
     * 海报列表
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_POSTER_LIST)
    fun fetchPosterList(
        @Field("id") typeID: String,
        @Field("page") page: Int,
    ): Observable<Response<List<PosterItemBean>?>>

    /**
     * 修改支付密码
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_CHANGE_PAY_PASSWORD)
    fun submitPayPassword(
        @Field("oldPassword") oldPassword: String,
        @Field("password") password: String,
        @Field("passwordConfirm") passwordConfirm: String,
    ): Observable<Response<EmptyBean?>>

    /**
     * 忘记支付密码
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_RESET_PAY_PASSWORD)
    fun submitPayPassword(
        @Field("password") password: String,
        @Field("code") code: String,
    ): Observable<Response<EmptyBean?>>
}