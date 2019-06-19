package br.com.welike.instagram.response;

import br.com.welike.instagram.model.Influencer;
import br.com.welike.instagram.model.Reference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {

    private String status;
    private Integer totalInfluencers;
    private List<Reference> references;
    private String erro;

}
