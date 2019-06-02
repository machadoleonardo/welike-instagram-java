package br.com.welike.instagram.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ScrapingRequest {

    private List<String> seguindo;
    private List<String> seguidores;

    @NotNull(message = "Campo maxFollowers é obrigatório")
    private Integer minFollowers;

}
