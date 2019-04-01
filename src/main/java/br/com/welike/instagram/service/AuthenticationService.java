package br.com.welike.instagram.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private WebDriver driver;

    @Autowired
    public AuthenticationService(WebDriver driver) {
        this.driver = driver;
    }

    public boolean exists(String xpath) {
        return driver.findElements(By.xpath(xpath)).size() != 0;
    }

}
