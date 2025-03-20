package casedoitTest;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class TestBot {
    private WebDriver driver;
    private String url;
    private final Random random = new Random();
    private final int TIMEOUT = getRandomTimeout();
    private WebDriverWait wait;
    private ArrayList<String> searchResults;
    private Actions actionProvider;
    public TestBot(String url){
        this.url = url;
        initializeDriver();
        initializeWait();
        initializeActionProvider();
        searchResults = new ArrayList<>();
    }

    public ArrayList<String> getSearchResults(){
        return searchResults;
    }
    private int getRandomTimeout(){ //For getting random timeout because we need different timeouts in between processes.
        return random.nextInt(6) + 5;
    }
    public void removeCookieOption() { //removes cookies but if they are already removed it prompts message
        String buttonXPath = "//button[@data-hook='consent-banner-apply-button' and contains(text(), 'Kabul Et')]";

        try {
            WebElement button = wait.until(driver -> driver.findElement(By.xpath(buttonXPath)));
            WebElement clickableButton = wait.until(ExpectedConditions.elementToBeClickable(button));
            clickableButton.click();
        } catch (NoSuchElementException | TimeoutException e) {
            System.out.println("Cookie banner not found or not clickable. No action needed.");
        }
    }
    private void storeData(){ //Stores search results in a list
        searchResults = new ArrayList<>();
        actionProvider.pause(Duration.ofSeconds(TIMEOUT)).build().perform();
        String listParentDivSelector = "div.sUfIOU0";
        String itemTitleSelector = "a[data-hook='item-title']";

        WebElement parentDiv = wait.until(driver -> driver.findElement(By.cssSelector(listParentDivSelector)));
        List<WebElement> searchResultItems = wait.until(driver -> parentDiv.findElements(By.cssSelector
                ("ul[data-hook='grid-layout'] > li")));

        for (WebElement resultItem : searchResultItems) {
            String itemName = resultItem.findElement(By.cssSelector(itemTitleSelector)).getText();
            searchResults.add(itemName);
        }
    }
    public void search(String query){ //Clicks search box and enters search input
        connect();
        removeCookieOption();
        WebElement searchBox = driver.findElement(By.id("input_search-box-input-comp-krswnht1"));
        searchBox.sendKeys(query);
        searchBox.sendKeys(Keys.RETURN);
        storeData();
        System.out.println(searchResults);
        System.out.println(searchResults.size());
    }
    public void login(String email, String password){ //finds login panel, enters login input and prompts login details.
        connect();
        removeCookieOption();
        actionProvider.pause(Duration.ofSeconds(TIMEOUT)).build().perform();
        WebElement loginpage = driver.findElement(By.className("YT_9QV"));
        loginpage.click();
        actionProvider.pause(Duration.ofSeconds(TIMEOUT)).build().perform();
        WebElement loginButton = driver.findElement(By.className("qufiy3"));
        loginButton.click();
        actionProvider.pause(Duration.ofSeconds(TIMEOUT)).build().perform();
        WebElement girişyapButton = driver.findElement(By.className("ka7snc"));
        girişyapButton.click();
        actionProvider.pause(Duration.ofSeconds(TIMEOUT)).build().perform();
        WebElement emailBox = driver.findElement(By.id("input_input_emailInput_SM_ROOT_COMP815"));
        emailBox.sendKeys(email);
        actionProvider.pause(Duration.ofSeconds(TIMEOUT)).build().perform();
        WebElement passwordBox = driver.findElement(By.id("input_input_passwordInput_SM_ROOT_COMP815"));
        passwordBox.sendKeys(password);
        actionProvider.pause(Duration.ofSeconds(TIMEOUT)).build().perform();
        WebElement theButton = driver.findElement(By.id("okButton_SM_ROOT_COMP815"));
        theButton.click();
        System.out.println("email:" + email + " password:"+ password);

    }


    public boolean isOnDashboard(){ //checks if user is on dashboard
        try {
            WebElement theLogo = driver.findElement(By.id("comp-krswhrdc"));
            return theLogo.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    public WebDriver getDriver() {
        return driver;
    }
    private void initializeActionProvider() {
        actionProvider = new Actions(driver);
    }
    public void initializeWait() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
    }
    private void initializeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--whitelisted-ips=''");
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
    }

    private void connect() {
        driver.get(url);
    }
    public void quit() {
        actionProvider.pause(Duration.ofSeconds(TIMEOUT)).build().perform();
        driver.quit();
    }
    public boolean isEmptyEmailMessageDisplayed(){//checks if the error message is displayed
        try{
            WebElement theEmailMessage = wait.until(driver->driver.findElement(By.id("siteMembersInputErrorMessage_emailInput_SM_ROOT_COMP815")));
            String errorMessageText = theEmailMessage.getText();
            System.out.println("Error Message: " + errorMessageText);
            return theEmailMessage.isDisplayed();
        }catch (NoSuchElementException e){
            return false;
        }
    }
    public boolean isErrorMessageDisplayed(){//checks if the error message is displayed
        try{
            WebElement theMessage = wait.until(driver->driver.findElement(By.id("siteMembersInputErrorMessage_passwordInput_SM_ROOT_COMP815")));
            String errorMessageText = theMessage.getText();
            System.out.println("Error Message: " + errorMessageText);
            return theMessage.isDisplayed() ;
        }catch (NoSuchElementException e){
            return false;
        }
    }
}

