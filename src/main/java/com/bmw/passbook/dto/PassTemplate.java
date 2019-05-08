package com.bmw.passbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassTemplate {
    private Integer id;
    private String title;
    private String summary;
    private String desc;
    private Long limit;
    private Boolean hasToken;
    private Integer background;
    private Date start;
    private Date end;

}
