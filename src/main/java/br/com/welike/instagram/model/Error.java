package br.com.welike.instagram.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Error {

    @Id
    @NotNull
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "LONGTEXT")
    private String message;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;
}
