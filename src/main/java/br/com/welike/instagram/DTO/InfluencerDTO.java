package br.com.welike.instagram.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfluencerDTO {

    private String userName;
    private Long id;
    private String name;
    private String picture;
    private Long followers;
    private Long following;

}
