package br.com.welike.instagram.service;

import br.com.welike.instagram.WebDriverControl;
import br.com.welike.instagram.enums.TransactionEnum;
import br.com.welike.instagram.model.Influencer;
import br.com.welike.instagram.model.Reference;
import br.com.welike.instagram.model.StatusControl;
import br.com.welike.instagram.model.Transaction;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class ScrapingService<T> {

    private final InfluencerService influencerService;
    private final StatusControlService statusControlService;
    private final TransactionService transactionService;

    @Autowired
    public ScrapingService(InfluencerService influencerService, StatusControlService statusControlService, TransactionService transactionService) {
        this.influencerService = influencerService;
        this.statusControlService = statusControlService;
        this.transactionService = transactionService;
    }

    public boolean exists(WebDriverControl webDriverControl, String xpath) {
        return webDriverControl.getDriver().findElements(By.xpath(xpath)).size() != 0;
    }

    public void waitVisibility(WebDriverWait wait, String xpath) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    public void executeJavaScript(WebDriver driver, String script) {
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript(script);
    }

    public void executeJavaScript(WebDriver driver, String script, WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript(script, element);
    }

    public void retryConsumer(T t, Consumer<T> consumer) {
        try {
            consumer.accept(t);
        } catch(StaleElementReferenceException ex) {
            consumer.accept(t);
        }
    }

    public WebElement getElementByJavaScript(WebDriver driver, String script) {
        JavascriptExecutor executor = (JavascriptExecutor)driver;
	    return (WebElement)executor.executeScript(script);
    }

    private void setSmallResolution(WebDriver driver) {
        Dimension dimension = new Dimension(300, 500);
        driver.manage().window().setSize(dimension);
    }

    private void scrollToMaxBotton(WebDriver driver) {
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollTo(0,document.body.scrollHeight);");
    }

    public void saveInfluencers(List<Influencer> influencers, Transaction transaction, String username) {
        List<Influencer> newInfluencers = influencers.stream()
                .filter((influencer) -> !influencerService.existsByUserName(influencer.getUserName()))
                .collect(Collectors.toList());
        newInfluencers = influencerService.save(newInfluencers);

        List<String> existsUsernameInfluencers = influencers.stream()
                .filter((influencer) -> influencerService.existsByUserName(influencer.getUserName()))
                .map(Influencer::getUserName)
                .collect(Collectors.toList());
        List<Influencer> existsInfluencers = influencerService.findAllByUserNameIn(existsUsernameInfluencers);

        newInfluencers.addAll(existsInfluencers);

        Reference reference = transaction.getReferences()
                                        .stream()
                                        .filter((referenceFilter -> referenceFilter.getUserName().equals(username)))
                                        .findFirst()
                                        .orElse(null);

        reference.setInfluencers(new HashSet<>(newInfluencers));

        StatusControl statusControl = statusControlService.findByTransactionId(transaction.getTransactionId());

        Integer newScrapings = statusControl.getScrapings() + 1;
        statusControl.setScrapings(newScrapings);

        if (newScrapings.equals(statusControl.getTotalScrapings())) {
            transaction.setStatus(TransactionEnum.SUCESSO.getDescription());
        }

        transactionService.save(transaction);
        statusControlService.save(statusControl);
    }
}
