Step 1:
	Format your three partitions
	```
	curl http://raw.githubusercontent.com/treewolf/Misc/master/ArchLinux/install_1.sh > install_1.sh
	chmod +x install_1.sh
	./install_1.sh
	```

Step 2:
	Make sure you are inside the arch-chroot
	```
	curl http://raw.githubusercontent.com/treewolf/Misc/master/ArchLinux/install_2.sh > install_2.sh
	chmod +x install_2.sh
	./install_2.sh
	```
	When the script completes, remove the live usb and reboot

*May not be complete
Step 3:
	Optional, automatically sets up services
	```
	curl http://raw.githubusercontent.com/treewolf/Misc/master/ArchLinux/install_13.sh > install_3.sh
	chmod +x install_3.sh
	./install_3.sh
	```
