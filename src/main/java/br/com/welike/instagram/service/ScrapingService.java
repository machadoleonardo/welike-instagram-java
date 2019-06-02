package br.com.welike.instagram.service;

import br.com.welike.instagram.WebDriverControl;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class ScrapingService<T> {

    public boolean exists(WebDriverControl webDriverControl, String xpath) {
        return webDriverControl.getDriver().findElements(By.xpath(xpath)). size() != 0;
    }

    public void waitVisibility(WebDriverWait wait, String xpath) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    public void executeJavaScript(WebDriver driver, String script) {
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript(script);
    }

    public void retryConsumer(T t, Consumer<T> consumer) {
        try {
            consumer.accept(t);
        } catch(StaleElementReferenceException ex) {
            consumer.accept(t);
        }
    }

    public WebElement getElementByJavaScript(WebDriver driver, String script) {
        JavascriptExecutor executor = (JavascriptExecutor)driver;
	    return (WebElement)executor.executeScript("return arguments[0].parentNode;");
    }

    private void setSmallResolution(WebDriver driver) {
        Dimension dimension = new Dimension(300, 500);
        driver.manage().window().setSize(dimension);
    }

    private void scrollToMaxBotton(WebDriver driver) {
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollTo(0,document.body.scrollHeight);");
    }
}
