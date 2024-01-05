package com.eazybank.loansservice.service.impl;

import com.eazybank.loansservice.config.Constants;
import com.eazybank.loansservice.domain.Loan;
import com.eazybank.loansservice.exception.BusinessException;
import com.eazybank.loansservice.service.dto.LoanDto;
import com.eazybank.loansservice.service.mapper.LoanMapper;
import com.eazybank.loansservice.repository.LoanRepository;
import com.eazybank.loansservice.service.LoansService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class LoansServiceImpl implements LoansService {

    private final LoanRepository loanRepository;

    @Override
    public void create(String mobileNumber) {
        loanRepository.findByMobileNumber(mobileNumber).ifPresent(loan -> {
            throw BusinessException
                    .builder()
                    .message("Loan already registered with given mobileNumber " + mobileNumber)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();

        });
        loanRepository.save(createNewLoan(mobileNumber));
    }

    private Loan createNewLoan(String mobileNumber) {
        Loan newLoan = new Loan();
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        newLoan.setLoanNumber(Long.toString(randomLoanNumber));
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(Constants.HOME_LOAN);
        newLoan.setTotalLoan(Constants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(Constants.NEW_LOAN_LIMIT);
        return newLoan;
    }

    @Override
    public LoanDto getByMobileNumber(String mobileNumber) {
        Loan loan = loanRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> BusinessException.buildNotFound("Loan", "mobileNumber", mobileNumber));
        return LoanMapper.mapToLoansDto(loan, new LoanDto());
    }

    @Override
    public boolean update(LoanDto loanDto) {
        Loan loan = loanRepository.findByLoanNumber(loanDto.getLoanNumber())
                .orElseThrow(() -> BusinessException.buildNotFound("Loan", "LoanNumber", loanDto.getLoanNumber()));
        LoanMapper.mapToLoans(loanDto, loan);
        loanRepository.save(loan);
        return true;
    }

    @Override
    public boolean delete(String mobileNumber) {
        Loan loan = loanRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> BusinessException.buildNotFound("Loan", "mobileNumber", mobileNumber));
        loanRepository.deleteById(loan.getLoanId());
        return true;
    }
}