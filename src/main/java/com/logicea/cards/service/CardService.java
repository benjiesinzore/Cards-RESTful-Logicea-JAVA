package com.logicea.cards.service;

import com.logicea.cards.models.Card;
import com.logicea.cards.models.UserLogin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface CardService {
    // Members
    List<Card> findByUser(UserLogin userLogin);

    Page<Card> findUserCardsByFiltersAndSort(Integer userId, String name, String color, String status, Timestamp startDate, Timestamp endDate, String sortBy, Pageable page);

    Card findById(Integer id);
    Card findByIdAndUserId(Integer id, Integer user);
    Card save(Card card);
    void delete(Card card);

    // Admin
    List<Card> findAllCards();
}
