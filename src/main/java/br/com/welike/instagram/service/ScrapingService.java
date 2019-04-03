package br.com.welike.instagram.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScrapingService {

    private WebDriver driver;

    @Autowired
    public ScrapingService(WebDriver driver) {
        this.driver = driver;
    }

    public boolean exists(String xpath) {
        return driver.findElements(By.xpath(xpath)).size() != 0;
    }
}
