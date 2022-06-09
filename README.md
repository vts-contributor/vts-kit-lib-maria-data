MariaDB Library for Spring Boot
-------
This library provides utilities that make it easy to integrate MariaDB into spring boot project

<b>Features</b>
* Manage database schema change with [Liquibase](#liquibase)
* Access data with [Spring JPA](#spring-jpa)
* Easy to execute [native query](#native-query)

Quick start
-------
* Just add the dependency to an existing Spring Boot project
```xml
<dependency>
    <groupId>com.atviettelsolutions</groupId>
    <artifactId>vts-kit-ms-maria-data</artifactId>
    <version>1.0.0</version>
</dependency>
```

* Then, add the following properties to your `application-*.yml` file.
```yaml
vtskit:
  mariadb:
    datasource:
      url: jdbc:mariadb://localhost:3306/your-database
      username: your-username
      password: your-password
```

Usage
-------
### Liquibase
All change logs files will be saved in `resources/liquibase-changelog` folder.

Now let's take a look at a simple changeLog file.\
Create `changelog-1.0.xml` in `liquibase-changelog` folder. This create Book table and add some example data:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:pro="http://www.liquibase.org/xml/ns/pro"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
      http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.1.xsd
      http://www.liquibase.org/xml/ns/pro
      http://www.liquibase.org/xml/ns/pro/liquibase-pro-4.1.xsd">
    <changeSet author="liquibase-bot" id="1">
        <!-- CREATE BOOK TABLE -->
        <createTable tableName="book">
            <column name="id" type="VARCHAR(255)">
                <constraints nullable="false" primaryKey="true" unique="true"/>
            </column>
            <column name="name" type="VARCHAR(255)" >
                <constraints nullable="false"/>
            </column>
            <column name="author" type="VARCHAR(255)" >
                <constraints nullable="false"/>
            </column>
        </createTable>

        <!-- CREATE EXAMPLE DATA -->
        <insert tableName="book">
            <column name="id" value="c4ca4238a0b923820dcc509a6f75849b"/>
            <column name="name" value="How Beautiful We Were"/>
            <column name="author" value="Imbolo Mbue"/>
        </insert>
        <insert tableName="book">
            <column name="id" value="c81e728d9d4c2f636f067f89cc14862c11"/>
            <column name="name" value="How Beautiful We Were"/>
            <column name="author" value="Imbolo Mbue"/>
        </insert>
        <insert tableName="book">
            <column name="id" value="c81e728d9d4c2f636f067f89cc14862c"/>
            <column name="name" value="Intimacies"/>
            <column name="author" value="Katie Kitamura"/>
        </insert>
    </changeSet>
</databaseChangeLog>
```
Let's now run your application and check database. `Book` table will be created with some sample data as above.

### Spring JPA

<b>Step 1</b>: Create Entity class
```java
@Data
@NoArgsConstructor
@Entity(name = "book")
public class Book {
    @Id
    private String id;
    private String name;
    private String author;
}
```

<b>Step 2</b>: Create Repository interface
```java
public interface BookRepository extends MariaRepository<Book, String> {}
```

<b>Step 3</b>: Usage Repository for access data

```java
private BookRepository bookRepository;

@Autowired
public void setBookRepository(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
}
```

<b>Saving data</b>
```java
Book book = new Book();
book.setId("bookId");
book.setName("bookName");
book.setAuthor("bookAuthor");
bookRepository.save(book);
```

<b>Getting by id</b>
```java
Book book = bookRepository.getById("bookId");
```

<b>Select all</b>
```java
List<Book> bookList = bookRepository.findAll();
```

<b>Select paging</b>
```java
Page<Book> result = bookRepository.findAll(Pageable.ofSize(1).withPage(0));
```

<b>Delete by id</b>
```java
bookRepository.deleteById("bookId");
```

<b>Delete all</b>
```java
bookRepository.deleteAll();
```

### Native query
If you want to execute the native query and mapping result to your DTO class, follow the steps below:

<b>Step 1</b>: Create DTO class
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private String id;
    private String name;
    private String author;
}
```

<b>Step 2</b>: Usage `CommonMariaRepository` for execute query

```java
private CommonMariaRepository commonRepository;

@Autowired
public void setCommonRepository(CommonMariaRepository commonRepository) {
    this.commonRepository = commonRepository;
}
```

<b>Select query</b>
```java
String query = "select * from book where author = :author";
HashMap<String, Object> params = new HashMap<>();
params.put("author", "Imbolo Mbue");
List<BookDTO> books = commonRepository.executeSelectQuery(query, params, BookDTO.class);
```

<b>Select paging</b>
```java
int startPage = 0;
int pageSize = 10;
String query = "select * from book where author = :author";
HashMap<String, Object> params = new HashMap<>();
params.put("author", "Imbolo Mbue");
Page<BookDTO> bookDTOPage = commonRepository.executeSelectPagingQuery(query, params, startPage, pageSize, BookDTO.class);
```

<b>Update query</b>
```java
String query = "update book set name = 'newName' where author = :author";
HashMap<String, Object> params = new HashMap<>();
params.put("author", "Imbolo Mbue");
commonRepository.executeUpdateQuery(query, params);
```

<b>Count Select</b>
```java
String selectQuery = "select * from book where author = :author";
HashMap<String, Object> params = new HashMap<>();
params.put("author", "Imbolo Mbue");
long total = commonRepository.executeCountSelectQuery(selectQuery, params);
```
### Advanced configuration
<b>`HikariDataSource`</b> configuration
```yaml
vtskit:
  mariadb:
    datasource:
      hikari:
        auto-commit: false
        maximum-pool-size: 5
        connection-timeout: 60000
        ...
```

<b>`JPA`</b> configuration:
```yaml
vtskit:
  mariadb:
    jpa:
      open-in-view: true
      generate-ddl: false
      show-sql: true
      ...
```

<b>`Liquibase`</b> configuration:
```yaml
vtskit:
  mariadb:
    liquibase:
      enabled: false
      tag: tag
      ...
```

Contribute
-------
#### Setting up the development environment
* <b>IDE:</b> Eclipse, Intellij IDEA
* <b>JDK:</b> >= JDK 8
* <b>Maven:</b> >= 3.6.0
* <b>Build:</b>
```shell script
mvn clean install
# Skip Unittest
mvn clean install -DskipTests
```
#### Contribute Guidelines
If you have any ideas, just open an issue and tell us what you think.

If you'd like to contribute, please refer [Contributors Guide](CONTRIBUTING.md)

License
-------
This code is under the [MIT License](https://opensource.org/licenses/MIT).

See the [LICENSE](LICENSE) file for required notices and attributions.
