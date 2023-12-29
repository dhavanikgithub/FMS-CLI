# File Management System
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
##### Project Overview:
Build a command-line-based file management system that allows users to navigate directories,
create, delete, copy, move files and folders, and perform basic file operations.

##### Features to Include:

1. **Directory Navigation:** Enable users to navigate through directories (folders) using commands.
2. **File and Folder Management:** Implement functionalities to create, delete, rename, copy, and
move files/folders.
3. **File Information:** Display information about files (size, permissions, creation date, etc.).
4. **Search Functionality:** Implement a search feature to find files by name or extension.
5. **Command-Line Interface (CLI):** Design a user-friendly command-line interface for interacting
with the file system.

#### Commands
1. **Create Operation** : File: `create -file filename or path` Folder: `create -dir foldername or path`
2. **Rename Operation** : File `rename -file filename or path` Folder: `rename -dir foldername or path`
3. **Copy Operation** : File: `cp -file filename or path` Folder: `cp -dir foldername or path`
4. **Move Operation** : File: `mv -file filename or path` Folder: `mv -dir foldername or path`
5. **Search Operation** : Local: `find filename or extention` Global: `find -g filename or extention`
6. **Delete Operation** : File: `del -file [filename1/path1] [filename2/path2] .....` Folder: `del -dir foldername or path`
7. **Folder Navigation** : Open: `open foldername or path` Back: `back`
8. **Files and Folders Linsting** : `list`
9. **File or Folder Details** : `prp filename or foldername or path`
10. **Help** : `help`
11. **Terminate Application** : `exit` or `quit` or `stop`
12. **Select Operation** : File: `select -file fileSerialNumber` Folder: `select -dir folderSerialNumber`


## Compatible
![Windows](https://img.shields.io/badge/Windows-0078D6?style=for-the-badge&logo=windows&logoColor=white)
![Linux](https://img.shields.io/badge/Linux-FCC624?style=for-the-badge&logo=linux&logoColor=black)
###### Not tested yet
![macOS](https://img.shields.io/badge/mac%20os-000000?style=for-the-badge&logo=macos&logoColor=F0F0F0)


## Run

Follow these steps to download and run the FMS-CLI JAR file:

### Step 1: Download the JAR File

Download the JAR file from the provided link:
[Download FMS-CLI JAR](https://github.com/dhavanikgithub/FMS-CLI/tree/master/out/artifacts/FMS_CLI_jar)

You can either click the link to download manually or use the following command in your terminal:

```bash
wget https://github.com/dhavanikgithub/FMS-CLI/raw/master/out/artifacts/FMS_CLI_jar/FMS_CLI.jar
```

### Step 2: Navigate to the Downloaded Directory

Navigate to the directory where you downloaded the JAR file. If you used the terminal command, you might use:

```bash
cd /path/to/downloaded/directory
```

### Step 3: Run the JAR File

Run the JAR file using the following command:

```bash
java -jar FMS_CLI.jar
```

Replace `FMS_CLI.jar` with the actual name of the JAR file if it's different.

Now, the FMS-CLI should be running, and you can interact with it as intended.

## Another way to run in windows using `setup.bat`

### Step 1: Download the JAR File and setup.bat file

Download the JAR file from the provided link:
[Download files](https://github.com/dhavanikgithub/FMS-CLI/tree/master/out/artifacts/FMS_CLI_jar)

### Step 2: Double click on setup.bat to Run Application

![image](https://github.com/dhavanikgithub/FMS-CLI/assets/110646988/1e169fba-8aa1-48bf-a0e3-a681101eed8e)


**Note:** Ensure that you have Java installed on your system before running the JAR file. You can download and install Java from [here](https://www.java.com/en/download/).


