package br.com.welike.instagram.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Long id;

    @Column
    private Long instagramId;

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

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Influencer)) {
            return false;
        }

        Influencer influencer = (Influencer) o;
        return this.userName.equals(influencer.getUserName());
    }

}
