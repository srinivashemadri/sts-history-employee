package com.sts.history.employee.model;

import com.sts.fullprofile.employee.model.BGVStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BGVModel {
    private int historyId;
    private BGVStatus bgvStatus;
}
