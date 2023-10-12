package com.sts.history.employee.mapper;


import com.sts.fullprofile.employee.model.BGVStatus;
import com.sts.history.employee.entity.CompanyHistoryEntity;
import com.sts.history.employee.entity.EmployeeFullProfileEntity;
import com.sts.history.employee.model.CompanyHistory;

public class ClassMapper {

    public static CompanyHistory mapCompanyHistoryEntityToModel(CompanyHistoryEntity companyHistoryEntity){
        CompanyHistory companyHistory = new CompanyHistory();
        companyHistory.setHistoryId(companyHistoryEntity.getHistoryId());
        companyHistory.setPrevCompanyAddress(companyHistoryEntity.getPrevCompanyAddress());
        companyHistory.setPrevCompanyDesignation(companyHistoryEntity.getPrevCompanyDesignation());
        companyHistory.setPrevCompanyHREmail(companyHistoryEntity.getPrevCompanyHREmail());
        companyHistory.setPrevCompanyName(companyHistoryEntity.getPrevCompanyName());
        companyHistory.setBackGroundVerificationStatus(companyHistoryEntity.getBackGroundVerificationStatus());
        companyHistory.setPrevCompanyStartDate(companyHistoryEntity.getPrevCompanyStartDate());
        companyHistory.setPrevCompanyEndDate(companyHistoryEntity.getPrevCompanyEndDate());
        return companyHistory;
    }

    public static CompanyHistoryEntity copyHistoryModelToEmployeeEntity(CompanyHistory companyHistory, EmployeeFullProfileEntity employeeFullProfileEntity){
        CompanyHistoryEntity companyHistoryEntity =new CompanyHistoryEntity();
        companyHistoryEntity.setEmployee(employeeFullProfileEntity);
        companyHistoryEntity.setPrevCompanyAddress(companyHistory.getPrevCompanyAddress());
        companyHistoryEntity.setPrevCompanyDesignation(companyHistory.getPrevCompanyDesignation());
        companyHistoryEntity.setPrevCompanyName(companyHistory.getPrevCompanyName());
        companyHistoryEntity.setPrevCompanyHREmail(companyHistory.getPrevCompanyHREmail());
        companyHistoryEntity.setBackGroundVerificationStatus(BGVStatus.IN_PROGRESS);
        companyHistoryEntity.setPrevCompanyStartDate(companyHistory.getPrevCompanyStartDate());
        companyHistoryEntity.setPrevCompanyEndDate(companyHistory.getPrevCompanyEndDate());

        return companyHistoryEntity;
    }

}


