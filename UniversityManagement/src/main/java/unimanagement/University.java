/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package unimanagement;

/**
 *
 * @authors Ufaq-009, Mojiz-044 and Zaki-048
 */

import java.util.Date;
import java.util.Random;

/*
 * This file holds the implementation of all classes (abstraction).
 * Runner.java only uses these classes through their public methods.
 */

/* ===================== PERSON ===================== */

class Person {
    private static int nextId = 1;      // static variable: shared by all objects
    private int id;
    private String name;
    private String email;

    // no-argument constructor, chains to the parameterized one using this()
    public Person() {
        this("Unknown", "unknown@cui.edu");
    }

    public Person(String name, String email) {
        this.id = nextId++;
        this.name = name;
        this.email = email;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }

    // static method
    public static int getTotalPeople() { return nextId - 1; }

    // sub classes override this
    public String getRole() { return "Person"; }

    @Override
    public String toString() {
        return getRole() + " #" + id + " | " + name + " | " + email;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Person) {
            Person p = (Person) o;
            return this.id == p.id;      // same id means same person
        }
        return false;
    }
}

/* ===================== STUDENTS ===================== */

class Student extends Person {
    private Department dept;                    // aggregation: student has a department
    private Course[] courses = new Course[6];   // 1D array of objects
    private double[] marks = new double[6];     // 1D array of simple values
    private int courseCount = 0;
    private double feePaid = 0;
    private Date admissionDate;

    public Student(String name, String email, Department dept) {
        super(name, email);                     // super() calls the Person constructor
        this.dept = dept;
        this.admissionDate = new Date();        // Date library
    }

    // default values, sub classes override them
    public int getMaxCourses() { return 5; }
    public double getFeePerCourse() { return 20000; }
    public String getLevel() { return "Student"; }

    @Override
    public String getRole() { return getLevel() + " Student"; }

    public Department getDept() { return dept; }
    public Date getAdmissionDate() { return admissionDate; }
    public int getCourseCount() { return courseCount; }
    public Course getCourse(int index) { return courses[index]; }
    public double getMark(int index) { return marks[index]; }

    // an object (Course) is passed to this method
    public boolean hasCourse(Course c) {
        for (int i = 0; i < courseCount; i++) {
            if (courses[i].equals(c)) {
                return true;
            }
        }
        return false;
    }

    public String enroll(Course c) {
        if (!getLevel().equals(c.getLevel())) {
            return "Failed: " + getName() + " is " + getLevel() + " but " + c.getCode() + " is a " + c.getLevel() + " course.";
        }
        if (hasCourse(c)) {
            return "Failed: " + getName() + " is already in " + c.getCode() + ".";
        }
        if (courseCount >= getMaxCourses()) {
            return "Failed: " + getName() + " already has the maximum number of courses.";
        }
        if (c.isFull()) {
            return "Failed: the room of " + c.getCode() + " is full.";
        }
        courses[courseCount] = c;
        marks[courseCount] = 0;
        courseCount++;
        c.addStudent();
        return "Success: " + getName() + " is enrolled in " + c.getCode() + ".";
    }

    public boolean setMark(Course c, double mark) {
        if (mark < 0 || mark > 100) {
            return false;
        }
        for (int i = 0; i < courseCount; i++) {
            if (courses[i].equals(c)) {
                marks[i] = mark;
                return true;
            }
        }
        return false;
    }

    public double getAverage() {
        if (courseCount == 0) {
            return 0;
        }
        double total = 0;
        for (int i = 0; i < courseCount; i++) {
            total = total + marks[i];
        }
        return total / courseCount;
    }

    public char getGrade() {
        double avg = getAverage();
        if (avg >= 85) return 'A';
        else if (avg >= 70) return 'B';
        else if (avg >= 60) return 'C';
        else if (avg >= 50) return 'D';
        else return 'F';
    }

    public double getTotalFee() { return courseCount * getFeePerCourse(); }
    public double getFeePaid() { return feePaid; }
    public double getFeeDue() { return getTotalFee() - feePaid; }

    // a variable (amount) is passed by value
    public void payFee(double amount) { feePaid = feePaid + amount; }

    @Override
    public String toString() {
        return super.toString() + " | Dept: " + dept.getName() + " | Courses: " + courseCount + "/" + getMaxCourses();
    }
}

class UndergraduateStudent extends Student {

    public UndergraduateStudent(String name, String email, Department dept) {
        super(name, email, dept);
    }

    @Override
    public int getMaxCourses() { return 6; }

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
    public int getMaxCourses() { return 4; }

    @Override
    public double getFeePerCourse() { return 25000; }

    @Override
    public String getLevel() { return "Graduate"; }

    @Override
    public String toString() {
        return super.toString() + " | Thesis: " + thesisTopic;
    }
}

/* ===================== FACULTY ===================== */

class Faculty extends Person {
    private String designation;
    private Department dept;

    public Faculty(String name, String email, Department dept, String designation) {
        super(name, email);
        this.dept = dept;
        this.designation = designation;
    }

    public Department getDept() { return dept; }
    public String getDesignation() { return designation; }

    @Override
    public String getRole(){return "Faculty";}

    @Override
    public String toString() {
        return super.toString() + " | " + designation + " | Dept: " + dept.getName();
    }
}

/* ===================== STAFF AND ITS 4 OFFICES ===================== */

class Staff extends Person {
    private String office;

    public Staff(String name, String email, String office) {
        super(name, email);
        this.office = office;
    }

    public String getOffice() { return office; }

    // each office overrides this
    public String performDuty() { return "General staff duties."; }

    @Override
    public String getRole() { return office + " Staff"; }
}

class AdmissionOffice extends Staff {

    public AdmissionOffice(String name, String email) {
        super(name, email, "Admission Office");
    }

    // objects (University and Student) are passed to this method
    public String admit(University uni, Student s) {
        if (!uni.addStudent(s)) {
            return "Failed: the university cannot take more students.";
        }
        s.getDept().addStudent();
        return s.getName() + " is admitted to " + s.getDept().getName() + " as " + s.getLevel()
                + " (ID " + s.getId() + ") on " + s.getAdmissionDate();
    }

    @Override
    public String performDuty() { return "Admits new students and gives them an ID."; }
}

class AccountOffice extends Staff {

    public AccountOffice(String name, String email) {
        super(name, email, "Account Office");
    }

    // method overloading: pay a given amount
    public String collectFee(Student s, double amount) {
        if (amount <= 0) {
            return "Failed: amount must be more than 0.";
        }
        if (amount > s.getFeeDue()) {
            return "Failed: amount is more than the fee due (Rs. " + s.getFeeDue() + ").";
        }
        s.payFee(amount);
        Random r = new Random();                       // Random library
        int challanNo = 1000 + r.nextInt(9000);
        return "Challan #" + challanNo + " | " + new Date() + "\nReceived Rs. " + amount
                + " from " + s.getName() + ". Fee still due: Rs. " + s.getFeeDue();
    }

    // method overloading: pay the full fee due
    public String collectFee(Student s) {
        if (s.getFeeDue() <= 0) {
            return "Nothing is due for " + s.getName() + ".";
        }
        return collectFee(s, s.getFeeDue());
    }

    @Override
    public String performDuty() { return "Collects fees and keeps the payment records."; }
}

class ExamOffice extends Staff {

    public ExamOffice(String name, String email) {
        super(name, email, "Exam Office");
    }

    public String recordMarks(Student s, Course c, double marks) {
        if (s.setMark(c, marks)) {
            return "Marks saved: " + s.getName() + " got " + marks + " in " + c.getCode() + ".";
        }
        return "Failed: student is not in this course, or marks are not between 0 and 100.";
    }

    @Override
    public String performDuty() { return "Records marks and prepares results."; }
}

class DepartmentalStaff extends Staff {
    private Department dept;

    public DepartmentalStaff(String name, String email, Department dept) {
        super(name, email, "Departmental Support");
        this.dept = dept;
    }

    public String scheduleClass(University uni, Course c, Room r, String slot) {
        if (!uni.isRoomFree(r, slot)) {
            return "Failed: room " + r.getRoomNo() + " is already booked at " + slot + ".";
        }
        c.setSchedule(r, slot);
        return c.getCode() + " is now in room " + r.getRoomNo() + " at " + slot + ".";
    }

    @Override
    public String performDuty() { return "Helps " + dept.getName() + " with schedules and class rooms."; }
}

/* ===================== ROOM, COURSE, DEPARTMENT ===================== */

// immutable class: private final data, only getters, no setters
class Room {
    private final String roomNo;
    private final int capacity;

    public Room(String roomNo, int capacity) {
        this.roomNo = roomNo;
        this.capacity = capacity;
    }

    public String getRoomNo() { return roomNo; }
    public int getCapacity() { return capacity; }

    @Override
    public String toString() { return roomNo + " (capacity " + capacity + ")"; }
}

class Course {
    private String code;
    private String name;
    private int creditHours;
    private String level;                   // "Undergraduate" or "Graduate"
    private Faculty teacher;                // aggregation: course has a teacher
    private Room room;                      // aggregation: course has a room
    private String slot = "Not scheduled";
    private int enrolledCount = 0;

    // constructor overloading
    public Course(String code, String name, int creditHours, String level) {
        this.code = code;
        this.name = name;
        this.creditHours = creditHours;
        this.level = level;
    }

    public Course(String code, String name, String level) {
        this(code, name, 3, level);         // constructor chaining, 3 credit hours by default
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getLevel() { return level; }
    public Room getRoom() { return room; }
    public String getSlot() { return slot; }

    public void setTeacher(Faculty teacher) { this.teacher = teacher; }

    public void setSchedule(Room room, String slot) {
        this.room = room;
        this.slot = slot;
    }

    public boolean isFull() {
        return room != null && enrolledCount >= room.getCapacity();
    }

    public void addStudent() { enrolledCount++; }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Course) {
            Course other = (Course) o;
            return this.code.equals(other.code);   // same code means same course
        }
        return false;
    }

    @Override
    public String toString() {
        String teacherText = "No teacher";
        if (teacher != null) {
            teacherText = teacher.getName();
        }
        String roomText = "No room";
        if (room != null) {
            roomText = room.getRoomNo();
        }
        return code + " | " + name + " | " + level + " | " + creditHours + " cr | " + teacherText
                + " | " + roomText + " | " + slot + " | Enrolled: " + enrolledCount;
    }
}

class Department {
    private String name;
    private int studentCount = 0;

    public Department(String name) { this.name = name; }

    public String getName() { return name; }
    public int getStudentCount() { return studentCount; }

    public void addStudent() { studentCount++; }
}

/* ===================== UNIVERSITY ===================== */

class University {
    private String name;
    private Department[] depts = new Department[4];
    private Room[] rooms = new Room[5];
    private Course[] courses = new Course[10];
    private Student[] students = new Student[50];
    private Person[] people = new Person[100];     // students, faculty and staff together
    private int courseCount = 0;
    private int studentCount = 0;
    private int peopleCount = 0;

    public University(String name) {
        this.name = name;

        depts[0] = new Department("Computer Science");
        depts[1] = new Department("Electrical Engineering");
        depts[2] = new Department("Management Science");
        depts[3] = new Department("Civil Engineering");

        rooms[0] = new Room("G-01", 40);
        rooms[1] = new Room("G-02", 35);
        rooms[2] = new Room("G-03", 30);
        rooms[3] = new Room("G-04", 30);
        rooms[4] = new Room("Lab-01", 25);
    }

    public String getName() { return name; }
    public int getDeptCount() { return depts.length; }
    public Department getDept(int index) { return depts[index]; }
    public int getRoomCount() { return rooms.length; }
    public Room getRoom(int index) { return rooms[index]; }
    public int getCourseCount() { return courseCount; }
    public Course getCourse(int index) { return courses[index]; }
    public int getPeopleCount() { return peopleCount; }
    public Person getPerson(int index) { return people[index]; }

    public void addCourse(Course c) {
        courses[courseCount] = c;
        courseCount++;
    }

    public void addPerson(Person p) {
        people[peopleCount] = p;
        peopleCount++;
    }

    public boolean addStudent(Student s) {
        if (studentCount >= students.length || peopleCount >= people.length) {
            return false;
        }
        students[studentCount] = s;
        studentCount++;
        addPerson(s);
        return true;
    }

    public Student findStudent(int id) {
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getId() == id) {
                return students[i];
            }
        }
        return null;
    }

    public Course findCourse(String code) {
        for (int i = 0; i < courseCount; i++) {
            if (courses[i].getCode().equalsIgnoreCase(code)) {
                return courses[i];
            }
        }
        return null;
    }

    // true if no course is already using this room at this time
    public boolean isRoomFree(Room r, String slot) {
        for (int i = 0; i < courseCount; i++) {
            if (courses[i].getRoom() == r && courses[i].getSlot().equals(slot)) {
                return false;
            }
        }
        return true;
    }
}