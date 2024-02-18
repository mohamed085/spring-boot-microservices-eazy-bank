package com.eazybank.accountsservice.resource;


import com.eazybank.accountsservice.config.Constants;
import com.eazybank.accountsservice.model.ErrorResponse;
import com.eazybank.accountsservice.model.Response;
import com.eazybank.accountsservice.service.AccountService;
import com.eazybank.accountsservice.service.dto.CustomerDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/accounts")
@Tag(name = "CRUD REST APIs for Accounts in EazyBank", description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH AND DELETE account details")
public class AccountResource {

    private final AccountService accountService;

    @Operation(summary = "Create Account REST API", description = "REST API to create new Customer &  Account inside EazyBank")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping("")
    public ResponseEntity<Response> create(@Valid @RequestBody CustomerDto customerDto) {
        accountService.create(customerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new Response(Constants.STATUS_201, Constants.MESSAGE_201));
    }

    @Operation(summary = "Fetch Account Details REST API", description = "REST API to fetch Customer &  Account details based on a mobile number")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("")
    public ResponseEntity<CustomerDto> findByMobileNumber(@RequestParam @Pattern(regexp="(^$|[0-9]{11})",message = "Mobile number must be 11 digits") String mobileNumber) {
        CustomerDto customerDto = accountService.getByMobileNumber(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }

    @Operation(summary = "Update Account Details REST API", description = "REST API to update Customer &  Account details based on a account number")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "417", description = "Expectation Failed"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PutMapping("")
    public ResponseEntity<Response> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto) {
        boolean isUpdated = accountService.update(customerDto);
        if (isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new Response(Constants.STATUS_200, Constants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new Response(Constants.STATUS_417, Constants.MESSAGE_417_UPDATE));
        }
    }


    @Operation(summary = "Delete Account & Customer Details REST API", description = "REST API to delete Customer &  Account details based on a mobile number")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "417", description = "Expectation Failed"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @DeleteMapping("")
    public ResponseEntity<Response> deleteAccountDetails(@RequestParam @Pattern(regexp="(^$|[0-9]{11})",message = "Mobile number must be 11 digits")
                                                            String mobileNumber) {
        boolean isDeleted = accountService.delete(mobileNumber);
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
