package br.com.welike.instagram.configuration;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

@Configuration
public class WebDriverConfiguration {

    @Value("${apostas.webdriver}")
    private String webdriver;

    @Bean
    public WebDriver config() throws FileNotFoundException {
        if (StringUtils.contains(webdriver, "chrome")) {
            File file = ResourceUtils.getFile("classpath:driver/chromedriver.exe");
            System.setProperty(webdriver, file.getAbsolutePath());
//            ChromeOptions options = new ChromeOptions();
//            options.addArguments("--headless");
//            return new ChromeDriver(options);
            return new ChromeDriver();
        } else {
            File file = ResourceUtils.getFile("classpath:driver/geckodriver.exe");
            System.setProperty(webdriver, file.getAbsolutePath());
            return new FirefoxDriver();
        }
    }

}
