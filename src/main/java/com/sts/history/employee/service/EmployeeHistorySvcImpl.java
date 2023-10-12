package com.sts.history.employee.service;

import com.sts.fullprofile.employee.model.BGVStatus;
import com.sts.fullprofile.employee.model.UpdateEmployeeStatusResponse;
import com.sts.history.employee.entity.CompanyHistoryEntity;
import com.sts.history.employee.entity.EmployeeFullProfileEntity;
import com.sts.history.employee.exception.CompanyHistoryException;
import com.sts.history.employee.model.*;
import com.sts.history.employee.repository.CompanyHistoryRepository;
import com.sts.history.employee.repository.EmployeeFullProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sts.history.employee.mapper.ClassMapper.*;

@Service
public class EmployeeHistorySvcImpl implements EmployeeHistorySvc{

    @Autowired
    EmployeeFullProfileRepository employeeFullProfileRepository;

    @Autowired
    CompanyHistoryRepository companyHistoryRepository;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public ResponseEntity<RegisterCompanyHistoryResponse> registerCompanyHistory(List<CompanyHistory> companyHistoryList, String empId){

        RegisterCompanyHistoryResponse registerCompanyHistoryResponse = new RegisterCompanyHistoryResponse();

        try{
            Optional<EmployeeFullProfileEntity> employeeFullProfileEntity = employeeFullProfileRepository.findById(empId);

            if(employeeFullProfileEntity.isPresent()){
                EmployeeFullProfileEntity employeeFullProfile = employeeFullProfileEntity.get();
                List<CompanyHistoryEntity> companyHistoryEntityList = new ArrayList<>();
                for(CompanyHistory companyHistory : companyHistoryList){
                    CompanyHistoryEntity companyHistoryEntity = copyHistoryModelToEmployeeEntity(companyHistory,employeeFullProfile);
                    companyHistoryEntityList.add(companyHistoryEntity);
                }
                companyHistoryEntityList = companyHistoryRepository.saveAll(companyHistoryEntityList);
                companyHistoryList.clear();
                for(CompanyHistoryEntity companyHistoryEntity: companyHistoryEntityList){
                    companyHistoryList.add(mapCompanyHistoryEntityToModel(companyHistoryEntity));
                }


                BGVStatusResponse bgvModelListResponseHolder = doBackGroundVerification(companyHistoryList,empId);

                if(bgvModelListResponseHolder.isResponseHasError()){
                    throw new CompanyHistoryException(bgvModelListResponseHolder.getErrorMessage());
                }

                UpdateEmployeeStatusResponse updateEmployeeStatusResponse = updateEmployeeStatusBasedOnBGV(empId);

                if(updateEmployeeStatusResponse.isHasError()){
                    throw new CompanyHistoryException(updateEmployeeStatusResponse.getErrorMessage());
                }

                registerCompanyHistoryResponse.setCompanyHistoryList(companyHistoryList);
                registerCompanyHistoryResponse.setHasError(false);
                registerCompanyHistoryResponse.setErrorMessage(null);
                return new ResponseEntity<>(registerCompanyHistoryResponse, HttpStatus.OK);
            }
            else{
                throw new CompanyHistoryException("Corresponding empId: "+ empId+" has not been found");
            }
        }
        catch (Exception e){
            registerCompanyHistoryResponse.setCompanyHistoryList(new ArrayList<>());
            registerCompanyHistoryResponse.setHasError(true);
            registerCompanyHistoryResponse.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(registerCompanyHistoryResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private BGVStatusResponse doBackGroundVerification(List<CompanyHistory> companyHistoryList, String empId) throws CompanyHistoryException {

        String url = "http://localhost:8082/bgv/employee";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> requestEntity = new HttpEntity<>(companyHistoryList, headers);

        ResponseEntity<BGVStatusResponse> bgvModelListResponseEntity = restTemplate.exchange(url,
                HttpMethod.PUT,requestEntity, BGVStatusResponse.class);

        if(bgvModelListResponseEntity.getBody() == null || bgvModelListResponseEntity.getStatusCode() != HttpStatus.OK){
            throw new CompanyHistoryException("Error occurred in application bgv-employee");
        }

        return bgvModelListResponseEntity.getBody();
    }

    private UpdateEmployeeStatusResponse updateEmployeeStatusBasedOnBGV(String empId) throws CompanyHistoryException {
        boolean resp = companyHistoryRepository.isCurrEmp(empId, BGVStatus.COMPLETED);
        String url = "http://localhost:8081/full-profile/employee/status/"+empId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<UpdateEmployeeStatusResponse> updateEmployeeStatusResponseResponseEntity = null;

        HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);
        if(resp){
            url = url +"/true";
            updateEmployeeStatusResponseResponseEntity = restTemplate.exchange(url, HttpMethod.PUT,requestEntity, UpdateEmployeeStatusResponse.class);
        }
        else{
            url = url + "/false";
            updateEmployeeStatusResponseResponseEntity = restTemplate.exchange(url, HttpMethod.PUT,requestEntity, UpdateEmployeeStatusResponse.class);
        }
        if(updateEmployeeStatusResponseResponseEntity.getStatusCode() != HttpStatus.OK || updateEmployeeStatusResponseResponseEntity.getBody() == null){
            throw new CompanyHistoryException("Error occurred in connecting employee-full-profile micro service");
        }
        return updateEmployeeStatusResponseResponseEntity.getBody();
    }

    @Override
    public ResponseEntity<UpdateBGVStatusResponse> updateBGVStatus(List<BGVModel> bgvModels) {
        List<BGVModel> bgvModelList = new ArrayList<>();
        UpdateBGVStatusResponse updateBGVStatusResponse = new UpdateBGVStatusResponse();
        try{
            for(BGVModel bgvModel: bgvModels){
                int isUpdateSuccess = companyHistoryRepository.updateBgvStatusByHistoryId(bgvModel.getHistoryId(),bgvModel.getBgvStatus());
                if(isUpdateSuccess > 0){
                    bgvModelList.add(bgvModel);
                }
                else{
                    bgvModel.setBgvStatus(BGVStatus.IN_PROGRESS);
                    bgvModelList.add(bgvModel);
                }
            }
            updateBGVStatusResponse.setErrorMessage(null);
            updateBGVStatusResponse.setBgvModelList(bgvModelList);
            updateBGVStatusResponse.setHasError(false);
            return new ResponseEntity<>(updateBGVStatusResponse,HttpStatus.OK);
        }
        catch (Exception e){
            updateBGVStatusResponse.setErrorMessage(e.getMessage());
            updateBGVStatusResponse.setBgvModelList(bgvModelList);
            updateBGVStatusResponse.setHasError(true);
            return new ResponseEntity<>(updateBGVStatusResponse,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<GetCompanyHistoryOfEmployeeResponse> getAllPreviousCompanyDetails(String empId) {
        GetCompanyHistoryOfEmployeeResponse companyHistoryOfEmployeeResponse = new GetCompanyHistoryOfEmployeeResponse();
        List<CompanyHistory> companiesList = new ArrayList<>();
        try{
            List<CompanyHistoryEntity> companyHistoryEntities = companyHistoryRepository.getAllHistory(empId);


            for(CompanyHistoryEntity companyHistoryEntity:companyHistoryEntities){
                companiesList.add(mapCompanyHistoryEntityToModel(companyHistoryEntity));
            }

            companyHistoryOfEmployeeResponse.setCompanyHistoryList(companiesList);
            companyHistoryOfEmployeeResponse.setHasError(false);
            companyHistoryOfEmployeeResponse.setErrorMessage(null);
            return new ResponseEntity<>(companyHistoryOfEmployeeResponse, HttpStatus.OK);
        }
        catch (Exception e){
            companyHistoryOfEmployeeResponse.setCompanyHistoryList(companiesList);
            companyHistoryOfEmployeeResponse.setHasError(true);
            companyHistoryOfEmployeeResponse.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(companyHistoryOfEmployeeResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<GetAllBGVFailedEmpIDResponse> getAllEmpIdsOfStatusFailed() {
        GetAllBGVFailedEmpIDResponse getAllBGVFailedEmpIDResponse = new GetAllBGVFailedEmpIDResponse();
        try{
            List<String> empIds_bgv_failed = companyHistoryRepository.getAllEmpIds_BGV_failed();
            getAllBGVFailedEmpIDResponse.setEmployeeIdList(empIds_bgv_failed);
            getAllBGVFailedEmpIDResponse.setHasError(false);
            getAllBGVFailedEmpIDResponse.setErrorMessage(null);
            return new ResponseEntity<>(getAllBGVFailedEmpIDResponse, HttpStatus.OK);

        }
        catch (Exception e){
            getAllBGVFailedEmpIDResponse.setEmployeeIdList(new ArrayList<>());
            getAllBGVFailedEmpIDResponse.setHasError(true);
            getAllBGVFailedEmpIDResponse.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(getAllBGVFailedEmpIDResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
