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
