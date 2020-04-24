package Pages;

import Utilites.Utils;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;
import java.util.Properties;

public class Application {

   static WebDriver driver;
    ExtentTest logger;

    public Application(WebDriver driver, ExtentTest logger) {
        this.driver = driver;
        this.logger = logger;

        logger.log(Status.PASS,
                "<b style=\"color:FUCHSIA\">PBMiniApp Page URL Has Updated Correctly </b>"
                        + driver.getCurrentUrl());
//        logger.addScreenCaptureFromPath(DriverUtils.takeScreenShot("Landing Page...."));
        PageFactory.initElements(driver, this);

    }

    @FindBy(xpath = "//input[@name='postal_code_input' and @class='postal-code']")
    private WebElement city;

    @FindBy(xpath = "//div[contains(@class,'styles__title')]")
    private WebElement plumberCountTitle;

    @FindBy(xpath = "//*/text()[normalize-space(.)='Next']/parent::*")
    private WebElement popupNext;

    @FindBy(xpath = "//div[contains(@class,'titleContainer') or contains(@class,'styles__titleV2') or contains(@class,'styles__title')]")
    private WebElement getQuestion;

    @FindBy(css = "[class*='popup__content'] [class*='styles__rootWithOneButtonV2']>div>button")
    private WebElement parentNext;

    @FindBy(css = "[class*='popup__content'] [class*='styles__rootWithTwoButtonsV2']>div:nth-child(2)>button")
    private WebElement childNext;

    @FindBy(xpath = "//div[contains(@class,'inputContainer')]//textarea[contains(@class,'text-area')]")
    private WebElement optionalTextBox;

    @FindBy(xpath = "//select[@data-test = 'step_time']")
    private WebElement timeSlot;

    @FindBy(xpath = "//input[@placeholder='Email address']")
    private WebElement emailField;

    public void submitApplication(Properties prop) throws InterruptedException, IOException {
        city.clear();
        city.sendKeys(prop.getProperty("City"));
        Thread.sleep(2000);
        city.sendKeys(Keys.ARROW_DOWN);
        city.sendKeys(Keys.ENTER);

        //Thread.sleep(2000);
        String plumberCount = plumberCountTitle.getAttribute("innerText");
        logger.log(Status.INFO, plumberCount);
        popupNext.click();

        FillPopUP(prop);

    }

    public static void selectOption(String value){
        WebElement option = driver.findElement(By.xpath("//div/label/div[contains(text(),'"+value+"')]"));
        option.click();

    }


    public boolean checkIfEmailreached(Properties prop) {
        try {
            emailField.isDisplayed();
            emailField.sendKeys("TestingCompleted");
            logger.info("Email Field Displayed");
            logger.addScreenCaptureFromPath(Utils.takeScreenShot(driver, "EmailPage", logger));

            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public void FillPopUP(Properties prop) throws IOException {
        Boolean isEmailSectionReached = checkIfEmailreached(prop);
        try {
            if (!isEmailSectionReached) {
                String getQuestionFromPopUp = getQuestion.getAttribute("innerText"); // read question from pop up for appropriate case
                String question = getQuestionFromPopUp.split("\\?")[0];
                question.substring(1, question.length());

                logger.log(Status.INFO, "The Question is " + getQuestionFromPopUp);
                switch (question) {
                    case "The problems are to do with which of the following things":
                        selectOption("Tap");
                        parentNext.click();
                        break;
                    case "What problem(s) do you have":
                        selectOption("Leak in a pipe");
                        childNext.click();
                        break;
                    case "What do you need done":
                        selectOption("Replace");
                        childNext.click();
                        break;
                    case "Is there anything else that the Plumber needs to know":
                        optionalTextBox.sendKeys(prop.getProperty("OptionalField"));
                        childNext.click();
                        break;
                    case "When do you require plumbing":
                        selectOption("On a specific date");
                        childNext.click();
                        break;
                    case "On what date":
                        String dateToSelect = Utils.addDaysToCurrentDate(2);
                        String dateForXpath = dateToSelect.split(",")[0];

                        driver.findElement(By.cssSelector("[class*='CalendarMonth'] [aria-label*='" + dateForXpath + "']")).click();
                        childNext.click();
                        break;
                    case "What time do you need the Plumber":
                        Select sel = new Select(timeSlot);
                        sel.selectByVisibleText(prop.getProperty("TimeSlot"));
                        childNext.click();
                    default:
                        break;

                }
                FillPopUP(prop);
            } else {
                System.out.println("in else");

            }
        } catch (Exception e) {
            logger.log(Status.INFO, e);
            logger.addScreenCaptureFromPath(Utils.takeScreenShot(driver, "Something went wrong", logger));

        }
    }

}
