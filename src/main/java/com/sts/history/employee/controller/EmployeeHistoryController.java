package com.sts.history.employee.controller;

import com.sts.history.employee.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
public interface EmployeeHistoryController {


    @Operation(summary = "Register company History of an employee", description = "This API Registers company History of an employee based on the employee ID", tags = {"companyHistoryOfEmployee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterCompanyHistoryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Path not found"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "405", description = "Validation exception"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @RequestMapping(value = "/{empId}", produces = "application/json", method = RequestMethod.POST)
    ResponseEntity<RegisterCompanyHistoryResponse> registerCompanyHistory(@RequestBody List<CompanyHistory> companyHistoryList, @PathVariable(value = "empId") String empId);

    @Operation(summary = "Update BGV Status of an employee", description = "This API will update BGV Status of an employee", tags = {"companyHistoryOfEmployee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateBGVStatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "Path not found"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "405", description = "Validation exception"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @RequestMapping(value = "/bgvStatus", produces = "application/json", method = RequestMethod.PUT)
    ResponseEntity<UpdateBGVStatusResponse> updateBGVStatus(@RequestBody List<BGVModel> bgvModels);

    @Operation(summary = "Get all previous company details of an employee", description = "This API will list out all the companies that he/her had worked in based on the employee Id", tags ={"companyHistoryOfEmployee"} )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetCompanyHistoryOfEmployeeResponse.class))),
            @ApiResponse(responseCode = "404", description = "Path not found"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "405", description = "Validation exception"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @RequestMapping(value = "/{empId}", produces = "application/json", method = RequestMethod.GET)
    ResponseEntity<GetCompanyHistoryOfEmployeeResponse> getAllPreviousCompanyDetails(@PathVariable String empId);

    @Operation(summary = "Get all employee ID's whose BGV was failed", description = "This API will list out all the employee ID's who's back ground verification check got failed", tags = {"companyHistoryOfEmployee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetAllBGVFailedEmpIDResponse.class))),
            @ApiResponse(responseCode = "404", description = "Path not found"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "405", description = "Validation exception"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @RequestMapping(value = "/bgvFailed", produces = "application/json", method = RequestMethod.GET)
    ResponseEntity<GetAllBGVFailedEmpIDResponse> getAllEmpIdsOfStatusFailed();

}