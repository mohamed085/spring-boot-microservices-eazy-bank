package com.eazybank.cardsservice.service.mapper;

import com.eazybank.cardsservice.domain.Card;
import com.eazybank.cardsservice.service.dto.CardDto;

public class CardMapper {

    public static CardDto mapToCardDto(Card cards, CardDto cardDto) {
        cardDto.setCardNumber(cards.getCardNumber());
        cardDto.setCardType(cards.getCardType());
        cardDto.setMobileNumber(cards.getMobileNumber());
        cardDto.setTotalLimit(cards.getTotalLimit());
        cardDto.setAvailableAmount(cards.getAvailableAmount());
        cardDto.setAmountUsed(cards.getAmountUsed());
        return cardDto;
    }

    public static Card mapToCard(CardDto cardDto, Card cards) {
        cards.setCardNumber(cardDto.getCardNumber());
        cards.setCardType(cardDto.getCardType());
        cards.setMobileNumber(cardDto.getMobileNumber());
        cards.setTotalLimit(cardDto.getTotalLimit());
        cards.setAvailableAmount(cardDto.getAvailableAmount());
        cards.setAmountUsed(cardDto.getAmountUsed());
        return cards;
    }

}