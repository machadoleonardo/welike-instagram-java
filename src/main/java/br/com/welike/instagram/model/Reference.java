package br.com.welike.instagram.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Reference implements Serializable {

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "reference_influencer",
            joinColumns = @JoinColumn(
                    name = "reference_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "influencer_id", referencedColumnName = "id"))
    private Set<Influencer> influencers;
}
