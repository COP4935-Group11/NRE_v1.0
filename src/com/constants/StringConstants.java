package com.constants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.configuration.RunConfiguration;
import com.configuration.RunConfiguration.OSType;


public class StringConstants {
	
	//Text color
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String BLACK_TEXT = "\u001B[30m";
	public static final String RED_TEXT = "\u001B[31m";
	public static final String GREEN_TEXT = "\u001B[32m";
	public static final String YELLOW_TEXT = "\u001B[33m";
	public static final String BLUE_TEXT = "\u001B[34m";
	public static final String PURPLE_TEXT = "\u001B[35m";
	public static final String CYAN_TEXT = "\u001B[36m";
	public static final String WHITE_TEXT = "\u001B[37m";
	
    // Bold
    public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
    public static final String RED_BOLD = "\033[1;31m";    // RED
    public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
    public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
    public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
    public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
    public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
    public static final String WHITE_BOLD = "\033[1;37m";  // WHITE
    
    // Underline
    public static final String BLACK_UNDERLINED = "\033[4;30m";  // BLACK
    public static final String RED_UNDERLINED = "\033[4;31m";    // RED
    public static final String GREEN_UNDERLINED = "\033[4;32m";  // GREEN
    public static final String YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
    public static final String BLUE_UNDERLINED = "\033[4;34m";   // BLUE
    public static final String PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
    public static final String CYAN_UNDERLINED = "\033[4;36m";   // CYAN
    public static final String WHITE_UNDERLINED = "\033[4;37m";  // WHITE
    
	//Background
	public static final String BLACK_BACKGROUND = "\u001B[40m";
	public static final String RED_BACKGROUND = "\u001B[41m";
	public static final String GREEN_BACKGROUND = "\u001B[42m";
	public static final String YELLOW_BACKGROUND = "\u001B[43m";
	public static final String BLUE_BACKGROUND = "\u001B[44m";
	public static final String PURPLE_BACKGROUND = "\u001B[45m";
	public static final String CYAN_BACKGROUND = "\u001B[46m";
	public static final String WHITE_BACKGROUND = "\u001B[47m";
	
    // Bold High Intensity
    public static final String BLACK_BOLD_BRIGHT = "\033[1;90m";
    public static final String RED_BOLD_BRIGHT = "\033[1;91m";
    public static final String GREEN_BOLD_BRIGHT = "\033[1;92m";
    public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";
    public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";
    public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";
    public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";
    public static final String WHITE_BOLD_BRIGHT = "\033[1;97m";
    
    public static String CLEAR_SCAPE_CHAIN = "\033[H\033[2J";
    public static String NEW_LINE = "\n";
    public static String UNDERSCORE = "_";
	public static String COLON = " : ";
	public static String HYPHEN = " - ";
	public static Charset STANDARD_CHARSET = StandardCharsets.UTF_8;
    
	public static String ID_SEPARATOR = getSeparator();
	public static String ROOT_DIR = System.getProperty("user.dir")+ID_SEPARATOR;
	public static String HOME_DIR = System.getProperty("user.home")+ID_SEPARATOR;
	public static String PROFILE_FOLDER = "Profiles";
	public static String KEYWORDS_FOLDER = "Keywords";
	public static String DEFAULT_REPORT_DIR = "Reports";
	public static String LOGS_DIR = "Logs";
	public static String SCREENSHOT_FOLDER = "Screenshots";
	public static String DEFAULT_PROFILE = "default";
	public static String DEFAULT_BROWSER = "chrome(headless)";
		
	public static String BIN_FOLDER = "bin";
	public static String COMPILED_STEPS_FOLDER = "bin"+ID_SEPARATOR+"steps";
	public static String COMPILED_INTERNAL_FOLDER = "bin"+ID_SEPARATOR+"internal";
	public static String DEFAULT_SCRIPTS_PACKAGE = "package steps";
	//public static String COMPILED_TEST_SUITE_FOLDER = "suites";
		
	public static String DEFAULT_TEST_SUITES_FOLDER = "Test Suites";
	
	public static String FEATURES_SOURCE = "Include"+ID_SEPARATOR+"features";
	public static String DATA_FILES_SOURCE = "Data Files";
	public static String SCRIPTS_SOURCE = ID_SEPARATOR+"Include"+ID_SEPARATOR+"scripts";
	public static String TESTCASE_SCRIPTS_SOURCE = ID_SEPARATOR+"Scripts";
	
	public static String GROOVY_EXT = ".groovy";
	public static String XML_EXT = ".xml";
	public static String PROFILE_EXT = ".glbl";
	public static String DATA_FILES_EXT = ".dat";
	public static String OBJECTS_EXT = ".rs";
	public static String TEST_CASES_EXT = ".tc";
	public static String TEST_SUITES_EXT = ".ts";
	public static String JSON_EXT = ".json";
	public static String HTML_EXT = ".html";
	public static String PNG_EXT = ".png";
	
	public static String CUSTOM_ATTRIBUTES_FILE = "DS_Store";
	public static String GHERKIN_LANGUAGE = "en";
	public static String OS = "OS";
	public static String RUNNING_MODE = "RUNNING MODE";
	public static String SEQUENTIAL_KEYWORD = "SEQUENTIAL";
	public static String PARALLEL_KEYWORD = "PARALLEL";
	public static String PASS_LOG = "PASSED";
	public static String PARSING_PHASE_MESSAGE = "Parsing files";
	public static String SETTING_SUITE_MESSAGE = "Initializing Suite";
	
	public static String TEMP_FOLDER = "temp";
	public static String TEMP_INTERNAL_FOLDER = "internal";
	public static String EXTENT_XML_CONFIG_FILE = "lib" + ID_SEPARATOR + "extent_config.xml";
	public static String README_FILE = "lib" + ID_SEPARATOR + "readme.txt";
	public static String SCREENSHOT_PREFIX = "failure" + UNDERSCORE;
	public static String FEATURES_FOLDER = "temp"+ID_SEPARATOR+"features";
	public static String SCRIPTS_FOLDER = "temp"+ID_SEPARATOR+"scripts";
	public static String TESTCASE_SCRIPTS_FOLDER = "temp"+ID_SEPARATOR+"Scripts";
	public static String SUITE_FOLDER = "temp"+ID_SEPARATOR+"suite";
			
	public static String TEST_SUITES_REPORT_TAG = "Test Suites";
	public static String TEST_CASES_REPORT_TAG = "Test Cases";
	public static String HOOKS_REPORT_TAG = "Hooks";
	public static String FEATURES_REPORT_TAG = "Features";
	public static String SCENARIOS_REPORT_TAG = "Scenarios";
	public static String STEPS_REPORT_TAG = "Steps";
		
	public static String[] IMPORTS = {"import com.ucf.pcte.CucumberKW as CucumberKW", 
			"import com.ucf.pcte.gold.WebUI as WebUI", 
			"import static com.ucf.pcte.gold.WebUI.findTestObject",
			"import cucumber.api.java.en.*",
			"import com.ucf.pcte.CSVData",
			"import com.constants.CSVSeparator",
			"import com.configuration.RunConfiguration",
			"import com.constants.*",
			"import com.auxiliary.WebUiCommonHelper",
			"import com.auxiliary.ConditionType",
			"import com.auxiliary.KeywordUtil",
			"import com.ucf.pcte.gold.TestObject",
			"import com.ucf.pcte.gold.FailureHandling",
			"import edu.ucf.irl.cryptographer.Cryptographer",
			"import com.warrenstrange.googleauth.GoogleAuthenticator",
			"import com.warrenstrange.googleauth.GoogleAuthenticatorConfig",
			"import com.warrenstrange.googleauth.GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder",
			"import com.warrenstrange.googleauth.HmacHashFunction",
			"import java.awt.Robot",
			"import java.awt.Toolkit",
			"import java.awt.datatransfer.StringSelection",
			"import java.awt.event.KeyEvent",
			"import com.annotation.SetUp",
			"import com.annotation.TearDown",
			"import com.annotation.SetupTestCase",
			"import com.annotation.TearDownTestCase",
			};
	
	public static String[] SUITE_IMPORTS = {
			"import com.ucf.pcte.CucumberKW as CucumberKW", 
			"import com.ucf.pcte.gold.WebUI as WebUI", 
			"import static com.ucf.pcte.gold.WebUI.findTestObject",
			"import cucumber.api.java.en.*",
			"import com.ucf.pcte.CSVData",
			"import com.constants.CSVSeparator",
			"import com.configuration.RunConfiguration",
			"import com.constants.*",
			"import com.auxiliary.WebUiCommonHelper",
			"import com.auxiliary.ConditionType",
			"import com.auxiliary.KeywordUtil",
			"import com.ucf.pcte.gold.TestObject",
			"import com.ucf.pcte.gold.FailureHandling",
			"import edu.ucf.irl.cryptographer.Cryptographer",
			"import com.warrenstrange.googleauth.GoogleAuthenticator",
			"import com.warrenstrange.googleauth.GoogleAuthenticatorConfig",
			"import com.warrenstrange.googleauth.GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder",
			"import com.warrenstrange.googleauth.HmacHashFunction",
			"import java.awt.Robot",
			"import java.awt.Toolkit",
			"import java.awt.datatransfer.StringSelection",
			"import java.awt.event.KeyEvent",
			"import com.annotation.SetUp",
			"import com.annotation.TearDown",
			"import com.annotation.SetupTestCase",
			"import com.annotation.TearDownTestCase",
			};

		public static String[] KEYWORD_IMPORTS = {
			"import com.annotation.Keyword", 
			"import com.ucf.pcte.CSVData", 
			"import com.auxiliary.ConditionType",
			"import com.ucf.pcte.gold.TestObject",
			"import com.auxiliary.WebUiCommonHelper",
			"import com.auxiliary.KeywordUtil",
			"import com.ucf.pcte.gold.WebUI as WebUI",
			"import static com.ucf.pcte.gold.WebUI.findTestObject",
			"import com.driver.DriverFactory"
			};

	protected static final String getSeparator() {
		
		if(RunConfiguration.getPlatform().compareTo(OSType.WINDOWS) == 0)
			return new String("\\");
		else
			return new String("/");	
	}
	

}
