package umanagement;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Zaki
 */

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Zaki
 */

public class Runner{

    static Scanner input = new Scanner(System.in);
    static University uni = new University("COMSATS University Islamabad, Wah Campus");

    // the four offices of the university
    static AdmissionOffice admission = new AdmissionOffice("Ms. Faheeda Akhtar", "admission@cuiwah.edu.pk");
    static AccountOffice account = new AccountOffice("Ms. Hafsa Sajid", "accounts@cuiwah.edu.pk");
    static ExamOffice exam = new ExamOffice("Ms. Faiza Alam", "exams@cuiwah.edu.pk");
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
        csStaff = new DepartmentalStaff("Mr. Ahmed Raza", "cs.support@cuiwah.edu", cs);
        uni.addStaff(csStaff);

        // faculty
        Faculty f1 = new Faculty("Dr. Nadir Shah", "nadirshah@cuiwah.edu.pk", cs, "Professor");
        Faculty f2 = new Faculty("Sir Taimur Sajjad", "taimursajjad@cuiwah.edu.pk", ee, "Lecturer");
        Faculty f3 = new Faculty("Dr. Bilal Sheikh", "bilal@cuiwah.edu.pk", ms, "Lecturer");
        uni.addFaculty(f1);
        uni.addFaculty(f2);
        uni.addFaculty(f3);

        // courses
        Course c1 = new Course("cs101", "Programming Fundamentals", 3, "Undergraduate");
        Course c2 = new Course("cs102", "Object Oriented Programming", 3, "Undergraduate");
        Course c3 = new Course("cs103", "Machine Learning", 3, "Graduate");


        cs.addCourse(c1);
        cs.addCourse(c2);
        cs.addCourse(c3);

        // who teaches what
        f1.assignCourse(c2);
        f2.assignCourse(c1);
        f3.assignCourse(c3);

        // class rooms and time slots
        Room[] rooms = uni.getRooms();
        csStaff.scheduleClass(c1, rooms[0], "Mon 09:00");
        csStaff.scheduleClass(c2, rooms[0], "Mon 11:00");
        csStaff.scheduleClass(c3, rooms[4], "Tue 14:00");

        // students
        Student s1 = new UndergraduateStudent("Ufaq Akram", "ufaq@cui.edu", cs);
        Student s2 = new UndergraduateStudent("Mojiz Kazmi", "mojiz@cui.edu", ee);
        Student s3 = new GraduateStudent("Zaki Ul Hassan", "zaki@cui.edu", cs, "AI in Education");
        admission.admit(uni, s1);
        admission.admit(uni, s2);
        admission.admit(uni, s3);

        // a few enrollments and marks
        c1.enroll(s1);
        c2.enroll(s2);
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
        System.out.println(" 6. Schedule a course in a room");
        System.out.println(" 7. View class rooms and bookings");
        System.out.println(" 8. View what each office does");
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
        System.out.print("Student name: ");
        String name = input.nextLine();
        System.out.print("Student email: ");
        String email = input.nextLine();

        System.out.println("Departments:");
        for (int d = 0; d < uni.getDeptCount(); d++) {
            System.out.println("  " + (d + 1) + ". " + uni.getDept(d).getName());
        }
        System.out.print("Choose dept number: ");
        int deptChoice = Integer.parseInt(input.nextLine());
        if (deptChoice < 1 || deptChoice > uni.getDeptCount()) {
            System.out.println("Invalid dept.");
            return;
        }
        Department dept = uni.getDept(deptChoice - 1);

        System.out.print("Level (1 = Undergraduate, 2 = Graduate): ");
        int level = Integer.parseInt(input.nextLine());
        Student s;
        if (level == 1) {
            s = new UndergraduateStudent(name, email, dept);
        } else if (level == 2) {
            System.out.print("Thesis topic: ");
            String thesis = input.nextLine();
            s = new GraduateStudent(name, email, dept, thesis);
        } else {
            System.out.println("Invalid level.");
            return;
        }
        System.out.println(admission.admit(uni, s));
    }

    static void enrollStudent() {
        System.out.print("Student ID: ");
        Student s = uni.findStudent(Integer.parseInt(input.nextLine()));
        if (s == null) {
            System.out.println("No student with that ID.");
            return;
        }
        System.out.print("Course code (example CS101): ");
        Course c = uni.findCourse(input.nextLine());
        if (c == null) {
            System.out.println("No course with that code.");
            return;
        }
        System.out.println(c.enroll(s));
    }

    static void recordMarks() {
        System.out.print("Student ID: ");
        Student s = uni.findStudent(Integer.parseInt(input.nextLine()));
        if (s == null) {
            System.out.println("No student with that ID.");
            return;
        }
        System.out.print("Course code: ");
        Course c = uni.findCourse(input.nextLine());
        if (c == null) {
            System.out.println("No course with that code.");
            return;
        }
        System.out.print("Marks (0 to 100): ");
        double marks = Double.parseDouble(input.nextLine());
        System.out.println(exam.recordMarks(s, c, marks));
    }

    static void scheduleCourse() {
        System.out.print("Course code: ");
        Course c = uni.findCourse(input.nextLine());
        if (c == null) {
            System.out.println("No course with that code.");
            return;
        }
        Room[] rooms = uni.getRooms();
        for (int i = 0; i < rooms.length; i++) {
            System.out.println("  " + (i + 1) + ". " + rooms[i].getRoomNo()
                    + " (capacity " + rooms[i].getCapacity() + ")");
        }
        System.out.print("Choose room number: ");
        int r = Integer.parseInt(input.nextLine());
        if (r < 1 || r > rooms.length) {
            System.out.println("Invalid room.");
            return;
        }
        System.out.print("Time slot (example Mon 09:00): ");
        String slot = input.nextLine();
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

    // ---------------- small helper ----------------
    /* static double roundTo2(double x) {
        return (int) (x * 100 + 0.5) / 100.0;
    } */

    public static void main(String[] args) {
        loadSampleData();

        int choice = -1;
        while (choice != 0) {
            printMenu();
            System.out.print("Choose an option: ");
            choice = input.nextInt();
            input.nextLine();   // flush the leftover Enter key so the next nextLine() call isn't skipped
            System.out.println();

            switch (choice) {
                case 1: viewDepartments(); break;
                case 2: viewPeople(); break;
                case 3: admitStudent(); break;
                case 4: enrollStudent(); break;
                case 5: recordMarks(); break;
                case 6: scheduleCourse(); break;
                case 7: showRooms(); break;
                case 8: showStaffDuties(); break;
                case 0: System.out.println("Goodbye!"); break;
                default: System.out.println("Invalid option, try again.");
            }
        }
    }
}
