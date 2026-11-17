STUDENT MANAGEMENT SYSTEM - SETUP GUIDE
========================================

This is a Java desktop/console application (not a website).
It uses Java + JDBC + MySQL and supports:
1. Add student
2. View all students
3. Delete student by ID

REQUIREMENTS
------------
- Windows 10
- JDK installed
- MySQL Server installed and running
- MySQL Workbench installed
- Maven installed and available in Command Prompt

STEP 1 - CREATE THE DATABASE
----------------------------
1. Open MySQL Workbench and connect to your local MySQL server.
2. Open the file database.sql from this project folder.
3. Click the lightning-bolt Execute button.
4. You should see the student_management database and students table.

STEP 2 - SET YOUR MYSQL PASSWORD
--------------------------------
1. Open config.properties in Notepad.
2. Replace CHANGE_ME with the password you use for MySQL user root.
3. Save the file.
Do not add quotation marks around the password.

STEP 3 - CHECK JAVA AND MAVEN
-----------------------------
Open Command Prompt and run:
    java -version
    mvn -version

Both commands should show version information.
If 'mvn' is not recognized, Maven is not configured in PATH yet.

STEP 4 - RUN THE APPLICATION
----------------------------
1. Open the StudentManagementSystem folder.
2. Double-click run.bat.
   OR open Command Prompt in this folder and run:
       mvn clean package
       java -jar target\student-management-1.0.0.jar

The first Maven build needs internet once to download the MySQL driver and Maven plugins.

HOW TO USE
----------
Choose:
1 - Add Student: enter name, email, department and age.
2 - View All Students: displays saved student records.
3 - Delete Student: enter the ID shown in the list.
4 - Exit.

TROUBLESHOOTING
---------------
- Access denied for user 'root': check username/password in config.properties.
- Communications link failure: make sure MySQL Server is running.
- Unknown database: run database.sql in MySQL Workbench.
- 'mvn' is not recognized: Maven is not installed/configured in PATH.
- If a student email already exists, use a different email (email is unique).

NOTE
----
This is a simple educational project. Keep your MySQL password private.
