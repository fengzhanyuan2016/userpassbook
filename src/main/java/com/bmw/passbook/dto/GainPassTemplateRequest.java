package com.bmw.passbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GainPassTemplateRequest {
    private Long userId;
    private PassTemplate passTemplate;

}
