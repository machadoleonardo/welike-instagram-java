package br.com.welike.instagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InstagramApplication {

	public static void main(String[] args) {
		SpringApplication.run(InstagramApplication.class, args);
	}

}

// NOTE: broken into separate statements for clarity. Could be done as one statement.
//	JavascriptExecutor executor = (JavascriptExecutor)driver;
//	WebElement parentElement = (WebElement)executor.executeScript("return arguments[0].parentNode;", childElement);

//    function getElementByXpath(path) {
//        return document.evaluate(path, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;
//    }
//
//console.log( getElementByXpath("//html[1]/body[1]/div[1]") );