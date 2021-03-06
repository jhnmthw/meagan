package com.pheonix.nfr.meagan.selenium;

import com.pheonix.nfr.meagan.selenium.config.DriverFactory;
import com.pheonix.nfr.meagan.selenium.listeners.ScreenshotListener;

import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Listeners(ScreenshotListener.class)
public class DriverBase {

    private static List<DriverFactory> webDriverThreadPool = Collections.synchronizedList(new ArrayList<DriverFactory>());
    private static ThreadLocal<DriverFactory> driverFactory;

    @BeforeSuite(alwaysRun = true)
    public static void instantiateDriverObject() {
        driverFactory = new ThreadLocal<DriverFactory>() {
            @Override
            protected DriverFactory initialValue() {
                DriverFactory driverFactory = new DriverFactory();
                webDriverThreadPool.add(driverFactory);
                return driverFactory;
            }
        };
    }

    public static WebDriver getDriver() throws Exception {
        return driverFactory.get().getDriver();
    }

    @AfterMethod(alwaysRun = true)
    public static void clearCookies() throws Exception {
    	try{
        getDriver().manage().deleteAllCookies();
    	}catch(WebDriverException e){
    		System.out.println("*************Broser is closed******************");
    		System.out.println("Continuing.....");
    	}
    }

    @AfterSuite(alwaysRun = true)
    public static void closeDriverObjects() {
        for (DriverFactory driverFactory : webDriverThreadPool) {
            driverFactory.quitDriver();
        }
    }
}