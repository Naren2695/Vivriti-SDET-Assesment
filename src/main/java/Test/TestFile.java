package Test;

import Pages.Application;
import Utilites.ReporterPrerequisite;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.annotations.Test;


public class TestFile extends ReporterPrerequisite {

    public static ExtentReports reporter;
    public static ExtentTest logger;


    public TestFile() {
    }


    @Test
    public void HappyPath() {

        try {
            logger = Reporter.createTest("Demo Test");
            driver.get("https://www.starofservice.in/dir/telangana/hyderabad/hyderabad/plumbing#_");
            Application application = new Application(driver, logger);
            application.submitApplication(prop);
        } catch (Exception e) {
            logger.log(Status.FAIL, "Error is  " + e + " ########");
        }
    }
}
