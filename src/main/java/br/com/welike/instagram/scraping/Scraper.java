package br.com.welike.instagram.scraping;

import br.com.welike.instagram.service.InfluencerService;
import br.com.welike.instagram.service.ScrapingService;
import me.postaddict.instagram.scraper.Instagram;
import me.postaddict.instagram.scraper.cookie.CookieHashSet;
import me.postaddict.instagram.scraper.cookie.DefaultCookieJar;
import me.postaddict.instagram.scraper.interceptor.ErrorInterceptor;
import me.postaddict.instagram.scraper.model.Account;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
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

    private WebDriverWait wait;
    private final InfluencerService influencerService;
    private final ScrapingService scrapingService;

    @Autowired
    public Scraper(InfluencerService influencerService, ScrapingService scrapingService) {
        this.influencerService = influencerService;
        this.scrapingService = scrapingService;
    }

    @Async
    public void execute(String username, String transactionId) throws InterruptedException, IOException, AWTException {
        WebDriver driver = newWebDriver();

        driver.get(HOST_INSTAGRAM);
        authentication(driver);
        Thread.sleep(5000);
        if (scrapingService.exists(driver, LINK_BAIXAR_APLICATIVO)) {
            driver.findElement(By.xpath(LINK_NAO_BAIXAR_APLICATIVO)).click();
        }
        if (scrapingService.exists(driver, BUTTON_NAO_ATIVAR_NOTIFICACOES)) {
            driver.findElement(By.xpath(BUTTON_NAO_ATIVAR_NOTIFICACOES)).click();
        }
        findUser(driver, username);
        Thread.sleep(5000);
        influencerService.saveInfluencers(findSeguidores(driver, username), transactionId);
    }

    private WebDriver newWebDriver() throws FileNotFoundException {
        WebDriver driver = null;
        if (StringUtils.contains(webdriver, "chrome")) {
            File file = ResourceUtils.getFile("classpath:driver/chromedriver.exe");
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

        this.wait = new WebDriverWait(driver, 20);
        return driver;
    }

    private List<String> findSeguidores(WebDriver driver, String username) throws InterruptedException, AWTException {
        driver.findElement(By.xpath(String.format(LINK_SEGUINDO, username))).click();
        scrapingService.waitVisibility(wait, BUTTON_SEGUINDO);

//        moveMouse(driver, driver.findElement(By.xpath(DIALOG)));

        List<WebElement> usuariosSeguindo = driver.findElement(By.xpath(MODAL_SEGUINDO))
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

    private void findUser(WebDriver driver, String username) throws InterruptedException {
        String xpathUserSelected = String.format(LINK_USER_SELECTED, username);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(INPUT_FIND_USER)));
        driver.findElement(By.xpath(INPUT_FIND_USER)).sendKeys(username);
        scrapingService.waitVisibility(wait, xpathUserSelected);
        driver.findElement(By.xpath(xpathUserSelected)).click();
    }

    private void authentication(WebDriver driver) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(INPUT_LOGIN)));

        driver.findElement(By.xpath(INPUT_LOGIN)).sendKeys(login);
        driver.findElement(By.xpath(INPUT_PASSWORD)).sendKeys(password);

        driver.findElement(By.xpath(BUTTON_LOGIN)).click();
    }
}
