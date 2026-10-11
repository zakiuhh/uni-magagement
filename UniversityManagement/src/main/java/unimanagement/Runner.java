/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package unimanagement;

/**
 *
 * @authors Ufaq-009, Mojiz-044 and Zaki-048
 */

import java.util.Scanner;

public class Runner {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        University uni = new University("COMSATS University Islamabad, Wah Campus");

        //assigning faculty
        AdmissionOffice admission = new AdmissionOffice("Ms. Faheeda Akhtar", "admission@cuiwah.edu.pk");
        AccountOffice account = new AccountOffice("Ms. Hafsa Sajid", "accounts@cuiwah.edu.pk");
        ExamOffice exam = new ExamOffice("Ms. Faiza Alam", "exams@cuiwah.edu.pk");
        DepartmentalStaff csStaff;

        /* ---------------- sample data setup ---------------- */
        Department cs = uni.getDept(0);
        Department ee = uni.getDept(1);
        Department ms = uni.getDept(2);

        csStaff = new DepartmentalStaff("Mr. Ahmed Raza", "cs.support@cuiwah.edu.pk", cs);
        uni.addPerson(admission);
        uni.addPerson(account);
        uni.addPerson(exam);
        uni.addPerson(csStaff);

        Faculty f1 = new Faculty("Dr. Nadir Shah", "nadirshah@cuiwah.edu.pk", cs, "Professor");
        Faculty f2 = new Faculty("Sir Taimur Sajjad", "taimursajjad@cuiwah.edu.pk", ee, "Lecturer");
        Faculty f3 = new Faculty("Dr. Bilal Sheikh", "bilal@cuiwah.edu.pk", ms, "Lecturer");
        uni.addPerson(f1);
        uni.addPerson(f2);
        uni.addPerson(f3);

        Course c1 = new Course("CS101", "Programming Fundamentals", 3, "Undergraduate");
        Course c2 = new Course("CS102", "Object Oriented Programming", "Undergraduate");
        Course c3 = new Course("CS103", "Machine Learning", "Graduate");
        c1.setTeacher(f2);
        c2.setTeacher(f1);
        c3.setTeacher(f3);
        uni.addCourse(c1);
        uni.addCourse(c2);
        uni.addCourse(c3);

        csStaff.scheduleClass(uni, c1, uni.getRoom(0), "Mon 09:00");
        csStaff.scheduleClass(uni, c2, uni.getRoom(2), "Mon 11:00");
        csStaff.scheduleClass(uni, c3, uni.getRoom(4), "Tue 14:00");

        Student s1 = new UndergraduateStudent("Ufaq Akram", "ufaq@cui.edu", cs);
        Student s2 = new UndergraduateStudent("Mojiz Kazmi", "mojiz@cui.edu", ee);
        Student s3 = new GraduateStudent("Zaki Ul Hassan", "zaki@cui.edu", cs, "AI in Education");
        admission.admit(uni, s1);
        admission.admit(uni, s2);
        admission.admit(uni, s3);

        s1.enroll(c1);
        s1.enroll(c2);
        s2.enroll(c2);
        s3.enroll(c3);
        exam.recordMarks(s1, c1, 88);
        exam.recordMarks(s1, c2, 76);

        System.out.println("Welcome to the University Management System");

        int choice = -1;
        while (choice != 0) {
            char[] line = new char[45];
            for (int i = 0; i < line.length; i++) {
                line[i] = '=';
            }
            System.out.println();
            System.out.println(line);
            System.out.println(uni.getName());
            System.out.println(line);
            System.out.println(" 1. View departments");
            System.out.println(" 2. View courses");
            System.out.println(" 3. View all people (students, faculty, staff)");
            System.out.println(" 4. Admit a new student (Admission Office)");
            System.out.println(" 5. Enroll a student in a course");
            System.out.println(" 6. Schedule a course in a room (Departmental Staff)");
            System.out.println(" 7. Record marks (Exam Office)");
            System.out.println(" 8. View student result");
            System.out.println(" 9. Collect fee (Account Office)");
            System.out.println("10. View class rooms");
            System.out.println("11. View what each office does");
            System.out.println(" 0. Exit");
            System.out.print("Choose an option: ");
           
            choice = input.nextInt();
            input.nextLine();
            System.out.println();

            switch (choice) {
                case 1:
                    for (int i = 0; i < uni.getDeptCount(); i++) {
                        Department d = uni.getDept(i);
                        System.out.println((i + 1) + ". " + d.getName() + " | Students: " + d.getStudentCount());
                    }
                    break;

                case 2:
                    for (int i = 0; i < uni.getCourseCount(); i++) {
                        System.out.println(uni.getCourse(i));
                    }
                    break;

                case 3:
                    for (int i = 0; i < uni.getPeopleCount(); i++) {
                        System.out.println(uni.getPerson(i));
                    }
                    System.out.println("Total people: " + Person.getTotalPeople());
                    break;

                case 4:
                    System.out.print("Student name: ");
                    String name = input.nextLine();
                    System.out.print("Student email: ");
                    String email = input.nextLine();

                    for (int i = 0; i < uni.getDeptCount(); i++) {
                        Department d = uni.getDept(i);
                        System.out.println((i + 1) + ". " + d.getName() + " | Students: " + d.getStudentCount());
                    }
                    System.out.print("Choose department number: ");
                    int dNum = input.nextInt();
                    input.nextLine();
                    if (dNum < 1 || dNum > uni.getDeptCount()) {
                        System.out.println("Invalid department.");
                        break;
                    }
                    Department dept = uni.getDept(dNum - 1);

                    System.out.print("Level (1 = Undergraduate, 2 = Graduate): ");
                    int level = input.nextInt();
                    input.nextLine();

                    Student s;
                    if (level == 1) {
                        s = new UndergraduateStudent(name, email, dept);
                    } else if (level == 2) {
                        System.out.print("Thesis topic: ");
                        String thesis = input.nextLine();
                        s = new GraduateStudent(name, email, dept, thesis);
                    } else {
                        System.out.println("Invalid level.");
                        break;
                    }
                    System.out.println(admission.admit(uni, s));
                    break;

                case 5:
                    System.out.print("Student ID: ");
                    int sId = input.nextInt();
                    input.nextLine();
                    Student targetStudent = uni.findStudent(sId);
                    if (targetStudent == null) {
                        System.out.println("No student with that ID.");
                        break;
                    }

                    System.out.print("Course code (example CS101): ");
                    String cCode = input.nextLine();
                    Course targetCourse = uni.findCourse(cCode);
                    if (targetCourse == null) {
                        System.out.println("No course with that code.");
                        break;
                    }

                    System.out.println(targetStudent.enroll(targetCourse));
                    break;

                case 6:
                    System.out.print("Course code (example CS101): ");
                    String schedCode = input.nextLine();
                    Course schedCourse = uni.findCourse(schedCode);
                    if (schedCourse == null) {
                        System.out.println("No course with that code.");
                        break;
                    }

                    for (int i = 0; i < uni.getRoomCount(); i++) {
                        System.out.println((i + 1) + ". " + uni.getRoom(i));
                    }
                    System.out.print("Choose room number: ");
                    int rNum = input.nextInt();
                    input.nextLine();
                    if (rNum < 1 || rNum > uni.getRoomCount()) {
                        System.out.println("Invalid room.");
                        break;
                    }

                    System.out.print("Time slot (example Mon 09:00): ");
                    String slot = input.nextLine();
                    System.out.println(csStaff.scheduleClass(uni, schedCourse, uni.getRoom(rNum - 1), slot));
                    break;

                case 7:
                    System.out.print("Student ID: ");
                    int markStuId = input.nextInt();
                    input.nextLine();
                    Student markStudent = uni.findStudent(markStuId);
                    if (markStudent == null) {
                        System.out.println("No student with that ID.");
                        break;
                    }

                    System.out.print("Course code (example CS101): ");
                    String markCourseCode = input.nextLine();
                    Course markCourse = uni.findCourse(markCourseCode);
                    if (markCourse == null) {
                        System.out.println("No course with that code.");
                        break;
                    }

                    System.out.print("Marks (0 to 100): ");
                    double marksVal = input.nextDouble();
                    input.nextLine();
                    System.out.println(exam.recordMarks(markStudent, markCourse, marksVal));
                    break;

                case 8:
                    System.out.print("Student ID: ");
                    int resStuId = input.nextInt();
                    input.nextLine();
                    Student resStudent = uni.findStudent(resStuId);
                    if (resStudent == null) {
                        System.out.println("No student with that ID.");
                        break;
                    }

                    System.out.println(resStudent);
                    if (resStudent instanceof GraduateStudent) {
                        GraduateStudent g = (GraduateStudent) resStudent;
                        System.out.println("Thesis topic: " + g.getThesisTopic());
                    }

                    for (int i = 0; i < resStudent.getCourseCount(); i++) {
                        System.out.println("  " + resStudent.getCourse(i).getCode() + " : " + resStudent.getMark(i));
                    }

                    Double average = resStudent.getAverage();
                    Integer total = resStudent.getCourseCount();
                    int coursesCount = total;
                    System.out.println("Courses: " + coursesCount + " | Average: " + average + " | Grade: " + resStudent.getGrade());
                    break;

                case 9:
                    System.out.print("Student ID: ");
                    int feeStuId = input.nextInt();
                    input.nextLine();
                    Student feeStudent = uni.findStudent(feeStuId);
                    if (feeStudent == null) {
                        System.out.println("No student with that ID.");
                        break;
                    }

                    System.out.println("Total fee: Rs. " + feeStudent.getTotalFee() + " | Paid: Rs. " + feeStudent.getFeePaid()
                            + " | Due: Rs. " + feeStudent.getFeeDue());
                    System.out.println("1. Pay a part of the fee");
                    System.out.println("2. Pay the full fee due");
                    System.out.print("Choose: ");
                    int choiceFee = input.nextInt();
                    input.nextLine();

                    if (choiceFee == 1) {
                        System.out.print("Amount: ");
                        double amount = input.nextDouble();
                        input.nextLine();
                        System.out.println(account.collectFee(feeStudent, amount));
                    } else if (choiceFee == 2) {
                        System.out.println(account.collectFee(feeStudent));
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;

                case 10:
                    for (int i = 0; i < uni.getRoomCount(); i++) {
                        Room r = uni.getRoom(i);
                        System.out.print(r + " : ");
                        boolean used = false;
                        for (int j = 0; j < uni.getCourseCount(); j++) {
                            Course c = uni.getCourse(j);
                            if (c.getRoom() == r) {
                                System.out.print(c.getCode() + " at " + c.getSlot() + "; ");
                                used = true;
                            }
                        }
                        if (!used) {
                            System.out.print("no classes");
                        }
                        System.out.println();
                    }
                    break;

                case 11:
                    Staff[] offices = {admission, account, exam, csStaff};
                    for (int i = 0; i < offices.length; i++) {
                        System.out.println(offices[i].getOffice() + " (" + offices[i].getName() + "): " + offices[i].performDuty());
                    }
                    break;

                case 0:
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option, try again.");
            }
        }
        input.close();
    }
}