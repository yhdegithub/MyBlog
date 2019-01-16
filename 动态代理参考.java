//接口
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
interface  Saying{
    public void sayHello(String name);
    public void talking(String name);
}
//一个简单的实现类
class SayingImpl implements Saying {

//    Executors pool = (Executors) Executors.newSingleThreadExecutor();

    @Override
    public void sayHello(String name){
        System.out.println( name + "   说 ： 大家好啊！");
    }
    @Override
    public void talking(String name){
        System.out.println( name + " : 构建民主和谐的社会！");
    }
}

/**
JDK动态代理主要用到java.lang.reflect包中的两个类：Proxy和InvocationHandler.

InvocationHandler是一个接口，通过实现该接口定义横切逻辑，并通过反射机制调用目标类的代码，
    动态的将横切逻辑和业务逻辑编织在一起。

Proxy利用InvocationHandler动态创建一个符合某一接口的实例，生成目标类的代理对象
**/
class MyInvocationHandler implements InvocationHandler {

    private Object target;
    public MyInvocationHandler(Object target){
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
        //目标方法前执行
        System.out.println("\n------- 有请下一位观众 ------");
        Object obj = method.invoke(target,args);       //执行代理目标的某个方法
        //目标方法执行后
        System.out.println("-------- 大家鼓掌！ --------");
        return obj;
    }
}

public class testProxy {
    public static void main(String args[]){
        //希望被代理的类
        Saying target = new SayingImpl();
        //把目标类和横切类交织到一起
        MyInvocationHandler handler = new MyInvocationHandler(target);
        //创建动态代理
        Saying proxy = (Saying) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),        //目标类加载器
                target.getClass().getInterfaces(),          //目标类的接口
                handler                                      // 横切类
        );
        proxy.sayHello(" 小明 ");
        proxy.talking(" 小王 ");
    }
}







import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibProxy implements MethodInterceptor {
    Enhancer enhancer = new Enhancer();
    public Object getProxy(Class clazz){
        //设置需要的子类
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        //通过字节码创建子类实例
        return enhancer.create();
    }
    @Override
    public Object  intercept(Object obj, Method method , Object[] args, MethodProxy proxy) throws Throwable{
        //执行之前
        System.out.println("\n------- 有请下一位观众 ------");
        //目标方法调用
        Object result = proxy.invokeSuper(obj,args);
        //执行之后
        System.out.println("-------- 大家鼓掌！ --------");
        return result;
    }
}


class CglibProxyTest{
    public static void main(String args[]){
        CglibProxy proxy = new CglibProxy();
        //通过动态代理生成子类的方式创建代理类
        Saying target = (Saying)proxy.getProxy(SayingImpl.class);
        target.sayHello("小圆");
        target.talking("晓蘅");


    }


}

