package com.sts.history.employee.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sts.fullprofile.employee.model.BGVStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompanyHistoryEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int historyId;

    @Column
    @NotNull
    private String prevCompanyName;

    @Column
    @NotNull
    private String prevCompanyAddress;

    @Column
    @NotNull
    private Date prevCompanyStartDate;

    @Column
    @NotNull
    private Date prevCompanyEndDate;

    @Column
    @NotNull
    private String prevCompanyHREmail;

    @Column
    @NotNull
    private String prevCompanyDesignation;

    @Column
    @NotNull
    @Enumerated(value = EnumType.STRING)
    private BGVStatus backGroundVerificationStatus;

    @ManyToOne
    @JoinColumn(name = "empIdFK")
    @JsonBackReference
    private EmployeeFullProfileEntity employee;
}
