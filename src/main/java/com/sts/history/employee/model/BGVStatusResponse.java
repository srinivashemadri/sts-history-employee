package com.sts.history.employee.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BGVStatusResponse {
    private boolean responseHasError;
    private String errorMessage;
    private List<BGVModel> bgvModelList;
}
