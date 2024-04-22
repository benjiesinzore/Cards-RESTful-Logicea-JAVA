package com.logicea.cards.controller;

import com.logicea.cards.models.Card;
import com.logicea.cards.models.Role;
import com.logicea.cards.service.AuthenticationService;
import com.logicea.cards.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/cards")
public class AdminCardsController {

    private final CardService cardService;
    private final AuthenticationService authenticationService;

    @Autowired
    public AdminCardsController(CardService cardService,
                                AuthenticationService authenticationService) {
        this.cardService = cardService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public ResponseEntity<List<Card>> getCards() {
        if (authenticationService.getLoggedInUser().getRole().equals(Role.ADMIN)) {
            List<Card> cards = cardService.findAllCards();
            return ResponseEntity.ok(cards);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
