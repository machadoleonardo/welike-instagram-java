package br.com.welike.instagram.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionEnum {

    SUCESSO("Sucesso"),
    ERRO("Erro"),
    EM_ANDAMENTO("Em andamento");

    private String description;

}