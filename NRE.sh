#!/bin/bash

INSTALL_DIR="/usr/local/bin"
PROJECT_FOLDER=${PWD}

#INSTALLING CHROMEDRIVER AND GECKODRIVER

if [ $(uname) == "Darwin" ]; then
DRIVER_FOLDER="${PROJECT_FOLDER}/drivers/macos/" ;
elif [ $(uname) == "Linux" ]; then
DRIVER_FOLDER="${PROJECT_FOLDER}/drivers/linux/" ;        
else
   exit 0
fi &&

sudo cp "${DRIVER_FOLDER}chromedriver" "$INSTALL_DIR"
sudo cp "${DRIVER_FOLDER}geckodriver" "$INSTALL_DIR"

sudo chmod +x "${INSTALL_DIR}/chromedriver"
sudo chmod +x "${INSTALL_DIR}/geckodriver"
##################################################################


#ADDING 'nre' TO USER'S PATH
dir=/usr/local/bin/;

sudo cat << EOF > ${dir}nre 

#!/bin/bash
cd "$PROJECT_FOLDER"

xvfb-run java -cp ./bin:./lib/*:./lib/Groovy/*:./lib/Cucumber/*:./lib/ExtentReporter/* com.main.Main \${@}
EOF
sudo chmod +x ${dir}nre;
#####################################################################
