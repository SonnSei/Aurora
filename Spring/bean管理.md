
# Spring工厂类
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191120105145215.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9oZWxsb3F1ZWVuamVzc2ljYS5ibG9nLmNzZG4ubmV0,size_16,color_FFFFFF,t_70)

**FileSystemXmlApplicationContext**用于加载文件系统中的配置文件（不在工程中）
```java
	@Test
    /**
     * 读取磁盘系统中的配置文件
     */
    public void demo3(){
        ApplicationContext applicationContext = new FileSystemXmlApplicationContext("d:\\applicationContext.xml");
        UserService userService = (UserService) applicationContext.getBean("userService");
        userService.sayHello();
    }
```

之前的版本用的是**BeanFactory**而不是**ApplicationContext**
- **BeanFactory**在工厂实例化之后，在调用getBean的时候才会加载类
- **ApplicationContext**在加载配置文件的是就会把所有单例的类加载了
```java
	@Test
    /**
     * 传统方式工厂类
     */
    public void demo4(){
        BeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("applicationContext.xml"));
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.sayHello();
    }
    
	@Test
    /**
     * 传统方式工厂类读取磁盘系统中的配置文件
     */
    public void demo4(){
        BeanFactory beanFactory = new XmlBeanFactory(new FileSystemResource("applicationContext.xml"));
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.sayHello();
    }
```

# Spring的Bean管理（XML）
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

## Bean的常用配置
- id和name
	1. id必须是唯一的，name也需要唯一
	2. 如果有特殊字符，就需要用name
- class：设置一个类的全路径
- scope：作用域
## Bean的作用域
|类别|说明|
|--|--|
|singleton（**默认**）|在SpringIOC容器中仅存在一个Bean实例，以单例形式存在|
|prototype|每次调用getBean（）都会返回一个新的实例|
|request|每次HTTP请求都会创建一个新的Bean，该作用域仅适用于WebApplicationContext环境|
|session|同一个HTTP Session共享一个Bean，不同的Session使用不同的Bean。该作用域仅适用于WebApplicationContext环境|

## Bean的生命周期
### init和destroy方法
方法名可以任意
```html
<bean id="man" class="Man" init-method="setup" destroy-method="tearDown"></bean>
```
```java
public class Man {
    public Man(){
        System.out.println("MAN被实例化了");
    }
    public void setup(){
        System.out.println("MAN被初始化了");
    }
    public void tearDown(){
        System.out.println("MAN被销毁了");
    }
}

@Test
public void demo2(){
    ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    Man man = (Man) applicationContext.getBean("man");
    System.out.println(man);
}
输出：
MAN被实例化了
MAN被初始化了
com.imooc.ioc.demo3.Man@52feb982
```
**如果要看到destroy方法的输出，需要直接声明ApplicationContext的实现类**
```java
@Test
public void demo2(){
    ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    Man man = (Man) applicationContext.getBean("man");
    System.out.println(man);
    applicationContext.close();
}
```
### 完整生命周期
```java
/**
 * @Classname Man
 * @Description Bean生命周期
 * @Date 2019/11/20 21:50
 * @Created by Jesse
 */
public class Man implements BeanNameAware, BeanFactoryAware,ApplicationContextAware, InitializingBean, DisposableBean {

    String name;

    public Man(){
        System.out.println("第一步：初始化");
    }

    public void setName(String name) {
        System.out.println("第二步：设置属性");
        this.name = name;
    }
    
    @Override
    //实现BeanNameAware接口
    public void setBeanName(String s) {
        System.out.println("第三步：设置bean的名称"+s);
    }

    @Override
    //实现ApplicationContextAware
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("第四步：了解工厂的信息ApplicationContextAware");
    }

    @Override
    //BeanFactoryAware, 运行时会先于setApplicationContext
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("第四步：了解工厂的信息BeanFactoryAware");
    }

    // 实现InitializingBean
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("第六步：属性设置后");
    }

    public void setup(){
        System.out.println("第七步：init-method里面指定的方法");
    }

    public void run(){
        System.out.println("第九步：执行类自身的业务方法");
    }
    
    @Override
    //实现DisposableBean
    public void destroy() throws Exception {
        System.out.println("第十步：执行Spring的销毁方法");
    }

    public void tearDown(){
        System.out.println("第十一步：执行destroy-method配置的方法");
    }
}

public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        System.out.println("第五步：初始化前方法");
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        System.out.println("第八步：初始化后方法");
        return o;
    }
}
```
### BeanPostProcessor的作用
**JDK代理方式**
```java
@Override
public Object postProcessAfterInitialization(final Object bean, String beanName) throws BeansException {
    if("userDao".equals(beanName)){
        Object  proxy = Proxy.newProxyInstance(bean.getClass().getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if ("save".equals(method.getName())) {
                    System.out.println("权限校验");
                    return method.invoke(bean, args);
                }
                return method.invoke(bean, args);
            }
        });
        return proxy;
    }
    return bean;
}
```
# Spring的属性注入（XML）
- 对于成员变量，注入方式有三种
	1. 构造函数注入
	2. 属性setter注入
	3. 接口注入
- Spring支持前两种

## 构造函数注入
```html
<bean id="user" class="com.imooc.ioc.demo4.User">
    <constructor-arg name="name" value="Jessia"/>
    <constructor-arg name="age" value="18"/>
</bean>
```
```java
public class User {
    private String name;
    private Integer age;

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
```

## set方法注入(开发中更习惯用这个)
```html
<!--bean的set方法的属性注入-->
<bean id="cat" class="com.imooc.ioc.demo4.Cat" >
    <property name="name" value="Tom"/>
</bean>
<bean id="person" class="com.imooc.ioc.demo4.Person">
    <property name="name" value="Jesse"/>
    <property name="age" value="18"/>
    <!--ref用来引用其他的beanid-->
    <property name="cat" ref="cat"/>
</bean>
```
```java
/**
 * @Classname Person
 * @Description set方法属性注入测试
 * @Date 2019/11/21 7:24
 * @Created by Jesse
 */
public class Person {
    private String name;
    private Integer age;
    private Cat cat;

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", cat=" + cat +
                '}';
    }
}
```

## p名称空间的属性注入
**为了简化XML文件配置，Spring从2.5开始引入**
- P:<属性名>="xxx"  引入常量值
- P:<属性名>-ref="xxx" 引用其它Bean对象

>引入P名称空间：xmlns:p="http://www.springframework.org/schema/p"

```html
<!--p名称空间的属性注入-->
<bean id="cat" class="com.imooc.ioc.demo4.Cat" p:name="Tom"/>
<bean id="person" class="com.imooc.ioc.demo4.Person" p:age="18" p:name="Jessica" p:cat-ref="cat" />
```

## SpEL的属性注入
- 语法：#{表达式}
- <bean id ="" value = "#{表达式}">
	- #{'hello'}： 使用字符串
	- #{beanId}：使用另一个bean
	- #{beanId.content.toUpperCase()}：使用指定名属性，并使用方法
	- #{T(java.lang.Math).PI}：使用静态字段或方法

```html
<!--SpEL的属性注入-->
<bean id="category" class="com.imooc.ioc.demo4.Category">
    <property name="name" value="#{'category'}"/>
</bean>
<bean id="categoryInfo" class="com.imooc.ioc.demo4.ProductInfo"/>
<bean id="product" class="com.imooc.ioc.demo4.Product">
    <property name="name" value="#{'男装'}"/>
<!--<property name="price" value="#{100.99}"/>-->
    <property name="price" value="#{categoryInfo.calculatePrice()}"/>
    <property name="category" value="#{category}"/>
</bean>
```

## 复杂类型的属性注入
### 数组类型的属性注入
```html
<!--数组类型-->
<property name="arrs">
    <list>
        <value>aaa</value>
        <value>bbb</value>
        <value>ccc</value>
    </list>
</property>
```
### List类型的属性注入
```html
<!--list集合类型-->
<property name="list">
    <list>
        <value>111</value>
        <value>222</value>
        <value>333</value>
    </list>
</property>
```

### Set类型的属性注入
```html
<!--set集合类型-->
<property name="set">
    <set>
        <value>ddd</value>
        <value>eee</value>
        <value>fff</value>
    </set>
</property>
```
### Map类型的属性注入
```html
<!--map集合类型-->
<property name="map">
    <map>
        <entry key="aaa" value="111"/>
        <entry key="bbb" value="222"/>
        <entry key="ccc" value="333"/>
    </map>
</property>
```
### Properties类型的属性注入
```html
<!--properties类型-->
<property name="properties">
    <props>
        <prop key="username">root</prop>
        <prop key="password">1234</prop>
    </props>
</property>
```

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

# 传统XML和注解配置混合使用
- XML方式的优势
	- 结构清晰，易于阅读
- 注解方式的优势
	- 开发便捷，属性注入方便  
- XML与注解的整合开发
	1. 引入context命名空间
	2. 在配置文件中添加context：annotation-config标签 

>属性注入用注解方式，而类的管理用XML方式

```html
<context:annotation-config/>  <!--这个只管属性注入，Controller之类的注解是没法用的-->
<bean id="productService" class="com.imooc.ioc_annotation.demo3.ProductService"></bean>
<bean id="categoryDao" class="com.imooc.ioc_annotation.demo3.CategoryDao"></bean>
<bean id="productDao" class="com.imooc.ioc_annotation.demo3.ProductDao"></bean>
```
```java
/**
 * @Classname ProductService
 * @Description 属性注入用注解方式，类管理用XML方式
 * @Date 2019/11/21 9:05
 * @Created by Jesse
 */
public class ProductService {

    @Resource(name = "productDao")
    private ProductDao productDao;
    @Resource(name = "categoryDao")
    private CategoryDao categoryDao;

    public void save(){
        System.out.println("ProductService的save方法执行了");
        categoryDao.save();
        productDao.save();
    }
}
```
