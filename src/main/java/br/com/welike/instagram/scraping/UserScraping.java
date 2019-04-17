package br.com.welike.instagram.scraping;

import br.com.welike.instagram.WebDriverControl;
import br.com.welike.instagram.model.Influencer;
import org.openqa.selenium.By;

public class UserScraping {

    private final String HOST_INSTAGRAM = "https://www.instagram.com/%s";

    private final String LINK_SEGUINDO = "//*/a[text()=' seguindo']";
    private final String LINK_SEGUIDORES = "//*/a[text()=' seguidores']";
    private final String H1 = "//h1";
    private final String IMG_FOTO_PERFIL = "//*[@alt='Foto do perfil de %s']";

    private WebDriverControl webDriverControl;

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
        String usuariosSeguindo = webDriverControl.getDriver()
              .findElement(By.xpath(LINK_SEGUINDO))
              .findElement(By.tagName("span"))
              .getAttribute("title");

        if (usuariosSeguindo.isEmpty()) {
            usuariosSeguindo = webDriverControl.getDriver()
                    .findElement(By.xpath(LINK_SEGUINDO))
                    .findElement(By.tagName("span"))
                    .getText();
        }

        return Integer.parseInt(usuariosSeguindo.replace(".", ""));
    }

    private int getSeguidores() {
        String seguidores = webDriverControl.getDriver()
                                          .findElement(By.xpath(LINK_SEGUIDORES))
                                          .findElement(By.tagName("span"))
                                          .getAttribute("title");

        if (seguidores.isEmpty()) {
            seguidores = webDriverControl.getDriver()
                    .findElement(By.xpath(LINK_SEGUINDO))
                    .findElement(By.tagName("span"))
                    .getText();
        }

        return Integer.parseInt(seguidores.replace(".", ""));
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
}
