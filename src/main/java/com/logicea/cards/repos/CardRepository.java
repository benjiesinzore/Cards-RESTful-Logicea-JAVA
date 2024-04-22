package com.logicea.cards.repos;

import com.logicea.cards.models.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;

public interface CardRepository extends JpaRepository<Card, Integer> {
    Card findCardByIdAndUserId(Integer id, Integer user);

    @Query("SELECT c FROM Card c WHERE c.userId = :userId AND " +
            "(c.creationDate >= :startDate) AND " +
            "(c.creationDate <= :endDate) AND ( " +
            "(c.name = :name) OR " +
            "(c.color = :color) OR " +
            "(c.status = :status)) " +
            "ORDER BY " +
            "CASE WHEN :sortBy = 'name' THEN c.name END ASC, " +
            "CASE WHEN :sortBy = 'color' THEN c.color END ASC, " +
            "CASE WHEN :sortBy = 'status' THEN c.status END ASC, " +
            "CASE WHEN :sortBy = 'creationDate' THEN c.creationDate END ASC")
    Page<Card> findUserCardsByFiltersAndSort(@Param("userId") Integer userId, @Param("name") String name,
                                             @Param("color") String color, @Param("status") String status,
                                             @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate,
                                             @Param("sortBy") String sortBy, Pageable pageable);

}
