-->Description
NRE is a runtime engine designed to execute test suites created in Katalon Studio. This software is intended to be used by Dr. Glenn Martin and Dr. Bruce Caulkins at
the University of Central Florida and their respective teams.

-->Installation
Step to using project ZIP: 1. Unzip project inside desired NRE folder 2. Open a terminal and navigate to the unzipped NRE folder 3. type "chmod +x NRE.sh" 4. type
"sudo bash NRE.sh"
These two commands will allow the user to type 'NRE' from anywhere in their terminal to access the software.
Note: If the unzipped NRE folder is moved after typing the two commands then the 'nre' command will not work from the command line. The user must repeat the
steps listed from 2 - 4 from the new folder location.

-->Usage and Parameters
After following steps 1 - 4 in the 'Installation' section the user will be able to type 'nre' from their anywhere in their terminal to access the software.
To execute a test suite the user will enter the following information: 1. projectPath 2. testSuitePath 3. executionProfile 4. browserType

-->Example:
-projectPath=/media/psf/Home/Desktop/Projects/new_prj/PCTE -testSuitePath=Test Suites/9001-9500 Smoke/S9001 All Smoke Tests -browserType=firefox executionProfile=Authentication

-->Project Path:
The project path is the path to the Katalon Studio project where all of the files required to execute a test suite are stored.
The project path is an absolute path.

-->Test Suite Path:
The test suite path is the relative path containing the test suite files.
The test suite path is a relative path from the project path.

-->Execution Profile
The execution profile is the name of the profile selected to run with the test suite.

-->Browser Type
The browser type specifies which browser to execute the tests in along with determing if the program should run in a headless state.

-->Output
After a test suite has been run a cucumber report will be generated detailing the number of successful and unsuccessful test cases.
If a test case is unsuccessful there will be additional information detailing why that test case failed.
This report can be accessed under the "testOutput" folder.

-->Support
This project will not receive updates following May 2021.

-->Developers
Ben, Scott, Tomer, David, Sebastian, Richard, Sean, Wanda, Triet

