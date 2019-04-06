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
    private String website;
    private Boolean is_business;
    private Integer media;
    private Integer follows;
    private Integer followed_by;

}
