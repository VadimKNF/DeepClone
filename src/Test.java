import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) throws NoSuchMethodException, IntrospectionException, InvocationTargetException, InstantiationException, IllegalAccessException {

        List<String> books = new ArrayList<>();
        books.add("book1");
        books.add("book2");
        books.add("book3");

        Man man = new Man("name1", 99, books);
        System.out.println(CopyMachine.deepCopy(man));

        /*Object copiedMan = CopyMachine.deepCopy(man);

        System.out.println(man);
        System.out.println(copiedMan);
        System.out.println(man == copiedMan);*/
    }

}
