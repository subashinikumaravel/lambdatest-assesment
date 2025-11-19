package com.lambdatest.assesment;

import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChromeTest extends BaseTest {
	@Test
	public void testScenarioChrome() {
		try {

			driver.get("https://www.lambdatest.com");
			waitForPageLoad();

			By exploreIntegrationsXPath = By.xpath(
					"//*[contains(text(),'Explore all Integrations') or contains(.,'Explore all Integrations')]");
			WebElement exploreEl = waitForElement(exploreIntegrationsXPath);
			((JavascriptExecutor) driver)
					.executeScript("arguments[0].scrollIntoView({behavior:'smooth', block:'center'});", exploreEl);

			exploreEl.click();

			Set<String> handles = driver.getWindowHandles();
			List<String> handlesList = new ArrayList<>(handles);
			System.out.println("Window Handles after click (count=" + handlesList.size() + "):");
			handlesList.forEach(System.out::println);

			Assert.assertTrue(handlesList.size() >= 2,
					"Expected new tab to open after clicking Explore all Integrations");

			String originalWindow = driver.getWindowHandle();
			String newWindow = null;
			for (String h : handlesList) {
				if (!h.equals(originalWindow)) {
					newWindow = h;
					break;
				}
			}
			driver.switchTo().window(newWindow);
			waitForPageLoad();

			String currentUrl = driver.getCurrentUrl();
			System.out.println("New tab URL: " + currentUrl);
			Assert.assertTrue(
					currentUrl.toLowerCase().contains("integrations")
							|| currentUrl.toLowerCase().contains("/integrations"),
					"Expected integrations page in new tab. Actual: " + currentUrl);

			By codelessXpath = By.xpath("//*[contains(text(),'Codeless Automation')]");
			WebElement codelessEl = waitForElement(codelessXpath);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", codelessEl);

			By testingWhizLink = By.partialLinkText("TestingWhiz");
			WebElement twLink = waitForElement(testingWhizLink);

			twLink.click();
			waitForPageLoad();

			String expectedTitle = "TestingWhiz Integration With LambdaTest";
			String actualTitle = driver.getTitle().trim();
			System.out.println("TestingWhiz page title: " + actualTitle);
			Assert.assertEquals(actualTitle, expectedTitle, "Page title mismatch for TestingWhiz integration page.");

			driver.close();

			driver.switchTo().window(originalWindow);
			Set<String> handlesAfterClose = driver.getWindowHandles();
			System.out.println("Window count after closing testingWhiz window: " + handlesAfterClose.size());

			driver.get("https://www.lambdatest.com/blog");
			waitForPageLoad();

			By communityLink = By.linkText("Community");
			WebElement commEl = waitForElement(communityLink);
			commEl.click();

			Set<String> finalHandles = driver.getWindowHandles();
			List<String> finalHandlesList = new ArrayList<>(finalHandles);

			String current = driver.getWindowHandle();
			if (finalHandlesList.size() > 1) {
				for (String h : finalHandlesList) {
					if (!h.equals(originalWindow)) {
						driver.switchTo().window(h);
						break;
					}
				}
			}

			waitForPageLoad();
			String communityUrl = driver.getCurrentUrl();
			System.out.println("Community URL: " + communityUrl);

			Assert.assertTrue(communityUrl.startsWith("https://community.lambdatest.com"),
					"Community URL mismatch. Actual: " + communityUrl);

			markTestStatus("passed", "Test completed successfully");
		} catch (Throwable t) {
			markTestStatus("failed", t.getMessage());
			Assert.fail("Test failed with exception: " + t.getMessage(), t);
		}
	}
}
