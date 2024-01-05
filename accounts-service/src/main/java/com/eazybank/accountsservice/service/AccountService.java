package com.eazybank.accountsservice.service;

import com.eazybank.accountsservice.service.dto.CustomerDto;

public interface AccountService {

    void create(CustomerDto customerDto);

    CustomerDto getByMobileNumber(String mobileNumber);

    boolean update(CustomerDto customerDto);

    boolean delete(String mobileNumber);


}
