package com.sts.history.employee.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCompanyHistoryResponse {

    private boolean hasError;
    private String errorMessage;
    private List<CompanyHistory> companyHistoryList;

}
