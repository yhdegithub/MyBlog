1. 只适用于单线程环境
public class Singleton{
	private static Singleton  instance = null ;
	//私有构造函数
	private Singleton(){
		
	}
	//单线程下获得单例,多线程时候 ，判断instance==null，可能导致重复创建instace 。
	public static Singleton getInstance(){
		if(instance==null)
          instance = new Singleton();
        return  instance ;	  
	}
}



适合多线程时候 。 

2. 懒汉式 

public class Singleton{
	
	private static Singleton  instance = null ;
	//私有构造函数
	private Singleton(){
		
	}
//直接加锁synchronized ，故名为懒汉 ，但锁的获取释放比较耗时
	public static synchronized Singleton getInstance(){
		if(instance == null)
			instance = new Singleton();
		
		return instace;
		
	}
	
	
}





3. 双重判断加锁 ，基于2的稍微改进 ，但容易出错

public class Singleton {
    private volatile static Singleton instance=null;      //一定要加上volatile变量
    private Singleton(){
        
    }
    public static Singleton getInstance(){
        if(instance==null){
            synchronized(Singleton.class){
                if(instance==null){
                    instance=new Singleton();
                }
            }
        }
        return instance;
    }
}



4. 饿汉式

public class Singleton{
	//慌里慌张的创建实例 ，不是按需的 ， 所以叫饿死鬼方式
	private static Singleton  instance = new Singleton() ;
	//私有构造函数
	private Singleton(){
	
	}
	public static Singleton getInstance(){
		return instance;
	}
	
}



5 . 最优的方式 ，建议使用 ，按需创建  ,使用静态内部类

public class Singleton{
	private Singleton(){
		
	}
	//使用静态内部类 ，利用Java特性， static 只会初始化一次
/**
	定义一个私有的内部类，在第一次用这个嵌套类时，会创建一个实例。
而类型为SingletonHolder的类，
只有在Singleton.getInstance()中调用，由于私有的属性，他人无法使用SingleHolder，
不调用Singleton.getInstance()就不会创建实例。
  
  **/	
	private static  class Singleton_Holder{
		 private final static Singleton instance =  new Singleton();
	 }
	 //获取 ，用钩子类
	 public static Singleton getInstance(){
		 return Single_Holder.instance;
	 }
}



