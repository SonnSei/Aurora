# Spring的Bean管理（注解）
## 引入约束并开启注解扫描
```css
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation=
               "http://www.springframework.org/schema/beans
                http://www.springframework.org/schema/beans/spring-beans.xsd
                http://www.springframework.org/schema/context
                http://www.springframework.org/schema/context/spring-context.xsd">

    <!--开启注解扫描-->
    <context:component-scan base-package="com.imooc.ioc_annotation.demo1"/>
</beans>
```

## 添加注解
```java
/**
 * @Classname UserService
 * @Description Springbean管理的注解方式
 * @Date 2019/11/21 8:16
 * @Created by Jesse
 */

@Component("userService")
public class UserService {
    public String sayHello(String name){
        return "hello "+name;
    }
}
```
### 除了Component之外，Spring还提供了3个功能基本和@Component等效的注解
- @Resposity：用于对DAO实现类进行标注
- @Service：用于对Service实现类进行标注
- @Controller：用于对Controller实现类进行标注

# Spring的属性注入（注解）
## 简单类型
```java
@Service("userService")
public class UserService {
    @Value("apple")
    private String something;

//    @Value("banana")
//    public void setSomething(String something) {
//        this.something = something;
//    }
    public void eat(){
        System.out.println("eat "+something);
    }
}
```
- 注解方式的属性注入不需要set方法
- 如果有set方法，一般把注解放在set方法上，因为👇
- 如果set方法和变量的注解不一样，最后会按照set方法的注解执行

## 引用类型
```java

@Service("userService")
public class UserService {
    //默认按照类型，如果两个bean类型相同，则按照id
//    @Autowired
//    private UserDao userDao;

    //强制按照名称
//    @Autowired
//    @Qualifier("userDao")
//    private UserDao userDao;

    //相当于Autowired+Qualifier
    @Resource(name = "userDao")
    private UserDao userDao;
    
    public void save(){
        System.out.println("Seivice中的保存用户");
        userDao.save();
    }
}
```

## 其它注解
### init-method和destroy-method（单例模式的destroy才有效）
```java
/**
 * @Classname Bean1
 * @Description 测试init-method/destroy-method的注解方式
 * @Date 2019/11/21 8:46
 * @Created by Jesse
 */
@Component("bean1")
public class Bean1 {
    @PostConstruct
    public void init(){
        System.out.println("init bean。。。");
    }

    public void say(){
        System.out.println("say。。。");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("destroy bean。。。");
    }
}
```
### 作用范围的注解
```java
@Component("bean2")
@Scope("prototype")
public class Bean2 {
}
```
