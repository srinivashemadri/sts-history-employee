package com.sts.history.employee.service;

import com.sts.history.employee.model.*;
import com.sts.history.employee.model.CompanyHistory;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmployeeHistorySvc {

    ResponseEntity<RegisterCompanyHistoryResponse>  registerCompanyHistory(List<CompanyHistory> companyHistoryList, String empId);

    ResponseEntity<UpdateBGVStatusResponse> updateBGVStatus(List<BGVModel> bgvModels);

    ResponseEntity<GetCompanyHistoryOfEmployeeResponse> getAllPreviousCompanyDetails(String empId);

    ResponseEntity<GetAllBGVFailedEmpIDResponse> getAllEmpIdsOfStatusFailed();
}