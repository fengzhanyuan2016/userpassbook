package com.bmw.passbook.dto;

import com.bmw.passbook.entity.Merchant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassInfo {
    private Pass pass;
    private PassTemplate passTemplate;
    private Merchant merchant;
}
