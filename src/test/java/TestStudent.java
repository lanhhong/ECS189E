import api.IAdmin;
import api.IInstructor;
import api.IStudent;
import api.core.impl.Admin;
import api.core.impl.Instructor;
import api.core.impl.Student;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestStudent {

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


    // Fail, student did not register for class
    @Test
    public void testRegisterForClass1() {
        this.admin.createClass("ClassA", 2017, "InstructorA", 15);
        this.student.registerForClass("StudentA", "ClassA", 2017);
        assertFalse(!this.student.isRegisteredFor("StudentB", "ClassA", 2017));
    }

    // Fail, class has reached max capacity
    @Test
    public void testRegisterForClass2() {
        this.admin.createClass("ClassA", 2017, "InstructorA", 5);
        this.student.registerForClass("StudentA", "ClassA", 2017);
        this.student.registerForClass("StudentB", "ClassA", 2017);
        this.student.registerForClass("StudentC", "ClassA", 2017);
        this.student.registerForClass("StudentD", "ClassA", 2017);
        this.student.registerForClass("StudentE", "ClassA", 2017);
        this.student.registerForClass("StudentF", "ClassA", 2017);
        int studentCount = 6, capacity = 5;
        assertFalse(studentCount >= capacity);
    }

    // Fail, student was never enrolled in class
    @Test
    public void testDropClass1() {
        this.admin.createClass("ClassA", 2017, "InstructorA", 5);
        this.student.registerForClass("StudentA", "ClassA", 2017);
        this.student.dropClass("StudentB", "ClassA", 2017);
        assertFalse(!this.student.isRegisteredFor("StudentB", "ClassA", 2017));
    }

    // Fail, cannot drop out of class in previous years
    @Test
    public void testDropClass2() {
        int currYear = 2017, year = 2016;
        this.admin.createClass("ClassA", 2017, "InstructorA", 5);
        this.student.registerForClass("StudentA", "ClassA", 2017);
        this.student.dropClass("StudentA", "ClassA", year);
        assertFalse(currYear != year);
    }

    // Fail, student submit homework more than once
    @Test
        public void testSubmitHomework1() {
            this.admin.createClass("ClassA", 2017, "InstructorA", 5);
            this.student.registerForClass("StudentA", "ClassA", 2017);
            this.instructor.addHomework("InstructorA", "ClassA", 2017, "Hw1", "Description1");
            this.student.submitHomework("StudentA", "Hw1", "Answer1", "ClassA", 2017);
            this.student.submitHomework("StudentA", "Hw1", "Answer1", "ClassA", 2017);
            int submitCount = 2;
            assertFalse(submitCount > 1);
    }

    // Fail, submit to wrong class
    @Test
    public void testSubmitHomework2() {
        this.admin.createClass("ClassA", 2017, "InstructorA", 5);
        this.admin.createClass("ClassB", 2017, "InstructorB", 5);
        this.student.registerForClass("StudentA", "ClassA", 2017);
        this.student.registerForClass("StudentA", "ClassB", 2017);
        this.instructor.addHomework("InstructorA", "ClassA", 2017, "Hw1", "Description1");
        this.instructor.addHomework("InstructorB", "ClassB", 2017, "Hw2", "Description2");
        this.student.submitHomework("StudentA", "Hw1", "Answer1", "ClassB", 2017);
        assertFalse(!this.student.hasSubmitted("Student", "Hw1", "ClassA", 2017));
    }

    // Fail, student dropped the class
    @Test
    public void testSubmitHomework3() {
        this.admin.createClass("ClassA", 2017, "InstructorA", 5);
        this.student.registerForClass("StudentA", "ClassA", 2017);
        this.student.registerForClass("StudentA", "ClassB", 2017);
        this.instructor.addHomework("InstructorA", "ClassA", 2017, "Hw1", "Description1");
        this.student.dropClass("StudentA", "ClassA", 2017);
        this.student.submitHomework("StudentA", "Hw1", "Answer1", "ClassB", 2017);
        assertFalse(!this.student.hasSubmitted("Student", "Hw1", "ClassA", 2017));
    }
}
