### File Management System

**Project Overview:**
Build a command-line-based file management system that allows users to navigate directories,
create, delete, copy, move files and folders, and perform basic file operations.

**Features to Include:**

1. **Directory Navigation:** Enable users to navigate through directories (folders) using commands.
2. **File and Folder Management:** Implement functionalities to create, delete, rename, copy, and
move files/folders.
3. **File Information:** Display information about files (size, permissions, creation date, etc.).
4. **Search Functionality:** Implement a search feature to find files by name or extension.
5. **Command-Line Interface (CLI):** Design a user-friendly command-line interface for interacting
with the file system.

###### Language: Java

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
