/**
*   这篇博客主要介绍clone()的浅克隆 和深度克隆的区别 ，及深度克隆的实现方式
*
*new
*new操作符的本意是分配内存。程序执行到new操作符时，首先去看new操作符后面的类型，*因为知道了类型，才能知道要分配多大的内存空间。
*分配完内存之后，再调用构造函数，填*充对象的各个域，这一步叫做对象的初始化，构造方法返回后，一个对象创建完毕，可以把
*他的引用（地址）发布到外部，在外部就可以使用这个引用操纵这个对象。
--------------------- 
*clone 
*clone在第一步是和new相似的， 都是分配内存，调用clone方法时，分配的内存和源对象（即调用clone方法的对象）相同
*，然后再使用原对象中对应的各个域，填充新对象的域， 
*填充完成之后，clone方法返回，一个新的相同的对象被创建，同样可以把这个新对象的引*用发布到外部。 
*如何利用clone的方式来得到一个对象呢？
*/
//实现深克隆  实现Serializeable接口 ， 实现深度克隆
import java.io.Serializable;
public class Person implements Cloneable, Serializable {
  private String name ;
  private int age ;
  public Person(String name,int age){
      this.name = name;
      this.age = age;
  }
  //eat
  public void eat(){
      System.out.println(" I am eating !! ");
  }
  //各函数
    public String getName(){
      return name;
    }
    public int getAge(){
      return age;
    }
    public void setName(String name){
      this.name = name;
    }
    public void setAge(int age){
      this.age = age;
    }
    //克隆函数
    @Override
    public Object clone() throws CloneNotSupportedException{
      return (Person)super.clone();
    }
}
//克隆函数
//使用该工具类的对象必须要实现 Serializable 接口，否则是没有办法实现克隆的。
import java.util.*;
import java.io.*;
public class CloneUtils implements Serializable{
    public static <T extends Serializable> T clone(T   obj){
        T cloneObj = null;
        try {
            //写入字节流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream obs = new   ObjectOutputStream(out);
            obs.writeObject(obj);
            obs.close();
            //分配内存，写入原始对象，生成新对象
            ByteArrayInputStream ios = new  ByteArrayInputStream(out.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(ios);
            //返回生成的新对象
            cloneObj = (T) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cloneObj;
    }
}
//测试函数
public class TestClone {
    public static void main(String args[]){
        try{
            Person p = new Person("jack",10);
            Person p1 =  CloneUtils.clone(p);
            System.out.println(p.getName()==p1.getName() ? "惨了是同一个对象 " :"还好不一样");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
