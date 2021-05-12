import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class CopyMachine {

    public Object deepCopy(Object parentObject) throws IntrospectionException,
            InvocationTargetException, InstantiationException, IllegalAccessException {

        Class<?> aClass = parentObject.getClass(); //retrieving class of parent object

        PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(aClass, Object.class)
                .getPropertyDescriptors();

        List<Method> getters = Arrays.stream(propertyDescriptors)
                .map(PropertyDescriptor::getReadMethod)
                .collect(Collectors.toList()); //retrieving getters from parent object

        List<Object> values = getters.stream().map(getter -> {
            Object value = null;
            try {
                value = getter.invoke(parentObject);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return value;
        }).collect(Collectors.toList()); //retrieving values from parent object fields

        List<Method> setters = Arrays.stream(propertyDescriptors)
                .map(PropertyDescriptor::getWriteMethod)
                .collect(Collectors.toList()); //retrieving setters from parent object

        Constructor<?> constructor = Arrays.stream(aClass.getDeclaredConstructors()).collect(Collectors.toList())
                .stream().min(Comparator.comparing(Constructor::getParameterCount)).get(); //retrieving constructor from parent object
        //no need to check Optional on NutNULL cuz there will be at least one constructor in the class, for example default one

        List<Class<?>> constructorParametersTypes = Arrays.stream(constructor.getParameterTypes())
                .collect(Collectors.toList()); //retrieving parameter types from constructor arguments

        Object[] defaultParameters = constructorParametersTypes.stream()
                .map(o -> {
                    if (o.isPrimitive()){
                        switch (o.getTypeName())
                        {
                            case "byte":
                            case "short":
                            case "int":
                                return 0;
                            case "long":
                                return 0L;
                            case "float":
                                return 0.0f;
                            case "double":
                                return 0.0d;
                            case "char":
                                return '\u0000';
                            case "boolean":
                                return false;
                        }
                    }
                    return null;
                })
                .toArray(); //collecting an array of default values

        Object copiedObject;
        if (constructorParametersTypes.size() != 0){
            copiedObject = constructor.newInstance(defaultParameters);
        } else {
            copiedObject = constructor.newInstance();
        } //filling new object by default values


        Object finalCopiedObject = copiedObject;
        setters.forEach(setter -> {
            try {
                setter.invoke(finalCopiedObject, values.get(setters.indexOf(setter)));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }); //invoking setters for final object using values from parentObject

        return finalCopiedObject;
    }
}
