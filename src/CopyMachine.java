import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class CopyMachine {

    public static Object deepCopy(Object object) throws IntrospectionException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Class<?> aClass = object.getClass();



        PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(aClass, Object.class).getPropertyDescriptors();

        List<Method> getters = Arrays.stream(propertyDescriptors)
                .map(PropertyDescriptor::getReadMethod)
                .collect(Collectors.toList());

        List<Method> setters = Arrays.stream(propertyDescriptors)
                .map(PropertyDescriptor::getWriteMethod)
                .collect(Collectors.toList());

        List<Object> parameters = getters.stream().map(getter -> {
            try {
                return getter.invoke(object);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());

        List<? extends Class<?>> gettersReturnTypes = parameters.stream().map(Object::getClass).collect(Collectors.toList());

        Class[] classes = new Class[gettersReturnTypes.size()];
        gettersReturnTypes.toArray(classes);

        Constructor<?> constructor = Arrays.stream(aClass.getDeclaredConstructors()).collect(Collectors.toList())
                .stream().max(Comparator.comparing(Constructor::getParameterCount)).get();

        List<Class<?>> constructorParametersTypes = Arrays.stream(constructor.getParameterTypes()).collect(Collectors.toList());

        Object o = constructor.newInstance();

        return constructorParametersTypes;
    }
}
