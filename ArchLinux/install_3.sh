#!/bin/bash

###########
#post installation
# packages

list=(clamav chromium firefox i3-wm i3status ipset lxde nmap ntp openssh p7zip rxvt_unicode)
for i in ${list}; do
	pacman -S --noconfirm ${i}
done



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

