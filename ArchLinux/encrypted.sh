# Unencrypted Boot partition with encrypted root and swap partitions with LVM
#Part 2 includes default options for locale and charset
#Post installation holds optional software. May also install default configs for software
# later configs will be in separate folder. This script will only hold the config name and respective proper pathname.

# If want, before running script, dd drive with random and then with zero.
## dd bs=4M if=/dev/urandom of=/dev/sda status=progress && dd bs=4M if=/dev/zero of=/dev/sda status=progress

##### ALL GLOBAL VARIABLES BE SET HERE #####
# Encrypted volume name
VOLUME=
# Encrypted volume sizes M=megabyte G=gigabyte 
SIZE_ROOT=
SIZE_SWAP=
# uuid of the root partition
#ROOT_UUID=`blkid|grep -oP '(?<=/dev/sda2: UUID=")[0-9,a-f,-]*'`
# new uuid for luks encrypted partition
DEV_UUID=`uuidgen`
# Hostname of computer system, no periods
HOSTNAME=
# User name of first non-root user
USERNAME=
# Set to 1 if installing box with touchpad, 0 if not
IS_LAPTOP=

##### MAKE SURE ABOVE STEPS ARE COMPLETED ######

if [ ${1} -eq "1" ]; then 
	parted -s /dev/sda mklabel msdos
	parted -s /dev/sda -a optimal mkpart primary 2MB 110MB set 1 boot on
	parted -s /dev/sda -a optimal mkpart primary 110MB 100% set 2 lvm on
	cryptsetup luksFormat --verify-passphrase --hash=sha512 --key-size=512 --uuid=${DEV_UUID} /dev/sda2
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
	rm /etc/localtime
	ln -s /usr/share/zoneinfo/America/Los_Angeles /etc/localtime
	hwclock --systohc --utc
	sed -i -e '/^HOOKS/c\HOOKS=(base systemd autodetect keyboard modconf block sd-vconsole sd-encrypt sd-lvm2 fsck filesystems)' /etc/mkinitcpio.conf
	#create vconsole.conf because not made automatically
	touch /etc/vconsole.conf
	mkinitcpio -p linux

	#nano /etc/pacman.conf if want more repositories
	## aur
	#[archlinuxfr]
	#SigLevel = Never
	#Server = http://repo.archlinux.fr/$arch
	##[blackarch]
	#Server = http://mirror.team-cymru.org/blackarch/$repo/os/$arch

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
	sed -i -e '/^GRUB_ENABLE_CRYPTODISK.*/c\GRUB_ENABLE_CRYPTODISK=Y' /etc/default/grub
	sed -i -e '/^GRUB_CMDLINE_LINUX_DEFAULT.*/c\GRUB_CMDLINE_LINUX_DEFAULT=""' /etc/default/grub
        echo 'GRUB_PRELOAD_MODULES="lvm"' >> /etc/default/grub
	echo GRUB_CMDLINE_LINUX=\"luks.uuid=${DEV_UUID}\" >> /etc/default/grub
        cryptdevice=/dev/${VOLUME}/root:${VOLUME}-root
        root=/dev/mapper/${VOLUME}-root
        swap=/dev/mapper/${VOLUME}-swap
	grub-install /dev/sda
	pacman -S --noconfirm os-prober
	grub-mkconfig -o /boot/grub/grub.cfg
	pacman -S --noconfirm iw wpa_supplicant
	pacman -S --noconfirm xorg-apps xorg-server xorg xorg-xinit
	# volume
	pacman -S --noconfirm alsa-utils
	# touchpad
	case "$IS_LAPTOP" in
		1)
			pacman -S --noconfirm xf86-input-synaptics;;
		0)
			echo "Options read no touchpad: pacman -S xf86-input-synaptics if error";;
	esac
	echo "DONE INSTALLATION. AFTER REBOOT READ 'post installation' SECTION or run 'sh ${0} post'"
	echo "Before rebooting, type"
	echo "	exit"
	echo "	sh ${0} 3"
elif [ ${1} -eq "post" ]; then
	systemctl enable iptables

	pacman -S cups && systemctl enable org.cups.cupsd
	pacman -S clamav && freshclam && systemctl enable freshclamd # not enabling clamav daemon
	pacman -S firefox
	pacman -S i3-wm i3lock i3status
	pacman -S libreoffice-still #should adjust properties metatags and image memory
	pacman -S ntp && systemctl enable ntpd
	pacman -S openssh && pacman -S openvpn
	pacman -S rxvt-unicode #must add config for .Xdefaults and global
		#URxvt*transparent: true
		#URxvt*shading: 40
		#URxvt*background: #000000
		#URxvt*foreground: #cccccc
		#URxvt*scrollBar: false
		#URxvt*saveLines: 32767
		#URxvt*font: xft:hack:size=9:antialias=true ##if ttf-hack installed
		#URxvt*font: 8x13 ##if ttf-hack not installed
	pacman -S ttf-hack #must edit /usr/share/fonts/40* and 60*, put hack first
	
	# configs
	echo -e "xset b off &\nexec i3" > /home/${USERNAME}/.xinitrc
elif [ ${1} -eq "3" ]; then
	umount -R /mnt && reboot
else
	echo "sh ${0} 1		Setup partitions"
	echo "sh ${0} 2		Installation"
	echo "sh ${0} 3		Clean up"
fi
