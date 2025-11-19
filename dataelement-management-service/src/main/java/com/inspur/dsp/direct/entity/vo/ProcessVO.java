package com.inspur.dsp.direct.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessVO implements Serializable {

    private String sourceactivityname;
    
    private String processusername;
    
    private String processunitname;
    
    private Date processdatetime;
}