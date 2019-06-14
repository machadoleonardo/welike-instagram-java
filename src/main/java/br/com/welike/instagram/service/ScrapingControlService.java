package br.com.welike.instagram.service;

import br.com.welike.instagram.enums.TransactionEnum;
import br.com.welike.instagram.model.Reference;
import br.com.welike.instagram.model.StatusControl;
import br.com.welike.instagram.model.Transaction;
import br.com.welike.instagram.request.ScrapingRequest;
import br.com.welike.instagram.scraping.Scraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScrapingControlService {

    private final TransactionService transactionService;
    private final StatusControlService statusControlService;
    private final Scraper scraper;
    private final ReferenceService referenceService;

    @Autowired
    public ScrapingControlService(TransactionService transactionService, StatusControlService statusControlService, Scraper scraper, ReferenceService referenceService) {
        this.transactionService = transactionService;
        this.statusControlService = statusControlService;
        this.scraper = scraper;
        this.referenceService = referenceService;
    }

    public String startScraping(ScrapingRequest request) {
        String transactionId = String.valueOf(new Date().getTime());
        Transaction transaction = saveTransaction(transactionId);

        List<String> userNamesNewReferences = filterNewReferences(request.getSeguindo());
        List<Reference> newReferences = getReferences(userNamesNewReferences);

        // TODAS AS REFERÊCNIAS JÁ EXISTEM NA BASE.
        if (CollectionUtils.isEmpty(newReferences)) {
            List<Reference> references = referenceService.findAllByUserNameIn(request.getSeguindo());
            transaction.setReferences(new HashSet<>(references));
            transaction.setStatus(TransactionEnum.SUCESSO.getDescription());
            transactionService.save(transaction);
            saveStatusControl(transactionId, request.getSeguindo().size(), request.getSeguindo().size());

            return transactionId;
        }

        List<String> userNamesToScraper = new ArrayList<>(0);
        saveReference(newReferences);

        // ALGUMAS REFERÊNCIAS EXISTEM NA BASE E OUTRAS NÃO
        if (newReferences.size() <  request.getSeguindo().size()) {
            userNamesToScraper.addAll(userNamesNewReferences);
            int sizeScrapings = request.getSeguindo().size() - userNamesToScraper.size();
            saveStatusControl(transactionId, request.getSeguindo().size(), sizeScrapings);
        }

        // NENHUMA REFERÊNCIA EXISTIA NA BASE, TODAS SÃO NOVAS
        if (newReferences.size() == request.getSeguindo().size()) {
            userNamesToScraper.addAll(request.getSeguindo());
            saveStatusControl(transactionId, request.getSeguindo().size(), 0);
        }

        List<Reference> references = referenceService.findAllByUserNameIn(request.getSeguindo());
        transaction.setReferences(new HashSet<>(references));
        transactionService.save(transaction);

        executeScraping(transaction, userNamesToScraper, request.getMinFollowers());

        return transactionId;
    }

    private void saveReference(List<Reference> references) {
        referenceService.save(references);
    }

    private void executeScraping(Transaction transaction, List<String> seguindo, Integer minFollowers) {
        for (String username : seguindo) {
            scraper.execute(username, transaction, minFollowers);
        }
    }

    private List<Reference> getReferences(List<String> seguindo) {
        return seguindo.stream()
                .map(this::toReference)
                .collect(Collectors.toList());
    }

    private Reference toReference(String userName) {
        Reference reference = new Reference();
        reference.setUserName(userName);
        return reference;
    }

    private List<String> filterNewReferences(List<String> seguindoList) {
        return seguindoList.stream()
                            .filter(seguindo -> !referenceService.existsByUserName(seguindo))
                            .collect(Collectors.toList());
    }

    private List<String> filterReferencesNotSave(List<String> seguindoList) {
        return seguindoList.stream()
                            .filter(referenceService::existsByUserName)
                            .collect(Collectors.toList());
    }

    private Transaction saveTransaction(String transactionId) {
        Transaction transaction = new Transaction();

        transaction.setTransactionId(transactionId);
        transaction.setStatus(TransactionEnum.EM_ANDAMENTO.getDescription());

        return transactionService.save(transaction);
    }

    private void saveStatusControl(String transactionId, int totalScrapings, int scrapings) {
        StatusControl statusControl = new StatusControl();

        statusControl.setTotalScrapings(totalScrapings);
        statusControl.setTransactionId(transactionId);
        statusControl.setScrapings(scrapings);

        statusControlService.save(statusControl);
    }
}
