package com.bmw.passbook.dto;

import com.bmw.passbook.entity.Merchant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

//优惠卷模板信息
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassTemplateInfo extends PassTemplate{
    private PassTemplate passTemplate;
    private Merchant merchant;

}
