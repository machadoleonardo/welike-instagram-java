package br.com.welike.instagram.converter;

import br.com.welike.instagram.Converter;
import br.com.welike.instagram.model.Influencer;
import br.com.welike.instagram.model.Transaction;
import br.com.welike.instagram.response.TransactionResponse;
import me.postaddict.instagram.scraper.model.Account;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class TransactionResponseConverter implements Converter<Transaction, TransactionResponse> {

    @Override
    public TransactionResponse encode(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();

        response.setInfluencers(new ArrayList<>(transaction.getInfluencers()));
        response.setStatus(transaction.getStatus());
        response.setTotalInfluencers(transaction.getInfluencers().size());

        return response;
    }

    @Override
    public Transaction decode(TransactionResponse response) {
        return null;
    }
}
