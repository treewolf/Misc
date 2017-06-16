# ALL GLOBAL VARIABLES BE SET HERE

# Hostname of computer system
HOSTNAME=

# User name of first non-root user
USERNAME=

# Set to 1 if installing box with touchpad, 0 if not
IS_LAPTOP=

# Connect pendrive, keyboard, ethernet

# Boot up from pendrive

# cfdisk
#	- clear all partitions
#	- create 3 new partitions, two as primary, one as swap

# run fsck on the primary partitions
#	- (we will say sda1 is /, sda2 is swap, and sda3 is home)
fsck -y /dev/sda1
fsck -y /dev/sda3

mkfs.ext4 /dev/sda1
mkfs.ext4 /dev/sda3
mkswap /dev/sda2
swapon /dev/sda2

mount /dev/sda1 /mnt
mkdir /mnt/home
mount /dev/sda3 /mnt/home

pacstrap -i /mnt base base-devel
genfstab -U -p /mnt >> /mnt/etc/fstab
arch-chroot /mnt /bin/bash

# add hostname
echo "${HOSTNAME}" > /etc/hostname

# Chnage /etc/locale.gen 
#	- uncomment en_US.UTF-8 
#	- use the time of Los_Angeles
sed -i -e 's/#en_US.UTF-8/en_US.UTF-8/g' /etc/locale.gen
locale-gen
echo LANG=en_US.UTF-8 > /etc/locale.conf
export LANG=en_US.UTF-8
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
sleep 5
exit
umount -R /mnt
reboot

###########
#post installation
# packages

clamav
fail2ban
firefox
i3-wm i3status
ipset
lxde
ntp
openssh
packer
p7zip
physlock
rxvt-unicode
wget

################
# configure all #
#################
echo "exec i3" > ~/.xinitrc

#change finger for all users, use "none"
chfn <user>

# erase the "quiet" from /etc/default/grub and /boot/grub/grub.cfg

#####################
### Secure station ###
######################
# comment all tty out
sudo nano /etc/securetty

# change cipher in /etc/ssh/ssh_config
Cipher aes256

#add aur to /etc/pacman.conf
[archlinuxfr]
SigLevel = Never
Server = http://repo.archlinux.fr/$arch

# change perm on wget
chmod 750 /usr/bin/wget

#edit /etc/ssh/sshd_config
#change to "no"
PermitRootLogin no

#add
AllowUsers	<users>

# enable and start fail2ban
systemctl start fail2ban
systemctl enable fail2ban
systemctl restart iptables.service

# start and enable ipset
systemctl start ipset
systemctl enable ipset
systemctl restart iptables.service

# virus protection
systemctl enable freshclamd
systemctl enable clamd
systemctl start freshclamd
freshclam
systemctl start clamd

#create jail for virus
sudo mkdir /jail


# refresh
reboot

