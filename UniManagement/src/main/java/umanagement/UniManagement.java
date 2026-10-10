/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package umanagement;

/**
 *
 * @author Zaki
 */

import java.util.ArrayList;

class Person {
    private static int nextId = 1;   // shared by all objects: gives each person a new id
    private int id;
    private String name;
    private String email;

    public Person(String name, String email){
        this.id = nextId++;          // use the current id, then add 1 for the next person
        setName(name);
        this.email = email;
    }

    public int getId(){return id;}
    public String getName(){return name;}

    public void setName(String name){this.name = name;}


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
        feePaid += amount;
    }

    @Override
    public String getDetails() {
        return super.getDetails() + " | Dept: " + dept.getName()+" | Courses: "+courses.size()+"/"+getMaxCourses();
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

    public Department getDept(){return dept;}

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

    // OVERLOADED: same name, different parameters. This one pays the full fee due.
    public String collectFee(Student s){
        if (s.getFeeDue() <= 0){
            return "Nothing due for " + s.getName() + ".";
        }
        return collectFee(s, s.getFeeDue());   // calls the other collectFee
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
    }


    public String getCode() { return code; }
    public String getName() { return name; }
    public String getLevel() { return level; }
    public void setInstructor(Faculty instructor) { this.instructor = instructor; }

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
