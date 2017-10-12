Step 1:
	Format your three partitions according to the directions in the file. THIS MAY OVERWRITE YOUR CURRENT PARTITIONS!!

Step 2: Download the install file
	
```bash
	curl http://raw.githubusercontent.com/treewolf/Misc/master/ArchLinux/normal.sh > install.sh
	chmod +x install.sh
```
OR
```bash
	curl http://raw.githubusercontent.com/treewolf/Misc/master/ArchLinux/encrypted.sh > install.sh
	chmod +x install.sh
```
Step 3: Fill in the necessary variables in the top of the file

Step 4: Run
```bash
	./install.sh {1,2,3}
```
