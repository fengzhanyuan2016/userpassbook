package com.bmw.passbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//可用优惠卷
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {
    private Long userId;
    private List<PassTemplateInfo> passTemplateInfos;
}
