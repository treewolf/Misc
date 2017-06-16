# Connect pendrive, keyboard, ethernet
# Boot up from pendrive

# cfdisk
#	- clear all partitions
#	- create 3 new partitions, two as primary, one as swap
# sda1 = / bootable; sda2 = swap; sda3 = /home


##### MAKE SURE ABOVE STEPS ARE COMPLETED ######

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
