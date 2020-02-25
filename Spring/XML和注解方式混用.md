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
