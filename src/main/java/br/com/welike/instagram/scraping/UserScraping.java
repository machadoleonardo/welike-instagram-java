package br.com.welike.instagram.scraping;

import br.com.welike.instagram.WebDriverControl;
import br.com.welike.instagram.model.Influencer;
import br.com.welike.instagram.service.ScrapingService;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserScraping {

    private final ScrapingService scrapingService;

    private final String HOST_INSTAGRAM = "https://www.instagram.com/%s";

    private final String LINK_SEGUINDO = "//*/a[text()=' seguindo']";
    private final String LINK_SEGUIDORES = "//*/a[text()=' seguidores']";
    private final String SPAN_SEGUINDO = "//*/span[text()=' seguindo']";
    private final String SPAN_SEGUIDORES = "//*/span[text()=' seguidores']";
    private final String H1 = "//h1";
    private final String IMG_FOTO_PERFIL = "//*[@alt='Foto do perfil de %s']";
    private final String H2_CONTA_PRIVADA = "//*/h2[text()='Esta conta é privada']";

    private WebDriverControl webDriverControl;

    @Autowired
    public UserScraping(ScrapingService scrapingService) {
        this.scrapingService = scrapingService;
    }

    public Influencer execute(WebDriverControl webDriverControl, String username) throws InterruptedException {
        Influencer influencer = new Influencer();
        this.webDriverControl = webDriverControl;

        this.webDriverControl.getDriver().get(String.format(HOST_INSTAGRAM, username));
        Thread.sleep(5000);

        influencer.setFollowedBy(getSeguidores());
        influencer.setFollows(getSeguindo());
        influencer.setFullName(getName());
        influencer.setUserName(username);
        influencer.setProfilePicture(getProfilePicture(username));

        return influencer;
    }

    private int getSeguindo() {
        return getNumberUsers(LINK_SEGUINDO, SPAN_SEGUINDO);
    }

    private int getSeguidores() {
        return getNumberUsers(LINK_SEGUIDORES, SPAN_SEGUIDORES);
    }

    private String getName() {
        return webDriverControl.getDriver()
                               .findElements(By.xpath(H1))
                               .get(1)
                               .getText();
    }

    private String getProfilePicture(String username) {
        return webDriverControl.getDriver()
                               .findElement(By.xpath(String.format(IMG_FOTO_PERFIL, username)))
                               .getAttribute("src");
    }

    private int getNumberUsers(String linkUsers, String spanUsers) {
        String usuariosSeguindo;

        // QUANDO A CONTA É PRIVADA O NÚMERO DE SEGUIDORES/SEGUINDO FICA DENTRO DE UM SPAN
        if (isPrivateAccount()) {
            usuariosSeguindo = webDriverControl.getDriver()
                    .findElement(By.xpath(spanUsers))
                    .findElement(By.tagName("span"))
                    .getAttribute("title");
        } else {
            // QUANDO A CONTA É PÚBLICA O NÚMERO DE SEGUIDORES/SEGUINDO FICA DENTRO DE UM LINK
            usuariosSeguindo = webDriverControl.getDriver()
                    .findElement(By.xpath(linkUsers))
                    .findElement(By.tagName("span"))
                    .getAttribute("title");

            if (usuariosSeguindo.isEmpty()) {
                usuariosSeguindo = webDriverControl.getDriver()
                        .findElement(By.xpath(linkUsers))
                        .findElement(By.tagName("span"))
                        .getText();
            }
        }

        return Integer.parseInt(usuariosSeguindo.replace(".", ""));
    }

    private boolean isPrivateAccount() {
        return scrapingService.exists(webDriverControl, H2_CONTA_PRIVADA);
    }
}
