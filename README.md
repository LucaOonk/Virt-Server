# Virt-Server
A server for Virt Commander made in Java so you can start, stop, add, remove to virsh and connect to them from another machine.

This is far from production ready so be ware.

## Depends on:
- libvirt https://www.libvirt.org
- QEMU https://www.qemu.org
- virt-viewer

## Compatibility
Works on MacOS 11.
MacOS 12 is not supported for now as the dependencies are not ready yet.

## How to install dependencies on MacOS:
- First, install homebrew, which is a package manager for macOS. (This is automaticaly done at startup)
- Run `brew install qemu gcc libvirt virt-viewer`. (This is automaticaly done at startup)

Since macOS doesn't support QEMU security features, we need to disable them:
- `echo 'security_driver = "none"' >> /usr/local/etc/libvirt/qemu.conf`
- `echo "dynamic_ownership = 0" >> /usr/local/etc/libvirt/qemu.conf`
- `echo "remember_owner = 0" >> /usr/local/etc/libvirt/qemu.conf`
  
Finally start the libvirt service, with `brew services start libvirt`. It will start after boot as well.

# Download:
Releases can be found here: https://github.com/LucaOonk/Virsh-Gui/releases

# Planned features:
- Extend functionality
- Increased security 

# Known Issues:

