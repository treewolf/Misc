#!/bin/bash

# ALL GLOBAL VARIABLES BE SET HERE

# Hostname of computer system
HOSTNAME=

# User name of first non-root user
USERNAME=

# Set to 1 if installing box with touchpad, 0 if not
IS_LAPTOP=

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
sudo visudo
# Defaults:ALL timestamp_timeout=0

pacman -S --noconfirm grub
grub-install /dev/sda
pacman -S --noconfirm os-prober
grub-mkconfig -o /boot/grub/grub.cfg

pacman -S --noconfirm iw wpa_supplicant connman
systemctl enable connman

pacman -S --noconfirm xorg-xinit xorg-server xorg-utils xorg-server-utils mesa

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
exit
umount -R /mnt
reboot
