package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AuthPage {
    WebDriver driver;

    public AuthPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//p/span[text()=\"Login here\"]")
    private WebElement lnkCreateAccountMenu;

    @FindBy(xpath = "//p/span[text()=\"Login here\"]")
    private WebElement lnkLoginMenu;

    @FindBy(xpath = "//p[text()=\"Full Name\"]/following-sibling::input")
    private WebElement txtFullName;

    @FindBy(xpath = "//p[text()=\"Email\"]/following-sibling::input")
    private WebElement txtUserName;

    @FindBy(xpath = "//p[text()=\"Password\"]/following-sibling::input")
    private WebElement txtPassword;

    @FindBy(xpath = "//button[text()=\"Login\"]")
    private WebElement btnLogin;

    @FindBy(xpath = "//form/div/button[text()=\"Create Account\"]")
    private WebElement btnCreateAccount;
}
