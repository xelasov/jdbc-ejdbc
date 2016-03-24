# jdbc-ejdbc
A wrapper library around JDBC interfaces to make working with JDBC easy


# USAGE
## Function
Function class is used to call stored procedures that return a single value, which could be a ResultSet.
1. Call a stored procedure that takes two input parameters (Strings) and returns a long value:
```
final String userName = ...;
final String passWord = ...;
final DataSource ds = ...;
//
// pass all input parameters to constructor
//
final Long pk = new Function(new DBLong(), "account.create_user", new DBString(userName), new DBString(passWord)).execute(ds);
//
// or, pass parameters separately
//
final Long pk = new Function(new DBLong(), "account.create_user").inString(userName).inString(passWord).execute(ds);
```

2. A more complicated example: stored procedure returns a ResultSet that contain zero or one row.
    1. Define a bean to store our retrieved record (if any):
    ```
    public class User {
      public final long userId;
      public final String userName;

      public User(long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
      }
    }
    ```
    2. Define a RowMapper class to convert a row from the ResultSet to an instance of our data bean:
    ```
    public class UserRowMapper implements RowMapper<User> {

      User mapRow(final int rowIndex, final ResultSetWrapper rsw) throws SQLException {
        return new User(rsw.getLong(1), rsw.getString(2));
      }

    }
    ```
    3. Call the stored procedure:
    ```
    final User user = new Function<User>(new DBBeanResultSet(new UserMapper()), "account.get_user_by_id").inLong(userId).execute(ds);
    if (user == null) System.out.println("User does not exist");
    ```
3. Calling a procedure that returns multiple Users is easy, too:
    ```
    final List<User> users = new Function<List<User>>(new DBListResultSet<User>(new UserMapper()), "account.get_all_users").execute(ds);
    ```


