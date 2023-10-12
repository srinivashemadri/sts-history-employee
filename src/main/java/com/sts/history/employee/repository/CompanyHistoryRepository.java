package com.sts.history.employee.repository;

import com.sts.fullprofile.employee.model.BGVStatus;
import com.sts.history.employee.entity.CompanyHistoryEntity;
import jakarta.transaction.Transactional;
import jakarta.websocket.server.PathParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyHistoryRepository extends JpaRepository<CompanyHistoryEntity, Integer> {

    @Query(value = "SELECT * FROM company_history_entity c WHERE emp_idfk = :empId", nativeQuery = true)
    List<CompanyHistoryEntity> getAllHistory(@PathParam("empId") String empId);

    @Query(value = "SELECT DISTINCT emp_idfk FROM company_history_entity WHERE back_ground_verification_status = 'FAILED' ", nativeQuery = true)
    List<String> getAllEmpIds_BGV_failed();

    @Modifying
    @Transactional
    @Query("UPDATE CompanyHistoryEntity che SET che.backGroundVerificationStatus = :bgvStatus WHERE che.historyId = :historyId")
    int updateBgvStatusByHistoryId(int historyId, BGVStatus bgvStatus);

    @Query("SELECT CASE WHEN COUNT(che) > 0 THEN false ELSE true END FROM CompanyHistoryEntity che WHERE che.employee.empId = :empId AND che.backGroundVerificationStatus != :bgvStatus")
    boolean isCurrEmp(String empId, BGVStatus bgvStatus);

}
