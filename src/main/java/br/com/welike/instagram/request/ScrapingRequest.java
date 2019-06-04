package br.com.welike.instagram.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class ScrapingRequest {

    @Size(max=3)
    private List<String> seguindo;

    @Size(max=3)
    private List<String> seguidores;

    @NotNull(message = "Campo minFollowers é obrigatório")
    private Integer minFollowers;

}
