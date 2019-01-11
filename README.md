# Lombok简介、使用、工作原理、优缺点

## 1.Lombok简介

>  官方介绍

<table><tr><td bgcolor=WhiteSmoke>Project Lombok is a java library that automatically plugs into your editor and build tools, spicing up your java.Never write another getter or equals method again, with one annotation your class has a fully featured builder, Automate your logging variables, and much more.</td></tr></table>

大概的意思：Lombok是一个Java库，能自动插入编辑器并构建工具，简化Java开发。通过添加注解的方式，不需要为类编写getter或eques方法，同时可以自动化日志变量。[官网链接](https://www.projectlombok.org/)

简而言之：Lombok能以简单的注解形式来简化java代码，提高开发人员的开发效率。
[博客及源码GitHub链接](https://github.com/JourWon/test-lombok)

## 2.Lombok使用

使用Lombok需要的开发环境**Java+Maven+IntelliJ IDEA或者Eclipse(安装Lombok Plugin)**

### 2.1添加maven依赖

```java
<dependency>
	<groupId>org.projectlombok</groupId>
	<artifactId>lombok</artifactId>
	<version>1.18.4</version>
	<scope>provided</scope>
</dependency>
```

### 2.2安装插件

使用Lombok还需要插件的配合，我使用开发工具为idea，这里只讲解idea中安装lombok插件，使用eclipse和myeclipse的小伙伴和自行google安装方法。
打开idea的设置，点击**Plugins**，点击**Browse repositories**，在弹出的窗口中搜索**lombok**，然后安装即可。

![安装lombok插件](https://raw.githubusercontent.com/JourWon/image/master/lombok/%E5%AE%89%E8%A3%85lombok%E6%8F%92%E4%BB%B6.png)

### 2.3解决编译时出错问题

编译时出错，可能是没有enable注解处理器。`Annotation Processors > Enable annotation processing`。设置完成之后程序正常运行。

![开启注解配置](https://raw.githubusercontent.com/JourWon/image/master/lombok/%E5%BC%80%E5%90%AF%E6%B3%A8%E8%A7%A3%E9%85%8D%E7%BD%AE.png)

### 2.4示例

下面举两个栗子，看看使用lombok和不使用的区别。

创建一个用户类

**不使用Lombok**

```java
public class User implements Serializable {

    private static final long serialVersionUID = -8054600833969507380L;

    private Integer id;

    private String username;

    private Integer age;

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(age, user.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, age);
    }

}
```

**使用Lombok**

```java
@Data
public class User implements Serializable {

    private static final long serialVersionUID = -8054600833969507380L;

    private Integer id;

    private String username;

    private Integer age;

}
```

编译源文件，然后反编译class文件，反编译结果如下图。说明@Data注解在类上，会为类的所有属性自动生成setter/getter、equals、canEqual、hashCode、toString方法，如为final属性，则不会为该属性生成setter方法。

![反编译用户类](https://raw.githubusercontent.com/JourWon/image/master/lombok/%E5%8F%8D%E7%BC%96%E8%AF%91%E7%94%A8%E6%88%B7%E7%B1%BB.png)

----

自动化日志变量

```java
@Slf4j
@RestController
@RequestMapping(("/user"))
public class UserController {

    @GetMapping("/getUserById/{id}")
    public User getUserById(@PathVariable Integer id) {
        User user = new User();
        user.setUsername("风清扬");
        user.setAge(21);
        user.setId(id);

        if (log.isInfoEnabled()) {
            log.info("用户 {}", user);
        }

        return user;
    }

}
```

通过反编译可以看到@Slf4j注解生成了log日志变量（严格意义来说是常量），无需去声明一个log就可以在类中使用log记录日志。

![反编译用户controller类](https://raw.githubusercontent.com/JourWon/image/master/lombok/%E5%8F%8D%E7%BC%96%E8%AF%91%E7%94%A8%E6%88%B7controller%E7%B1%BB.png)

### 2.5常用注解

下面介绍一下常用的几个注解：

-  **@Setter** 注解在类或字段，注解在类时为所有字段生成setter方法，注解在字段上时只为该字段生成setter方法。
-  **@Getter** 使用方法同上，区别在于生成的是getter方法。
-  **@ToString** 注解在类，添加toString方法。
-  **@EqualsAndHashCode** 注解在类，生成hashCode和equals方法。
-  **@NoArgsConstructor** 注解在类，生成无参的构造方法。
-  **@RequiredArgsConstructor** 注解在类，为类中需要特殊处理的字段生成构造方法，比如final和被@NonNull注解的字段。
-  **@AllArgsConstructor** 注解在类，生成包含类中所有字段的构造方法。
-  **@Data** 注解在类，生成setter/getter、equals、canEqual、hashCode、toString方法，如为final属性，则不会为该属性生成setter方法。
-  **@Slf4j** 注解在类，生成log变量，严格意义来说是常量。private static final Logger log = LoggerFactory.getLogger(UserController.class);

## 3.Lombok工作原理

在Lombok使用的过程中，只需要添加相应的注解，无需再为此写任何代码。自动生成的代码到底是如何产生的呢？

核心之处就是对于注解的解析上。JDK5引入了注解的同时，也提供了两种解析方式。

- 运行时解析

运行时能够解析的注解，必须将@Retention设置为RUNTIME，这样就可以通过反射拿到该注解。java.lang.reflect反射包中提供了一个接口AnnotatedElement，该接口定义了获取注解信息的几个方法，Class、Constructor、Field、Method、Package等都实现了该接口，对反射熟悉的朋友应该都会很熟悉这种解析方式。

- 编译时解析

编译时解析有两种机制，分别简单描述下：

1）Annotation Processing Tool

apt自JDK5产生，JDK7已标记为过期，不推荐使用，JDK8中已彻底删除，自JDK6开始，可以使用Pluggable Annotation Processing API来替换它，apt被替换主要有2点原因：

- api都在com.sun.mirror非标准包下
- 没有集成到javac中，需要额外运行

2）Pluggable Annotation Processing API

[JSR 269](https://jcp.org/en/jsr/detail?id=269)自JDK6加入，作为apt的替代方案，它解决了apt的两个问题，javac在执行的时候会调用实现了该API的程序，这样我们就可以对编译器做一些增强，javac执行的过程如下： 

![lombok工作原理](https://raw.githubusercontent.com/JourWon/image/master/lombok/lombok%E5%B7%A5%E4%BD%9C%E5%8E%9F%E7%90%86.png)

Lombok本质上就是一个实现了“[JSR 269 API](https://www.jcp.org/en/jsr/detail?id=269)”的程序。在使用javac的过程中，它产生作用的具体流程如下：

1. javac对源代码进行分析，生成了一棵抽象语法树（AST）
2. 运行过程中调用实现了“JSR 269 API”的Lombok程序
3. 此时Lombok就对第一步骤得到的AST进行处理，找到@Data注解所在类对应的语法树（AST），然后修改该语法树（AST），增加getter和setter方法定义的相应树节点
4. javac使用修改后的抽象语法树（AST）生成字节码文件，即给class增加新的节点（代码块）

通过读Lombok源码，发现对应注解的实现都在HandleXXX中，比如@Getter注解的实现在HandleGetter.handle()。还有一些其它类库使用这种方式实现，比如[Google Auto](https://github.com/google/auto)、[Dagger](http://square.github.io/dagger/)等等。

## 4.Lombok的优缺点

**优点：**

1. 能通过注解的形式自动生成构造器、getter/setter、equals、hashcode、toString等方法，提高了一定的开发效率
2. 让代码变得简洁，不用过多的去关注相应的方法
3. 属性做修改时，也简化了维护为这些属性所生成的getter/setter方法等

**缺点：**

1. 不支持多种参数构造器的重载
2. 虽然省去了手动创建getter/setter方法的麻烦，但大大降低了源代码的可读性和完整性，降低了阅读源代码的舒适度