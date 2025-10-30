package com.inspur.dsp.direct.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCatalogAssociatedDTO implements Serializable {
    private String keyword;
    private String dataid;
    private List<String> orgCodeList;
}