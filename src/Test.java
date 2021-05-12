import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Test {

    public static void main(String[] args) throws NoSuchMethodException, IntrospectionException, InvocationTargetException, InstantiationException, IllegalAccessException {

        CopyMachine copyMachine = new CopyMachine();

        List<String> books = new ArrayList<>();
        books.add("book1");
        books.add("book2");
        books.add("book3");

        Man man = new Man("name1", 99, books);

        Object clonedObject = copyMachine.deepCopy(man);

        PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(clonedObject.getClass(), Object.class)
                .getPropertyDescriptors();

        List<Object> fieldValuesOfClonedObject = Arrays.stream(propertyDescriptors)
                .map(propertyDescriptor -> {
                    try {
                        return propertyDescriptor.getReadMethod().invoke(clonedObject);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());

        System.out.println(man);
        System.out.println(clonedObject);
        System.out.println(man.equals(clonedObject));
        System.out.println(fieldValuesOfClonedObject);
    }

}
