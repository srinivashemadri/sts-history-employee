package com.sts.history.employee.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBGVStatusResponse {
    private boolean hasError;
    private String errorMessage;
    private List<BGVModel> bgvModelList;
}
