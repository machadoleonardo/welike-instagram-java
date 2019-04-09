package br.com.welike.instagram.scraping;

import br.com.welike.instagram.WebDriverControl;
import br.com.welike.instagram.service.InfluencerService;
import br.com.welike.instagram.service.ScrapingService;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Scraper {

    @Value("${apostas.webdriver}")
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

    @Autowired
    public Scraper(InfluencerService influencerService, ScrapingService scrapingService) {
        this.influencerService = influencerService;
        this.scrapingService = scrapingService;
    }

    @Async
    public void execute(String username, String transactionId) throws InterruptedException, IOException, AWTException {
        WebDriverControl webDriverControl = getWebDriverControl();

        webDriverControl.getDriver().get(HOST_INSTAGRAM);
        authentication(webDriverControl);
        Thread.sleep(5000);
        if (scrapingService.exists(webDriverControl, LINK_BAIXAR_APLICATIVO)) {
            webDriverControl.getDriver().findElement(By.xpath(LINK_NAO_BAIXAR_APLICATIVO)).click();
        }
        if (scrapingService.exists(webDriverControl, BUTTON_NAO_ATIVAR_NOTIFICACOES)) {
            webDriverControl.getDriver().findElement(By.xpath(BUTTON_NAO_ATIVAR_NOTIFICACOES)).click();
        }
        findUser(webDriverControl, username);
        Thread.sleep(5000);
        List<String> seguidores = findSeguidores(webDriverControl, username);
        webDriverControl.getDriver().close();
        influencerService.saveInfluencers(seguidores, transactionId);
    }

    private WebDriver getWebDriver() throws FileNotFoundException {
        WebDriver driver = null;
        if (StringUtils.contains(webdriver, "chrome")) {
            File file = ResourceUtils.getFile("classpath:driver/chromedriver");
            System.setProperty(webdriver, file.getAbsolutePath());
//            ChromeOptions options = new ChromeOptions();
//            options.addArguments("--headless");
//            return new ChromeDriver(options);
            driver = new ChromeDriver();
        } else {
            File file = ResourceUtils.getFile("classpath:driver/geckodriver.exe");
            System.setProperty(webdriver, file.getAbsolutePath());
            driver = new FirefoxDriver();
        }

        return driver;
    }

    private WebDriverControl getWebDriverControl() throws FileNotFoundException {
        WebDriverControl webDriverControl = new WebDriverControl();
        WebDriver driver = getWebDriver();

        webDriverControl.setDriver(driver);
        webDriverControl.setWait(new WebDriverWait(driver, 20));

        return webDriverControl;
    }

    private List<String> findSeguidores(WebDriverControl webDriverControl, String username) throws InterruptedException, AWTException {
        webDriverControl.getDriver().findElement(By.xpath(String.format(LINK_SEGUINDO, username))).click();
        scrapingService.waitVisibility(webDriverControl.getWait(), BUTTON_SEGUINDO);
        Thread.sleep(1000);

//        moveMouse(driver, driver.findElement(By.xpath(DIALOG)));

        List<WebElement> usuariosSeguindo = webDriverControl.getDriver().findElement(By.xpath(MODAL_SEGUINDO))
                                                  .findElements(By.tagName("li"));
        return usuariosSeguindo.stream()
                               .map(this::mapUsuariosSeguindoToUserName)
                               .collect(Collectors.toList());
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
