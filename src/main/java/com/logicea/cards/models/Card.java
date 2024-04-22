package com.logicea.cards.models;

import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)

    private Integer userId;
    private String name;
    private String description;
    private String color;
    private String status;
    private Timestamp creationDate;
}