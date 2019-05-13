package br.com.welike.instagram;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

@Component
@Scope("prototype")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class WebDriverControl {

    @Value("${welike.webdriver}")
    private String webdriver;

    private WebDriver driver;
    private WebDriverWait wait;

    private WebDriver getWebDriver() throws FileNotFoundException {
        WebDriver driver = null;
        if (StringUtils.contains(webdriver, "chrome")) {
//            File file = ResourceUtils.getFile("classpath:driver/chromedriver");
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

        return driver;
    }

    public void setWebDriverControl() throws FileNotFoundException {
        WebDriver driver = getWebDriver();

        this.setDriver(driver);
        this.setWait(new WebDriverWait(driver, 20));
    }
}
