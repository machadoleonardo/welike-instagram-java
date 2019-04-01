package br.com.welike.instagram.response;

import br.com.welike.instagram.DTO.InfluencerDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusResponse {

    private String status;
    private InfluencerDTO influencerDTO;

}
