package lambdatest;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;
import org.testng.annotations.*;
import java.util.HashMap;
import java.net.URL;
import java.net.MalformedURLException;


public class TestLambda {
	  WebDriver driver;

	    @BeforeClass
	    public void setUp() throws Exception {
	        String username = "YOUR_USERNAME";
	        String accessKey = "YOUR_ACCESS_KEY";

	        HashMap<String, Object> ltOptions = new HashMap<String, Object>();
	        ltOptions.put("build", "Selenium 4 Advanced Project");
	        ltOptions.put("name", "LambdaTest Demo");
	        ltOptions.put("platformName", "Windows 10");
	        ltOptions.put("browserName", "Chrome");
	        ltOptions.put("browserVersion", "latest");
	        ltOptions.put("network", true);    //  capture network logs
	        ltOptions.put("video", true);      //  record video
	        ltOptions.put("console", "true");  // capture console logs
	        ltOptions.put("visual", true);     //  screenshots

	        DesiredCapabilities caps = new DesiredCapabilities();
	        caps.setCapability("LT:Options", ltOptions);
	        try {
	            driver = new RemoteWebDriver(new URL("https://" + username + ":" + accessKey + "@hub.lambdatest.com/wd/hub"), caps);
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } 

	    }

	    @Test
	    public void testExample() {
	        driver.get("https://www.lambdatest.com");
	        String title = driver.getTitle();
	        System.out.println("Title is: " + title);
	        assert title.contains("LambdaTest");
	    }

	    @AfterClass
	    public void tearDown() {
	        if (driver != null) driver.quit();
	    }
}
