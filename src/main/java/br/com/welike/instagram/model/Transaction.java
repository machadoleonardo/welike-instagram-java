package br.com.welike.instagram.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @NotNull
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonInclude
    @JsonIgnore
    private Long id;

    @NotNull
    @Column
    @JsonIgnore
    private String transactionId;

    @NotNull
    @Column
    private String status;

    @OneToMany(mappedBy="transaction")
    private Set<Error> errors;

    @JsonInclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "transaction_reference",
            joinColumns = @JoinColumn(
                    name = "transaction_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "reference_id", referencedColumnName = "id"))
    private Set<Reference> references;

}
