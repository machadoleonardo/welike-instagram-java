package br.com.welike.instagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
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

//https://www.instagram.com/simoneweberp/?__a=1
//ResponseType obj=  new RestTemplate().getForObject(URL, ResponseType.class, params);
//ResponseEntity<CurrencyConversionBean> responseEntity =
//		new RestTemplate().getForEntity(
//				"http://localhost:8091/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class,  uriVariables);

//ou Feign Client do Spring Boot e netflix



//"//*[@role='dialog']/descendant::a[@title]/text()"