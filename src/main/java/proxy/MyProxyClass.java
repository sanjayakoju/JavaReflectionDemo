package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyProxyClass implements InvocationHandler {

    Object originalObject;

    public MyProxyClass(Object originalObject) {
        this.originalObject = originalObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        System.out.println("Proxy Before the method is called");
        result = method.invoke(originalObject, args);
        System.out.println("Proxy After the method is called");
        return result;
    }

    public Object getOriginalObject() {
        return originalObject;
    }
}
