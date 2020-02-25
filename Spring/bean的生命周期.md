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
