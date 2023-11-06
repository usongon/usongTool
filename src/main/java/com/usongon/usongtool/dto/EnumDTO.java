package com.usongon.usongtool.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author usong
 * @date 2023-11-02 23:00
 */
@Data
public class EnumDTO implements Serializable {
    private String code;

    private String desc;
}
