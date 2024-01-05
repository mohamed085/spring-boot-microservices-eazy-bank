package com.eazybank.cardsservice.service.impl;

import com.eazybank.cardsservice.config.Constants;
import com.eazybank.cardsservice.domain.Card;
import com.eazybank.cardsservice.exception.BusinessException;
import com.eazybank.cardsservice.repository.CardRepository;
import com.eazybank.cardsservice.service.CardService;
import com.eazybank.cardsservice.service.dto.CardDto;
import com.eazybank.cardsservice.service.mapper.CardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    @Override
    public void create(String mobileNumber) {
        cardRepository.findByMobileNumber(mobileNumber).ifPresent(card -> {
            throw BusinessException
                    .builder()
                    .message("Card already registered with given mobileNumber")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        });
        cardRepository.save(createNewCard(mobileNumber));
    }

    private Card createNewCard(String mobileNumber) {
        Card newCard = new Card();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNumber(Long.toString(randomCardNumber));
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(Constants.CREDIT_CARD);
        newCard.setTotalLimit(Constants.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(Constants.NEW_CARD_LIMIT);
        return newCard;
    }


    @Override
    public CardDto getByMobileNumber(String mobileNumber) {
        Card card = cardRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> BusinessException.buildNotFound("Card", "mobileNumber", mobileNumber));
        return CardMapper.mapToCardDto(card, new CardDto());
    }

    @Override
    public boolean update(CardDto cardDto) {
        Card card = cardRepository.findByCardNumber(cardDto.getCardNumber())
                .orElseThrow(() -> BusinessException.buildNotFound("Card", "CardNumber", cardDto.getCardNumber()));
        CardMapper.mapToCard(cardDto, card);
        cardRepository.save(card);
        return  true;
    }

    @Override
    public boolean deleteByMobileNumber(String mobileNumber) {
        Card card = cardRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> BusinessException.buildNotFound("Card", "mobileNumber", mobileNumber));
        cardRepository.deleteById(card.getCardId());
        return true;
    }

}
