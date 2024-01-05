package com.eazybank.loansservice.service;


import com.eazybank.loansservice.service.dto.LoanDto;

public interface LoansService {

    void create(String mobileNumber);

    LoanDto getByMobileNumber(String mobileNumber);

    boolean update(LoanDto loanDto);

    boolean delete(String mobileNumber);

}