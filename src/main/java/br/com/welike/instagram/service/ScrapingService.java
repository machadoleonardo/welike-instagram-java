package br.com.welike.instagram.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScrapingService {

    public boolean exists(WebDriver driver, String xpath) {
        return driver.findElements(By.xpath(xpath)). size() != 0;
    }
}
