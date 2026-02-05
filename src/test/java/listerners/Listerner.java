package listerners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import base.DriverSetup;
//import utils.ScreenshotUtils;
import utils.Screenshots;

public class Listerner extends DriverSetup implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getName();
        Screenshots.takeScreenshot(driver, testName);
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("TEST STARTED: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("TEST PASSED: " + result.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("TEST SUITE FINISHED");
    }
}
