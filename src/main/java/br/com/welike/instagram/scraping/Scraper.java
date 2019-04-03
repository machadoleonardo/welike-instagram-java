package br.com.welike.instagram.scraping;

import br.com.welike.instagram.service.InfluencerService;
import br.com.welike.instagram.service.ScrapingService;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Scraper {

    private final String HOST_INSTAGRAM = "https://www.instagram.com/accounts/login/?source=auth_switcher";

//    private final String INPUT_LOGIN = "//*/input[@name='username']";

    private final String INPUT_LOGIN = "//*[@id=\"react-root\"]/section/main/div/article/div/div[1]/div/form/div[2]/div/div[1]/input";
    private final String INPUT_PASSWORD = "//*[@id=\"react-root\"]/section/main/div/article/div/div[1]/div/form/div[3]/div/div[1]/input";
    private final String BUTTON_LOGIN = "//*[@id=\"react-root\"]/section/main/div/article/div/div[1]/div/form/div[4]/button";
    private final String INPUT_FIND_USER = "//*[@id=\"react-root\"]/section/nav/div[2]/div/div/div[2]/input";
    private final String BUTTON_NAO_ATIVAR_NOTIFICACOES = "/html/body/div[2]/div/div/div[3]/button[2]";
    private final String LINK_SEGUINDO = "//*[@id=\"react-root\"]/section/main/div/ul/li[3]/a";
    private final String DIV_TABELA_PESSOAS_SEGUINDO = "//*[@id=\"react-root\"]/section/main/div[2]/ul/div";

    @Value("${instagram.auth.login}")
    private String login;

    @Value("${instagram.auth.password}")
    private String password;

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final InfluencerService influencerService;
    private final ScrapingService scrapingService;

    @Autowired
    public Scraper(WebDriver driver, InfluencerService influencerService, ScrapingService scrapingService) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 20);
        this.influencerService = influencerService;
        this.scrapingService = scrapingService;
    }

    @Async
    public void execute(String username, String transactionId) throws InterruptedException {
        driver.get(HOST_INSTAGRAM);
        authentication();
        Thread.sleep(5000);
        if (scrapingService.exists(BUTTON_NAO_ATIVAR_NOTIFICACOES)) {
            driver.findElement(By.xpath(BUTTON_NAO_ATIVAR_NOTIFICACOES)).click();
        }
        findUser(username);
        Thread.sleep(5000);
        influencerService.saveInfluencers(findSeguidores(), transactionId);
    }

    private List<String> findSeguidores() throws InterruptedException {
        setSmallResolution();
        driver.findElement(By.xpath(LINK_SEGUINDO)).click();
        Thread.sleep(5000);
        scrollToMaxBotton();
        Thread.sleep(10000);

        List<WebElement> usuariosSeguindo = driver.findElement(By.xpath(DIV_TABELA_PESSOAS_SEGUINDO))
                                                  .findElements(By.tagName("li"));
        return usuariosSeguindo.stream()
                               .map(this::mapUsuariosSeguindoToUserName)
                               .collect(Collectors.toList());
    }

    private String mapUsuariosSeguindoToUserName(WebElement li) {
        String userName = "";
        List<WebElement> possiveisUserNames = li.findElements(By.tagName("a"));

        for (WebElement possivelUserName : possiveisUserNames) {
            if (!possivelUserName.getText().equals("")) {
                userName = possivelUserName.getText();
                break;
            }
        }

        return userName;
    }

    private void setSmallResolution() {
        Dimension dimension = new Dimension(400, 500);
        driver.manage().window().setSize(dimension);
    }

    private void scrollToMaxBotton() {
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollTo(0,document.body.scrollHeight);");
    }

    private void findUser(String username) throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(INPUT_FIND_USER)));
        driver.findElement(By.xpath(INPUT_FIND_USER)).sendKeys(username);
        Thread.sleep(5000);


        try{
            driver.findElements(By.xpath("//*[contains(text(),'" + username + "')]")).get(0).click();
        }catch (Exception e){
            driver.findElements(By.xpath("//*[contains(text(),'" + username + "')]")).get(1).click();
        }
    }

    private void authentication() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(INPUT_LOGIN)));

        driver.findElement(By.xpath(INPUT_LOGIN)).sendKeys(login);
        driver.findElement(By.xpath(INPUT_PASSWORD)).sendKeys(password);

        driver.findElement(By.xpath(BUTTON_LOGIN)).click();
    }
}
