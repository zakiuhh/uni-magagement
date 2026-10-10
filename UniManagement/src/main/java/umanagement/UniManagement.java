/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package umanagement;

/**
 *
 * @author Zaki
 */

import java.util.ArrayList;
import java.util.Scanner;

class Person {
    private static int nextId = 1;   // shared by all objects: gives each person a new id
    private int id;
    private String name;
    private String email;

    public Person(String name, String email) {
        this.id = nextId++;          // use the current id, then add 1 for the next person
        setName(name);               // setName checks that the name is not empty
        this.email = email;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    public void setName(String name){this.name = name;}

    public void setEmail(String email){this.email = email;}

    public static int getTotalPeople(){return nextId - 1;}

    // child classes override this to say what their role actually is;
    // "Person" is just the fallback if nobody overrides it
    public String getRole() { return "Person"; }

    public String getDetails() {
        return getRole() + " #" + id + " | " + name + " | " + email;
    }

    @Override
    public String toString(){return getDetails();}

    @Override
    public boolean equals(Object o) {
        if (o instanceof Person) {
            Person p = (Person) o;
            return id == p.id;       // same id means same person
        }
        return false;
    }

    @Override
    public int hashCode(){return id;}
}


/* STUDENTS */
class Student extends Person{
    private Department dept;
    private ArrayList<Course> courses = new ArrayList<>();
    private ArrayList<Double> marks = new ArrayList<>();   // marks[i] belongs to courses[i]
    private double feePaid = 0;

    public Student(String name, String email, Department dept) {
        super(name, email);          // let Person set up id, name, email
        this.dept = dept;
    }

    // undergraduate and graduate students override these with their own numbers;
    // these are just the plain defaults for a generic Student
    public int getMaxCourses(){return 5;}
    public double getFeePerCourse(){return 20000;}
    public String getLevel(){return "Student";}

    @Override
    public String getRole(){return getLevel() + " Student";}

    public Department getDept(){return dept;}

    public int getCourseCount(){return courses.size();}

    public Course getCourse(int index){
        return courses.get(index);
    }

    public double getMark(int index){
        return marks.get(index);
    }

    public boolean canEnroll(){
        return courses.size() < getMaxCourses();
    }

    public boolean addCourse(Course c){
        if (!canEnroll() || courses.contains(c)) {
            return false;
        }
        courses.add(c);
        marks.add(0.0);              // starting mark is 0 (autoboxing: double -> Double)
        return true;
    }

    public boolean setMark(Course c, double mark){
        int index = courses.indexOf(c);
        if (index < 0 || mark < 0 || mark > 100) {
            return false;
        }
        marks.set(index, mark);
        return true;
    }

    public double getAverage(){
        if (marks.size() == 0){
            return 0;
        }
        double total = 0;
        for (int i = 0; i < marks.size(); i++) {
            total += marks.get(i);   // unboxing: Double -> double
        }
        return total / marks.size();
    }

    public String getGrade(){
        double avg = getAverage();
        if (avg >= 85) return "A";
        else if (avg >= 70) return "B";
        else if (avg >= 60) return "C";
        else if (avg >= 50) return "D";
        else return "F";
    }

    public double getTotalFee(){
        return courses.size() * getFeePerCourse();
    }
    public double getFeePaid(){return feePaid;}
    public double getFeeDue(){return getTotalFee() - feePaid;}

    public void payFee(double amount) {
        if (amount > 0){
            feePaid += amount;
        }
    }

    @Override
    public String getDetails() {
        return super.getDetails() + " | Dept: " + dept.getName()
                + " | Courses: " + courses.size() + "/" + getMaxCourses();
    }
}

class UndergraduateStudent extends Student {
    public UndergraduateStudent(String name, String email, Department dept) {
        super(name, email, dept);
    }

    @Override
    public int getMaxCourses(){
        return 6;
    }

    @Override
    public double getFeePerCourse() { return 15000; }

    @Override
    public String getLevel() { return "Undergraduate"; }
}

class GraduateStudent extends Student {
    private String thesisTopic;

    public GraduateStudent(String name, String email, Department dept, String thesisTopic) {
        super(name, email, dept);
        this.thesisTopic = thesisTopic;
    }

    public String getThesisTopic() { return thesisTopic; }

    @Override
    public int getMaxCourses(){
        return 4;
    }

    @Override
    public double getFeePerCourse(){
        return 25000;
    }

    @Override
    public String getLevel(){
        return "Graduate";
    }

    @Override
    public String getDetails() {
        return super.getDetails() + " | Thesis: " + thesisTopic;
    }
}


/* FACULTY */

class Faculty extends Person {
    private String designation;
    private Department dept;
    private ArrayList<Course> teaching = new ArrayList<>();

    public Faculty(String name, String email, Department dept, String designation){
        super(name, email);
        this.dept = dept;
        this.designation = designation;
    }

    public String getDesignation(){return designation;}
    public Department getDept(){return dept;}
    public int getTeachingCount(){return teaching.size();}

    public void assignCourse(Course c){
        teaching.add(c);
        c.setInstructor(this);       // tell the course who teaches it
    }

    @Override
    public String getRole(){return "Faculty";}

    @Override
    public String getDetails(){
        return super.getDetails()+" | "+designation+" | Dept: "+dept.getName()+" | Teaching: "+teaching.size()+" course(s)";
    }
}


/* STAFF AND ITS FOUR OFFICES */

class Staff extends Person{
    private String office;

    public Staff(String name, String email, String office){
        super(name, email);
        this.office = office;
    }

    public String getOffice(){return office;}

    // each office overrides this to describe its own job;
    // this is just the plain default for a generic Staff member
    public String performDuty(){return "General staff duties.";}

    @Override
    public String getRole(){return office+" Staff";}
}

class AdmissionOffice extends Staff{
    public AdmissionOffice(String name, String email){
        super(name, email, "Admission Office");
    }

    public String admit(University uni, Student s){
        uni.addStudent(s);
        s.getDept().addStudent(s);
        return s.getName() + " is admitted to " + s.getDept().getName()+" as "+s.getLevel()+" (ID "+s.getId()+")";
    }

    @Override
    public String performDuty(){return "Admits new students and gives them an ID.";}
}

class AccountOffice extends Staff{
    public AccountOffice(String name, String email){
        super(name, email, "Account Office");
    }

    public String collectFee(Student s, double amount){
        if (amount <=0){
            return "Failed: amount must be more than 0.";
        }
        s.payFee(amount);
        return "Received Rs. " + amount + " from " + s.getName()+". Fee still due: Rs. "+s.getFeeDue();
    }

    @Override
    public String performDuty(){return "Collects fees and keeps the payment records.";}
}

class ExamOffice extends Staff{
    public ExamOffice(String name, String email){
        super(name, email, "Exam Office");
    }

    public String recordMarks(Student s, Course c, double marks){
        if (s.setMark(c, marks)){
            return "Marks saved: "+s.getName()+" got "+marks +" in "+c.getCode();
        }
        return "Failed: student is not in this course, or marks are not between 0 and 100.";
    }

    @Override
    public String performDuty(){return "Records marks and prepares results.";}
}

class DepartmentalStaff extends Staff {
    private Department dept;

    public DepartmentalStaff(String name, String email, Department dept){
        super(name, email, "Departmental Support");
        this.dept = dept;
    }

    public String scheduleClass(Course c, Room r, String slot) {
        if (c.schedule(r, slot)){
            return c.getCode()+" is now in room "+r.getRoomNo()+" at "+slot;
        }
        return "Failed: room "+r.getRoomNo()+" is already booked at "+slot;
    }

    @Override
    public String performDuty(){
        return "Helps "+dept.getName()+" with schedules and class rooms.";
    }
}



/* ROOM AND COURSE */

class Room {
    private String roomNo;
    private int capacity;
    private ArrayList<String> bookedSlots = new ArrayList<>();

    public Room(String roomNo, int capacity){
        this.roomNo = roomNo;
        this.capacity = capacity;
    }

    public String getRoomNo(){return roomNo;}
    public int getCapacity(){return capacity;}
    public int getBookedCount(){return bookedSlots.size();}
    public String getBookedSlot(int index){return bookedSlots.get(index);}

    public boolean isFree(String slot) {return !bookedSlots.contains(slot);}

    public boolean book(String slot){
        if (isFree(slot)){
            bookedSlots.add(slot);
            return true;
        }
        return false;
    }
}

class Course {
    private static int totalCourses=0;
    private String code;
    private String name;
    private int creditHours;
    private String level;                 // "Undergraduate" or "Graduate"
    private Faculty instructor;           // can be null until assigned
    private ArrayList<Student> students = new ArrayList<>();
    private Room room;                    // can be null until scheduled
    private String slot = "Not scheduled";

    public Course(String code, String name, int creditHours, String level) {
        this.code = code;
        this.name = name;
        this.creditHours = creditHours;
        this.level = level;
        totalCourses++;
    }

    public static int getTotalCourses() { return totalCourses; }

    public String getCode() { return code; }
    public String getName() { return name; }
    public int getCreditHours() { return creditHours; }
    public String getLevel() { return level; }
    public Faculty getInstructor() { return instructor; }
    public void setInstructor(Faculty instructor) { this.instructor = instructor; }
    public Room getRoom() { return room; }
    public String getSlot() { return slot; }
    public int getStudentCount() { return students.size(); }

    public boolean schedule(Room r, String slot) {
        if (r.book(slot)) {
            this.room = r;
            this.slot = slot;
            return true;
        }
        return false;
    }

    public String enroll(Student s) {
        if (!s.getLevel().equals(level)) {
            return "Failed: " + s.getName() + " is " + s.getLevel() + " but " + code
                    + " is a " + level + " course.";
        }
        if (students.contains(s)) {
            return "Failed: " + s.getName() + " is already in " + code + ".";
        }
        if (room != null && students.size() >= room.getCapacity()) {
            return "Failed: room " + room.getRoomNo() + " is full.";
        }
        if (!s.canEnroll()) {
            return "Failed: " + s.getName() + " already has the maximum number of courses.";
        }
        students.add(s);
        s.addCourse(this);
        return "Success: " + s.getName() + " is enrolled in " + code + " (" + name + ").";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Course) {
            Course other = (Course) o;
            return code.equals(other.code);   // same code means same course
        }
        return false;
    }

    @Override
    public int hashCode() { return code.hashCode(); }

    @Override
    public String toString() {
        String teacher = (instructor == null) ? "No instructor" : instructor.getName();
        String roomText = (room == null) ? "No room" : room.getRoomNo();
        return code + " | " + name + " | " + level + " | " + creditHours + " cr | "
                + teacher + " | " + roomText + " | " + slot
                + " | Enrolled: " + students.size();
    }
}


/* DEPARTMENT AND UNIVERSITY */

class Department {
    private String name;
    private ArrayList<Course> courses = new ArrayList<>();
    private ArrayList<Faculty> faculty = new ArrayList<>();
    private ArrayList<Student> students = new ArrayList<>();

    public Department(String name) { this.name = name; }

    public String getName(){
        return name;
    }
    public int getCourseCount(){
        return courses.size();
    }
    public Course getCourse(int index){
        return courses.get(index);
    }
    public int getFacultyCount(){
        return faculty.size();
    }
    public int getStudentCount(){
        return students.size();
    }

    public void addCourse(Course c){
        courses.add(c);
    }
    public void addFaculty(Faculty f){
        faculty.add(f);
    }
    public void addStudent(Student s){
        students.add(s);
    }
}

class University {
    private String name;
    private ArrayList<Department> depts = new ArrayList<>();
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Faculty> facultyList = new ArrayList<>();
    private ArrayList<Staff> staffList = new ArrayList<>();
    private Room[] rooms = new Room[5];       // a fixed number of class rooms (array)

    public University(String name) {
        this.name = name;
        // the university creates its own depts and rooms (composition)
        depts.add(new Department("Computer Science"));
        depts.add(new Department("Electrical Engineering"));
        depts.add(new Department("Management Science"));
        depts.add(new Department("Civil Engineering"));

        rooms[0] = new Room("G-01", 40);
        rooms[1] = new Room("G-02", 35);
        rooms[2] = new Room("G-03", 30);
        rooms[3] = new Room("G-04", 30);
        rooms[4] = new Room("Lab-01", 25);
    }

    public String getName(){
        return name;
    }
    public int getDeptCount(){
        return depts.size();
    }
    public Department getDept(int index) { return depts.get(index); }
    public Room[] getRooms(){return rooms;}

    public void addStudent(Student s){students.add(s);}

    public void addFaculty(Faculty f){
        facultyList.add(f);
        f.getDept().addFaculty(f);
    }

    public void addStaff(Staff s){staffList.add(s);}

    public int getStaffCount(){return staffList.size();}
    public Staff getStaff(int index){return staffList.get(index);}

    public Student findStudent(int id){
        for (int i=0; i<students.size(); i++) {
            if (students.get(i).getId() == id) {
                return students.get(i);
            }
        }
        return null; // if id not found
    }

    public Course findCourse(String code) {
        for (int d=0; d<depts.size(); d++) {
            Department dept = depts.get(d);
            for (int c=0; c<dept.getCourseCount(); c++) {
                if (dept.getCourse(c).getCode().equalsIgnoreCase(code)) {
                    return dept.getCourse(c);
                }
            }
        }
        return null;
    }

    // puts students, faculty and staff in ONE list of Person (polymorphism)
    public ArrayList<Person> getEveryone(){
        ArrayList<Person> everyone = new ArrayList<>();
        everyone.addAll(students);
        everyone.addAll(facultyList);
        everyone.addAll(staffList);
        return everyone;
    }
}


/* MAIN CLASS (the "client" that uses all the classes above) */

public class UniManagement {

    static Scanner input = new Scanner(System.in);
    static University uni = new University("COMSATS University Islamabad, Wah Campus");

    // the four offices of the university
    static AdmissionOffice admission = new AdmissionOffice("Mr. Zaki", "admission@uni.edu");
    static AccountOffice account = new AccountOffice("Ms. Ufaq Akram", "accounts@uni.edu");
    static ExamOffice exam = new ExamOffice("Mr. Mojiz Kazmi", "exams@uni.edu");
    static DepartmentalStaff csStaff;

    // ---------------- sample data so the program is not empty ----------------
    static void loadSampleData() {
        Department cs = uni.getDept(0);
        Department ee = uni.getDept(1);
        Department ms = uni.getDept(2);
        Department ce = uni.getDept(3);

        // staff
        uni.addStaff(admission);
        uni.addStaff(account);
        uni.addStaff(exam);
        csStaff = new DepartmentalStaff("Mr. Imran", "cs.support@uni.edu", cs);
        uni.addStaff(csStaff);

        // faculty
        Faculty f1 = new Faculty("Dr. Ahmed Raza", "ahmed@uni.edu", cs, "Associate Professor");
        Faculty f2 = new Faculty("Dr. Ayesha Khan", "ayesha@uni.edu", ee, "Professor");
        Faculty f3 = new Faculty("Dr. Bilal Sheikh", "bilal@uni.edu", ms, "Lecturer");
        Faculty f4 = new Faculty("Dr. Sara Malik", "sara@uni.edu", ce, "Assistant Professor");
        Faculty f5 = new Faculty("Dr. Hamza Tariq", "hamza@uni.edu", cs, "Professor");
        uni.addFaculty(f1);
        uni.addFaculty(f2);
        uni.addFaculty(f3);
        uni.addFaculty(f4);
        uni.addFaculty(f5);

        // courses
        Course c1 = new Course("CS101", "Programming Fundamentals", 3, "Undergraduate");
        Course c2 = new Course("CS241", "Object Oriented Programming", 3, "Undergraduate");
        Course c3 = new Course("CS601", "Machine Learning", 3, "Graduate");
        Course c4 = new Course("EE201", "Circuit Analysis", 3, "Undergraduate");
        Course c5 = new Course("MS101", "Principles of Management", 3, "Undergraduate");
        Course c6 = new Course("CE301", "Structural Analysis", 3, "Undergraduate");
        cs.addCourse(c1);
        cs.addCourse(c2);
        cs.addCourse(c3);
        ee.addCourse(c4);
        ms.addCourse(c5);
        ce.addCourse(c6);

        // who teaches what
        f1.assignCourse(c2);
        f5.assignCourse(c1);
        f5.assignCourse(c3);
        f2.assignCourse(c4);
        f3.assignCourse(c5);
        f4.assignCourse(c6);

        // class rooms and time slots
        Room[] rooms = uni.getRooms();
        csStaff.scheduleClass(c1, rooms[0], "Mon 09:00");
        csStaff.scheduleClass(c2, rooms[0], "Mon 11:00");
        csStaff.scheduleClass(c3, rooms[4], "Tue 14:00");
        csStaff.scheduleClass(c4, rooms[1], "Wed 09:00");
        csStaff.scheduleClass(c5, rooms[2], "Thu 10:00");
        csStaff.scheduleClass(c6, rooms[3], "Fri 08:30");

        // students
        Student s1 = new UndergraduateStudent("Ali Hassan", "ali@uni.edu", cs);
        Student s2 = new UndergraduateStudent("Fatima Noor", "fatima@uni.edu", ee);
        Student s3 = new GraduateStudent("Usman Tariq", "usman@uni.edu", cs, "AI in Education");
        admission.admit(uni, s1);
        admission.admit(uni, s2);
        admission.admit(uni, s3);

        // a few enrollments and marks
        c1.enroll(s1);
        c2.enroll(s1);
        c4.enroll(s2);
        c3.enroll(s3);
        exam.recordMarks(s1, c1, 88);
        exam.recordMarks(s1, c2, 76);
    }

    // ---------------- menu ----------------
    static void printMenu() {
        System.out.println();
        System.out.println("=== " + uni.getName() + " ===");
        System.out.println(" 1. View depts and courses");
        System.out.println(" 2. View all people (students, faculty, staff)");
        System.out.println(" 3. Admit a new student");
        System.out.println(" 4. Enroll a student in a course");
        System.out.println(" 5. Record marks (Exam Office)");
        System.out.println(" 6. Show student report card");
        System.out.println(" 7. Pay fee (Account Office)");
        System.out.println(" 8. Schedule a course in a room");
        System.out.println(" 9. View class rooms and bookings");
        System.out.println("10. View what each office does");
        System.out.println(" 0. Exit");
    }

    static void viewDepartments() {
        for (int d = 0; d < uni.getDeptCount(); d++) {
            Department dept = uni.getDept(d);
            System.out.println("[" + dept.getName() + "] Faculty: " + dept.getFacultyCount()
                    + " | Students: " + dept.getStudentCount());
            for (int c = 0; c < dept.getCourseCount(); c++) {
                System.out.println("    " + dept.getCourse(c));
            }
        }
    }

    static void viewPeople() {
        ArrayList<Person> everyone = uni.getEveryone();
        for (int i = 0; i < everyone.size(); i++) {
            System.out.println(everyone.get(i));     // each object prints its OWN getDetails()
        }
        System.out.println("Total people created: " + Person.getTotalPeople());
    }

    static void admitStudent() {
        String name = readText("Student name: ");
        String email = readText("Student email: ");

        System.out.println("Departments:");
        for (int d = 0; d < uni.getDeptCount(); d++) {
            System.out.println("  " + (d + 1) + ". " + uni.getDept(d).getName());
        }
        int deptChoice = readInt("Choose dept number: ");
        if (deptChoice < 1 || deptChoice > uni.getDeptCount()) {
            System.out.println("Invalid dept.");
            return;
        }
        Department dept = uni.getDept(deptChoice - 1);

        int level = readInt("Level (1 = Undergraduate, 2 = Graduate): ");
        Student s;
        if (level == 1) {
            s = new UndergraduateStudent(name, email, dept);
        } else if (level == 2) {
            String thesis = readText("Thesis topic: ");
            s = new GraduateStudent(name, email, dept, thesis);
        } else {
            System.out.println("Invalid level.");
            return;
        }
        System.out.println(admission.admit(uni, s));
    }

    static void enrollStudent() {
        Student s = uni.findStudent(readInt("Student ID: "));
        if (s == null) {
            System.out.println("No student with that ID.");
            return;
        }
        Course c = uni.findCourse(readText("Course code (example CS101): "));
        if (c == null) {
            System.out.println("No course with that code.");
            return;
        }
        System.out.println(c.enroll(s));
    }

    static void recordMarks() {
        Student s = uni.findStudent(readInt("Student ID: "));
        if (s == null) {
            System.out.println("No student with that ID.");
            return;
        }
        Course c = uni.findCourse(readText("Course code: "));
        if (c == null) {
            System.out.println("No course with that code.");
            return;
        }
        double marks = readDouble("Marks (0 to 100): ");
        System.out.println(exam.recordMarks(s, c, marks));
    }

    static void showReportCard() {
        Student s = uni.findStudent(readInt("Student ID: "));
        if (s == null) {
            System.out.println("No student with that ID.");
            return;
        }
        System.out.println(s.getDetails());
        for (int i = 0; i < s.getCourseCount(); i++) {
            System.out.println("   " + s.getCourse(i).getCode() + " " + s.getCourse(i).getName()
                    + " : " + s.getMark(i));
        }
        System.out.println("Average: " + roundTo2(s.getAverage()) + " | Grade: " + s.getGrade());
        System.out.println("Fee total: Rs. " + s.getTotalFee() + " | Paid: Rs. " + s.getFeePaid()
                + " | Due: Rs. " + s.getFeeDue());
    }

    static void payFee() {
        Student s = uni.findStudent(readInt("Student ID: "));
        if (s == null) {
            System.out.println("No student with that ID.");
            return;
        }
        double amount = readDouble("Amount to pay: ");
        System.out.println(account.collectFee(s, amount));
    }

    static void scheduleCourse() {
        Course c = uni.findCourse(readText("Course code: "));
        if (c == null) {
            System.out.println("No course with that code.");
            return;
        }
        Room[] rooms = uni.getRooms();
        for (int i = 0; i < rooms.length; i++) {
            System.out.println("  " + (i + 1) + ". " + rooms[i].getRoomNo()
                    + " (capacity " + rooms[i].getCapacity() + ")");
        }
        int r = readInt("Choose room number: ");
        if (r < 1 || r > rooms.length) {
            System.out.println("Invalid room.");
            return;
        }
        String slot = readText("Time slot (example Mon 09:00): ");
        System.out.println(csStaff.scheduleClass(c, rooms[r - 1], slot));
    }

    static void showRooms() {
        Room[] rooms = uni.getRooms();
        for (int i = 0; i < rooms.length; i++) {
            System.out.print(rooms[i].getRoomNo() + " (capacity " + rooms[i].getCapacity() + ") booked: ");
            if (rooms[i].getBookedCount() == 0) {
                System.out.print("none");
            }
            for (int j = 0; j < rooms[i].getBookedCount(); j++) {
                System.out.print(rooms[i].getBookedSlot(j) + "; ");
            }
            System.out.println();
        }
    }

    static void showStaffDuties() {
        for (int i = 0; i < uni.getStaffCount(); i++) {
            Staff st = uni.getStaff(i);
            System.out.println(st.getOffice() + " (" + st.getName() + "): " + st.performDuty());
        }
    }

    // ---------------- small helper methods for reading input safely ----------------
    static int readInt(String prompt) {
        System.out.print(prompt);
        while (!input.hasNextInt()) {
            input.next();                       // throw away the bad word
            System.out.print("Please type a whole number: ");
        }
        int value = input.nextInt();
        input.nextLine();                       // eat the leftover Enter key
        return value;
    }

    static double readDouble(String prompt) {
        System.out.print(prompt);
        while (!input.hasNextDouble()) {
            input.next();
            System.out.print("Please type a number: ");
        }
        double value = input.nextDouble();
        input.nextLine();
        return value;
    }

    static String readText(String prompt) {
        System.out.print(prompt);
        return input.nextLine();
    }

    static double roundTo2(double x) {
        return (int) (x * 100 + 0.5) / 100.0;
    }

    public static void main(String[] args) {
        loadSampleData();

        int choice = -1;
        while (choice != 0) {
            printMenu();
            choice = readInt("Choose an option: ");
            System.out.println();

            switch (choice) {
                case 1: viewDepartments(); break;
                case 2: viewPeople(); break;
                case 3: admitStudent(); break;
                case 4: enrollStudent(); break;
                case 5: recordMarks(); break;
                case 6: showReportCard(); break;
                case 7: payFee(); break;
                case 8: scheduleCourse(); break;
                case 9: showRooms(); break;
                case 10: showStaffDuties(); break;
                case 0: System.out.println("Goodbye!"); break;
                default: System.out.println("Invalid option, try again.");
            }
        }
    }
}
