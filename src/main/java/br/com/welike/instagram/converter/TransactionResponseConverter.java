package br.com.welike.instagram.converter;

import br.com.welike.instagram.Converter;
import br.com.welike.instagram.enums.TransactionEnum;
import br.com.welike.instagram.helper.Utils;
import br.com.welike.instagram.model.Error;
import br.com.welike.instagram.model.Reference;
import br.com.welike.instagram.model.Transaction;
import br.com.welike.instagram.response.TransactionResponse;
import br.com.welike.instagram.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class TransactionResponseConverter implements Converter<Transaction, TransactionResponse> {

    @Override
    public TransactionResponse encode(Transaction transaction) {
        int totalInfluencers = 0;
        TransactionResponse response = new TransactionResponse();

        response.setStatus(transaction.getStatus());

        if (transaction.getStatus().equals(TransactionEnum.ERRO.getDescription())) {
            response.setErro(getLastElement(transaction.getErrors()).getMessage());
            return response;
        }

        for (Reference reference : transaction.getReferences()) {
            totalInfluencers = reference.getInfluencers().size();
        }

        response.setReferences(new ArrayList<>(transaction.getReferences()));
        response.setTotalInfluencers(totalInfluencers);

        return response;
    }

    private Error getLastElement(Set<Error> errors) {
        return errors.stream()
                .reduce((first, second) -> second)
                .orElse(null);
    }

    @Override
    public Transaction decode(TransactionResponse response) {
        return null;
    }
}
