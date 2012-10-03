LUJA
====

Launcher and Updater for Java Applications 

What it is
----------

As the name says, LUJA is a simple java tool to keep another java application up to date.
It allows users to choose how often updates are installed. It also allows users to start older verions,
unless they delete them. If the user whishes, LUJA does this automatically for them.

Command Line Parameters
-----------------------

Command line parameters are forwarded to the real application. Only a few arguments are used by LUJA. They are:

* `-L:offline` does not perform an update check
* `-L:update` forces an update check
* `-L:version=123` starts version 123.
* `-L:select` shows a window to select the version to start. This dialog allows to select update periods, auto updates, etc.

Building LUJA
-------------

To use LUJA in your own application, build LUJA from source and provide a few options:

```
$ git clone git://github.com/jochenwierum/luja.git
$ cd luja
$ mvn package -Dluja.name=SSH-Client -Dluja.dirName=sshclient -Dluja.uri=http://jowisoftware.de/ssh/build.properties
$ mv target/luja.jar luja-ssh.jar
```

Test your LUJA:

```
$ java -jar luja-ssh.jar
```

Of course, you should use your own values:

* `luja.name` is the displayed name of the program
* `luja.dirName` is the name of the directory where LUJA will cache the jar files. LUJA stores them in your home directory in linux or in %LOCALAPPDATA% in windows.
* `luja.uri` uri to a properties file which holds information about the current version.

The online properties file should have the following content:

```
; The date when the software was build
Build-Date: 2012-10-02
; A unique version number or name
SCM-Revision: 1c32ab09b0dd680a17afb61184eec1275d775954
; A link to a downloadable jar file (it should be startable with java -jar)
Download-URI: http://jowisoftware.de/ssh/ssh.jar
; The size of the jar file
Size: 1202575
```
