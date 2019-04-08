package br.com.welike.instagram.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Influencer implements Serializable {

    @Id
    @NotNull
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String userName;

    @Column
    private String profilePicture;

    @Column
    private String fullName;

    @Column
    private String bio;

    @Column
    private String website;

    @Column
    private Boolean isPrivate;

    @Column
    private Integer media;

    @Column
    private Integer follows;

    @Column
    private Integer followedBy;

}
