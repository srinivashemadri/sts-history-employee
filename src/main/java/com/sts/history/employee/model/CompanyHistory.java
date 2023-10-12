package com.sts.history.employee.model;

import com.sts.fullprofile.employee.model.BGVStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompanyHistory {
    @NotNull
    private String prevCompanyName;

    @NotNull
    private String prevCompanyAddress;

    @NotNull
    private Date prevCompanyStartDate;

    @NotNull
    private Date prevCompanyEndDate;

    @NotNull
    private String prevCompanyHREmail;

    @NotNull
    private String prevCompanyDesignation;

    private BGVStatus backGroundVerificationStatus;
    private int historyId;
}
