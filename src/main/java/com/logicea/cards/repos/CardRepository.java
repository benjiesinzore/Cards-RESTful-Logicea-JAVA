package com.logicea.cards.repos;

import com.logicea.cards.models.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface CardRepository extends JpaRepository<Card, Integer> {
    List<Card> findCardsByUserId (Integer user);
    Card findCardByIdAndUserId(Integer id, Integer user);

    @Query("SELECT c FROM Card c WHERE c.userId = :userId AND " +
            "(:name IS NULL OR c.name LIKE %:name%) OR " +
            "(:color IS NULL OR c.color = :color) OR " +
            "(:status IS NULL OR c.status = :status) OR " +
            "(:startDate IS NULL OR c.creationDate >= :startDate) OR " +
            "(:endDate IS NULL OR c.creationDate <= :endDate) " +
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
