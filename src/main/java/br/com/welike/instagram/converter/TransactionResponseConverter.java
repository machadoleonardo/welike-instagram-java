package br.com.welike.instagram.converter;

import br.com.welike.instagram.Converter;
import br.com.welike.instagram.enums.TransactionEnum;
import br.com.welike.instagram.model.Transaction;
import br.com.welike.instagram.response.TransactionResponse;
import br.com.welike.instagram.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class TransactionResponseConverter implements Converter<Transaction, TransactionResponse> {

    private final TransactionService transactionService;

    @Autowired
    public TransactionResponseConverter(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public TransactionResponse encode(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();

        response.setStatus(transaction.getStatus());

        if (transaction.getStatus().equals(TransactionEnum.ERRO.getDescription())) {
            response.setErro(transaction.getError().getMessage());
            return response;
        }

        response.setInfluencers(new ArrayList<>(transaction.getInfluencers()));
        response.setTotalInfluencers(transaction.getInfluencers().size());

        return response;
    }

    @Override
    public Transaction decode(TransactionResponse response) {
        return null;
    }
}
