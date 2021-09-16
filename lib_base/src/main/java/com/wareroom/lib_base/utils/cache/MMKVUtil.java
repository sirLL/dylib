package com.wareroom.lib_base.utils.cache;

import com.tencent.mmkv.MMKV;

public class MMKVUtil {

    /************************************* 用户信息 START *************************************/
    private static final String KEY_USER_USER_NAME = "KEY_USER_USER_NAME";
    private static final String KEY_USER_USER_ID = "KEY_USER_USER_ID";
    private static final String KEY_USER_TOKEN = "KEY_USER_TOKEN";
    private static final String KEY_USER_NICK = "KEY_USER_NICK";
    private static final String KEY_USER_PHONE = "KEY_USER_PHONE";
    private static final String KEY_USER_LOGIN_PASSWORD = "KEY_USER_LOGIN_PASSWORD";
    private static final String KEY_USER_INVITE_CODE = "KEY_USER_INVITE_CODE";
    private static final String KEY_USER_AVATAR = "KEY_USER_AVATAR";
    private static final String KEY_USER_TEAM_NAME = "KEY_USER_TEAM_NAME";
    private static final String KEY_USER_IS_TEAM_LEADER = "KEY_USER_IS_TEAM_LEADER";

    //UID
    public static boolean saveUserID(String userID) {
        return MMKV.defaultMMKV().encode(KEY_USER_USER_ID, userID);
    }

    public static String getUserID() {
        return MMKV.defaultMMKV().decodeString(KEY_USER_USER_ID, "");
    }

    //用户名
    public static boolean saveUserName(String userName) {
        return MMKV.defaultMMKV().encode(KEY_USER_USER_NAME, userName);
    }

    public static String getUserName() {
        return MMKV.defaultMMKV().decodeString(KEY_USER_USER_NAME, "");
    }

    //登录密码
    public static boolean saveLoginPassword(String loginPassword) {
        return MMKV.defaultMMKV().encode(KEY_USER_LOGIN_PASSWORD, loginPassword);
    }

    public static String getLoginPassword() {
        return MMKV.defaultMMKV().decodeString(KEY_USER_LOGIN_PASSWORD, "");
    }

    //token
    public static boolean saveToken(String token) {
        return MMKV.defaultMMKV().encode(KEY_USER_TOKEN, token);
    }

    public static String getToken() {
        return MMKV.defaultMMKV().decodeString(KEY_USER_TOKEN, "");
    }

    //昵称
    public static boolean saveNick(String nick) {
        return MMKV.defaultMMKV().encode(KEY_USER_NICK, nick);
    }

    public static String getNick() {
        return MMKV.defaultMMKV().decodeString(KEY_USER_NICK, "");
    }

    //电话号码
    public static boolean savePhone(String phone) {
        return MMKV.defaultMMKV().encode(KEY_USER_PHONE, phone);
    }

    public static String getPhone() {
        return MMKV.defaultMMKV().decodeString(KEY_USER_PHONE, "");
    }

    //邀请码
    public static boolean saveInviteCode(String phone) {
        return MMKV.defaultMMKV().encode(KEY_USER_INVITE_CODE, phone);
    }

    public static String getInviteCode() {
        return MMKV.defaultMMKV().decodeString(KEY_USER_INVITE_CODE, "");
    }

    //头像
    public static boolean saveAvatar(String avatar) {
        return MMKV.defaultMMKV().encode(KEY_USER_AVATAR, avatar);
    }

    public static String getAvatar() {
        return MMKV.defaultMMKV().decodeString(KEY_USER_AVATAR, "");
    }


    //团队名称
    public static boolean saveTeam(String teamName) {
        return MMKV.defaultMMKV().encode(KEY_USER_TEAM_NAME, teamName);
    }

    public static String getTeam() {
        return MMKV.defaultMMKV().decodeString(KEY_USER_TEAM_NAME, "");
    }

    public static boolean saveIsTeamLeader(boolean isTeamLeader) {
        return MMKV.defaultMMKV().encode(KEY_USER_IS_TEAM_LEADER, isTeamLeader);
    }

    public static boolean getIsTeamLeader() {
        return MMKV.defaultMMKV().decodeBool(KEY_USER_IS_TEAM_LEADER, false);
    }
    /************************************* 用户信息 END *************************************/
}
