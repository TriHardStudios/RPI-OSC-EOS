#!/bin/bash
printf "RPi-EOS Repair\n"
printf "Please password authinicate\n"
[ "$UID" -eq 0 ] || exec sudo bash "$0" "$@"
printf "Starting repair...\n"

MAC=""
IP=""
USRIN=""
cd /usr/trihardstudios/
printf "\n\nChecking for files...\n"
if [ -e "trihardstudios-rpi-eos-osc.jar" ]
	then
		printf "Main jar OK\n"
	else
		printf "Main jar not found. Reinstall program to fix\n"
##		exit
fi
cd support
if [-e mac.thd]
	then
		printf "Remote startup file OK. \nDo you want to rebuild the remote startup file?(y/n)"
		read USRIN
		if [USRIN == "y" || USRIN == "Y"]
			then
				printf "Enter the MAC address of the target console: "
				read MAC
				printf "Enter the broadcast IP of the router that the console is connected to: "
				read IP
				printf "MAC address: $MAC IP address: $IP\n"
				rm mac.thd
				printf "$MAC"
				printf "\n$IP"
			else
				printf "Remote startup file not found. Recreating now...\n"
				printf "Enter the MAC address of the target console: "
				read MAC
				printf "Enter the broadcast IP of the router that the console is connected to: "
				read IP
				printf "MAC address: $MAC IP address: $IP\n"
				rm mac.thd
				printf "$MAC"
				printf "\n$IP"
fi
if [java --version &>/dev/null] 
	then
		printf "Java is installed\n"
		java --version
	else 
		printf "Java is not installed. Run java8-javaFX-install.sh to fix\n"
##		exit
fi
exit;


