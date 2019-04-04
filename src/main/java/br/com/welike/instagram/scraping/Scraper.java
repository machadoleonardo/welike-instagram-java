package br.com.welike.instagram.scraping;

import br.com.welike.instagram.service.InfluencerService;
import br.com.welike.instagram.service.ScrapingService;
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
    private final String LINK_SEGUINDO = "//*[@id=\"react-root\"]/section/main/div/header/section/ul/li[3]/a";
    private final String DIV_TABELA_PESSOAS_SEGUINDO = "//*[@id=\"react-root\"]/section/main/div[2]/ul/div";
    private final String DIALOG = "//*[@role='dialog']";


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
    public void execute(String username, String transactionId) throws InterruptedException, FileNotFoundException, AWTException {
        WebDriver driver = newWebDriver();

        driver.get(HOST_INSTAGRAM);
        authentication(driver);
        Thread.sleep(5000);
        if (scrapingService.exists(driver, BUTTON_NAO_ATIVAR_NOTIFICACOES)) {
            driver.findElement(By.xpath(BUTTON_NAO_ATIVAR_NOTIFICACOES)).click();
        }
        findUser(driver, username);
        Thread.sleep(5000);
        influencerService.saveInfluencers(findSeguidores(driver), transactionId);
    }

    private WebDriver newWebDriver() throws FileNotFoundException {
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

        this.wait = new WebDriverWait(driver, 20);
        return driver;
    }

    private List<String> findSeguidores(WebDriver driver) throws InterruptedException, AWTException {
//        setSmallResolution(driver);
        driver.findElement(By.xpath(LINK_SEGUINDO)).click();
        Thread.sleep(5000);
        moveMouse(driver, driver.findElement(By.xpath(DIALOG)));
        scrollToMaxBotton(driver);
        Thread.sleep(10000);

        List<WebElement> usuariosSeguindo = driver.findElement(By.xpath(DIV_TABELA_PESSOAS_SEGUINDO))
                                                  .findElements(By.tagName("li"));
        return usuariosSeguindo.stream()
                               .map(this::mapUsuariosSeguindoToUserName)
                               .collect(Collectors.toList());
    }

    private void moveMouse(WebDriver driver, WebElement element) throws AWTException {
        Actions actions = new Actions(driver);
        actions.moveToElement(element);
        actions.perform();


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

    private void setSmallResolution(WebDriver driver) {
        Dimension dimension = new Dimension(300, 500);
        driver.manage().window().setSize(dimension);
    }

    private void scrollToMaxBotton(WebDriver driver) {
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollTo(0,document.body.scrollHeight);");
    }

    private void findUser(WebDriver driver, String username) throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(INPUT_FIND_USER)));
        driver.findElement(By.xpath(INPUT_FIND_USER)).sendKeys(username);
        Thread.sleep(5000);
        driver.findElement(By.xpath("//*/span[text()='" + username + "']")).click();
    }

    private void authentication(WebDriver driver) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(INPUT_LOGIN)));

        driver.findElement(By.xpath(INPUT_LOGIN)).sendKeys(login);
        driver.findElement(By.xpath(INPUT_PASSWORD)).sendKeys(password);

        driver.findElement(By.xpath(BUTTON_LOGIN)).click();
    }
}
