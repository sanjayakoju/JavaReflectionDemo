import annotation.MyBasePackage;
import annotation.MyInterface;
import bean.My_Bean;
import framework.ObjectMaker;

import java.lang.reflect.Field;

@MyBasePackage(packageName = "bean")
public class Main {
    public static void main(String[] args) {
//        myIDE();

        ObjectMaker objectMaker = new ObjectMaker(Main.class);

        MyInterface myBean = objectMaker.getBean(My_Bean.class);
        System.out.println("my_bean class"+myBean);

        myBean.method();

//        MyBean myBean = (MyBean) objectMaker.getBean(objectMaker.getClass());
        //Using Casting
//        ObjectMaker myBean = objectMaker.getBean(objectMaker.getClass());
//        System.out.println(myBean);

//        MyASecondBean myASecondBean = objectMaker.getBean(MyASecondBean.class);

//        objectMaker.close();

//        myBean.method();
    }

    private static void myIDE() {
        try {
            My_Bean myBean = new My_Bean(10,"String");
            System.out.println(myBean);
            Class myBeanClass = Class.forName("bean.My_Bean");
            for (Field field: myBeanClass.getDeclaredFields()) {
                System.out.println(field.getName());
                if (field.getName().equals("myInt")) {
                    field.setAccessible(true);
                    field.set(myBean, 5);
                }
            }
            myBeanClass.getDeclaredFields();
            System.out.println("My Bean again"+myBean);
        } catch (ClassNotFoundException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
