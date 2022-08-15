package bean;

import annotation.MyInterface;

public class MyASecondBean implements MyInterface {

    private int MyInt = 20;
    private String myString = "My Second Bean";

    public MyASecondBean() {
        System.out.println("MYASecond bean call");
    }

    public int getMyInt() {
        return MyInt;
    }

    public void setMyInt(int myInt) {
        MyInt = myInt;
    }

    public String getMyString() {
        return myString;
    }

    public void setMyString(String myString) {
        this.myString = myString;
    }

    @Override
    public String toString() {
        return "MySecondBean{" +
                "MyInt=" + MyInt +
                ", myString='" + myString + '\'' +
                '}';
    }

    @Override
    public void method() {
        System.out.println("MYASeccond Bean");
    }
}
