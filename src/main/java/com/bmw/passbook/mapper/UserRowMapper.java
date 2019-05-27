package com.bmw.passbook.mapper;


import com.bmw.passbook.constant.Constants;
import com.bmw.passbook.dto.User;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public class UserRowMapper {
    private static byte[] FAMILY_B = Constants.UserTable.FAMILY_B.getBytes();
    private static byte[] NAME = Constants.UserTable.NAME.getBytes();
    private static byte[] AGE = Constants.UserTable.AGE.getBytes();
    private static byte[] SEX = Constants.UserTable.SEX.getBytes();
    private static byte[] FAMILY_O = Constants.UserTable.FAMILY_O.getBytes();
    private static byte[] PHONE = Constants.UserTable.PHONE.getBytes();
    private static byte[] ADDRESS = Constants.UserTable.ADDRESS.getBytes();

    public User map(Result result){
        if(result.isEmpty()){
            return null;
        }
        User.BaseInfo baseInfo = new User.BaseInfo();
        baseInfo.setName(Bytes.toString(result.getValue(FAMILY_B,NAME)));
        baseInfo.setAge(Bytes.toInt(result.getValue(FAMILY_B,AGE)));
        baseInfo.setSex(Bytes.toString(result.getValue(FAMILY_B,SEX)));

        User.OtherInfo otherInfo = new User.OtherInfo();
        otherInfo.setAddress(Bytes.toString(result.getValue(FAMILY_O,ADDRESS)));
        otherInfo.setPhone(Bytes.toString(result.getValue(FAMILY_O,PHONE)));

        User user = new User();
        user.setId(Bytes.toLong(result.getRow()));
        user.setBaseInfo(baseInfo);
        user.setOtherInfo(otherInfo);
        return user;
    }
}
