import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CopyMachine {

    public static Object deepCopy(Object object) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Class<?> aClass = object.getClass();

        Object copiedObject = object.getClass().getDeclaredConstructor().newInstance();

        List<Method> publicMethods = Arrays.stream(aClass.getMethods()).collect(Collectors.toList());

        List<Method> getters = publicMethods.stream()
                .collect(Collectors.groupingBy(method -> method.getName().startsWith("get") && method.getParameterCount() == 0))
                .values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        Map<String, Method> setters = publicMethods.stream()
                .collect(Collectors.groupingBy(method -> method.getName().startsWith("set") && method.getParameterCount() == 1))
                .values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Method::getName, method -> method));

        Map<String, Object> gettersInvokationMap = getters.stream().collect(Collectors.toMap(Method::getName, method -> {
            Object invoke = new Object();
            try {
                invoke = method.invoke(object);
                return invoke;
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return invoke;
        }));

        setters.forEach((s, o) -> {
             if (gettersInvokationMap.containsKey("get" + s.substring(2))) {
                 try {
                     o.invoke(copiedObject, gettersInvokationMap.get("get" + s.substring(2)));
                 } catch (IllegalAccessException | InvocationTargetException e) {
                     e.printStackTrace();
                 }
             }
        });

        return copiedObject;
    }

}
