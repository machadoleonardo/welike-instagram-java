package br.com.welike.instagram.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfluencerDTO {

    private Long id;
    private String userName;
    private String profile_picture;
    private String full_name;
    private String bio;
    private Boolean isPrivate;
    private Integer follows;
    private Integer followed_by;

}
