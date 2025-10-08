package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CommonUtilities {

    public static String testCaseFolderPath = "";

    // screenshots capturing method
    public static void captureScreenshot(String fileName, WebDriver driver){
        try {
            System.out.println("Capturing Screenshot...");

            //captureScreenshot;
            File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);

            String folderPath = CommonUtilities.testCaseFolderPath;
            if(folderPath == null){
                throw new RuntimeException("Folder path not initialized...");
            }

            //save screenshot
            String filePath = folderPath + "/" + fileName + "_" + System.currentTimeMillis() + ".png";

            //FileHandler.copy(src, new File(filePath));
            Files.copy(src.toPath(), new File(filePath).toPath());

            System.out.println("Screen saved at: " + filePath);
        } catch (IOException e) {
            System.err.println("Error while capturing screenshot: " + e.getMessage());
        }
    }

    // today's date in day-month-year format method
    public static String getTodaysDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        return formatter.format(new Date());
    }

    // today's date folder creation method
    public static String createTodaysDateFolder(){

        System.out.println("Creating today's date folder inside AutomationReports...");

        String todayDate = getTodaysDate();

        try {
            String folderDir = System.getProperty("user.dir") + "/AutomationReports/" + todayDate;

            File dir = new File(folderDir);

            if(dir.exists()){
                System.out.println("Folder already exists: " + folderDir);
            } else {
                if(dir.mkdirs()) {
                    System.out.println("Folder created successfully: " + folderDir);
                } else {
                    System.out.println("Failed to create folder: " + folderDir);
                }
            }

            return folderDir;
        } catch (Exception e) {
            System.err.println("Error while creating folder: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // recursive delete method
    public static void deleteRecursively(File file){
        if(file.isDirectory()){
            File[] children = file.listFiles();
            if(children != null){
                for(File child : children){
                    deleteRecursively(child);
                }
            }
            file.delete(); // deletes subfolder
        } else {
            file.delete(); // deletes file
        }
    }

    // executable test case folder creation method
    public static String createTestCaseFolder(String testCaseNumber){
        String todaysDate = getTodaysDate();
        String folderDir = System.getProperty("user.dir") + "/AutomationReports/" + todaysDate + "/TC_" + testCaseNumber;
        testCaseFolderPath = folderDir;
        File dir = new File(folderDir);

        if(dir.exists()){
            System.out.println("Folder already exists: " + folderDir);
            System.out.println("Clearing old contents...");

            File[] files = dir.listFiles();
            if(files != null){
                for(File file : files){
                    deleteRecursively(file);
                }
            }

            System.out.println("Folder cleared: " + folderDir);
        } else {
            if(dir.mkdirs()){
                System.out.println("Folder created successfully: " + folderDir);
            } else {
                System.out.println("Failed to create folder: " + folderDir);
            }
        }

        return folderDir;
    }

    // wait until locator gets visible method
    public static void waitUntilVisibleByLocator(WebDriver driver, By locator){
        System.out.println("Waiting until element is locator: " + locator);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        System.out.println("Locator is now visible: " + locator);
    }

    // wait until element gets visible method
    public static void waitUntilVisible(WebDriver driver, WebElement element){
        System.out.println("Waiting until element is visible: " + element);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(element));
        System.out.println("Element is now visible: " + element);
    }

    // wait until element becomes clickable method
    public static void waitUntilClickable(WebDriver driver, WebElement element){
        System.out.println("Waiting until element is clickable: " + element);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        System.out.println("Element is now clickable: " + element);
    }

    // scroll to element method
    public static void scrollToElement(WebDriver driver, WebElement element){
        try {
            System.out.println("Scrolling to element: " + element);
            ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", element);
            System.out.println("Successfully scrolled to element: " + element);
        } catch (Exception e) {
            System.err.println("Failed to scroll to element: " + e.getMessage());
            captureScreenshot("ScrollToElement_Failure", driver);
            throw e;
        }
    }

    // get current time method
    public static String getCurrentTime(){
        LocalTime currentTime = LocalTime.now();

        // format (HH for 24-hour format, mm for minutes)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return currentTime.format(formatter);
    }
}
