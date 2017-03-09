import api.IAdmin;
import api.IInstructor;
import api.IStudent;
import api.core.impl.Admin;
import api.core.impl.Instructor;
import api.core.impl.Student;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestInstructor {

    private IAdmin admin;
    private IInstructor instructor;
    private IStudent student;

    // This will run before every test case
    @Before
    public void setup() {
        this.admin = new Admin();
        this.instructor = new Instructor();
        this.student = new Student();
    }

    // Pass
    @Test
    public void testAddHomework1() {
        this.admin.createClass("ClassA", 2017, "Instructor1", 15);
        this.instructor.addHomework("Instructor1", "ClassA", 2017, "Hw1", "Description1");
        assertTrue(this.instructor.homeworkExists("ClassA", 2017, "Hw1"));
    }

    // Fail, cannot add to nonexistent class
    @Test
    public void testAddHomework2() {
        this.instructor.addHomework("Instructor1", "ClassA", 2017, "Hw1", "Description1");
        assertFalse(!this.admin.classExists("ClassA", 2017));
    }

    // Fail, cannot add homework to a class in a previous year
    @Test
    public void testAddHomework3() {
        int currYear = 2017, year = 2016;
        this.admin.createClass("ClassA", year, "Instructor1", 15);
        this.instructor.addHomework("Instructor1", "ClassA", year, "Hw1", "Description1");
        assertFalse(currYear != year);
    }

    // Fail, a different instructor cannot add homework to a class for another instructor
    @Test
    public void testAddHomework4() {
        String instructor2 = "Instructor2";
        this.admin.createClass("ClassA", 2017, "Instructor1", 15);
        this.instructor.addHomework(instructor2, "ClassA", 2017, "Hw1", "Description1");
        assertFalse(this.admin.getClassInstructor("ClassA", 2017) != instructor2);
    }

    // Fail, homework name is the same as another homework
    @Test
    public void testAddHomework5() {
        String oldHw = "Hw1", newHw = "Hw1";
        this.admin.createClass("ClassA", 2017, "Instructor1", 15);
        this.instructor.addHomework("Instructor1", "ClassA", 2017, oldHw, "Description1");
        this.instructor.addHomework("Instructor1", "ClassA", 2017, newHw, "Description1");
        assertFalse(this.instructor.homeworkExists("ClassA", 2017, "Hw1") && oldHw == newHw);
    }


    // Fail, student is not enrolled in class
    @Test
    public void testAssignGrade1() {
        this.admin.createClass("ClassA", 2017, "Instructor1", 15);
        this.admin.createClass("ClassB", 2017, "Instructor1", 15);
        this.student.registerForClass("StudentA", "ClassB", 2017);
        this.instructor.addHomework("Instructor1", "ClassA", 2017, "Hw1", "Description1");
        this.student.submitHomework("StudentB", "Hw1", "Answer1", "ClassB", 2017);
        this.instructor.assignGrade("Instructor1", "ClassA", 2017, "Hw1", "StudentA", 90);
        assertFalse(this.admin.classExists("ClassA", 2017) &&
                !this.student.isRegisteredFor("StudentA", "ClassA", 2017));
    }

    // Fail, submitted wrong homework for wrong class
    @Test
    public void testAssignGrade2() {
        this.admin.createClass("ClassA", 2017, "InstructorA", 15);
        this.admin.createClass("ClassB", 2017, "InstructorB", 15);
        this.student.registerForClass("StudentA", "ClassA", 2017);
        this.student.registerForClass("StudentA", "ClassB", 2017);
        this.instructor.addHomework("InstructorA", "ClassA", 2017, "Hw1", "Description1");
        this.instructor.addHomework("InstructorB", "ClassB", 2017, "Hw2", "Description1");
        this.student.submitHomework("StudentA", "Hw2", "Answer1", "ClassA", 2017);
        this.instructor.assignGrade("Instructor1", "ClassA", 2017, "Hw1", "StudentA", 90);
        assertFalse(this.instructor.getGrade("ClassA", 2017, "Hw1", "StudentA") == null);
    }

    // Fail, grade cannot drop below 0
    @Test
    public void testAssignGrade3() {
        this.admin.createClass("ClassA", 2017, "InstructorA", 15);
        this.student.registerForClass("StudentA", "ClassA", 2017);
        this.instructor.addHomework("InstructorA", "ClassA", 2017, "Hw1", "Description1");
        this.student.submitHomework("StudentA", "Hw2", "Answer1", "ClassA", 2017);
        this.instructor.assignGrade("Instructor1", "ClassA", 2017, "Hw1", "StudentA", -1);
        assertFalse(this.instructor.getGrade("ClassA", 2017, "Hw1", "StudentA") == null);
    }

}
