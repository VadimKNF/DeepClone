import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        List<String> books = new ArrayList<>();
        books.add("book1");
        books.add("book2");
        books.add("book3");

        Man man = new Man("name1", 99, books);

        Object copiedMan = CopyMachine.deepCopy(man);

        System.out.println(man);
        System.out.println(copiedMan);
        System.out.println(man == copiedMan);
    }

}
