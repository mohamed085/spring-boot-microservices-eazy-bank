package com.eazybank.accountsservice.service.impl;

import com.eazybank.accountsservice.config.Constants;
import com.eazybank.accountsservice.domain.Account;
import com.eazybank.accountsservice.domain.Customer;
import com.eazybank.accountsservice.exception.BusinessException;
import com.eazybank.accountsservice.repository.AccountRepository;
import com.eazybank.accountsservice.repository.CustomerRepository;
import com.eazybank.accountsservice.service.AccountService;
import com.eazybank.accountsservice.service.mapper.CustomerMapper;
import com.eazybank.accountsservice.service.dto.AccountDto;
import com.eazybank.accountsservice.service.dto.CustomerDto;
import com.eazybank.accountsservice.service.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void create(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        customerRepository.findByMobileNumber(customerDto.getMobileNumber()).ifPresent(exist -> {
            throw BusinessException.builder()
                    .message("Customer already registered with given mobileNumber " + customerDto.getMobileNumber())
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();

        });
        Customer savedCustomer = customerRepository.save(customer);
        accountRepository.save(createNewAccount(savedCustomer));
    }

    private Account createNewAccount(Customer customer) {
        Account newAccount = new Account();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(Constants.SAVINGS);
        newAccount.setBranchAddress(Constants.ADDRESS);
        return newAccount;
    }



    @Override
    public CustomerDto getByMobileNumber(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> BusinessException.buildNotFound("Customer", "mobileNumber", mobileNumber));
        Account accounts = accountRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(() -> BusinessException.buildNotFound("Account", "customerId", customer.getCustomerId().toString()));
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccount(AccountMapper.mapToAccountsDto(accounts, new AccountDto()));
        return customerDto;
    }

    @Override
    public boolean update(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountDto accountsDto = customerDto.getAccount();
        if(accountsDto !=null ){
            Account accounts = accountRepository.findById(accountsDto.getAccountNumber())
                    .orElseThrow(() -> BusinessException.buildNotFound("Account", "AccountNumber", accountsDto.getAccountNumber().toString()));
            AccountMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> BusinessException.buildNotFound("Customer", "CustomerId", customerId.toString()));
            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
    }

    @Override
    public boolean delete(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> BusinessException.buildNotFound("Customer", "mobileNumber", mobileNumber));
        accountRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }
}
