#Debate Forum
**CS 408 Team 7** Debate Forum

Alex Rosenberg • Cody Tyson • Naveen Ganessin • Michael Schloss • Roy Ramstad

**The private repository for Debate Forum**

##Organisation
Debate Forum is organized into several easy folders.  This project was built with Eclipse Neon and IntelliJ IDEA CE.  Sometimes Eclipse likes to separate modules and sub modules.  We don't know why it does that; Eclipse is weird.

The `Documents` folder contains for-grading documents due for the course.  Any revisions will have notes with summary of the revisions.

The `doc` folder contains all Javadoc for our classes in `html` format for easy viewing.

The `src` folder contains all of the source files for the project.

* `database`           The Web Server communicator.  Contains wrapper classes for security and overload safe communication
* `JSON_translation`   Translates data returned from the database into Objects for the UI
* `objects`            The Object wrappers that are used by UI and JSON_translation to manage data
* `UI`                 Hopefully self-explanatory
* `UIKit`              Helper classes to asynchronously transfer data across modules and store objects to on-device storage
* `tests`              Hopefully self-explanatory as well

##Compiling
To compile Debate Forum, simply download the zip and use eclipse or IntelliJ IDEA to open the directory.  Then, run the project.  There are a few dependencies, such as CommonCypto, that you may need to download from an external source, but they should be downloaded when pulling from GitHub.

