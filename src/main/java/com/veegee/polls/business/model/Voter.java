package com.veegee.polls.business.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Voter {

    @Id
    private String id;
    private String cpf;
    private boolean vote;

}
