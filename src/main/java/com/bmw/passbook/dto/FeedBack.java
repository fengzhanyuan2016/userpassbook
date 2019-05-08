package com.bmw.passbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedBack {
    private Long userId;
    private String type;
    private String templateId;
    private String comment;

    private boolean validate(){
        return null!=this.comment;
    }
}
