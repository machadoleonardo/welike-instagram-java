package br.com.welike.instagram.scraping;

import br.com.welike.instagram.WebDriverControl;
import br.com.welike.instagram.model.Influencer;
import br.com.welike.instagram.service.InfluencerService;
import br.com.welike.instagram.service.ScrapingService;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Scraper {

    @Value("${welike.webdriver}")
    private String webdriver;

    private final String HOST_INSTAGRAM = "https://www.instagram.com/accounts/login/?source=auth_switcher";

//    private final String INPUT_LOGIN = "//*/input[@name='username']";

    private final String INPUT_LOGIN = "//*[@id=\"react-root\"]/section/main/div/article/div/div[1]/div/form/div[2]/div/div[1]/input";
    private final String INPUT_PASSWORD = "//*[@id=\"react-root\"]/section/main/div/article/div/div[1]/div/form/div[3]/div/div[1]/input";
    private final String BUTTON_LOGIN = "//*[@type=\"submit\"]";
    private final String INPUT_FIND_USER = "//*[@id=\"react-root\"]/section/nav/div[2]/div/div/div[2]/input";
    private final String BUTTON_NAO_ATIVAR_NOTIFICACOES = "//*/button[contains(text(),'Agora não')]";
    private final String LINK_SEGUINDO = "//*[@href='/%s/following/']";
    private final String DIV_TABELA_PESSOAS_SEGUINDO = "//*[@id=\"react-root\"]/section/main/div[2]/ul/div";
    private final String MODAL_SEGUINDO = "//*[@role='dialog']";
    private final String BUTTON_SEGUINDO = "//*/button[text()='Seguir']";
    private final String LINK_USER_SELECTED = "//*/span[text()='%s']";
    private final String DIALOG = "//*[@role='dialog']";
    private final String LINK_EXPLORE_PEOPLE = "//*[@href='/explore/people/']";
    private final String LI_LAST_SEGUIDOR = "//*[@role='dialog']/descendant::li[last()]";
    private final String LINK_BAIXAR_APLICATIVO = "//*/a[text()='Baixar aplicativo']";
    private final String LINK_NAO_BAIXAR_APLICATIVO = "//*/a[text()='Agora não']";

    @Value("${instagram.auth.login}")
    private String login;

    @Value("${instagram.auth.password}")
    private String password;

    private final InfluencerService influencerService;
    private final ScrapingService scrapingService;
    private final BeanFactory beanFactory;

    @Autowired
    public Scraper(InfluencerService influencerService, ScrapingService scrapingService, BeanFactory beanFactory) {
        this.influencerService = influencerService;
        this.scrapingService = scrapingService;
        this.beanFactory = beanFactory;
    }

    @Async
    public void execute(String username, String transactionId) throws InterruptedException, IOException, AWTException {
        WebDriverControl webDriverControl = beanFactory.getBean(WebDriverControl.class);
        webDriverControl.setWebDriverControl();

        webDriverControl.getDriver().get(HOST_INSTAGRAM);

        List<Cookie> cookies = new ArrayList<>(0);

        cookies.add(new Cookie("csrftoken", "9CYUp865ljjZ9wzi2bFcgzCvX5YToRTQ"));
        cookies.add(new Cookie("ds_user_id", "8680661754"));
        cookies.add(new Cookie("mcd", "3"));
        cookies.add(new Cookie("mid", "XD96wQAEAAEE3tKTsRIWuSH1kdIV"));
        cookies.add(new Cookie("rur", "ATN"));
        cookies.add(new Cookie("sessionid", "8680661754%3Ays0O9VpkOq0hGU%3A10"));
        cookies.add(new Cookie("shbid", "3166"));
        cookies.add(new Cookie("shbts", "1555342831.1156552"));
        cookies.add(new Cookie("urlgen", "\"{\\\"189.39.26.163\\\": 16735}:1hG3ik:GQfp_Z9WjLD0rzEdaJKnhKj4XHs\""));

        for(Cookie cookie : cookies) {
            webDriverControl.getDriver().manage().addCookie(cookie);
        }

        webDriverControl.getDriver().navigate().refresh();

        Thread.sleep(5000);
        if (scrapingService.exists(webDriverControl, LINK_BAIXAR_APLICATIVO)) {
            webDriverControl.getDriver().findElement(By.xpath(LINK_NAO_BAIXAR_APLICATIVO)).click();
        }
        if (scrapingService.exists(webDriverControl, BUTTON_NAO_ATIVAR_NOTIFICACOES)) {
            webDriverControl.getDriver().findElement(By.xpath(BUTTON_NAO_ATIVAR_NOTIFICACOES)).click();
        }

        findUser(webDriverControl, username);
        Thread.sleep(5000);

        List<Influencer> seguidores = findSeguidores(webDriverControl, username);

        webDriverControl.getDriver().close();

        influencerService.save(seguidores, transactionId);
    }

    private List<Influencer> findSeguidores(WebDriverControl webDriverControl, String username) throws InterruptedException, AWTException {
        List<Influencer> influencers = new ArrayList<>(0);

        webDriverControl.getDriver().findElement(By.xpath(String.format(LINK_SEGUINDO, username))).click();
        scrapingService.waitVisibility(webDriverControl.getWait(), BUTTON_SEGUINDO);
        Thread.sleep(1000);

//        moveMouse(driver, driver.findElement(By.xpath(DIALOG)));

        List<WebElement> usuariosSeguindo = webDriverControl.getDriver().findElement(By.xpath(MODAL_SEGUINDO))
                                                  .findElements(By.tagName("li"));
        List<String> userNameList = usuariosSeguindo.stream()
                .map(this::mapUsuariosSeguindoToUserName)
                .collect(Collectors.toList());

//        for (String userNameOfList : userNameList) {
//            influencers.add(mapUserNameToInfluencer(webDriverControl, userNameOfList));
//        }

        influencers.add(mapUserNameToInfluencer(webDriverControl, userNameList.get(0)));

        return influencers;
    }

    private Influencer mapUserNameToInfluencer(WebDriverControl webDriverControl, String username) throws InterruptedException {
        UserScraping userScraping = new UserScraping();
        return userScraping.execute(webDriverControl, username);
    }

    private void moveMouse(WebDriver driver, WebElement element) throws AWTException, InterruptedException {
        Actions actions = new Actions(driver);
        for (int i = 0; i < 8; i++) {
            Thread.sleep(1000);
            actions.moveToElement(element).click().sendKeys(Keys.ARROW_DOWN).build().perform();
        }
        for (int i = 0; i < 6; i++) {
            actions.moveToElement(element).click().sendKeys(Keys.END).build().perform();
        }

//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LINK_EXPLORE_PEOPLE)));
//        driver.findElement(By.xpath(LINK_EXPLORE_PEOPLE)).click();
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

    private void findUser(WebDriverControl webDriverControl, String username) throws InterruptedException {
        String xpathUserSelected = String.format(LINK_USER_SELECTED, username);
        webDriverControl.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(INPUT_FIND_USER)));
        webDriverControl.getDriver().findElement(By.xpath(INPUT_FIND_USER)).sendKeys(username);
        scrapingService.waitVisibility(webDriverControl.getWait(), xpathUserSelected);
        webDriverControl.getDriver().findElement(By.xpath(xpathUserSelected)).click();
    }

    private void authentication(WebDriverControl webDriverControl) {
        webDriverControl.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(INPUT_LOGIN)));

        webDriverControl.getDriver().findElement(By.xpath(INPUT_LOGIN)).sendKeys(login);
        webDriverControl.getDriver().findElement(By.xpath(INPUT_PASSWORD)).sendKeys(password);

        webDriverControl.getDriver().findElement(By.xpath(BUTTON_LOGIN)).click();
    }
}
