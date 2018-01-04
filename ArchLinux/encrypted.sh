# Connect pendrive, keyboard, ethernet
# Boot up from pendrive

# cfdisk
#	- clear all partitions
#	- /dev/sda1 = /boot partition type 8300 with x>=100M
#	- /dev/sda2 = partition with type 8E00 that will be encrypted and contain /home /root and /swap

# ALL GLOBAL VARIABLES BE SET HERE

# Encrypted volume name
VOLUME=encvol
# Encrypted volume sizes
SIZE_ROOT=100G
SIZE_SWAP=12G
# uuid of the root partition
# run 'blkid' and look for the root
#ROOT_UUID=`blkid|grep -oP '(?<=/dev/sda2: UUID=")[0-9,a-f,-]*'`
DEV_UUID=`uuidgen`
# Hostname of computer system
HOSTNAME=Windows8.1
# User name of first non-root user
USERNAME=
# Set to 1 if installing box with touchpad, 0 if not
IS_LAPTOP=1

##### MAKE SURE ABOVE STEPS ARE COMPLETED ######

if [ ${1} -eq "1" ]; then 
	parted -s /dev/sda mklabel msdos
	parted -s /dev/sda -a optimal mkpart primary 2MB 110MB set 1 boot on
	parted -s /dev/sda -a optimal mkpart primary 110MB 100% set 2 lvm on
	cryptsetup luksFormat --uuid=${DEV_UUID} /dev/sda2
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
	cp ${0} /mnt/
	arch-chroot /mnt /bin/bash
elif [ ${1} -eq "2" ]; then
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
	
	sed -i -e '/^HOOKS/c\HOOKS=(base systemd autodetect keyboard modconf block sd-vconsole sd-encrypt sd-lvm2 fsck filesystems)' /etc/mkinitcpio.conf
	#create vconsole.conf because not made automatically
	touch /etc/vconsole.conf
	mkinitcpio -p linux

	#nano /etc/pacman.conf if want more repositories

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
	ROOT_UUID=`blkid|grep -oP '(?<=/dev/sda2: UUID=")[0-9,a-f,-]*'`
	echo 'GRUB_CMDLINE_LINUX="luks.uuid=${ROOT_UUID}"' >> /etc/default/grub
        cryptdevice=/dev/${VOLUME}/root:${VOLUME}-root
        root=/dev/mapper/${VOLUME}-root
        swap=/dev/mapper/${VOLUME}-swap
	grub-install /dev/sda
	
	pacman -S --noconfirm os-prober
	grub-mkconfig -o /boot/grub/grub.cfg
	pacman -S --noconfirm iw wpa_supplicant connman
	systemctl enable connman
	pacman -S --noconfirm xorg-apps xorg-server xorg
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
	echo "Before rebooting, type"
	echo "	sh ${0} 3" 
	exit
elif [ ${1} -eq "3" ]; then
	umount -R /mnt && reboot
else
	echo "sh ${0} 1		Setup partitions"
	echo "sh ${0} 2		Installation"
	echo "sh ${0} 3		Clean up"
fi
