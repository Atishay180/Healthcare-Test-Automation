package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.Map;

public class MyAppointmentsPage {
    private final WebDriver driver;
    private final Map<String, String> args;

    public MyAppointmentsPage(WebDriver driver, Map<String, String> args) {
        this.driver = driver;
        this.args = args;

        PageFactory.initElements(driver, this);
    }
}
