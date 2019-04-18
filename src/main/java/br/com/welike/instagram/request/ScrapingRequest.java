package br.com.welike.instagram.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ScrapingRequest {

    @NotNull(message = "Campo username é obrigatório")
    private List<String> userName;

    @NotNull(message = "Campo maxFollowers é obrigatório")
    private Integer maxFollowers;

}
