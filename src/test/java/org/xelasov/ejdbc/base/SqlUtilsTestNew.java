package org.xelasov.ejdbc.base;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SqlUtilsTest {

    @Mock
    private DataSource mockDataSource;
    
    @Mock
    private Connection mockConnection;
    
    @Mock
    private Statement mockStatement;
    
    @Mock
    private ResultSetWrapper mockResultSetWrapper;
    
    @Mock
    private ResultSet mockResultSet;
    
    @Mock
    private ResultSetMetaData mockResultSetMetaData;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("buildFunctionCallString Tests")
    class BuildFunctionCallStringTests {

        @Test
        @DisplayName("Should build function call string without return value and no parameters")
        void shouldBuildFunctionCallStringWithoutReturnValueAndNoParams() {
            String result = SqlUtils.buildFunctionCallString("test_function", false, 0);
            assertEquals("{ call test_function() }", result);
        }

        @Test
        @DisplayName("Should build function call string with return value and no parameters")
        void shouldBuildFunctionCallStringWithReturnValueAndNoParams() {
            String result = SqlUtils.buildFunctionCallString("test_function", true, 0);
            assertEquals("{ ? = call test_function() }", result);
        }

        @Test
        @DisplayName("Should build function call string without return value and one parameter")
        void shouldBuildFunctionCallStringWithoutReturnValueAndOneParam() {
            String result = SqlUtils.buildFunctionCallString("test_function", false, 1);
            assertEquals("{ call test_function(?) }", result);
        }

        @Test
        @DisplayName("Should build function call string with return value and one parameter")
        void shouldBuildFunctionCallStringWithReturnValueAndOneParam() {
            String result = SqlUtils.buildFunctionCallString("test_function", true, 1);
            assertEquals("{ ? = call test_function(?) }", result);
        }

        @Test
        @DisplayName("Should build function call string with multiple parameters")
        void shouldBuildFunctionCallStringWithMultipleParams() {
            String result = SqlUtils.buildFunctionCallString("test_function", false, 3);
            assertEquals("{ call test_function(?,?,?) }", result);
        }

        @Test
        @DisplayName("Should build function call string with return value and multiple parameters")
        void shouldBuildFunctionCallStringWithReturnValueAndMultipleParams() {
            String result = SqlUtils.buildFunctionCallString("test_function", true, 5);
            assertEquals("{ ? = call test_function(?,?,?,?,?) }", result);
        }

        @Test
        @DisplayName("Should handle empty function name")
        void shouldHandleEmptyFunctionName() {
            String result = SqlUtils.buildFunctionCallString("", true, 2);
            assertEquals("{ ? = call (?,?) }", result);
        }

        @Test
        @DisplayName("Should handle null function name")
        void shouldHandleNullFunctionName() {
            String result = SqlUtils.buildFunctionCallString(null, false, 1);
            assertEquals("{ call null(?) }", result);
        }
    }

    @Nested
    @DisplayName("repeat Tests")
    class RepeatTests {

        @Test
        @DisplayName("Should repeat string specified number of times")
        void shouldRepeatStringSpecifiedTimes() {
            String result = SqlUtils.repeat("ab", 3);
            assertEquals("ababab", result);
        }

        @Test
        @DisplayName("Should return empty string when count is zero")
        void shouldReturnEmptyStringWhenCountIsZero() {
            String result = SqlUtils.repeat("test", 0);
            assertEquals("", result);
        }

        @Test
        @DisplayName("Should return empty string when count is negative")
        void shouldReturnEmptyStringWhenCountIsNegative() {
            String result = SqlUtils.repeat("test", -1);
            assertEquals("", result);
        }

        @Test
        @DisplayName("Should handle empty string input")
        void shouldHandleEmptyStringInput() {
            String result = SqlUtils.repeat("", 5);
            assertEquals("", result);
        }

        @Test
        @DisplayName("Should handle null string input")
        void shouldHandleNullStringInput() {
            String result = SqlUtils.repeat(null, 3);
            assertEquals("nullnullnull", result);
        }

        @Test
        @DisplayName("Should repeat single character")
        void shouldRepeatSingleCharacter() {
            String result = SqlUtils.repeat("x", 4);
            assertEquals("xxxx", result);
        }
    }

    @Nested
    @DisplayName("closeSafely Statement Tests")
    class CloseSafelyStatementTests {

        @Test
        @DisplayName("Should close statement successfully")
        void shouldCloseStatementSuccessfully() throws SQLException {
            Statement statement = mock(Statement.class);
            
            SqlUtils.closeSafely(statement);
            
            verify(statement).close();
        }

        @Test
        @DisplayName("Should handle null statement gracefully")
        void shouldHandleNullStatementGracefully() {
            assertDoesNotThrow(() -> SqlUtils.closeSafely((Statement) null));
        }

        @Test
        @DisplayName("Should log error when closing statement fails")
        void shouldLogErrorWhenClosingStatementFails() throws SQLException {
            Statement statement = mock(Statement.class);
            SQLException sqlException = new SQLException("Test exception");
            doThrow(sqlException).when(statement).close();

            try (MockedStatic<LoggerFactory> loggerFactory = mockStatic(LoggerFactory.class)) {
                Logger mockLogger = mock(Logger.class);
                loggerFactory.when(() -> LoggerFactory.getLogger(SqlUtils.class)).thenReturn(mockLogger);
                
                SqlUtils.closeSafely(statement);
                
                verify(mockLogger).error("Error closing statement", sqlException);
            }
        }
    }

    @Nested
    @DisplayName("closeSafely Connection Tests")
    class CloseSafelyConnectionTests {

        @Test
        @DisplayName("Should close connection successfully")
        void shouldCloseConnectionSuccessfully() throws SQLException {
            Connection connection = mock(Connection.class);
            
            SqlUtils.closeSafely(connection);
            
            verify(connection).close();
        }

        @Test
        @DisplayName("Should handle null connection gracefully")
        void shouldHandleNullConnectionGracefully() {
            assertDoesNotThrow(() -> SqlUtils.closeSafely((Connection) null));
        }

        @Test
        @DisplayName("Should log error when closing connection fails")
        void shouldLogErrorWhenClosingConnectionFails() throws SQLException {
            Connection connection = mock(Connection.class);
            SQLException sqlException = new SQLException("Test exception");
            doThrow(sqlException).when(connection).close();

            try (MockedStatic<LoggerFactory> loggerFactory = mockStatic(LoggerFactory.class)) {
                Logger mockLogger = mock(Logger.class);
                loggerFactory.when(() -> LoggerFactory.getLogger(SqlUtils.class)).thenReturn(mockLogger);
                
                SqlUtils.closeSafely(connection);
                
                verify(mockLogger).error("Error closing connection", sqlException);
            }
        }
    }

    @Nested
    @DisplayName("commitSafely Tests")
    class CommitSafelyTests {

        @Test
        @DisplayName("Should commit transaction successfully")
        void shouldCommitTransactionSuccessfully() throws SQLException {
            SqlUtils.commitSafely(mockConnection);
            
            verify(mockConnection).commit();
        }

        @Test
        @DisplayName("Should handle null connection gracefully")
        void shouldHandleNullConnectionGracefully() {
            assertDoesNotThrow(() -> SqlUtils.commitSafely(null));
        }

        @Test
        @DisplayName("Should log error when commit fails")
        void shouldLogErrorWhenCommitFails() throws SQLException {
            SQLException sqlException = new SQLException("Commit failed");
            doThrow(sqlException).when(mockConnection).commit();

            try (MockedStatic<LoggerFactory> loggerFactory = mockStatic(LoggerFactory.class)) {
                Logger mockLogger = mock(Logger.class);
                loggerFactory.when(() -> LoggerFactory.getLogger(SqlUtils.class)).thenReturn(mockLogger);
                
                SqlUtils.commitSafely(mockConnection);
                
                verify(mockLogger).error("Error commiting TX", sqlException);
            }
        }
    }

    @Nested
    @DisplayName("rollbackSafely Tests")
    class RollbackSafelyTests {

        @Test
        @DisplayName("Should rollback transaction successfully")
        void shouldRollbackTransactionSuccessfully() throws SQLException {
            SqlUtils.rollbackSafely(mockConnection);
            
            verify(mockConnection).rollback();
        }

        @Test
        @DisplayName("Should handle null connection gracefully")
        void shouldHandleNullConnectionGracefully() {
            assertDoesNotThrow(() -> SqlUtils.rollbackSafely(null));
        }

        @Test
        @DisplayName("Should log error when rollback fails")
        void shouldLogErrorWhenRollbackFails() throws SQLException {
            SQLException sqlException = new SQLException("Rollback failed");
            doThrow(sqlException).when(mockConnection).rollback();

            try (MockedStatic<LoggerFactory> loggerFactory = mockStatic(LoggerFactory.class)) {
                Logger mockLogger = mock(Logger.class);
                loggerFactory.when(() -> LoggerFactory.getLogger(SqlUtils.class)).thenReturn(mockLogger);
                
                SqlUtils.rollbackSafely(mockConnection);
                
                verify(mockLogger).error("Error rolling back TX", sqlException);
            }
        }
    }

    @Nested
    @DisplayName("getConnection Tests")
    class GetConnectionTests {

        @Test
        @DisplayName("Should get connection with autoCommit already set correctly")
        void shouldGetConnectionWithAutoCommitAlreadySetCorrectly() throws SQLException {
            when(mockDataSource.getConnection()).thenReturn(mockConnection);
            when(mockConnection.getAutoCommit()).thenReturn(true);
            
            Connection result = SqlUtils.getConnection(mockDataSource, true);
            
            assertEquals(mockConnection, result);
            verify(mockConnection, never()).setAutoCommit(anyBoolean());
        }

        @Test
        @DisplayName("Should get connection and set autoCommit when different")
        void shouldGetConnectionAndSetAutoCommitWhenDifferent() throws SQLException {
            when(mockDataSource.getConnection()).thenReturn(mockConnection);
            when(mockConnection.getAutoCommit()).thenReturn(true);
            
            Connection result = SqlUtils.getConnection(mockDataSource, false);
            
            assertEquals(mockConnection, result);
            verify(mockConnection).setAutoCommit(false);
        }

        @Test
        @DisplayName("Should propagate SQLException from DataSource")
        void shouldPropagateSQLExceptionFromDataSource() throws SQLException {
            SQLException sqlException = new SQLException("Connection failed");
            when(mockDataSource.getConnection()).thenThrow(sqlException);
            
            assertThrows(SQLException.class, () -> SqlUtils.getConnection(mockDataSource, true));
        }

        @Test
        @DisplayName("Should propagate SQLException from setAutoCommit")
        void shouldPropagateSQLExceptionFromSetAutoCommit() throws SQLException {
            when(mockDataSource.getConnection()).thenReturn(mockConnection);
            when(mockConnection.getAutoCommit()).thenReturn(true);
            SQLException sqlException = new SQLException("setAutoCommit failed");
            doThrow(sqlException).when(mockConnection).setAutoCommit(false);
            
            assertThrows(SQLException.class, () -> SqlUtils.getConnection(mockDataSource, false));
        }
    }

    @Nested
    @DisplayName("printResultSet Tests")
    class PrintResultSetTests {

        @Test
        @DisplayName("Should print result set with headers every 10 rows")
        void shouldPrintResultSetWithHeadersEvery10Rows() throws SQLException {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            
            when(mockResultSetWrapper.getResultSet()).thenReturn(mockResultSet);
            when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
            when(mockResultSetMetaData.getColumnCount()).thenReturn(2);
            when(mockResultSetMetaData.getColumnLabel(1)).thenReturn("Column1");
            when(mockResultSetMetaData.getColumnLabel(2)).thenReturn("Column2");
            
            // Mock 12 rows to test header printing every 10 rows
            when(mockResultSetWrapper.next())
                .thenReturn(true, true, true, true, true, true, true, true, true, true, // 10 times
                           true, true, // 2 more times
                           false); // End
            
            when(mockResultSetWrapper.getStringOrNull(1)).thenReturn("Value1");
            when(mockResultSetWrapper.getStringOrNull(2)).thenReturn("Value2");
            
            SqlUtils.printResultSet(mockResultSetWrapper, printStream);
            
            String output = outputStream.toString();
            
            // Should contain headers twice (at row 0 and row 10)
            assertEquals(2, countOccurrences(output, "Column1"));
            assertEquals(2, countOccurrences(output, "Column2"));
            
            // Should contain 12 data rows
            assertEquals(12, countOccurrences(output, "Value1"));
            assertEquals(12, countOccurrences(output, "Value2"));
        }

        @Test
        @DisplayName("Should handle empty result set")
        void shouldHandleEmptyResultSet() throws SQLException {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            
            when(mockResultSetWrapper.getResultSet()).thenReturn(mockResultSet);
            when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
            when(mockResultSetWrapper.next()).thenReturn(false);
            
            SqlUtils.printResultSet(mockResultSetWrapper, printStream);
            
            String output = outputStream.toString();
            assertEquals("", output);
        }

        @Test
        @DisplayName("Should handle null values in result set")
        void shouldHandleNullValuesInResultSet() throws SQLException {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            
            when(mockResultSetWrapper.getResultSet()).thenReturn(mockResultSet);
            when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
            when(mockResultSetMetaData.getColumnCount()).thenReturn(1);
            when(mockResultSetMetaData.getColumnLabel(1)).thenReturn("TestColumn");
            when(mockResultSetWrapper.next()).thenReturn(true, false);
            when(mockResultSetWrapper.getStringOrNull(1)).thenReturn(null);
            
            SqlUtils.printResultSet(mockResultSetWrapper, printStream);
            
            String output = outputStream.toString();
            assertTrue(output.contains("null"));
        }

        @Test
        @DisplayName("Should propagate SQLException")
        void shouldPropagateSQLException() throws SQLException {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            
            when(mockResultSetWrapper.getResultSet()).thenReturn(mockResultSet);
            SQLException sqlException = new SQLException("ResultSet error");
            when(mockResultSet.getMetaData()).thenThrow(sqlException);
            
            assertThrows(SQLException.class, () -> 
                SqlUtils.printResultSet(mockResultSetWrapper, printStream));
        }

        private int countOccurrences(String text, String substring) {
            int count = 0;
            int index = 0;
            while ((index = text.indexOf(substring, index)) != -1) {
                count++;
                index += substring.length();
            }
            return count;
        }
    }
}
