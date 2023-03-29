//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.fc.javaee.core.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回的错误码
 * @author Ming Qiu
 */
public enum ReturnNo {
    /***************************************************
     *    系统级返回码
     **************************************************/

    //状态码 200
    OK(0,"成功"),
    CREATED(1, "创建成功"),
    STATENOTALLOW(7,"%s对象（id=%d）%s状态禁止此操作"),
    RESOURCE_FALSIFY(11, "信息签名不正确"),

    //状态码 404
    RESOURCE_ID_NOTEXIST(4,"%s对象(id=%d)不存在"),

    //状态码 500
    INTERNAL_SERVER_ERR(2,"服务器内部错误"),
    APPLICATION_PARAM_ERR(20, "服务器配置参数(%s)错误"),

    //所有需要登录才能访问的API都可能会返回以下错误
    //状态码 400
    FIELD_NOTVALID(3,"%s字段不合法"),
    IMG_FORMAT_ERROR(8,"图片格式不正确"),
    IMG_SIZE_EXCEED(9,"图片大小超限"),
    PARAMETER_MISSED(10, "缺少必要参数"),


    //状态码 401
    AUTH_INVALID_JWT(5,"JWT不合法"),
    AUTH_JWT_EXPIRED(6,"JWT过期"),
    AUTH_INVALID_ACCOUNT(12, "用户名不存在或者密码错误"),
    AUTH_ID_NOTEXIST(13,"登录用户id不存在"),
    AUTH_USER_FORBIDDEN(14,"用户被禁止登录"),
    AUTH_NEED_LOGIN(15, "需要先登录"),

    //状态码 403
    AUTH_NO_RIGHT(16, "无权限"),
    RESOURCE_ID_OUTSCOPE(17,"%s对象(id=%d)超出商铺（id = %d）的操作范围"),
    FILE_NO_WRITE_PERMISSION(18,"目录文件夹没有写入的权限"),

    /**************************************
     *  员工模块
     ************************************/
    STAFF_EXIST(103,"员工(name=%s)已经存在"),
    PREFERENCE_EXIST(104,"员工(id=%s)偏好不存在"),

    /***************************************************
     *   商铺模块
     **************************************************/
    STORE_EXIST(199,"商铺(id=%s)已经存在"),

    /***************************************************
     *   排班规则模块
     **************************************************/
    RULE_EXIST(201,"排班规则(id=%s)已经存在"),
    RULE_NOT_FIND(202,"排版规则(type=%s)未找到"),

    /**************************************
     *  排班模块
     ************************************/
    NO_ENOUGH_STAFF(301, "员工在当前设定下不能满足排班要求");


    private int errNo;
    private String message;
    private static final Map<Integer, ReturnNo> returnNoMap = new HashMap() {
        {
            for (Object enum1 : values()) {
                put(((ReturnNo) enum1).errNo, enum1);
            }
        }
    };

    ReturnNo(int code, String message){
        this.errNo = code;
        this.message = message;
    }

    public static ReturnNo getByCode(int code1) {
        ReturnNo[] all=ReturnNo.values();
        for (ReturnNo returnNo :all) {
            if (returnNo.errNo==code1) {
                return returnNo;
            }
        }
        return null;
    }
    public static ReturnNo getReturnNoByCode(int code){
        return returnNoMap.get(code);
    }
    public int getErrNo() {
        return errNo;
    }

    public String getMessage(){
        return message;
    }


    }
