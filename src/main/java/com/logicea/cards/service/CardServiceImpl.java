package com.logicea.cards.service;

import com.logicea.cards.models.Card;
import com.logicea.cards.repos.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public Page<Card> findUserCardsByFiltersAndSort(Integer userId, String name, String color, String status,
                                                    Timestamp startDate, Timestamp endDate, String sortBy, Pageable page) {
        return cardRepository.findUserCardsByFiltersAndSort(userId, name, color, status, startDate, endDate, sortBy, page);
    }

    @Override
    public Card findById(Integer id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public Card findByIdAndUserId(Integer id, Integer user) {
        return cardRepository.findCardByIdAndUserId(id, user);
    }

    @Override
    public Card save(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public void delete(Card card) {
        cardRepository.delete(card);
    }

    @Override
    public List<Card> findAllCards() {
        return cardRepository.findAll();
    }
}
