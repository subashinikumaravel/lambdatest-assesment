package com.lambdatest.assesment;

import org.testng.annotations.Test;

public class EdgeTest extends BaseTest {

    @Test
    public void testScenarioEdge() {

        new ChromeTest().testScenarioChrome();
    }
}
