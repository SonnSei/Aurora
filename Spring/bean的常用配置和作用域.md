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