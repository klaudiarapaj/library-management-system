package util;

import model.Book;
import model.LibraryItem;
import model.Magazine;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

//reflection class to test
public class ReflectionTest {

    public static void main(String[] args) {
        // created different instances
        LibraryItem book = new Book(1,"Klaudia Rapaj", 2002, 1006, "me","romance");
        Magazine magazine= new Magazine(2,"test", 1234, 2324, 5894 );

        System.out.println("\nReflection:");
        inspectClassDetails(book); //class details of the book object
        inspectClassDetails(magazine); //class details of the magazine object
        inspectMethods(book); //method details of the book object
    }

    //class details method
    public static void inspectClassDetails(Object obj) {
        Class<?> cl = obj.getClass(); // get runtime class
        System.out.println("Inspecting Class: " + cl.getName());

        //retrieve all declared fields
        Field[] fields = cl.getDeclaredFields();
        System.out.println("\nUnique Class Fields:");
        for (Field field : fields) {
            field.setAccessible(true); // make private fields accessible
            try {
                System.out.println("- Name: " + field.getName());
                System.out.println("  Type: " + field.getType().getSimpleName());
                System.out.println("  Value: " + field.get(obj)); //get field value
            } catch (IllegalAccessException e) {
                System.out.println("  [Error accessing field value]");
            }
        }

        // display its superclass
        System.out.println("\nSuperclass: " + cl.getSuperclass().getName());
    }

    //methods details method
    public static void inspectMethods(Object obj) {
        Class<?> cl = obj.getClass();
        System.out.println("Inspecting Class: " + cl.getName());
        System.out.println("\nMethods:");
        for (Method method : cl.getDeclaredMethods()) {
            System.out.println("- Name: " + method.getName()); //get the name of the method
            System.out.println("  Return Type: " + method.getReturnType().getSimpleName()); //get the return type
        }
    }
}

