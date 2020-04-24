package Utilites;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class ReporterPrerequisite {
    public ExtentReports Reporter;
    public static String testName, browserToRun, reportPath, ApplicationVersion;
    final static String dateTimeString = new SimpleDateFormat("MMddhhmm").format(new Date());
    public WebDriver driver;
    public Properties prop = new Properties();



    @Parameters({"browser", "AppVersion", "BatchInfo", "APPName"})
    @BeforeClass
    public void startReporter(final ITestContext testContext,@Optional("chrome") String browser,  @Optional("") String AppVersion,@Optional("") String BatchInfo,@Optional("") String APPName ) {
        testName = testContext.getSuite().getName();
        browserToRun = browser;
        reportPath = System.getProperty("user.dir") + "/Reports/" + dateTimeString + "-" + testName + "-" + browserToRun;
        File file = new File(reportPath);
        file.mkdirs();
        Reporter = createReporter(reportPath);
        driver = Utils.getDriver(browserToRun);
    }


    @BeforeMethod
    public void LoadProperty() {
        try {
            prop.load(this.getClass().getClassLoader().getResourceAsStream("Application.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @AfterMethod
    public void FreeResource() {
        Reporter.flush();
    }

    @AfterClass
    public void CloseDriver() {
        driver.quit();
    }


    public static ExtentReports createReporter(String Path) {
        ExtentReports Reporter = null;
        try {
            reportPath = Path;
            Reporter = new ExtentReports();
            ExtentSparkReporter reportObj = new ExtentSparkReporter(Path);
            reportObj.config().setReportName("Execution result - Demo");
            reportObj.config().setDocumentTitle("Automation Test Report");
            Reporter.attachReporter(reportObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Reporter;
    }
}
