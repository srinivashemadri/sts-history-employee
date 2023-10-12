package com.sts.history.employee.controller;


import com.sts.history.employee.model.*;
import com.sts.history.employee.model.CompanyHistory;
import com.sts.history.employee.service.EmployeeHistorySvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/history/employee")
public class EmployeeHistoryControllerImpl implements EmployeeHistoryController {

    @Autowired
    EmployeeHistorySvc employeeHistorySvc;

    @Override
    public ResponseEntity<RegisterCompanyHistoryResponse> registerCompanyHistory(List<CompanyHistory> companyHistoryList, String empId){
        return employeeHistorySvc.registerCompanyHistory(companyHistoryList, empId);
    }

    @Override
    public ResponseEntity<UpdateBGVStatusResponse> updateBGVStatus(List<BGVModel> bgvModels) {
        return employeeHistorySvc.updateBGVStatus(bgvModels);
    }

    @Override
    public ResponseEntity<GetCompanyHistoryOfEmployeeResponse> getAllPreviousCompanyDetails(String empId) {
        return employeeHistorySvc.getAllPreviousCompanyDetails(empId);
    }

    @Override
    public ResponseEntity<GetAllBGVFailedEmpIDResponse> getAllEmpIdsOfStatusFailed() {
        return employeeHistorySvc.getAllEmpIdsOfStatusFailed();
    }
}