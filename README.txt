FACULTY LEAVE MANAGEMENT - ORIGINAL UI + MYSQL

IMPORTANT:
1. The Swing page layouts, labels, buttons, and positions are kept the same as the original source.
2. Database actions and page navigation have been connected.
3. Open database.sql in MySQL Workbench and execute it.
4. In ALL six Java files, replace Amit@2006 with your MySQL root password.
5. Add your mysql-connector JAR to IntelliJ: File > Project Structure > Libraries > + > Java.
6. Keep all Java files in the same src folder / same package (default package).
7. Run Login.java.

Demo login:
amit / amit123 / Faculty
rahul / rahul123 / Substitute Faculty
neha / neha123 / Faculty
admin / admin123 / Admin / HOD

Date input format:
YYYY-MM-DD (example 2026-11-05)

NOTES:
- AppSession.java stores the currently logged-in user ID/name/role for the running application.
- Each page opens its own JDBC connection directly using DriverManager, as requested.
- LeaveApproval page loads the next pending application; Approve/Reject updates that row.
- Demo passwords are plain text for local college-project testing only. Use password hashing for real deployment.
- If you already have tables with different columns, back up your existing database before running the SQL script and compare schema first.
