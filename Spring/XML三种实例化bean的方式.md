## 三种实例化Bean的方式
### 使用构造器实例化（默认无参）
```java
<!--第一种，无参构造器的方式-->
<bean id="bean1" class="com.imooc.ioc.demo2.Bean1"></bean>

/**
 * Bean实例化的三种方式：采用无参的构造方法的方式
 */
public class Bean1 {
    public Bean1(){
        System.out.println("Bean1被实例化了");
    }
}
```
### 使用静态工厂方法实例化（简单工厂模式）
```java
<!--第二种，静态工厂的方式-->
<bean id="bean2" class="com.imooc.ioc.demo2.Bean2Factory" factory-method="createBean2"></bean>

public class Bean2 {}
/**
 * @Classname Bean2Factory
 * @Description Bean2的静态工厂
 * @Date 2019/11/20 17:55
 * @Created by Jesse
 */
public class Bean2Factory {
    public static Bean2 createBean2(){
        System.out.println("Bean2Factory的方法已经执行");
        return new Bean2();
    }
}

```
### 使用实例工厂方法实例化（工厂方法模式）
```java
<!--第三种，实例工厂的方式-->
<bean id="bean3Factory" class="com.imooc.ioc.demo2.Bean3Factory"></bean>
<bean id="bean3" factory-bean="bean3Factory" factory-method="createBean3"></bean>

public class Bean3 {}
/**
 * @Classname Bean3Factory
 * @Description TODO
 * @Date 2019/11/20 17:59
 * @Created by Jesse
 */
public class Bean3Factory {
    public Bean3 createBean3(){
        System.out.println("Bean3的工厂被执行了");
        return new Bean3();
    }
}
```
