package com.eazybank.cardsservice.resource;

import com.eazybank.cardsservice.config.Constants;
import com.eazybank.cardsservice.service.CardService;
import com.eazybank.cardsservice.service.dto.CardDto;
import com.eazybank.cardsservice.model.ErrorResponse;
import com.eazybank.cardsservice.model.Response;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/cards", produces = {MediaType.APPLICATION_JSON_VALUE})
@Tag(name = "CRUD REST APIs for Card in EazyBank", description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH AND DELETE card details")
public class CardResource {

    private final CardService cardService;

    @Operation(summary = "Create Card REST API", description = "REST API to create new Card inside EazyBank")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping("/create")
    public ResponseEntity<Response> createCard(@Valid @RequestParam
                                                      @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                      String mobileNumber) {
        cardService.create(mobileNumber);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new Response(Constants.STATUS_201, Constants.MESSAGE_201));
    }

    @Operation(summary = "Fetch Card Details REST API", description = "REST API to fetch card details based on a mobile number")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("")
    public ResponseEntity<CardDto> getByMobileNumber(@RequestParam @Pattern(regexp="(^$|[0-9]{11})",message = "Mobile number must be 10 digits") String mobileNumber) {
        CardDto cardDto = cardService.getByMobileNumber(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(cardDto);
    }

    @Operation(summary = "Update Card Details REST API", description = "REST API to update card details based on a card number")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "417", description = "Expectation Failed"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PutMapping("")
    public ResponseEntity<Response> updateCardDetails(@Valid @RequestBody CardDto cardDto) {
        boolean isUpdated = cardService.update(cardDto);
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

    @Operation(summary = "Delete Card Details REST API", description = "REST API to delete Card details based on a mobile number")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "417", description = "Expectation Failed"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @DeleteMapping("")
    public ResponseEntity<Response> deleteCardDetails(@RequestParam @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") String mobileNumber) {
        boolean isDeleted = cardService.deleteByMobileNumber(mobileNumber);
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