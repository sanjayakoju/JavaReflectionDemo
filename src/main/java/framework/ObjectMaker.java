package framework;

import annotation.*;
import proxy.MyProxyClass;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ObjectMaker {

    Map<Class, Object> context = new HashMap<>();

    Object beanInstance = null;

    public ObjectMaker(Class configurationClass) {

        if (configurationClass.isAnnotationPresent(MyBasePackage.class)) {
            MyBasePackage myBasePackageAnnotaion = (MyBasePackage) configurationClass.getAnnotation(MyBasePackage.class);
//            System.out.println("scanning package "+myBasePackageAnnotaion.packageName());
//            System.out.println("Found Configuration");

            InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(myBasePackageAnnotaion.packageName().replaceAll("[.]", "/"));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            Set<Class> classSet = bufferedReader.lines().filter(line -> line.endsWith(".class")).map(line -> {
                try {
                    return Class.forName(myBasePackageAnnotaion.packageName()+"."+line.substring(0, line.lastIndexOf(".")));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toSet());

            for (Class beanClass: classSet) {
                System.out.println("Bean "+beanClass.getName());
                try {
//                    Object beanInstance = beanClass.newInstance();
                    Object beanInstance = null;
                    boolean objectCreated = false;
                    for (Constructor constructor: beanClass.getConstructors()) {
                        if (constructor.isAnnotationPresent(MyConstructorDI.class)) {
                            Class parameterType = constructor.getParameterTypes()[0];
                            Object parameterObject = context.get(parameterType);
//                            beanInstance = constructor.newInstance(parameterObject);
                            MyProxyClass myProxyClass= (MyProxyClass) Proxy.getInvocationHandler(parameterObject);
                            beanInstance = constructor.newInstance(myProxyClass.getOriginalObject());
//                            System.out.println("bean instance"+beanInstance);
                            objectCreated = true;
                        }
                    }
                    if (!objectCreated) {
                        beanInstance = beanClass.getDeclaredConstructor().newInstance();
                    }
                    System.out.println(beanInstance);

                    for(Method method: beanClass.getMethods()) {
//                        System.out.println(method.getName());
                        if(method.isAnnotationPresent(MyInit.class)) {
                            boolean oldState = method.isAccessible();
                            method.setAccessible(true);
                            method.invoke(beanInstance);
                            method.setAccessible(oldState);
                        }
                    }

                    context.put(beanClass, beanInstance);

                    Object proxyObject = Proxy.newProxyInstance(beanClass.getClassLoader(),
                            new Class[]{MyInterface.class}, new MyProxyClass(context));

                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }



//            for (Object beanContext: context.values()) {
//                for (Field field: beanContext.getClass().getDeclaredFields()) {
//                    if (field.isAnnotationPresent(MyFieldDI.class)) {
//                       Object diObject = context.get(field.getType());
//                       field.setAccessible(true);
//                        try {
//                            field.set(beanContext, diObject);
//                        } catch (IllegalAccessException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }
//            }

        } else {
            throw new RuntimeException("Must Have annotation MyBasePackage for class"+configurationClass.getName());
        }
    }

//    public Object getBean(Class beanClass) {
//        if (context.containsKey(beanClass)) {
//            System.out.println("Found");
//            return context.get(beanClass);
//        } else {
//            System.out.println("Not Found");
//            return null;
//        }
//    }

        // Using Casting
    public <T> T getBean(Class<T> beanClass) {
        if (context.containsKey(beanClass)) {
            System.out.println("Found");
            return (T) context.get(beanClass);
        } else {
            System.out.println("Not Found");
            return null;
        }
    }

    public void close() {
//        Object beanInstance = null;
        for (Object bean: context.values()) {
            for(Method method: bean.getClass().getDeclaredMethods()) {
//              System.out.println(method.getName());
                if(method.isAnnotationPresent(MyDestroy.class)) {
                    boolean oldState = method.isAccessible();
                    method.setAccessible(true);
//                    method.invoke(beanInstance);
                    method.setAccessible(oldState);
                }
            }
        }
    }
}
