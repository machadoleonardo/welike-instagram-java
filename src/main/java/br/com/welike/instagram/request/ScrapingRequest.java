package br.com.welike.instagram.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ScrapingRequest {

    @NotNull(message = "Campo username é obrigatório")
    private String userName;

}
