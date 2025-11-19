package com.lambdatest.assesment;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Set;

public class BaseTest {
	protected RemoteWebDriver driver;
	protected String username;
	protected String accessKey;
	protected static final Duration TIMEOUT = Duration.ofSeconds(20);

	@BeforeClass(alwaysRun = true)
	@Parameters({ "browserName", "browserVersion", "platformName", "testName" })
	public void setUp(@Optional("Chrome") String browserName, @Optional("128.0") String browserVersion,
			@Optional("Windows 10") String platformName, @Optional("LambdaTest - Selenium Advanced") String testName)
			throws MalformedURLException {

		username = System.getenv("LT_USERNAME");
		accessKey = System.getenv("LT_ACCESS_KEY");
		if (username == null || accessKey == null) {
			throw new IllegalStateException("Please set LT_USERNAME and LT_ACCESS_KEY as environment variables.");
		}

		org.openqa.selenium.MutableCapabilities ltOptions = new org.openqa.selenium.MutableCapabilities();
		ltOptions.setCapability("user", username);
		ltOptions.setCapability("accessKey", accessKey);
		ltOptions.setCapability("build", "Selenium-Advanced-Assignment");
		ltOptions.setCapability("name", testName);
		ltOptions.setCapability("network", true); // enable network logs
		ltOptions.setCapability("visual", true);
		ltOptions.setCapability("video", true); // enable video
		ltOptions.setCapability("console", true); // enable console logs
		ltOptions.setCapability("selenium_version", "4.11.0");

		org.openqa.selenium.MutableCapabilities capabilities = new org.openqa.selenium.MutableCapabilities();
		capabilities.setCapability("browserName", browserName);
		capabilities.setCapability("browserVersion", browserVersion);
		capabilities.setCapability("platformName", platformName);
		capabilities.setCapability("LT:Options", ltOptions);

		URL hub = new URL("https://hub.lambdatest.com/wd/hub");

		driver = new RemoteWebDriver(hub, capabilities);
		driver.manage().timeouts().pageLoadTimeout(TIMEOUT);
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

	protected void waitForPageLoad() {
		WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
		wait.until((ExpectedCondition<Boolean>) wd -> ((JavascriptExecutor) wd)
				.executeScript("return document.readyState").equals("complete"));
	}

	protected WebElement waitForElement(By locator) {
		WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
		return wait.until(driver -> driver.findElement(locator));
	}

	protected void markTestStatus(String status, String reason) {
		try {
			((JavascriptExecutor) driver).executeScript("lambda-status=" + status + ";");
		} catch (Exception ignored) {
		}
	}
}
