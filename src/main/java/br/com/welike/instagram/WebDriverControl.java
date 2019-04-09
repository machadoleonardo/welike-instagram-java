package br.com.welike.instagram;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class WebDriverControl {

    private WebDriver driver;
    private WebDriverWait wait;

}
