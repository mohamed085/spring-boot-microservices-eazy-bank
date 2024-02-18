package com.eazybank.loansservice.resource;

import com.eazybank.loansservice.config.Constants;
import com.eazybank.loansservice.model.ErrorResponse;
import com.eazybank.loansservice.model.Response;
import com.eazybank.loansservice.service.LoansService;
import com.eazybank.loansservice.service.dto.LoanDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "CRUD REST APIs for Loans in EazyBank", description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH AND DELETE loan details")
@RestController
@RequestMapping(path = "/api/loans", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
public class LoanResource {

    private final LoansService loansService;

    @Operation(summary = "Create Loan REST API", description = "REST API to create new loan inside EazyBank")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping("")
    public ResponseEntity<Response> createLoan(@RequestParam @Pattern(regexp="(^$|[0-9]{11})",message = "Mobile number must be 11 digits") String mobileNumber) {
        loansService.create(mobileNumber);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new Response(Constants.STATUS_201, Constants.MESSAGE_201));
    }

    @Operation(summary = "Fetch Loan Details REST API", description = "REST API to fetch loan details based on a mobile number")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("")
    public ResponseEntity<LoanDto> fetchLoanDetails(@RequestParam @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") String mobileNumber) {
        LoanDto loanDto = loansService.getByMobileNumber(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(loanDto);
    }

    @Operation(summary = "Update Loan Details REST API", description = "REST API to update loan details based on a loan number")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "417", description = "Expectation Failed"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PutMapping("")
    public ResponseEntity<Response> updateLoanDetails(@Valid @RequestBody LoanDto loanDto) {
        boolean isUpdated = loansService.update(loanDto);
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new Response(Constants.STATUS_200, Constants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new Response(Constants.STATUS_417, Constants.MESSAGE_417_UPDATE));
        }
    }

    @Operation(summary = "Delete Loan Details REST API", description = "REST API to delete Loan details based on a mobile number")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "417", description = "Expectation Failed"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @DeleteMapping("")
    public ResponseEntity<Response> deleteLoanDetails(@RequestParam @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") String mobileNumber) {
        boolean isDeleted = loansService.delete(mobileNumber);
        if(isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new Response(Constants.STATUS_200, Constants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new Response(Constants.STATUS_417, Constants.MESSAGE_417_DELETE));
        }
    }

}