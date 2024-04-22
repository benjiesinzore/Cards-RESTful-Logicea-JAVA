package com.logicea.cards.controller;

import com.logicea.cards.dto.CardDTO;
import com.logicea.cards.models.Card;
import com.logicea.cards.service.AuthenticationService;
import com.logicea.cards.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/cards")
public class MemberCardsController {

    private final CardService cardService;
    private final AuthenticationService authenticationService;

    @Autowired
    public MemberCardsController(CardService cardService,
                                 AuthenticationService authenticationService) {
        this.cardService = cardService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public ResponseEntity<Page<Card>> getCards(@RequestParam String name,
                                               @RequestParam String color,
                                               @RequestParam String status,
                                               @RequestParam String startDate,
                                               @RequestParam String endDate,
                                               @RequestParam String sortBy,
                                               @RequestParam(value = "page") Optional<Integer> page) {
        int evalPage = page.orElse(0) < 1 ? 0 : page.get() - 1;

        Page<Card> cards = cardService.findUserCardsByFiltersAndSort(
                authenticationService.getLoggedInUser().getId(),
                name, color, status, convertToTimeStamp(startDate), convertToTimeStamp(endDate)
                , sortBy, PageRequest.of(evalPage, 2));
        return ResponseEntity.ok(cards);
    }

    private Timestamp convertToTimeStamp(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(date);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            return timestamp;
        } catch (Exception e) {
            Date newDate = new Date();
            Timestamp timestamp = new java.sql.Timestamp(newDate.getTime());
            return timestamp;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable Integer id) {
        Card card = cardService.findByIdAndUserId(id, authenticationService.getLoggedInUser().getId());
        if (card != null) {
            return ResponseEntity.ok(card);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Card> createCard(@RequestBody CardDTO cReq) {
        Card card = new Card();
        card.setUserId(authenticationService.getLoggedInUser().getId());
        card.setName(cReq.getName());
        card.setColor(cReq.getColor());
        card.setDescription(cReq.getDescription());
        card.setStatus("TO_DO");
        card.setCreationDate(Timestamp.from(Instant.now()));
        Card createdCard = cardService.save(card);
        return ResponseEntity.ok(createdCard);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCard(@PathVariable Integer id,
                                        @Valid @RequestBody CardDTO updatedCard,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }

        Card existingCard = cardService.findByIdAndUserId(id, authenticationService.getLoggedInUser().getId());
        if (existingCard != null) {
            existingCard.setName(updatedCard.getName());
            existingCard.setDescription(updatedCard.getDescription() == null ? existingCard.getDescription() : updatedCard.getDescription());
            existingCard.setColor(updatedCard.getColor() == null ? existingCard.getColor() : updatedCard.getColor());
            existingCard.setStatus(updatedCard.getStatus() == null || updatedCard.getStatus().isEmpty() ? existingCard.getStatus() : updatedCard.getStatus());
            Card savedCard = cardService.save(existingCard);
            return ResponseEntity.ok(savedCard);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCard(@PathVariable Integer id) {
        Card card = cardService.findById(id);
        if (card != null && card.getUserId().equals(authenticationService.getLoggedInUser().getId())) {
            cardService.delete(card);
            return ResponseEntity.ok("Card deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
