package com.bmw.passbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private BaseInfo baseInfo;
    private OtherInfo otherInfo;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BaseInfo{
        private String name;
        private Integer age;
        private String sex;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OtherInfo{
        private String phone;
        private String address;
    }





}
