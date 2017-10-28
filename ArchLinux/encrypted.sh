# Connect pendrive, keyboard, ethernet
# Boot up from pendrive

# cfdisk
#	- clear all partitions
#	- /dev/sda1 = /boot partition type 8300 with x>=100M
#	- /dev/sda2 = partition with type 8E00 that will be encrypted and contain /home /root and /swap

# ALL GLOBAL VARIABLES BE SET HERE

# Encrypted volume name
VOLUME=

# Encrypted volume sizes
SIZE_ROOT=
SIZE_SWAP=

# uuid of the root partition
# run 'blkid' and look for the root
ROOT_UUID=

# Hostname of computer system
HOSTNAME=

# User name of first non-root user
USERNAME=

# Set to 1 if installing box with touchpad, 0 if not
IS_LAPTOP=

##### MAKE SURE ABOVE STEPS ARE COMPLETED ######

if [ ${1} -eq "1" ]; then 
	cryptsetup luksFormat /dev/sda2
	cryptsetup open /dev/sda2 cryptolvm
	pvcreate /dev/mapper/cryptolvm
	vgcreate ${VOLUME} /dev/mapper/cryptolvm
	lvcreate -L ${SIZE_SWAP} ${VOLUME} -n swap
	lvcreate -L ${SIZE_ROOT} ${VOLUME} -n root
	lvcreate -l 100%FREE ${VOLUME} -n home
	mkfs.ext4 /dev/mapper/${VOLUME}-root 
	mkfs.ext4 /dev/mapper/${VOLUME}-home 
	mkswap /dev/mapper/${VOLUME}-swap
	mount /dev/mapper/${VOLUME}-root /mnt
	mkdir /mnt/home
	mount /dev/mapper/${VOLUME}-home /mnt/home
	swapon /dev/mapper/${VOLUME}-swap
	mkfs.ext2 /dev/sda1
	mkdir /mnt/boot
	mount /dev/sda1 /mnt/boot
	pacstrap -i /mnt base base-devel
	genfstab -U -p /mnt >> /mnt/etc/fstab
	arch-chroot /mnt /bin/bash
elif [ ${2} -eq "2" ]; then
	echo "Part 2"; sleep 3
	# add hostname
	echo "${HOSTNAME}" > /etc/hostname
	# Change /etc/locale.gen 
	#	- uncomment en_US.UTF-8 
	#	- default using the time of Los_Angeles
	sed -i -e 's/#en_US.UTF-8/en_US.UTF-8/g' /etc/locale.gen
	locale-gen
	echo LANG=en_US.UTF-8 > /etc/locale.conf
	export LANG=en_US.UTF-8
	#in case localtime file exists (usually when rebuilding from arch)
	rm /etc/localtime
	ln -s /usr/share/zoneinfo/America/Los_Angeles /etc/localtime
	hwclock --systohc --utc
	
	sed -i -e '/^HOOKS/c/HOOKS="base udev resume autodetect modconf keyboard block lvm2 encrypt filesystems fsck"' /etc/mkinitcpio.conf
	mkinitcpio -p linux

	#nano /etc/pacman.conf

	pacman -Syu --noconfirm
	echo "Set root password: "
	passwd
	useradd -mg users -G wheel,storage,power -s /bin/bash ${USERNAME}
	echo "Set ${USERNAME} password: "
	passwd ${USERNAME}
	pacman -S --noconfirm sudo
	
	# sudo visudo manually if script throws error
	#sudo visudo
	# Defaults:ALL timestamp_timeout=0

	pacman -S --noconfirm grub
        echo 'GRUB_PRELOAD_MODULES="lvm"' >> /etc/default/grub
        cryptdevice=/dev/${VOLUME}/root:${VOLUME}-root
        root=/dev/mapper/${VOLUME}-root
        swap=/dev/mapper/${VOLUME}-swap
	grub-install /dev/sda
	
	pacman -S --noconfirm os-prober
	grub-mkconfig -o /boot/grub/grub.cfg
	pacman -S --noconfirm iw wpa_supplicant connman
	systemctl enable connman
	pacman -S --noconfirm xorg xorg-apps xorg-server mesa
	# volume
	pacman -S --noconfirm alsa-utils

	# touchpad
	case "$IS_LAPTOP" in
		1)
			pacman -S --noconfirm xf86-input-synaptics;;
		0)
			echo "Options read no touchpad: pacman -S xf86-input-synaptics if error";;
	esac

	echo "DONE INSTALLATION. AFTER REBOOT READ 'post installation' SECTION"
	echo "Before rebooting, type \n./"${0}" 3" 
	exit
elif [ ${3} -eq "3" }; then
	umount -R /mnt && reboot
else
	echo "./${0} 1		Setup partitions"
	echo "./${0} 2		Installation"
	echo "./${0} 3		Clean up"
fi
