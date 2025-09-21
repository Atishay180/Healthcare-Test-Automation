package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.CommonUtilities;

public class HomePage {
    WebDriver driver;

    public HomePage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//*[@id=\"root\"]/div/div/div/div[1]/div/button/svg/path")
    private WebElement btnAlert;

    @FindBy(xpath = "//button[text()=\"Create Account\"]")
    private WebElement lnkAuthPage;

    public void closeAlertBar(){
        CommonUtilities.captureScreenshot("Alert Bar", driver);
        btnAlert.click();
    }

    public AuthPage getAuthPage(){
        CommonUtilities.captureScreenshot("Home Page", driver);
        lnkAuthPage.click();

        return new AuthPage(driver);
    }
}
