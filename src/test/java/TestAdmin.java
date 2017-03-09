import api.IAdmin;
import api.IInstructor;
import api.IStudent;
import api.core.impl.Admin;
import api.core.impl.Instructor;
import api.core.impl.Student;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestAdmin {

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
    public void testCreateClass1() {
        this.admin.createClass("ClassA", 2017, "Instructor1", 15);
        assertTrue(this.admin.classExists("ClassA", 2017));
    }

    // Pass
    @Test
    public void testCreateClass2() {
        this.admin.createClass("ClassA", 2017, "Instructor1", 15);
        this.admin.createClass("ClassB", 2017, "Instructor1", 15);
        assertTrue(this.admin.classExists("ClassA", 2017) && this.admin.classExists("ClassB", 2017));
    }

    // Fail, class has the same name as another class
    @Test
    public void testCreateClass3() {
        String classB = "ClassB", newClass = "ClassB";
        this.admin.createClass(classB, 2017, "Instructor1", 15);
        this.admin.createClass(newClass, 2017, "Instructor2", 15);
        assertFalse(this.admin.classExists("ClassB", 2017) && classB == newClass);
    }

    // Fail, no instructor can be assigned to more than two courses in a year
    @Test
    public void testCreateClass4() {
        this.admin.createClass("ClassA", 2017, "Instructor1", 15);
        this.admin.createClass("ClassB", 2017, "Instructor1", 15);
        this.admin.createClass("ClassC", 2017, "Instructor1", 15);
        assertFalse(this.admin.classExists("ClassA", 2017)  && this.admin.classExists("ClassB", 2017) && this.admin.classExists("ClassC", 2017));
    }

    // Fail, year cannot be in the past
    @Test
    public void testCreateClass5() {
        this.admin.createClass("ClassD", 2016, "Instructor2", 15);
        assertFalse(this.admin.classExists("ClassD", 2016));
    }

    // Fail, capacity has to be greater than 0
    @Test
    public void testCreateClass6() {
        this.admin.createClass("ClassE", 2017, "Instructor3", 0);
        assertFalse(this.admin.classExists("ClassE", 2017));
    }

    // Pass
    @Test
    public void testChangeCapacity1() {
        this.admin.createClass("ClassA", 2017, "Instructor1", 15);
        this.student.registerForClass("studentA", "classA", 2017);
        this.student.registerForClass("studentB", "classA", 2017);
        this.student.registerForClass("studentC", "classA", 2017);
        this.student.registerForClass("studentD", "classA", 2017);
        this.student.registerForClass("studentE", "classA", 2017);
        int newCapacity = 6;
        this.admin.changeCapacity("ClassA", 2017, newCapacity);
        assertTrue(this.admin.getClassCapacity("ClassA", 2017) <= newCapacity);
    }

    // Pass
    @Test
    public void testChangeCapacity2() {
        this.admin.createClass("ClassA", 2017, "Instructor1", 15);
        this.student.registerForClass("StudentA", "ClassA", 2017);
        this.student.registerForClass("StudentB", "ClassA", 2017);
        this.student.registerForClass("StudentC", "ClassA", 2017);
        this.student.registerForClass("StudentD", "ClassA", 2017);
        this.student.registerForClass("StudentE", "ClassA", 2017);
        int studentCount = 5, newCapacity = 5;
        this.admin.changeCapacity("ClassA", 2017, newCapacity);
        assertTrue(studentCount <= newCapacity);
    }

    // Fail, capacity must be at least equal to the number of students enrolled
    @Test
    public void testChangeCapacity3() {
        this.admin.createClass("ClassA", 2017, "Instructor1", 15);
        this.student.registerForClass("StudentA", "ClassA", 2017);
        this.student.registerForClass("StudentB", "ClassA", 2017);
        this.student.registerForClass("StudentC", "ClassA", 2017);
        this.student.registerForClass("StudentD", "ClassA", 2017);
        this.student.registerForClass("StudentE", "ClassA", 2017);
        int studentCount = 5, newCapacity = 4;
        this.admin.changeCapacity("ClassA", 2017, newCapacity);
        assertFalse(studentCount > newCapacity);
    }

    // Fail, cannot change capacity for nonexistent class
    @Test
    public void testChangeCapacity4() {
        this.admin.createClass("ClassA", 2017, "Instructor1", 15);
        this.admin.changeCapacity("ClassB", 2017, 20);
        assertFalse(!this.admin.classExists("ClassB", 2017));
    }

    // Fail, cannot change capacity for class in a previous year
    @Test
    public void testChangeCapacity5() {
        int currYear = 2017, year = 2016;
        this.admin.createClass("ClassA", year, "Instructor1", 15);
        this.admin.changeCapacity("ClassA", year, 20);
        assertFalse(currYear != year);
    }

}
