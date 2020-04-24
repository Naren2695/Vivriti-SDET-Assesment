package Utilites;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.github.bonigarcia.wdm.*;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Utils {
    static ExtentReports Reporter = null;
    static ExtentTest logger;
    public static final int DEFAULT_WAIT_TIME = 40;
    public static String reportPath;

    public static WebDriver getDriver(String browser) {
        WebDriver driver = null;
        try {
            switch (browser.toLowerCase()) {
                case "firefox":
                    FirefoxDriverManager.getInstance(DriverManagerType.FIREFOX).setup();
                    driver = new FirefoxDriver();
                    break;
                case "chrome":
                    ChromeDriverManager.getInstance(DriverManagerType.CHROME).setup();
                    driver = new ChromeDriver();
                    break;
                case "edge":
                    EdgeDriverManager.getInstance(DriverManagerType.EDGE).setup();
                    driver = new EdgeDriver();
                    break;
                case "ie":
                case "internet explorer":
                    InternetExplorerDriverManager.getInstance(DriverManagerType.IEXPLORER).setup();
                    driver = new InternetExplorerDriver();
                    break;
            }
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return driver;
    }


    public static void waitForPage(WebDriver driver) {
        try {
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT_TIME);
            final JavascriptExecutor executor = (JavascriptExecutor) driver;
            ExpectedCondition<Boolean> condition = new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver arg0) {
                    return executor.executeScript("return document.readyState").equals("complete");
                }
            };

            wait.until(condition);
        } catch (TimeoutException e) {
            logger.log(Status.FAIL,
                    "Page not loaded within " + DEFAULT_WAIT_TIME + " Seconds</br><pre>" + e.getMessage());
        } catch (WebDriverException e) {
            logger.log(Status.FAIL,
                    "Element not found within " + DEFAULT_WAIT_TIME + " Seconds</br><pre>" + e.getMessage());
        } catch (Exception e) {
            logger.log(Status.FAIL,
                    "Page not loaded within " + DEFAULT_WAIT_TIME + " Seconds</br><pre>" + e.getMessage());
        }
    }


    public static String takeScreenShot(WebDriver driver,String screenShotName,ExtentTest logger) {
        String dateTimeString = new SimpleDateFormat("MMddhhmmss").format(new Date());
        try {
            String screenShotPath = ReporterPrerequisite.reportPath + "/screenshot/" + screenShotName + dateTimeString + ".jpg";
            logger.log(Status.INFO,"The URL of the Page :: <b>"+driver.getCurrentUrl()+"</b>");
            new File(reportPath + "screenshot").mkdir();
            FileUtils.copyFile(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE), new File(screenShotPath));
            final BufferedImage image = ImageIO.read(new URL("file:///" + screenShotPath));

            Graphics g = image.getGraphics();
            // g.setFont(g.getFont().deriveFont(30f));
            g.setFont(new Font("default", Font.BOLD, 30));
            g.setColor(Color.red);
            g.drawString(driver.getTitle() + " :: " + driver.getCurrentUrl(), 50, 50);
            g.dispose();
            ImageIO.write(image, "png", new File(screenShotPath));
        } catch (IOException e) {
            logger.log(Status.FAIL, "Unable to Take Screenshot!!</br>" + e.getMessage());
        } catch (Exception e) {
            logger.log(Status.FAIL, "Unable to Take Screenshot!!</br>" + e.getMessage());
        }
        return "screenshot\\" + screenShotName + dateTimeString + ".jpg";
    }

    public static String addDaysToCurrentDate(int numberOfDays) {
        SimpleDateFormat dateformat = new SimpleDateFormat("MMMM dd,yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date()); // Now use today date.
        cal.add(Calendar.DATE, numberOfDays);
        System.out.println(dateformat.format(cal.getTime()));// Adding # of days
        return dateformat.format(cal.getTime());
    }



}
