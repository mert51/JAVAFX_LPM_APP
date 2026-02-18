# JAVAFX_LPM_APP
LPM_P1 — Local Place Management (JavaFX)
Short summary
A simple JavaFX desktop app to manage Places, Students, and Reservations. It demonstrates a small, clear separation between UI (controllers), data objects (DTOs), and database access (JDBC CRUD).
Project structure (key files)
•
src/main/java/com/sau/lpm/lpm_p1/LpmApplication.java — JavaFX application entry
•
src/main/java/com/sau/lpm/lpm_p1/controller/ — UI controllers:
◦
PlaceController.java, StudentController.java, ReservationController.java
•
src/main/java/com/sau/lpm/lpm_p1/dto/ — DTOs:
◦
Place.java, Student.java, Reservation.java
•
src/main/java/com/sau/lpm/lpm_p1/db/ — simple JDBC CRUD:
◦
PlaceCrudOperations.java, StudentCrudOperation.java, ReservationCrudOperations.java
•
src/main/resources/... — FXML views: Place.fxml, Student.fxml, Reservation.fxml, lpm-view.fxml
•
src/main/resources/LPM.sql — (if present) SQL schema helper
How it works (very brief)
•
DTOs: plain Java objects that hold data.
◦
Place: id, building, floor, room, seat.
◦
Student: id, name, department (mapped to DB column address).
◦
Reservation: studentId, placeId, date (YYYY-MM-DD string), duration.
•
Controllers: read UI, validate input, convert to DTOs, call DB methods, show alerts.
◦
Validate IDs, integers (seat/duration), and date format.
◦
Handle user confirmation when needed (e.g., deleting a student with reservations).
•
DB classes: use JDBC to run SQL against PostgreSQL. Each class opens connections, runs parameterized queries, maps results to DTOs, and returns simple status codes or throws exceptions for serious errors.
Database notes (important)
•
The DB connection settings are hard-coded in DB classes (DB_URL, USER, PASS). Update them before running.
•
Expected tables and columns:
◦
student(id, name, address)
◦
place(id, building, floor, room, seat)
◦
reservation(student_id, place_id, date, duration) with foreign keys to student and place
•
Reservation date is stored as SQL date; controllers require YYYY-MM-DD.
Build & run (minimal)
•
Build with Maven (use wrapper on Windows):
mvnw.cmd clean package
•
Run from your IDE by launching LpmApplication (recommended for JavaFX), or run with your usual JavaFX run configuration after packaging.
•
Ensure PostgreSQL is running and the schema/tables exist (use LPM.sql if provided).
Behavior & error handling (short)
•
Duplicate inserts return -1 from DB methods; controllers show a clear alert.
•
Deleting a student that has reservations prompts to remove related reservations first.
•
Foreign key violations in reservations produce descriptive errors (missing student/place).
Notes & suggestions
•
For real use, move DB credentials out of source code (env or config file).
•
Consider using LocalDate in DTOs instead of String for dates.
•
This project is an educational example showing simple JDBC + JavaFX patterns.
