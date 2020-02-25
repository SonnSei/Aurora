[toc]
# 1. AOP的概述
## 1.1 什么是AOP
- Aspect Oriented Programing 面向切面编程
- AOP采取横向抽取机制，取代了传统纵向继承体系重复性代码（性能监视、事务管理、安全检查、缓存）
- Spring AOP使用纯Java实现，不需要专门的编译过程和类加载器，在运行期间通过代理方式向目标类织入增强代码

## 1.2 AOP相关术语
- **JointPoint 连接点**：指的是被拦截到的点。在Spring中指的是方法，因为Spring只支持方法类型的连接点
- **Pointcut 切入点**：指的是我们要对哪些JointPoint进行拦截
- **Advice 通知/增强**：所谓通知就是指拦截到Pointcut之后要做哪些事情。分为前置通知、后置通知、异常通知、最终通知、环绕通知（切面要完成的功能）
- **Introduction 引介**：是一种特殊的通知，在不修改类代码的前提下可在运行期间为类动态地添加一些方法或Field
- **Target 目标对象**：代理的目标对象
- **waving 织入**：指把增强应用到目标对象来创建新的代理对象的过程。spring采用动态代理织入，而AspectJ采用编译期织入和类装载期织入
- **Proxy 代理**：一个类被AOP织入增强后，就会产生一个代理
- **Aspect 切面**：是切入点和通知（引介）的结合
# 2. AOP的底层实现
## 2.1 JDK动态代理
- **只能对实现了接口的类生成代理**
- **接口中方法不能用final修饰**

```java
/**
 * @Classname MyJDKProxy
 * @Description JDK动态代理测试
 * @Date 2019/11/21 10:34
 * @Created by Jesse
 */
public class MyJDKProxy implements InvocationHandler {
    private UserDao userDao;

    public MyJDKProxy(UserDao userDao) {
        this.userDao = userDao;
    }

    public Object createProxy(){
        Object proxy = Proxy.newProxyInstance(userDao.getClass().getClassLoader(), userDao.getClass().getInterfaces(), this);
        return proxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("save".equals(method.getName())) {
            System.out.println("拦截到了save，权限校验");
            return method.invoke(userDao,args);
        }
        return method.invoke(userDao,args);
    }
}


@Test
public void demo1() {
    UserDao userDao = (UserDao) new MyJDKProxy(new UserDaoImpl()).createProxy();
    userDao.save();
    userDao.delete();
    userDao.update();
    userDao.find();
}

输出：
拦截到了save，权限校验
保存用户
删除用户
修改用户
查询用户
```

## 2.2 使用CGLIB生成代理
- **对于没有实现接口的类，可以生成一个类来继承目标类**
- **类或方法不能用final修饰**
```java

/**
 * @Classname MyCglibProxy
 * @Description CGLIB生成代理
 * @Date 2019/11/21 10:46
 * @Created by Jesse
 */
public class MyCglibProxy implements MethodInterceptor {
    private ProductDao productDao;

    public MyCglibProxy(ProductDao productDao) {
        this.productDao = productDao;
    }

    public Object createProxy(){
        //1. 创建核心类
        Enhancer enhancer = new Enhancer();
        //2. 设置父类
        enhancer.setSuperclass(productDao.getClass());
        //3. 设置回调
        enhancer.setCallback(this);
        //4. 生成代理
        Object proxy = enhancer.create();
        return proxy;
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if ("save".equals(method.getName())) {
            System.out.println("拦截到了save，权限校验");
            return methodProxy.invokeSuper(proxy,args);
        }
        //不做处理
        return methodProxy.invokeSuper(proxy,args);
    }
}
```
# 3. Spring的传统AOP
**AOP是AOP联盟定义的，spring只是实现的比较好**
## 3.1 通知类型（传统）
- 前置通知：在目标方法执行前
- 后置通知：在目标方法执行后
- 环绕通知：在目标方法执行前、后
- 异常抛出通知：在方法抛出异常之后
- 引介通知：在目标类中添加一些方法和属性（spring并不支持）

## 3.2 切面类型（传统）
- Advisor：代表一般的切面，Advice本身就是一个切面，对目标类所有方法进行拦截
- PointcutAdvisor：代表具有切点的切面，可以指定拦截目标类哪些方法
- IntroductionAdvisor：引介切面，针对引介通知而使用切面

## 3.3 Advisor切面案例
**额外引入两个包**
```xml
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-aop</artifactId>
  <version>4.2.5.RELEASE</version>
</dependency>
<dependency>
  <groupId>aopalliance</groupId>
  <artifactId>aopalliance</artifactId>
  <version>1.0</version>
</dependency>
```
**配置文件**
```html
<!--配置目标类-->
<bean id="studentDao" class="com.imooc.aop.demo3.StudentDaoImpl"/>

<!--前置通知类型-->
<bean id="myBeforeAdvice" class="com.imooc.aop.demo3.MyBeforeAdvice"/>

<!--Spring的AOP产生代理对象-->
<bean id="studentDaoProxy" class="org.springframework.aop.framework.ProxyFactoryBean">
    <!--目标类-->
    <property name="target" ref="studentDao"/>
    <!--实现的接口-->
    <property name="proxyInterfaces" value="com.imooc.aop.demo3.StudentDao"/>
    <!--采用拦截的名称-->
    <property name="interceptorNames" value="myBeforeAdvice"/>
</bean>
```
**拦截器**
```java

/**
 * @Classname MyBeforeAdvice
 * @Description 前置通知
 * @Date 2019/11/21 11:40
 * @Created by Jesse
 */
public class MyBeforeAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {
        System.out.println("这是前置通知");
    }
}

```

**测试代码**
```java
/**
 * @Classname SpringDemo3
 * @Description TODO
 * @Date 2019/11/21 11:46
 * @Created by Jesse
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext3.xml")
public class SpringDemo3 {
    @Resource(name ="studentDaoProxy")
    private StudentDao studentDao;

    @Test
    public void demo1() {
        studentDao.save();
        studentDao.delete();
        studentDao.update();
        studentDao.find();
    }
}

输出：

这是前置通知
学生保存
这是前置通知
学生删除
这是前置通知
学生修改
这是前置通知
学生查询
```

## 3.4其它的一些属性
```html
<!--是否对类使用代理，当设置为true的时候，使用CGLib代理-->
<property name="proxyTargetClass" value="true"/>

<!--是否是单例，默认就是单例-->
<property name="singleton" value="true"/>

<!--当设置为true的时候，强制使用CGLib-->
<property name="optimize" value="true"/>
```

## 3.5 带有切入点的切面的配置案例
- 在实际开发中，常采用带有切入点的切面
- 常用的PointcutAdvisor实现类
	1. DefaultPointcutAdvisor：最常用的切面类，可以通过任意的Pointcut和Advice组合定义切面 
	2. JdkRegexpMethodPointcut：构造正则表达式切点

```html
<!--配置目标类-->
<bean id="customDao" class="com.imooc.aop.demo4.CustomDao"/>

<!--配置通知-->
<bean id="myArroundAdvice" class="com.imooc.aop.demo4.MyArroundAdvice"/>

<!--一般的切面是使用通知作为切面。要对目标类的某个方法进行增强，就需要配置一个带有切入点的切面-->
<bean id="myAdvicor" class="org.springframework.aop.support.RegexpMethodPointcutAdvisor">
    <!--pattern中配置的是一个正则表达式-->
    <property name="pattern" value=".*"/>
    <property name="advice" ref="myArroundAdvice"/>
</bean>

<!--配置产生代理-->
<bean id="customDaoProxy" class="org.springframework.aop.framework.ProxyFactoryBean">
    <property name="target" ref="customDao"/>
    <property name="proxyTargetClass" value="true"/>
    <property name="interceptorNames" value="myAdvicor"/>
</bean>
```
# 4. Spring的传统AOP的自动代理
**每个DAO都需要配置一个Proxy，非常麻烦，所以需要优化**

**解决方案：自动创建代理**
- BeanNameAutoProxyCreator：根据Bean的名称创建代理
- DefaultAdvisorAutoProxyCreator：根据Advisor本身包含的信息创建代理
- **AnnotationAwareAspectJAutoProxyCreator**：基于Bean中的AspectJ注解进自动代理
## 4.1 基于Bean名称的自动代理
```html
<!--配置目标类-->
<bean id="studentDao" class="com.imooc.aop.demo5.StudentDaoImpl"/>
<bean id="customDao" class="com.imooc.aop.demo5.CustomDao"/>

<!--配置增强-->
<bean id="myBeforeAdvice" class="com.imooc.aop.demo5.MyBeforeAdvice"/>
<bean id="myArroundAdvice" class="com.imooc.aop.demo5.MyArroundAdvice"/>

<!--对所有Dao结尾的bean添加切面-->
<bean class="org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator">
    <property name="beanNames" value="*Dao"/>
    <property name="interceptorNames" value="myBeforeAdvice"/>
</bean>
```
**如果只是想要增强bean里的某个方法，这种方式就有点欠缺了**
## 4.2 基于切面信息的自动代理
```html
<!--配置目标类-->
<bean id="studentDao" class="com.imooc.aop.demo6.StudentDaoImpl"/>
<bean id="customDao" class="com.imooc.aop.demo6.CustomDao"/>

<!--配置增强-->
<bean id="myBeforeAdvice" class="com.imooc.aop.demo6.MyBeforeAdvice"/>
<bean id="myArroundAdvice" class="com.imooc.aop.demo6.MyArroundAdvice"/>

<!--配置切面-->
<bean id="myAdvisor" class="org.springframework.aop.support.RegexpMethodPointcutAdvisor">
    <property name="pattern" value="com\.imooc\.aop\.demo6\.CustomDao\.save"/>
    <property name="advice" ref="myArroundAdvice"/>
</bean>

<!--基于切面信息产生代理-->
<bean class="org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator"/>
```