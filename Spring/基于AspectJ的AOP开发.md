**AspectJ简介**
- 是一个基于Java语言的AOP框架
- Spring2.0之后增加了对AspectJ切点表达式的支持
- @AspectJ是AspectJ新增技术，通过JDK5注解技术，允许在直接在Bean类中定义切面
- 新版本的Spring框架，建议使用AspectJ方式来开发AOP
- 需要导入Spring AOP和AspectJ的相关jar包
# 1.注解方式
## 添加依赖
```html
<dependency>
<groupId>aopalliance</groupId>
  <artifactId>aopalliance</artifactId>
  <version>1.0</version>
</dependency>
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-aop</artifactId>
  <version>4.2.5.RELEASE</version>
</dependency>

<!--aspectj相关-->
<dependency>
  <groupId>org.aspectj</groupId>
  <artifactId>aspectjweaver</artifactId>
  <version>1.8.9</version>
</dependency>
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-aspects</artifactId>
  <version>4.2.5.RELEASE</version>
</dependency>
```
## 配置约束
```html
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/aop
       http://www.springframework.org/schema/aop/spring-aop.xsd">

    <!--开启AspectJ的注解开发，自动代理。输入这个，IDEu 可以自动加载需要的约束 -->
    <aop:aspectj-autoproxy/>
</beans>
```

## @AspectJ提供不同的通知类型
- **@Before**前置通知，相当于BeforeAdvice
- **@AfterReturing**后置通知，相当于AfterReturingAdvice
- **@Around**环绕通知，相当于MethodInterceptor
- **@AfterThrowing**异常抛出通知，相当于ThrowAdvice
- **@After**最终final通知，不管是否异常，该通知都会执行
- **@DeclareParents**引介通知，相当于IntroductionInterceptor（不要求掌握）

## 在通知中通过value属性定义切点
- 通过execution函数，可以定义切点的方法切入
- 语法：
	- execution（<访问修饰符>?<返回类型><方法名>(<参数>)<异常>） 
- 例如：
	- 匹配所有类的public方法：execution（public * * （..）） 
	- 匹配指定包下面的所有方法：execution（* com.imooc.dao.*(..)）不包含子包
	- execution （* com.imooc.dao..* (..)）包含子包，注意dao后面有两个点
	- 匹配指定类所有方法：execution（* com.imooc.service.UserService.*(..)）
	- 匹配实现特定接口的所有类方法：execution（* com.imooc.dao.GenericDao+.*(..)）加号匹配子类
	- 匹配所有save开头的方法：execution（* save*（..））

## 入门案例
**配置文件**
```xml
<!--开启AspectJ的注解开发，自动代理-->
<aop:aspectj-autoproxy/>

<!--目标类，这里是IOC需要的-->
<bean id="productDao" class="com.imooc.aspect.demo1.ProductDao"/>

<!--定义切面-->
<bean class="com.imooc.aspect.demo1.MyAspectAnno"/>
```

**切面类**
```java

/**
 * @Classname MyAspectAnno
 * @Description 切面类
 * @Date 2019/11/22 9:36
 * @Created by Jesse
 */
@Aspect
public class MyAspectAnno {
    @Before(value = "execution(* com.imooc.aspect.demo1.ProductDao.*(..))")
    public void before(){
        System.out.println("前置通知");
    }
}
```

**测试代码**
```java

/**
 * @Classname SpringDemo1
 * @Description TODO
 * @Date 2019/11/22 9:40
 * @Created by Jesse
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext7.xml")
public class SpringDemo1 {
    @Resource(name = "productDao")
    ProductDao productDao;

    @Test
    public void demo1() {
        productDao.save();
        productDao.delete();
        productDao.update();
        productDao.find();
    }
}

输出：

前置通知
商品保存
前置通知
商品删除
前置通知
商品修改
前置通知
商品查找
```

## @Before前置通知
**可以在方法中传入JointPoint对象，获取切点信息**
```java
@Aspect
public class MyAspectAnno {
    @Before(value = "execution(* com.imooc.aspect.demo1.ProductDao.*(..))")
    public void before(JoinPoint joinPoint){
        System.out.println("前置通知"+joinPoint);
    }
}

输出变为：
前置通知execution(void com.imooc.aspect.demo1.ProductDao.save())
商品保存
前置通知execution(void com.imooc.aspect.demo1.ProductDao.delete())
商品删除
前置通知execution(void com.imooc.aspect.demo1.ProductDao.update())
商品修改
前置通知execution(void com.imooc.aspect.demo1.ProductDao.find())
商品查找
```

## @AfterReturing后置通知
```java
@Aspect
public class MyAspectAnno {
    @AfterReturning(value = "execution(* com.imooc.aspect.demo1.ProductDao.update(..))")
    public void afterReturing(){
        System.out.println("后置通知");
    }
}
```
**目标方法如果有返回值，后置方法可以通过returing获取返回值**
```java
@Aspect
public class MyAspectAnno {
    @AfterReturning(value = "execution(* com.imooc.aspect.demo1.ProductDao.testAfterReturing(..))",returning = "result")
    public void afterReturing(Object result){//需要和上面的returing名字一样
        System.out.println("后置通知"+result);
    }
}
```

## @Around环绕通知
- Around的返回值就是目标方法的返回值
- 参数为ProceedingJoinPoint可以拦截目标方法执行

```java
@Aspect
public class MyAspectAnno {
    @Around(value = "execution(* delete(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {//目标方法可能返回任意类型，所以这里是Object
        System.out.println("环绕通知----前");
        Object object = joinPoint.proceed();//执行目标方法，如果不调用，则目标方法不会执行
        System.out.println("环绕通知----后");
        return object;
    }
}
```

## @AfterThrowing异常抛出通知
- 通过设置throwing属性，可以设置发生异常对象参数
```java
@Aspect
public class MyAspectAnno {
    @AfterThrowing(value = "execution(* com.imooc.aspect.demo1.ProductDao.find(..))",throwing = "e")
    public void afterThrowing(Throwable e){
        System.out.println("异常抛出通知");
    }
}

```

## @After最终通知
```java
@Aspect
public class MyAspectAnno {
    @After(value = "execution(* com.imooc.aspect.demo1.ProductDao.find(..))")
    public void after() {
        //无论是否有异常，总会被执行
        System.out.println("最终通知");
    }
}
```

## 通过@Jointcut为切面命名
- 在每个通知内定义切点，会造成工作量大，不易维护，对于重复的切点，可以使用@Pointcut进行定义
- 切点方法：private void 无参数方法，方法名为切点名
- 当通知多个切点时，用||连接

```java
@Aspect
public class MyAspectAnno {
    @After(value = "myPointcut1()")
    public void after() {
        //无论是否有异常，总会被执行
        System.out.println("最终通知");
    }

    @Pointcut(value = "execution(* com.imooc.aspect.demo1.ProductDao.find(..))")
    private void myPointcut1(){};//这个方法没有实际意义，只是因为@Pointcut作为注解不能单独存在
}
```

# 2. XML方式
**配置文件**
```html
<!--XML的配置方式完成AOP的开发-->

<!--配置目标类-->
<bean id="customDao" class="com.imooc.aspect.demo2.CustomerDaoImpl"/>

<!--配置切面类-->
<bean id="myAspectXml" class="com.imooc.aspect.demo2.MyAspectXml"/>

<!--AOP的相关配置-->
<aop:config>
    <!--配置切入点-->
    <aop:pointcut id="pointcut1" expression="execution(* com.imooc.aspect.demo2.CustomerDao.save(..))"/>

    <!--配置切面-->
    <!--多个切入点和多个通知的组合，一般用这个-->
    <aop:aspect ref="myAspectXml">
        <!--配置前置增强-->
        <aop:before method="before" pointcut-ref="pointcut1"/>
    </aop:aspect>
    <!--一个切入点和一个通知的组合-->
    <!--<aop:advisor advice-ref=""/>-->
</aop:config>
```

**切面类，不需要注解**
```java
public class MyAspectXml {
    // 前置通知
    public void before(){
        System.out.println("XML方式的前置通知");
    }
}
```