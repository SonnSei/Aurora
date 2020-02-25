
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
