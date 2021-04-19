package com.driver;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

import com.configuration.RunConfiguration;
import com.configuration.RunConfiguration.OSType;
import com.constants.StringConstants;
import com.exception.StepFailedException;

public class Driver {
	
	protected SessionId sessionid;
	protected WebDriver driver;
	protected Boolean closed;
	
	public Driver() {this(StringConstants.DEFAULT_BROWSER);}

	public Driver(String browser){
		
		
		
		switch(browser.toUpperCase()) {
		case  "CHROME"				:	this.driver = createChromeDriver(false);
										this.sessionid = ((RemoteWebDriver) this.driver).getSessionId();	
										this.closed = false;
										break;
		case  "CHROME(HEADLESS)"	:	this.driver = createChromeDriver(true);
										this.sessionid = ((RemoteWebDriver) this.driver).getSessionId();	
										this.closed = false;
										break;
		case "FIREFOX(HEADLESS)"	:	this.driver = createFirefoxDriver(true);
										this.sessionid = ((RemoteWebDriver) this.driver).getSessionId();
										this.closed = false;
										break;
		case "FIREFOX"				:	this.driver = createFirefoxDriver(false);
										this.sessionid = ((RemoteWebDriver) this.driver).getSessionId();
										this.closed = false;
										break;
		case "EDGE"					:	this.driver = createEdgeDriver();
										this.sessionid = ((RemoteWebDriver) this.driver).getSessionId();
										this.closed = false;
										break;
		case "OPERA"				:	this.driver = createOperaDriver();
										this.sessionid = ((RemoteWebDriver) this.driver).getSessionId();
										this.closed = false;
										break;
		case "IE"					:	this.driver = createIEDriver();
										this.sessionid = ((RemoteWebDriver) this.driver).getSessionId();
										this.closed = false;
										break;
		default		:	this.driver = createChromeDriver(true);
						this.sessionid = ((RemoteWebDriver) this.driver).getSessionId();
						this.closed = false;
						break;
		}
	}
	
	
	protected FirefoxDriver createFirefoxDriver(Boolean headless) {
		
		if(RunConfiguration.getPlatform().equals(OSType.WINDOWS))	
    		System.setProperty("webdriver.gecko.driver", StringConstants.ROOT_DIR+"drivers"+StringConstants.ID_SEPARATOR+
					"windows"+StringConstants.ID_SEPARATOR+"geckodriver.exe");
    	
    	FirefoxOptions options = new FirefoxOptions();
		options.addArguments("--disable-web-security","--allow-running-insecure-content","--ignore-certificate-errors");
    	if(headless)
    		options.addArguments("--headless", "disable-gpu");
		
		return new FirefoxDriver(options);
	}
	
	protected ChromeDriver createChromeDriver(Boolean headless) {
		
		if(RunConfiguration.getPlatform().equals(OSType.WINDOWS))	
    		System.setProperty("webdriver.chrome.driver", StringConstants.ROOT_DIR+"drivers"+StringConstants.ID_SEPARATOR+
    							"windows"+StringConstants.ID_SEPARATOR+"chromedriver.exe");

    	
    	ChromeOptions options = new ChromeOptions();
    	
    		options.addArguments("--disable-web-security","--allow-running-insecure-content","--ignore-certificate-errors");
    		if(headless)
    			options.addArguments("--headless", "disable-gpu");
        return new ChromeDriver(options);
	}
	
	private EdgeDriver createEdgeDriver() {
		
		if(RunConfiguration.getPlatform().equals(OSType.WINDOWS))	
    		System.setProperty("webdriver.edge.driver", StringConstants.ROOT_DIR+"drivers"+StringConstants.ID_SEPARATOR+
    							"windows"+StringConstants.ID_SEPARATOR+"msedgedriver.exe");
		else if(RunConfiguration.getPlatform().equals(OSType.LINUX))
			System.setProperty("webdriver.edge.driver", StringConstants.ROOT_DIR+"drivers"+StringConstants.ID_SEPARATOR+
					"linux"+StringConstants.ID_SEPARATOR+"msedgedriver");
		else
			System.setProperty("webdriver.edge.driver", StringConstants.ROOT_DIR+"drivers"+StringConstants.ID_SEPARATOR+
					"macos"+StringConstants.ID_SEPARATOR+"msedgedriver");
    	
        return new EdgeDriver();
	}
		
	protected OperaDriver createOperaDriver(){
		
		if(RunConfiguration.getPlatform().equals(OSType.WINDOWS))	
    		System.setProperty("webdriver.opera.driver", StringConstants.ROOT_DIR+"drivers"+StringConstants.ID_SEPARATOR+
    							"windows"+StringConstants.ID_SEPARATOR+"operadriver.exe");
		else if(RunConfiguration.getPlatform().equals(OSType.LINUX))
			System.setProperty("webdriver.opera.driver", StringConstants.ROOT_DIR+"drivers"+StringConstants.ID_SEPARATOR+
					"linux"+StringConstants.ID_SEPARATOR+"operadriver");
		else
			System.setProperty("webdriver.opera.driver", StringConstants.ROOT_DIR+"drivers"+StringConstants.ID_SEPARATOR+
					"macos"+StringConstants.ID_SEPARATOR+"operadriver");
		
		return new OperaDriver();
	}
	
	
	@SuppressWarnings("deprecation")
	protected InternetExplorerDriver createIEDriver(){
		if(RunConfiguration.getPlatform().equals(OSType.WINDOWS))	
    		System.setProperty("webdriver.ie.driver", StringConstants.ROOT_DIR+"drivers"+StringConstants.ID_SEPARATOR+
    							"windows"+StringConstants.ID_SEPARATOR+"IEDriverServer.exe");
		else
			throw new StepFailedException("Internet Explorer Driver is only available for Windows OS!");
		DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
		capabilities.setCapability (InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);

		return new InternetExplorerDriver(capabilities);
	}
}