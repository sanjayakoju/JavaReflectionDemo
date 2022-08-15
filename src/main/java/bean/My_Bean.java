package bean;

import annotation.*;

public class My_Bean implements MyInterface {

    private int myInt;
    private String myString;

    @MyFieldDI
    private MyASecondBean myASecondBean;

    public My_Bean() {
    }

    @MyConstructorDI
    public My_Bean(MyASecondBean myASecondBean) {
        this.myASecondBean = myASecondBean;
        System.out.println("My constructor Call");
    }

    @MyInit
    @MyDestroy
    public void method() {
        System.out.println("My Bean Method() call");
    }



//    public MyBean(MyASecondBean myASecondBean) {
//        this.myASecondBean = myASecondBean;
//    }

    public My_Bean(int myInt, String myString) {
        this.myInt = myInt;
        this.myString = myString;
    }



    @Override
    public String toString() {
        return "MyBean{" +
                "myInt=" + myInt +
                ", myString='" + myString + '\'' +
                ", myASecondBean=" + myASecondBean +
                '}';
    }
}
