/*
 Copyright (C) 2002-2004 MySQL AB

 This program is free software; you can redistribute it and/or modify
 it under the terms of version 2 of the GNU General Public License as
 published by the Free Software Foundation.

 There are special exceptions to the terms and conditions of the GPL
 as it is applied to this software. View the full text of the
 exception in file EXCEPTIONS-CONNECTOR-J in the directory of this
 software distribution.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA



 */
package testsuite.regression;

import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import testsuite.BaseTestCase;

import com.mysql.jdbc.NotUpdatable;
import com.mysql.jdbc.SQLError;

/**
 * Regression test cases for the ResultSet class.
 * 
 * @author Mark Matthews
 */
public class ResultSetRegressionTest extends BaseTestCase {
	/**
	 * Creates a new ResultSetRegressionTest
	 * 
	 * @param name
	 *            the name of the test to run
	 */
	public ResultSetRegressionTest(String name) {
		super(name);
	}

	/**
	 * Runs all test cases in this test suite
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(ResultSetRegressionTest.class);
	}

	/**
	 * Tests fix for BUG#???? -- Numeric types and server-side prepared
	 * statements incorrectly detect nulls.
	 * 
	 * @throws Exception
	 *             if the test fails
	 */
	public void testBug2359() throws Exception {
		try {
			/*
			 * this.stmt.executeUpdate("DROP TABLE IF EXISTS testBug2359");
			 * this.stmt.executeUpdate("CREATE TABLE testBug2359 (field1 INT)
			 * TYPE=InnoDB"); this.stmt.executeUpdate("INSERT INTO testBug2359
			 * VALUES (null), (1)");
			 * 
			 * this.pstmt = this.conn.prepareStatement("SELECT field1 FROM
			 * testBug2359 WHERE field1 IS NULL"); this.rs =
			 * this.pstmt.executeQuery();
			 * 
			 * assertTrue(this.rs.next());
			 * 
			 * assertTrue(this.rs.getByte(1) == 0);
			 * assertTrue(this.rs.wasNull());
			 * 
			 * assertTrue(this.rs.getShort(1) == 0);
			 * assertTrue(this.rs.wasNull());
			 * 
			 * assertTrue(this.rs.getInt(1) == 0);
			 * assertTrue(this.rs.wasNull());
			 * 
			 * assertTrue(this.rs.getLong(1) == 0);
			 * assertTrue(this.rs.wasNull());
			 * 
			 * assertTrue(this.rs.getFloat(1) == 0);
			 * assertTrue(this.rs.wasNull());
			 * 
			 * assertTrue(this.rs.getDouble(1) == 0);
			 * assertTrue(this.rs.wasNull());
			 * 
			 * assertTrue(this.rs.getBigDecimal(1) == null);
			 * assertTrue(this.rs.wasNull());
			 * 
			 * this.rs.close();
			 * 
			 * this.pstmt = this.conn.prepareStatement("SELECT max(field1) FROM
			 * testBug2359 WHERE field1 IS NOT NULL"); this.rs =
			 * this.pstmt.executeQuery(); assertTrue(this.rs.next());
			 * 
			 * assertTrue(this.rs.getByte(1) == 1);
			 * assertTrue(!this.rs.wasNull());
			 * 
			 * assertTrue(this.rs.getShort(1) == 1);
			 * assertTrue(!this.rs.wasNull());
			 * 
			 * assertTrue(this.rs.getInt(1) == 1);
			 * assertTrue(!this.rs.wasNull());
			 * 
			 * assertTrue(this.rs.getLong(1) == 1);
			 * assertTrue(!this.rs.wasNull());
			 * 
			 * assertTrue(this.rs.getFloat(1) == 1);
			 * assertTrue(!this.rs.wasNull());
			 * 
			 * assertTrue(this.rs.getDouble(1) == 1);
			 * assertTrue(!this.rs.wasNull());
			 * 
			 * assertTrue(this.rs.getBigDecimal(1) != null);
			 * assertTrue(!this.rs.wasNull());
			 * 
			 */
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testBug2359_1");
			this.stmt
					.executeUpdate("CREATE TABLE testBug2359_1 (id INT) TYPE=InnoDB");
			this.stmt.executeUpdate("INSERT INTO testBug2359_1 VALUES (1)");

			this.pstmt = this.conn
					.prepareStatement("SELECT max(id) FROM testBug2359_1");
			this.rs = this.pstmt.executeQuery();

			if (this.rs.next()) {
				assertTrue(this.rs.getInt(1) != 0);
				this.rs.close();
			}

			this.rs.close();
		} finally {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testBug2359_1");
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testBug2359");

			this.rs.close();
			this.pstmt.close();
		}
	}

	/**
	 * Tests fix for BUG#2643, ClassCastException when using this.rs.absolute()
	 * and server-side prepared statements.
	 * 
	 * @throws Exception
	 */
	public void testBug2623() throws Exception {
		PreparedStatement pStmt = null;

		try {
			pStmt = this.conn
					.prepareStatement("SELECT NOW()",
							ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);

			this.rs = pStmt.executeQuery();

			this.rs.absolute(1);
		} finally {
			if (this.rs != null) {
				this.rs.close();
			}

			this.rs = null;

			if (pStmt != null) {
				pStmt.close();
			}
		}
	}

	/**
	 * Tests fix for BUG#2654, "Column 'column.table' not found" when "order by"
	 * in query"
	 * 
	 * @throws Exception
	 *             if the test fails
	 */
	public void testBug2654() throws Exception {
		if (false) { // this is currently a server-level bug

			try {
				this.stmt.executeUpdate("DROP TABLE IF EXISTS foo");
				this.stmt.executeUpdate("DROP TABLE IF EXISTS bar");

				this.stmt.executeUpdate("CREATE TABLE foo ("
						+ "  id tinyint(3) default NULL,"
						+ "  data varchar(255) default NULL"
						+ ") TYPE=MyISAM DEFAULT CHARSET=latin1");
				this.stmt
						.executeUpdate("INSERT INTO foo VALUES (1,'male'),(2,'female')");

				this.stmt.executeUpdate("CREATE TABLE bar ("
						+ "id tinyint(3) unsigned default NULL,"
						+ "data char(3) default '0'"
						+ ") TYPE=MyISAM DEFAULT CHARSET=latin1");

				this.stmt
						.executeUpdate("INSERT INTO bar VALUES (1,'yes'),(2,'no')");

				String statement = "select foo.id, foo.data, "
						+ "bar.data from foo, bar" + "	where "
						+ "foo.id = bar.id order by foo.id";

				String column = "foo.data";

				this.rs = this.stmt.executeQuery(statement);

				ResultSetMetaData rsmd = this.rs.getMetaData();
				System.out.println(rsmd.getTableName(1));
				System.out.println(rsmd.getColumnName(1));

				this.rs.next();

				String fooData = this.rs.getString(column);
			} finally {
				this.stmt.executeUpdate("DROP TABLE IF EXISTS foo");
				this.stmt.executeUpdate("DROP TABLE IF EXISTS bar");
			}
		}
	}

	/**
	 * Tests for fix to BUG#1130
	 * 
	 * @throws Exception
	 *             if the test fails
	 */
	public void testClobTruncate() throws Exception {
		if (!isRunningOnJdk131()) {
			try {
				this.stmt.executeUpdate("DROP TABLE IF EXISTS testClobTruncate");
				this.stmt
						.executeUpdate("CREATE TABLE testClobTruncate (field1 TEXT)");
				this.stmt
						.executeUpdate("INSERT INTO testClobTruncate VALUES ('abcdefg')");
	
				this.rs = this.stmt.executeQuery("SELECT * FROM testClobTruncate");
				this.rs.next();
	
				Clob clob = this.rs.getClob(1);
				clob.truncate(3);
	
				Reader reader = clob.getCharacterStream();
				char[] buf = new char[8];
				int charsRead = reader.read(buf);
	
				String clobAsString = new String(buf, 0, charsRead);
	
				assertTrue(clobAsString.equals("abc"));
			} finally {
				this.stmt.executeUpdate("DROP TABLE IF EXISTS testClobTruncate");
			}
		}
	}

	/**
	 * Tests that streaming result sets are registered correctly.
	 * 
	 * @throws Exception
	 *             if any errors occur
	 */
	public void testClobberStreamingRS() throws Exception {
		try {
			Properties props = new Properties();
			props.setProperty("clobberStreamingResults", "true");

			Connection clobberConn = getConnectionWithProps(props);

			Statement clobberStmt = clobberConn.createStatement();

			clobberStmt.executeUpdate("DROP TABLE IF EXISTS StreamingClobber");
			clobberStmt
					.executeUpdate("CREATE TABLE StreamingClobber ( DUMMYID "
							+ " INTEGER NOT NULL, DUMMYNAME VARCHAR(32),PRIMARY KEY (DUMMYID) )");
			clobberStmt
					.executeUpdate("INSERT INTO StreamingClobber (DUMMYID, DUMMYNAME) VALUES (0, NULL)");
			clobberStmt
					.executeUpdate("INSERT INTO StreamingClobber (DUMMYID, DUMMYNAME) VALUES (1, 'nro 1')");
			clobberStmt
					.executeUpdate("INSERT INTO StreamingClobber (DUMMYID, DUMMYNAME) VALUES (2, 'nro 2')");
			clobberStmt
					.executeUpdate("INSERT INTO StreamingClobber (DUMMYID, DUMMYNAME) VALUES (3, 'nro 3')");

			Statement streamStmt = null;

			try {
				streamStmt = clobberConn.createStatement(
						java.sql.ResultSet.TYPE_FORWARD_ONLY,
						java.sql.ResultSet.CONCUR_READ_ONLY);
				streamStmt.setFetchSize(Integer.MIN_VALUE);

				this.rs = streamStmt.executeQuery("SELECT DUMMYID, DUMMYNAME "
						+ "FROM StreamingClobber ORDER BY DUMMYID");

				this.rs.next();

				// This should proceed normally, after the driver
				// clears the input stream
				clobberStmt.executeQuery("SHOW VARIABLES");
				this.rs.close();
			} finally {
				if (streamStmt != null) {
					streamStmt.close();
				}
			}
		} finally {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS StreamingClobber");
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public void testEmptyResultSetGet() throws Exception {
		try {
			this.rs = this.stmt.executeQuery("SHOW VARIABLES LIKE 'foo'");
			System.out.println(this.rs.getInt(1));
		} catch (SQLException sqlEx) {
			assertTrue("Correct exception not thrown",
					SQLError.SQL_STATE_GENERAL_ERROR
							.equals(sqlEx.getSQLState()));
		}
	}

	/**
	 * Checks fix for BUG#1592 -- cross-database updatable result sets are not
	 * checked for updatability correctly.
	 * 
	 * @throws Exception
	 *             if the test fails.
	 */
	public void testFixForBug1592() throws Exception {
		if (versionMeetsMinimum(4, 1)) {
			Statement updatableStmt = this.conn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);

			try {
				updatableStmt.execute("SELECT * FROM mysql.user");

				this.rs = updatableStmt.getResultSet();
			} catch (SQLException sqlEx) {
				String message = sqlEx.getMessage();

				if ((message != null) && (message.indexOf("denied") != -1)) {
					System.err
							.println("WARN: Can't complete testFixForBug1592(), access to"
									+ " 'mysql' database not allowed");
				} else {
					throw sqlEx;
				}
			}
		}
	}

	/**
	 * Tests fix for BUG#2006, where 2 columns with same name in a result set
	 * are returned via findColumn() in the wrong order...The JDBC spec states,
	 * that the _first_ matching column should be returned.
	 * 
	 * @throws Exception
	 *             if the test fails
	 */
	public void testFixForBug2006() throws Exception {
		try {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testFixForBug2006_1");
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testFixForBug2006_2");
			this.stmt
					.executeUpdate("CREATE TABLE testFixForBug2006_1 (key_field INT NOT NULL)");
			this.stmt
					.executeUpdate("CREATE TABLE testFixForBug2006_2 (key_field INT NULL)");
			this.stmt
					.executeUpdate("INSERT INTO testFixForBug2006_1 VALUES (1)");

			this.rs = this.stmt
					.executeQuery("SELECT testFixForBug2006_1.key_field, testFixForBug2006_2.key_field FROM testFixForBug2006_1 LEFT JOIN testFixForBug2006_2 ON testFixForBug2006_1.key_field = testFixForBug2006_2.key_field");

			ResultSetMetaData rsmd = this.rs.getMetaData();

			assertTrue(rsmd.getColumnName(1).equals(rsmd.getColumnName(2)));
			assertTrue(rsmd.isNullable(this.rs.findColumn("key_field")) == ResultSetMetaData.columnNoNulls);
			assertTrue(rsmd.isNullable(2) == ResultSetMetaData.columnNullable);
			assertTrue(this.rs.next());
			assertTrue(this.rs.getObject(1) != null);
			assertTrue(this.rs.getObject(2) == null);
		} finally {
			if (this.rs != null) {
				try {
					this.rs.close();
				} catch (SQLException sqlEx) {
					// ignore
				}

				this.rs = null;
			}

			this.stmt.executeUpdate("DROP TABLE IF EXISTS testFixForBug2006_1");
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testFixForBug2006_2");
		}
	}

	/**
	 * Tests that ResultSet.getLong() does not truncate values.
	 * 
	 * @throws Exception
	 *             if any errors occur
	 */
	public void testGetLongBug() throws Exception {
		this.stmt.executeUpdate("DROP TABLE IF EXISTS getLongBug");
		this.stmt
				.executeUpdate("CREATE TABLE IF NOT EXISTS getLongBug (int_col int, bigint_col bigint)");

		int intVal = 123456;
		long longVal1 = 123456789012345678L;
		long longVal2 = -2079305757640172711L;
		this.stmt.executeUpdate("INSERT INTO getLongBug "
				+ "(int_col, bigint_col) " + "VALUES (" + intVal + ", "
				+ longVal1 + "), " + "(" + intVal + ", " + longVal2 + ")");

		try {
			this.rs = this.stmt
					.executeQuery("SELECT int_col, bigint_col FROM getLongBug ORDER BY bigint_col DESC");
			this.rs.next();
			assertTrue(
					"Values not decoded correctly",
					((this.rs.getInt(1) == intVal) && (this.rs.getLong(2) == longVal1)));
			this.rs.next();
			assertTrue(
					"Values not decoded correctly",
					((this.rs.getInt(1) == intVal) && (this.rs.getLong(2) == longVal2)));
		} finally {
			if (this.rs != null) {
				try {
					this.rs.close();
				} catch (Exception ex) {
					// ignore
				}
			}

			this.stmt.executeUpdate("DROP TABLE IF EXISTS getLongBug");
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public void testGetTimestampWithDate() throws Exception {
		try {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testGetTimestamp");
			this.stmt.executeUpdate("CREATE TABLE testGetTimestamp (d date)");
			this.stmt
					.executeUpdate("INSERT INTO testGetTimestamp values (now())");

			this.rs = this.stmt.executeQuery("SELECT * FROM testGetTimestamp");
			this.rs.next();
			System.out.println(this.rs.getTimestamp(1));
		} finally {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testGetTimestamp");
		}
	}

	/**
	 * Tests a bug where ResultSet.isBefireFirst() would return true when the
	 * result set was empty (which is incorrect)
	 * 
	 * @throws Exception
	 *             if an error occurs.
	 */
	public void testIsBeforeFirstOnEmpty() throws Exception {
		try {
			// Query with valid rows: isBeforeFirst() correctly returns True
			this.rs = this.stmt.executeQuery("SHOW VARIABLES LIKE 'version'");
			assertTrue("Non-empty search should return true", this.rs
					.isBeforeFirst());

			// Query with empty result: isBeforeFirst() falsely returns True
			// Sun's documentation says it should return false
			this.rs = this.stmt.executeQuery("SHOW VARIABLES LIKE 'garbage'");
			assertTrue("Empty search should return false ", !this.rs
					.isBeforeFirst());
		} finally {
			this.rs.close();
		}
	}

	/**
	 * Tests a bug where ResultSet.isBefireFirst() would return true when the
	 * result set was empty (which is incorrect)
	 * 
	 * @throws Exception
	 *             if an error occurs.
	 */
	public void testMetaDataIsWritable() throws Exception {
		try {
			// Query with valid rows
			this.rs = this.stmt.executeQuery("SHOW VARIABLES LIKE 'version'");

			ResultSetMetaData rsmd = this.rs.getMetaData();

			int numColumns = rsmd.getColumnCount();

			for (int i = 1; i <= numColumns; i++) {
				assertTrue("rsmd.isWritable() should != rsmd.isReadOnly()",
						rsmd.isWritable(i) != rsmd.isReadOnly(i));
			}
		} finally {
			this.rs.close();
		}
	}

	/**
	 * Tests fix for bug # 496
	 * 
	 * @throws Exception
	 *             if an error happens.
	 */
	public void testNextAndPrevious() throws Exception {
		try {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testNextAndPrevious");
			this.stmt
					.executeUpdate("CREATE TABLE testNextAndPrevious (field1 int)");
			this.stmt
					.executeUpdate("INSERT INTO testNextAndPrevious VALUES (1)");

			this.rs = this.stmt
					.executeQuery("SELECT * from testNextAndPrevious");

			System.out.println("Currently at row " + this.rs.getRow());
			this.rs.next();
			System.out.println("Value at row " + this.rs.getRow() + " is "
					+ this.rs.getString(1));

			this.rs.previous();

			try {
				System.out.println("Value at row " + this.rs.getRow() + " is "
						+ this.rs.getString(1));
				fail("Should not be able to retrieve values with invalid cursor");
			} catch (SQLException sqlEx) {
				assertTrue(sqlEx.getMessage().startsWith("Before start"));
			}

			this.rs.next();

			this.rs.next();

			try {
				System.out.println("Value at row " + this.rs.getRow() + " is "
						+ this.rs.getString(1));
				fail("Should not be able to retrieve values with invalid cursor");
			} catch (SQLException sqlEx) {
				assertTrue(sqlEx.getMessage().startsWith("After end"));
			}
		} finally {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testNextAndPrevious");
		}
	}

	/**
	 * Tests fix for BUG#1630 (not updatable exception turning into NPE on
	 * second updateFoo() method call.
	 * 
	 * @throws Exception
	 *             if an unexpected exception is thrown.
	 */
	public void testNotUpdatable() throws Exception {
		this.rs = null;

		try {
			String sQuery = "SHOW VARIABLES";
			this.pstmt = this.conn
					.prepareStatement(sQuery, ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);

			this.rs = this.pstmt.executeQuery();

			if (this.rs.next()) {
				this.rs.absolute(1);

				try {
					this.rs.updateInt(1, 1);
				} catch (SQLException sqlEx) {
					assertTrue(sqlEx instanceof NotUpdatable);
				}

				try {
					this.rs.updateString(1, "1");
				} catch (SQLException sqlEx) {
					assertTrue(sqlEx instanceof NotUpdatable);
				}
			}
		} finally {
			if (this.pstmt != null) {
				try {
					this.pstmt.close();
				} catch (Exception e) {
					// ignore
				}
			}
		}
	}

	/**
	 * Tests that streaming result sets are registered correctly.
	 * 
	 * @throws Exception
	 *             if any errors occur
	 */
	public void testStreamingRegBug() throws Exception {
		try {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS StreamingRegBug");
			this.stmt
					.executeUpdate("CREATE TABLE StreamingRegBug ( DUMMYID "
							+ " INTEGER NOT NULL, DUMMYNAME VARCHAR(32),PRIMARY KEY (DUMMYID) )");
			this.stmt
					.executeUpdate("INSERT INTO StreamingRegBug (DUMMYID, DUMMYNAME) VALUES (0, NULL)");
			this.stmt
					.executeUpdate("INSERT INTO StreamingRegBug (DUMMYID, DUMMYNAME) VALUES (1, 'nro 1')");
			this.stmt
					.executeUpdate("INSERT INTO StreamingRegBug (DUMMYID, DUMMYNAME) VALUES (2, 'nro 2')");
			this.stmt
					.executeUpdate("INSERT INTO StreamingRegBug (DUMMYID, DUMMYNAME) VALUES (3, 'nro 3')");

			PreparedStatement streamStmt = null;

			try {
				streamStmt = this.conn.prepareStatement(
						"SELECT DUMMYID, DUMMYNAME "
								+ "FROM StreamingRegBug ORDER BY DUMMYID",
						java.sql.ResultSet.TYPE_FORWARD_ONLY,
						java.sql.ResultSet.CONCUR_READ_ONLY);
				streamStmt.setFetchSize(Integer.MIN_VALUE);

				this.rs = streamStmt.executeQuery();

				while (this.rs.next()) {
					this.rs.getString(1);
				}

				this.rs.close(); // error occurs here
			} catch (SQLException sqlEx) {

			} finally {
				if (streamStmt != null) {
					try {
						streamStmt.close();
					} catch (SQLException exWhileClose) {
						exWhileClose.printStackTrace();
					}
				}
			}
		} finally {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS StreamingRegBug");
		}
	}

	/**
	 * Tests that result sets can be updated when all parameters are correctly
	 * set.
	 * 
	 * @throws Exception
	 *             if any errors occur
	 */
	public void testUpdatability() throws Exception {
		this.rs = null;

		this.stmt.execute("DROP TABLE IF EXISTS updatabilityBug");
		this.stmt.execute("CREATE TABLE IF NOT EXISTS updatabilityBug ("
				+ " id int(10) unsigned NOT NULL auto_increment,"
				+ " field1 varchar(32) NOT NULL default '',"
				+ " field2 varchar(128) NOT NULL default '',"
				+ " field3 varchar(128) default NULL,"
				+ " field4 varchar(128) default NULL,"
				+ " field5 varchar(64) default NULL,"
				+ " field6 int(10) unsigned default NULL,"
				+ " field7 varchar(64) default NULL," + " PRIMARY KEY  (id)"
				+ ") TYPE=InnoDB;");
		this.stmt.executeUpdate("insert into updatabilityBug (id) values (1)");

		try {
			String sQuery = " SELECT * FROM updatabilityBug WHERE id = ? ";
			this.pstmt = this.conn
					.prepareStatement(sQuery, ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
			this.conn.setAutoCommit(false);
			this.pstmt.setInt(1, 1);
			this.rs = this.pstmt.executeQuery();

			if (this.rs.next()) {
				this.rs.absolute(1);
				this.rs.updateInt("id", 1);
				this.rs.updateString("field1", "1");
				this.rs.updateString("field2", "1");
				this.rs.updateString("field3", "1");
				this.rs.updateString("field4", "1");
				this.rs.updateString("field5", "1");
				this.rs.updateInt("field6", 1);
				this.rs.updateString("field7", "1");
				this.rs.updateRow();
			}

			this.conn.commit();
			this.conn.setAutoCommit(true);
		} finally {
			if (this.pstmt != null) {
				try {
					this.pstmt.close();
				} catch (Exception e) {
					// ignore
				}
			}

			this.stmt.execute("DROP TABLE IF EXISTS updatabilityBug");
		}
	}

	/**
	 * Test fixes for BUG#1071
	 * 
	 * @throws Exception
	 *             if the test fails.
	 */
	public void testUpdatabilityAndEscaping() throws Exception {
		Properties props = new Properties();
		props.setProperty("useUnicode", "true");
		props.setProperty("characterEncoding", "big5");

		Connection updConn = getConnectionWithProps(props);
		Statement updStmt = updConn.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

		try {
			updStmt
					.executeUpdate("DROP TABLE IF EXISTS testUpdatesWithEscaping");
			updStmt
					.executeUpdate("CREATE TABLE testUpdatesWithEscaping (field1 INT PRIMARY KEY, field2 VARCHAR(64))");
			updStmt
					.executeUpdate("INSERT INTO testUpdatesWithEscaping VALUES (1, null)");

			String stringToUpdate = "\" \\ '";

			this.rs = updStmt
					.executeQuery("SELECT * from testUpdatesWithEscaping");

			this.rs.next();
			this.rs.updateString(2, stringToUpdate);
			this.rs.updateRow();

			assertTrue(stringToUpdate.equals(this.rs.getString(2)));
		} finally {
			updStmt
					.executeUpdate("DROP TABLE IF EXISTS testUpdatesWithEscaping");
			updStmt.close();
			updConn.close();
		}
	}

	/**
	 * Tests the fix for BUG#661 ... refreshRow() fails when primary key values
	 * have escaped data in them.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	public void testUpdatabilityWithQuotes() throws Exception {
		Statement updStmt = null;

		try {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testUpdWithQuotes");
			this.stmt
					.executeUpdate("CREATE TABLE testUpdWithQuotes (keyField CHAR(32) PRIMARY KEY NOT NULL, field2 int)");

			PreparedStatement pStmt = this.conn
					.prepareStatement("INSERT INTO testUpdWithQuotes VALUES (?, ?)");
			pStmt.setString(1, "Abe's");
			pStmt.setInt(2, 1);
			pStmt.executeUpdate();

			updStmt = this.conn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);

			this.rs = updStmt.executeQuery("SELECT * FROM testUpdWithQuotes");
			this.rs.next();
			this.rs.updateInt(2, 2);
			this.rs.updateRow();
		} finally {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testUpdWithQuotes");

			if (this.rs != null) {
				this.rs.close();
			}

			this.rs = null;

			if (updStmt != null) {
				updStmt.close();
			}

			updStmt = null;
		}
	}

	/**
	 * Checks whether or not ResultSet.updateClob() is implemented
	 * 
	 * @throws Exception
	 *             if the test fails
	 */
	public void testUpdateClob() throws Exception {
		if (!isRunningOnJdk131()) {
			Statement updatableStmt = this.conn.createStatement(
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
	
			try {
				this.stmt.executeUpdate("DROP TABLE IF EXISTS testUpdateClob");
				this.stmt
						.executeUpdate("CREATE TABLE testUpdateClob(intField INT NOT NULL PRIMARY KEY, clobField TEXT)");
				this.stmt
						.executeUpdate("INSERT INTO testUpdateClob VALUES (1, 'foo')");
	
				this.rs = updatableStmt
						.executeQuery("SELECT intField, clobField FROM testUpdateClob");
				this.rs.next();
	
				Clob clob = this.rs.getClob(2);
	
				clob.setString(1, "bar");
	
				this.rs.updateClob(2, clob);
				this.rs.updateRow();
	
				this.rs.moveToInsertRow();
	
				clob.setString(1, "baz");
				this.rs.updateInt(1, 2);
				this.rs.updateClob(2, clob);
				this.rs.insertRow();
	
				clob.setString(1, "bat");
				this.rs.updateInt(1, 3);
				this.rs.updateClob(2, clob);
				this.rs.insertRow();
	
				this.rs.close();
	
				this.rs = this.stmt
						.executeQuery("SELECT intField, clobField FROM testUpdateClob ORDER BY intField");
	
				this.rs.next();
				assertTrue((this.rs.getInt(1) == 1)
						&& this.rs.getString(2).equals("bar"));
	
				this.rs.next();
				assertTrue((this.rs.getInt(1) == 2)
						&& this.rs.getString(2).equals("baz"));
	
				this.rs.next();
				assertTrue((this.rs.getInt(1) == 3)
						&& this.rs.getString(2).equals("bat"));
			} finally {
				this.stmt.executeUpdate("DROP TABLE IF EXISTS testUpdateClob");
			}
		}
	}

	/**
	 * Tests fix for BUG#4482, ResultSet.getObject() returns wrong type for
	 * strings when using prepared statements.
	 * 
	 * @throws Exception
	 *             if the test fails.
	 */
	public void testBug4482() throws Exception {
		this.rs = this.conn.prepareStatement("SELECT 'abcdef'").executeQuery();
		assertTrue(this.rs.next());
		assertTrue(this.rs.getObject(1) instanceof String);
	}

	/**
	 * Test fix for BUG#4689 - WasNull not getting set correctly for binary
	 * result sets.
	 */
	public void testBug4689() throws Exception {
		try {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testBug4689");
			this.stmt
					.executeUpdate("CREATE TABLE testBug4689 (tinyintField tinyint, tinyintFieldNull tinyint, "
							+ "intField int, intFieldNull int, "
							+ "bigintField bigint, bigintFieldNull bigint, "
							+ "shortField smallint, shortFieldNull smallint, "
							+ "doubleField double, doubleFieldNull double)");

			this.stmt.executeUpdate("INSERT INTO testBug4689 VALUES (1, null, "
					+ "1, null, " + "1, null, " + "1, null, " + "1, null)");

			PreparedStatement pStmt = this.conn
					.prepareStatement("SELECT tinyintField, tinyintFieldNull,"
							+ "intField, intFieldNull, "
							+ "bigintField, bigintFieldNull, "
							+ "shortField, shortFieldNull, "
							+ "doubleField, doubleFieldNull FROM testBug4689");
			this.rs = pStmt.executeQuery();
			assertTrue(this.rs.next());

			assertTrue(this.rs.getByte(1) == 1);
			assertTrue(this.rs.wasNull() == false);
			assertTrue(this.rs.getByte(2) == 0);
			assertTrue(this.rs.wasNull() == true);

			assertTrue(this.rs.getInt(3) == 1);
			assertTrue(this.rs.wasNull() == false);
			assertTrue(this.rs.getInt(4) == 0);
			assertTrue(this.rs.wasNull() == true);

			assertTrue(this.rs.getInt(5) == 1);
			assertTrue(this.rs.wasNull() == false);
			assertTrue(this.rs.getInt(6) == 0);
			assertTrue(this.rs.wasNull() == true);

			assertTrue(this.rs.getShort(7) == 1);
			assertTrue(this.rs.wasNull() == false);
			assertTrue(this.rs.getShort(8) == 0);
			assertTrue(this.rs.wasNull() == true);

			assertTrue(this.rs.getDouble(9) == 1);
			assertTrue(this.rs.wasNull() == false);
			assertTrue(this.rs.getDouble(10) == 0);
			assertTrue(this.rs.wasNull() == true);
		} finally {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testBug4689");
		}
	}

	/**
	 * Tests fix for BUG#5032 -- ResultSet.getObject() doesn't return type
	 * Boolean for pseudo-bit types from prepared statements on 4.1.x.
	 * 
	 * @throws Exception
	 *             if the test fails.
	 */
	public void testBug5032() throws Exception {
		if (versionMeetsMinimum(4, 1)) {
			PreparedStatement pStmt = null;

			try {
				this.stmt.executeUpdate("DROP TABLE IF EXISTS testBug5032");
				this.stmt.executeUpdate("CREATE TABLE testBug5032(field1 BIT)");
				this.stmt.executeUpdate("INSERT INTO testBug5032 VALUES (1)");

				pStmt = this.conn
						.prepareStatement("SELECT field1 FROM testBug5032");
				this.rs = pStmt.executeQuery();
				assertTrue(this.rs.next());
				assertTrue(this.rs.getObject(1) instanceof Boolean);
			} finally {
				this.stmt.executeUpdate("DROP TABLE IF EXISTS testBug5032");

				if (pStmt != null) {
					pStmt.close();
				}
			}
		}
	}

	/**
	 * Tests fix for BUG#5069 -- ResultSet.getMetaData() should not return
	 * incorrectly-initialized metadata if the result set has been closed, but
	 * should instead throw a SQLException. Also tests fix for getRow() and
	 * getWarnings() and traversal methods.
	 * 
	 * @throws Exception
	 *             if the test fails.
	 */
	public void testBug5069() throws Exception {
		try {
			this.rs = this.stmt.executeQuery("SELECT 1");
			this.rs.close();

			try {
				ResultSetMetaData md = this.rs.getMetaData();
			} catch (NullPointerException npEx) {
				fail("Should not catch NullPointerException here");
			} catch (SQLException sqlEx) {
				assertTrue(SQLError.SQL_STATE_GENERAL_ERROR.equals(sqlEx
						.getSQLState()));
			}

			try {
				this.rs.getRow();
			} catch (NullPointerException npEx) {
				fail("Should not catch NullPointerException here");
			} catch (SQLException sqlEx) {
				assertTrue(SQLError.SQL_STATE_GENERAL_ERROR.equals(sqlEx
						.getSQLState()));
			}

			try {
				this.rs.getWarnings();
			} catch (NullPointerException npEx) {
				fail("Should not catch NullPointerException here");
			} catch (SQLException sqlEx) {
				assertTrue(SQLError.SQL_STATE_GENERAL_ERROR.equals(sqlEx
						.getSQLState()));
			}

			try {
				this.rs.first();
			} catch (NullPointerException npEx) {
				fail("Should not catch NullPointerException here");
			} catch (SQLException sqlEx) {
				assertTrue(SQLError.SQL_STATE_GENERAL_ERROR.equals(sqlEx
						.getSQLState()));
			}

			try {
				this.rs.beforeFirst();
			} catch (NullPointerException npEx) {
				fail("Should not catch NullPointerException here");
			} catch (SQLException sqlEx) {
				assertTrue(SQLError.SQL_STATE_GENERAL_ERROR.equals(sqlEx
						.getSQLState()));
			}

			try {
				this.rs.last();
			} catch (NullPointerException npEx) {
				fail("Should not catch NullPointerException here");
			} catch (SQLException sqlEx) {
				assertTrue(SQLError.SQL_STATE_GENERAL_ERROR.equals(sqlEx
						.getSQLState()));
			}

			try {
				this.rs.afterLast();
			} catch (NullPointerException npEx) {
				fail("Should not catch NullPointerException here");
			} catch (SQLException sqlEx) {
				assertTrue(SQLError.SQL_STATE_GENERAL_ERROR.equals(sqlEx
						.getSQLState()));
			}

			try {
				this.rs.relative(0);
			} catch (NullPointerException npEx) {
				fail("Should not catch NullPointerException here");
			} catch (SQLException sqlEx) {
				assertTrue(SQLError.SQL_STATE_GENERAL_ERROR.equals(sqlEx
						.getSQLState()));
			}

			try {
				this.rs.next();
			} catch (NullPointerException npEx) {
				fail("Should not catch NullPointerException here");
			} catch (SQLException sqlEx) {
				assertTrue(SQLError.SQL_STATE_GENERAL_ERROR.equals(sqlEx
						.getSQLState()));
			}

			try {
				this.rs.previous();
			} catch (NullPointerException npEx) {
				fail("Should not catch NullPointerException here");
			} catch (SQLException sqlEx) {
				assertTrue(SQLError.SQL_STATE_GENERAL_ERROR.equals(sqlEx
						.getSQLState()));
			}

			try {
				this.rs.isBeforeFirst();
			} catch (NullPointerException npEx) {
				fail("Should not catch NullPointerException here");
			} catch (SQLException sqlEx) {
				assertTrue(SQLError.SQL_STATE_GENERAL_ERROR.equals(sqlEx
						.getSQLState()));
			}

			try {
				this.rs.isFirst();
			} catch (NullPointerException npEx) {
				fail("Should not catch NullPointerException here");
			} catch (SQLException sqlEx) {
				assertTrue(SQLError.SQL_STATE_GENERAL_ERROR.equals(sqlEx
						.getSQLState()));
			}

			try {
				this.rs.isAfterLast();
			} catch (NullPointerException npEx) {
				fail("Should not catch NullPointerException here");
			} catch (SQLException sqlEx) {
				assertTrue(SQLError.SQL_STATE_GENERAL_ERROR.equals(sqlEx
						.getSQLState()));
			}

			try {
				this.rs.isLast();
			} catch (NullPointerException npEx) {
				fail("Should not catch NullPointerException here");
			} catch (SQLException sqlEx) {
				assertTrue(SQLError.SQL_STATE_GENERAL_ERROR.equals(sqlEx
						.getSQLState()));
			}
		} finally {
			if (this.rs != null) {
				this.rs.close();
				this.rs = null;
			}
		}
	}

	/**
	 * Tests for BUG#5235, ClassCastException on all-zero date field when
	 * zeroDatetimeBehavior is 'convertToNull'...however it appears that this
	 * bug doesn't exist. This is a placeholder until we get more data from the
	 * user on how they provoke this bug to happen.
	 * 
	 * @throws Exception
	 *             if the test fails.
	 */
	public void testBug5235() throws Exception {
		try {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testBug5235");
			this.stmt.executeUpdate("CREATE TABLE testBug5235(field1 DATE)");
			this.stmt
					.executeUpdate("INSERT INTO testBug5235 (field1) VALUES ('0000-00-00')");

			Properties props = new Properties();
			props.setProperty("zeroDateTimeBehavior", "convertToNull");

			Connection nullConn = getConnectionWithProps(props);

			this.rs = nullConn.createStatement().executeQuery(
					"SELECT field1 FROM testBug5235");
			this.rs.next();
			assertTrue(null == this.rs.getObject(1));
		} finally {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testBug5235");
		}
	}

	/**
	 * Tests for BUG#5136, GEOMETRY types getting corrupted, turns out to be a
	 * server bug.
	 * 
	 * @throws Exception
	 *             if the test fails.
	 */
	public void testBug5136() throws Exception {
		if (false) {
			PreparedStatement toGeom = this.conn
					.prepareStatement("select GeomFromText(?)");
			PreparedStatement toText = this.conn
					.prepareStatement("select AsText(?)");

			String inText = "POINT(146.67596278 -36.54368233)";

			// First assert that the problem is not at the server end
			this.rs = this.stmt.executeQuery("select AsText(GeomFromText('"
					+ inText + "'))");
			this.rs.next();

			String outText = this.rs.getString(1);
			this.rs.close();
			assertTrue(
					"Server side only\n In: " + inText + "\nOut: " + outText,
					inText.equals(outText));

			// Now bring a binary geometry object to the client and send it back
			toGeom.setString(1, inText);
			this.rs = toGeom.executeQuery();
			this.rs.next();

			// Return a binary geometry object from the WKT
			Object geom = this.rs.getObject(1);
			this.rs.close();
			toText.setObject(1, geom);
			this.rs = toText.executeQuery();
			this.rs.next();

			// Return WKT from the binary geometry
			outText = this.rs.getString(1);
			this.rs.close();
			assertTrue("Server to client and back\n In: " + inText + "\nOut: "
					+ outText, inText.equals(outText));
		}
	}

	/**
	 * Tests fix for BUG#5664, ResultSet.updateByte() when on insert row throws
	 * ArrayOutOfBoundsException.
	 * 
	 * @throws Exception
	 *             if the test fails.
	 */
	public void testBug5664() throws Exception {
		try {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testBug5664");
			this.stmt
					.executeUpdate("CREATE TABLE testBug5664 (pkfield int PRIMARY KEY NOT NULL, field1 SMALLINT)");
			this.stmt.executeUpdate("INSERT INTO testBug5664 VALUES (1, 1)");

			Statement updatableStmt = this.conn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);

			this.rs = updatableStmt
					.executeQuery("SELECT pkfield, field1 FROM testBug5664");
			this.rs.next();
			this.rs.moveToInsertRow();
			this.rs.updateInt(1, 2);
			this.rs.updateByte(2, (byte) 2);
		} finally {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testBug5664");
		}
	}

	public void testBogusTimestampAsString() throws Exception {

		this.rs = this.stmt.executeQuery("SELECT '2004-08-13 13:21:17.'");

		this.rs.next();

		// We're only checking for an exception being thrown here as the bug
		this.rs.getTimestamp(1);

	}

	/**
	 * Tests our ability to reject NaN and +/- INF in
	 * PreparedStatement.setDouble();
	 */
	public void testBug5717() throws Exception {
		try {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testBug5717");
			this.stmt.executeUpdate("CREATE TABLE testBug5717 (field1 DOUBLE)");
			this.pstmt = this.conn
					.prepareStatement("INSERT INTO testBug5717 VALUES (?)");

			try {
				this.pstmt.setDouble(1, Double.NEGATIVE_INFINITY);
				fail("Exception should've been thrown");
			} catch (Exception ex) {
				// expected
			}

			try {
				this.pstmt.setDouble(1, Double.POSITIVE_INFINITY);
				fail("Exception should've been thrown");
			} catch (Exception ex) {
				// expected
			}

			try {
				this.pstmt.setDouble(1, Double.NaN);
				fail("Exception should've been thrown");
			} catch (Exception ex) {
				// expected
			}
		} finally {
			if (this.pstmt != null) {
				this.pstmt.close();
			}

			this.stmt.executeUpdate("DROP TABLE IF EXISTS testBug5717");
		}
	}

	/**
	 * Tests fix for server issue that drops precision on aggregate operations
	 * on DECIMAL types, because they come back as DOUBLEs.
	 * 
	 * @throws Exception
	 *             if the test fails.
	 */
	public void testBug6537() throws Exception {
		if (versionMeetsMinimum(4, 1, 0)) {
			String tableName = "testBug6537";

			try {
				createTable(
						tableName,
						"(`id` int(11) NOT NULL default '0',"
								+ "`value` decimal(10,2) NOT NULL default '0.00', `stringval` varchar(10),"
								+ "PRIMARY KEY  (`id`)"
								+ ") ENGINE=MyISAM DEFAULT CHARSET=latin1");
				this.stmt
						.executeUpdate("INSERT INTO "
								+ tableName
								+ "(id, value, stringval) VALUES (1, 100.00, '100.00'), (2, 200, '200')");

				String sql = "SELECT SUM(value) as total FROM " + tableName
						+ " WHERE id = ? ";
				PreparedStatement pStmt = this.conn.prepareStatement(sql);
				pStmt.setInt(1, 1);
				this.rs = pStmt.executeQuery();
				assertTrue(this.rs.next());

				assertTrue("100.00".equals(this.rs.getBigDecimal("total")
						.toString()));

				sql = "SELECT stringval as total FROM " + tableName
						+ " WHERE id = ? ";
				pStmt = this.conn.prepareStatement(sql);
				pStmt.setInt(1, 2);
				this.rs = pStmt.executeQuery();
				assertTrue(this.rs.next());

				assertTrue("200.00".equals(this.rs.getBigDecimal("total", 2)
						.toString()));

			} finally {
				dropTable(tableName);
			}
		}
	}

	/**
	 * Tests fix for BUG#6231, ResultSet.getTimestamp() on a column with TIME in
	 * it fails.
	 * 
	 * @throws Exception
	 *             if the test fails.
	 */
	public void testBug6231() throws Exception {
		if (!isRunningOnJdk131()) {
			try {
				this.stmt.executeUpdate("DROP TABLE IF EXISTS testBug6231");
				this.stmt.executeUpdate("CREATE TABLE testBug6231 (field1 TIME)");
				this.stmt
						.executeUpdate("INSERT INTO testBug6231 VALUES ('09:16:00')");
	
				this.rs = this.stmt.executeQuery("SELECT field1 FROM testBug6231");
				this.rs.next();
				long asMillis = this.rs.getTimestamp(1).getTime();
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(asMillis);
				assertTrue(cal.get(Calendar.HOUR) == 9);
				assertTrue(cal.get(Calendar.MINUTE) == 16);
				assertTrue(cal.get(Calendar.SECOND) == 0);
			} finally {
				this.stmt.executeUpdate("DROP TABLE IF EXISTS testBug6231");
			}
		}
	}

	public void testBug6619() throws Exception {
		try {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testBug6619");
			this.stmt.executeUpdate("CREATE TABLE testBug6619 (field1 int)");
			this.stmt.executeUpdate("INSERT INTO testBug6619 VALUES (1), (2)");

			PreparedStatement pStmt = this.conn
					.prepareStatement("SELECT SUM(field1) FROM testBug6619");

			this.rs = pStmt.executeQuery();
			this.rs.next();
			System.out.println(this.rs.getString(1));

		} finally {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testBug6619");
		}
	}

	public void testBug6743() throws Exception {
		// 0x835C U+30BD # KATAKANA LETTER SO
		String katakanaStr = "\u30BD";

		Properties props = new Properties();

		props.setProperty("useUnicode", "true");
		props.setProperty("characterEncoding", "SJIS");

		Connection sjisConn = null;
		Statement sjisStmt = null;

		try {
			sjisConn = getConnectionWithProps(props);
			sjisStmt = sjisConn.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);

			sjisStmt.executeUpdate("DROP TABLE IF EXISTS testBug6743");
			StringBuffer queryBuf = new StringBuffer(
					"CREATE TABLE testBug6743 (pkField INT NOT NULL PRIMARY KEY, field1 VARCHAR(32)");

			if (versionMeetsMinimum(4, 1)) {
				queryBuf.append(" CHARACTER SET SJIS");
			}

			queryBuf.append(")");
			sjisStmt.executeUpdate(queryBuf.toString());
			sjisStmt.executeUpdate("INSERT INTO testBug6743 VALUES (1, 'abc')");

			this.rs = sjisStmt
					.executeQuery("SELECT pkField, field1 FROM testBug6743");
			this.rs.next();
			this.rs.updateString(2, katakanaStr);
			this.rs.updateRow();

			String retrString = this.rs.getString(2);
			assertTrue(katakanaStr.equals(retrString));

			this.rs = sjisStmt
					.executeQuery("SELECT pkField, field1 FROM testBug6743");
			this.rs.next();

			retrString = this.rs.getString(2);
			assertTrue(katakanaStr.equals(retrString));
		} finally {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testBug6743");

			if (sjisStmt != null) {
				sjisStmt.close();
			}

			if (sjisConn != null) {
				sjisConn.close();
			}
		}
	}

	/**
	 * Tests for presence of BUG#6561, NPE thrown when dealing with 0 dates and
	 * non-unpacked result sets.
	 * 
	 * @throws Exception
	 *             if the test occurs.
	 */
	public void testBug6561() throws Exception {

		try {
			Properties props = new Properties();
			props.setProperty("zeroDateTimeBehavior", "convertToNull");

			Connection zeroConn = getConnectionWithProps(props);

			this.stmt.executeUpdate("DROP TABLE IF EXISTS testBug6561");
			this.stmt
					.executeUpdate("CREATE TABLE testBug6561 (ofield int, field1 DATE, field2 integer, field3 integer)");
			this.stmt
					.executeUpdate("INSERT INTO testBug6561 (ofield, field1,field2,field3)	VALUES (1, 0,NULL,0)");
			this.stmt
					.executeUpdate("INSERT INTO testBug6561 (ofield, field1,field2,field3) VALUES (2, '2004-11-20',NULL,0)");

			PreparedStatement ps = zeroConn
					.prepareStatement("SELECT field1,field2,field3 FROM testBug6561 ORDER BY ofield");
			this.rs = ps.executeQuery();

			assertTrue(rs.next());
			assertTrue(null == rs.getObject("field1"));
			assertTrue(null == rs.getObject("field2"));
			assertTrue(0 == rs.getInt("field3"));

			assertTrue(rs.next());
			assertTrue(rs.getString("field1").equals("2004-11-20"));
			assertTrue(null == rs.getObject("field2"));
			assertTrue(0 == rs.getInt("field3"));

			ps.close();
		} finally {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS test");
		}
	}

	public void testBug7686() throws SQLException {
		String tableName = "testBug7686";
		createTable(tableName, "(id1 int(10) unsigned NOT NULL,"
				+ " id2 DATETIME, "
				+ " field1 varchar(128) NOT NULL default '',"
				+ " PRIMARY KEY  (id1, id2)) TYPE=InnoDB;");

		this.stmt.executeUpdate("insert into " + tableName
				+ " (id1, id2, field1)"
				+ " values (1, '2005-01-05 13:59:20', 'foo')");

		String sQuery = " SELECT * FROM " + tableName
				+ " WHERE id1 = ? AND id2 = ?";
		this.pstmt = this.conn.prepareStatement(sQuery,
				ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

		this.conn.setAutoCommit(false);
		this.pstmt.setInt(1, 1);
		GregorianCalendar cal = new GregorianCalendar();
		cal.clear();
		cal.set(2005, 00, 05, 13, 59, 20);
		Timestamp jan5before2pm = new java.sql.Timestamp(cal.getTime().getTime());
		this.pstmt.setTimestamp(2, jan5before2pm);
		this.rs = this.pstmt.executeQuery();
		assertTrue(this.rs.next());
		this.rs.absolute(1);
		this.rs.updateString("field1", "bar");
		this.rs.updateRow();
		this.conn.commit();
		this.conn.setAutoCommit(true);
	}

	/**
	 * Tests fix for BUG#7715 - Timestamps converted incorrectly to strings with
	 * SSPS and Upd. Result Sets.
	 * 
	 * @throws Exception
	 *             if the test fails.
	 */
	public void testBug7715() throws Exception {
		PreparedStatement pStmt = null;

		try {
			this.stmt
					.executeUpdate("DROP TABLE IF EXISTS testConvertedBinaryTimestamp");
			this.stmt
					.executeUpdate("CREATE TABLE testConvertedBinaryTimestamp (field1 VARCHAR(32), field2 VARCHAR(32), field3 VARCHAR(32), field4 TIMESTAMP)");
			this.stmt
					.executeUpdate("INSERT INTO testConvertedBinaryTimestamp VALUES ('abc', 'def', 'ghi', NOW())");

			pStmt = this.conn
					.prepareStatement(
							"SELECT field1, field2, field3, field4 FROM testConvertedBinaryTimestamp",
							ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);

			this.rs = pStmt.executeQuery();
			assertTrue(this.rs.next());

			this.rs.getObject(4); // fails if bug exists
		} finally {
			this.stmt
					.executeUpdate("DROP TABLE IF EXISTS testConvertedBinaryTimestamp");
		}
	}

	/**
	 * Tests fix for BUG#8428 - getString() doesn't maintain format stored on
	 * server.
	 * 
	 * @throws Exception
	 *             if the test fails.
	 */
	public void testBug8428() throws Exception {
		Connection noSyncConn = null;

		try {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testBug8428");
			this.stmt
					.executeUpdate("CREATE TABLE testBug8428 (field1 YEAR, field2 DATETIME)");
			this.stmt
					.executeUpdate("INSERT INTO testBug8428 VALUES ('1999', '2005-02-11 12:54:41')");

			Properties props = new Properties();
			props.setProperty("noDatetimeStringSync", "true");
			props.setProperty("useUsageAdvisor", "true");
			props.setProperty("yearIsDateType", "false"); // for 3.1.9+

			noSyncConn = getConnectionWithProps(props);

			this.rs = noSyncConn.createStatement().executeQuery(
					"SELECT field1, field2 FROM testBug8428");
			this.rs.next();
			assertEquals("1999", this.rs.getString(1));
			assertEquals("2005-02-11 12:54:41", this.rs.getString(2));

			this.rs = noSyncConn.prepareStatement(
					"SELECT field1, field2 FROM testBug8428").executeQuery();
			this.rs.next();
			assertEquals("1999", this.rs.getString(1));
			assertEquals("2005-02-11 12:54:41", this.rs.getString(2));
		} finally {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testBug8428");
		}
	}

	/**
	 * Tests fix for Bug#8868, DATE_FORMAT() queries returned as BLOBs from
	 * getObject().
	 * 
	 * @throws Exception
	 *             if the test fails.
	 */
	public void testBug8868() throws Exception {
		if (versionMeetsMinimum(4, 1)) {
			createTable("testBug8868",
					"(field1 DATE, field2 VARCHAR(32) CHARACTER SET BINARY)");
			this.stmt
					.executeUpdate("INSERT INTO testBug8868 VALUES (NOW(), 'abcd')");
			try {
				this.rs = this.stmt
						.executeQuery("SELECT DATE_FORMAT(field1,'%b-%e %l:%i%p') as fmtddate, field2 FROM testBug8868");
				this.rs.next();
				assertEquals("java.lang.String", this.rs.getObject(1)
						.getClass().getName());
			} finally {
				if (this.rs != null) {
					this.rs.close();
				}
			}
		}
	}

	/**
	 * Tests fix for BUG#9098 - Server doesn't give us info to distinguish
	 * between CURRENT_TIMESTAMP and 'CURRENT_TIMESTAMP' for default values.
	 * 
	 * @throws Exception
	 *             if the test fails
	 */
	public void testBug9098() throws Exception {
		if (versionMeetsMinimum(4, 1, 10)) {
			Statement updatableStmt = null;

			try {
				this.stmt.executeUpdate("DROP TABLE IF EXISTS testBug9098");
				this.stmt
						.executeUpdate("CREATE TABLE testBug9098(pkfield INT PRIMARY KEY NOT NULL AUTO_INCREMENT, \n"
								+ "tsfield TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, tsfield2 TIMESTAMP NOT NULL DEFAULT '2005-12-25 12:20:52', charfield VARCHAR(4) NOT NULL DEFAULT 'abcd')");
				updatableStmt = this.conn.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				this.rs = updatableStmt
						.executeQuery("SELECT pkfield, tsfield, tsfield2, charfield FROM testBug9098");
				this.rs.moveToInsertRow();
				this.rs.insertRow();

			} finally {
				this.stmt.executeUpdate("DROP TABLE IF EXISTS testBug9098");
			}
		}
	}

	/**
	 * Tests fix for BUG#9236, a continuation of BUG#8868, where functions used
	 * in queries that should return non-string types when resolved by temporary
	 * tables suddenly become opaque binary strings (work-around for server
	 * limitation)
	 * 
	 * @throws Exception
	 *             if the test fails.
	 */
	public void testBug9236() throws Exception {
		if (versionMeetsMinimum(4, 1)) {
			try {
				createTable(
						"testBug9236",
						"("
								+ "field_1 int(18) NOT NULL auto_increment,"
								+ "field_2 varchar(50) NOT NULL default '',"
								+ "field_3 varchar(12) default NULL,"
								+ "field_4 int(18) default NULL,"
								+ "field_5 int(18) default NULL,"
								+ "field_6 datetime default NULL,"
								+ "field_7 varchar(30) default NULL,"
								+ "field_8 varchar(50) default NULL,"
								+ "field_9 datetime default NULL,"
								+ "field_10 int(18) NOT NULL default '0',"
								+ "field_11 int(18) default NULL,"
								+ "field_12 datetime NOT NULL default '0000-00-00 00:00:00',"
								+ "PRIMARY KEY  (field_1)," + "KEY (field_4),"
								+ "KEY (field_2)," + "KEY (field_3),"
								+ "KEY (field_7,field_1)," + "KEY (field_5),"
								+ "KEY (field_6,field_10,field_9),"
								+ "KEY (field_11,field_10),"
								+ "KEY (field_12,field_10)"
								+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1");

				this.stmt
						.executeUpdate("INSERT INTO testBug9236 VALUES "
								+ "(1,'0',NULL,-1,0,'0000-00-00 00:00:00','123456789','-1','2004-03-13 14:21:38',0,NULL,'2004-03-13 14:21:38'),"
								+ "(2,'0',NULL,1,0,'0000-00-00 00:00:00',NULL,'1',NULL,0,NULL,'2004-07-13 14:29:52'),"
								+ "(3,'0',NULL,1,0,'0000-00-00 00:00:00',NULL,'2',NULL,0,NULL,'2004-07-16 13:20:51'),"
								+ "(4,'0',NULL,1,0,'0000-00-00 00:00:00',NULL,'3','2004-07-16 13:43:39',0,NULL,'2004-07-16 13:22:01'),"
								+ "(5,'0',NULL,1,0,'0000-00-00 00:00:00',NULL,'4','2004-07-16 13:23:48',0,NULL,'2004-07-16 13:23:01'),"
								+ "(6,'0',NULL,1,0,'0000-00-00 00:00:00',NULL,'5',NULL,0,NULL,'2004-07-16 14:41:07'),"
								+ "(7,'0',NULL,1,0,'0000-00-00 00:00:00',NULL,'6',NULL,0,NULL,'2004-07-16 14:41:34'),"
								+ "(8,'0',NULL,1,0,'0000-00-00 00:00:00',NULL,'7',NULL,0,NULL,'2004-07-16 14:41:54'),"
								+ "(9,'0',NULL,1,0,'0000-00-00 00:00:00',NULL,'8',NULL,0,NULL,'2004-07-16 14:42:42'),"
								+ "(10,'0','PI',1,0,'0000-00-00 00:00:00',NULL,'9',NULL,0,NULL,'2004-07-18 10:51:30'),"
								+ "(11,'0',NULL,1,0,'0000-00-00 00:00:00',NULL,'10','2004-07-23 17:23:06',0,NULL,'2004-07-23 17:18:19'),"
								+ "(12,'0',NULL,1,0,'0000-00-00 00:00:00',NULL,'11','2004-07-23 17:24:45',0,NULL,'2004-07-23 17:23:57'),"
								+ "(13,'0',NULL,1,0,'0000-00-00 00:00:00',NULL,'12','2004-07-23 17:30:51',0,NULL,'2004-07-23 17:30:15'),"
								+ "(14,'0',NULL,1,0,'0000-00-00 00:00:00',NULL,'13','2004-07-26 17:50:19',0,NULL,'2004-07-26 17:49:38'),"
								+ "(15,'0','FRL',1,0,'0000-00-00 00:00:00',NULL,'1',NULL,0,NULL,'2004-08-19 18:29:18'),"
								+ "(16,'0','FRL',1,0,'0000-00-00 00:00:00',NULL,'15',NULL,0,NULL,'2005-03-16 12:08:28')");

				createTable("testBug9236_1",
						"(field1 CHAR(2) CHARACTER SET BINARY)");
				this.stmt
						.executeUpdate("INSERT INTO testBug9236_1 VALUES ('ab')");
				this.rs = this.stmt
						.executeQuery("SELECT field1 FROM testBug9236_1");

				ResultSetMetaData rsmd = this.rs.getMetaData();
				assertEquals("[B", rsmd.getColumnClassName(1));
				assertTrue(this.rs.next());
				Object asObject = rs.getObject(1);
				assertEquals("[B", asObject.getClass().getName());

				this.rs = this.stmt
						.executeQuery("select DATE_FORMAT(field_12, '%Y-%m-%d') as date, count(*) as count from testBug9236 where field_10 = 0 and field_3 = 'FRL' and field_12 >= '2005-03-02 00:00:00' and field_12 <= '2005-03-17 00:00:00' group by date");
				rsmd = this.rs.getMetaData();
				assertEquals("java.lang.String", rsmd.getColumnClassName(1));
				this.rs.next();
				asObject = rs.getObject(1);
				assertEquals("java.lang.String", asObject.getClass().getName());

				this.rs.close();

				createTable("testBug8868_2",
						"(field1 CHAR(4) CHARACTER SET BINARY)");
				this.stmt
						.executeUpdate("INSERT INTO testBug8868_2 VALUES ('abc')");
				this.rs = this.stmt
						.executeQuery("SELECT field1 FROM testBug8868_2");

				rsmd = this.rs.getMetaData();
				assertEquals("[B", rsmd.getColumnClassName(1));
				this.rs.next();
				asObject = rs.getObject(1);
				assertEquals("[B", asObject.getClass().getName());
			} finally {
				if (this.rs != null) {
					this.rs.close();
					this.rs = null;
				}
			}
		}
	}

	/**
	 * Tests fix for BUG#9437, IF() returns type of [B or java.lang.String
	 * depending on platform. Fixed earlier, but in here to catch if it ever
	 * regresses.
	 * 
	 * @throws Exception
	 *             if the test fails.
	 */
	public void testBug9437() throws Exception {
		String tableName = "testBug9437";

		if (versionMeetsMinimum(4, 1, 0)) {
			try {
				createTable(
						tableName,
						"("
								+ "languageCode char(2) NOT NULL default '',"
								+ "countryCode char(2) NOT NULL default '',"
								+ "supported enum('no','yes') NOT NULL default 'no',"
								+ "ordering int(11) default NULL,"
								+ "createDate datetime NOT NULL default '1000-01-01 00:00:03',"
								+ "modifyDate timestamp NOT NULL default CURRENT_TIMESTAMP on update"
								+ " CURRENT_TIMESTAMP,"
								+ "PRIMARY KEY  (languageCode,countryCode),"
								+ "KEY languageCode (languageCode),"
								+ "KEY countryCode (countryCode),"
								+ "KEY ordering (ordering),"
								+ "KEY modifyDate (modifyDate)"
								+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8");

				this.stmt.executeUpdate("INSERT INTO " + tableName
						+ " (languageCode) VALUES ('en')");

				String alias = "someLocale";
				String sql = "select if ( languageCode = ?, ?, ? ) as " + alias
						+ " from " + tableName;
				this.pstmt = this.conn.prepareStatement(sql);

				int count = 1;
				this.pstmt.setObject(count++, "en");
				this.pstmt.setObject(count++, "en_US");
				this.pstmt.setObject(count++, "en_GB");

				this.rs = this.pstmt.executeQuery();

				assertTrue(this.rs.next());

				Object object = this.rs.getObject(alias);

				if (object != null) {
					assertEquals("java.lang.String", object.getClass()
							.getName());
					assertEquals("en_US", object.toString());
				}

			} finally {
				if (this.rs != null) {
					this.rs.close();
					this.rs = null;
				}

				if (this.pstmt != null) {
					this.pstmt.close();
					this.pstmt = null;
				}
			}
		}
	}

	public void testBug9684() throws Exception {
		if (versionMeetsMinimum(4, 1, 9)) {
			String tableName = "testBug9684";

			try {
				createTable(tableName,
						"(sourceText text character set utf8 collate utf8_bin)");
				this.stmt.executeUpdate("INSERT INTO " + tableName
						+ " VALUES ('abc')");
				this.rs = this.stmt.executeQuery("SELECT sourceText FROM "
						+ tableName);
				assertTrue(this.rs.next());
				assertEquals("java.lang.String", this.rs.getString(1)
						.getClass().getName());
				assertEquals("abc", this.rs.getString(1));
			} finally {
				if (this.rs != null) {
					this.rs.close();
					this.rs = null;
				}
			}
		}
	}

	/**
	 * Tests fix for BUG#10156 - Unsigned SMALLINT treated as signed
	 * 
	 * @throws Exception
	 *             if the test fails.
	 */
	public void testBug10156() throws Exception {
		String tableName = "testBug10156";
		try {
			createTable(tableName, "(field1 smallint(5) unsigned, "
					+ "field2 tinyint unsigned," + "field3 int unsigned)");
			this.stmt.executeUpdate("INSERT INTO " + tableName
					+ " VALUES (32768, 255, 4294967295)");
			this.rs = this.conn.prepareStatement(
					"SELECT field1, field2, field3 FROM " + tableName)
					.executeQuery();
			assertTrue(this.rs.next());
			assertEquals(32768, this.rs.getInt(1));
			assertEquals(255, this.rs.getInt(2));
			assertEquals(4294967295L, this.rs.getLong(3));

			assertEquals(String.valueOf(this.rs.getObject(1)), String
					.valueOf(this.rs.getInt(1)));
			assertEquals(String.valueOf(this.rs.getObject(2)), String
					.valueOf(this.rs.getInt(2)));
			assertEquals(String.valueOf(this.rs.getObject(3)), String
					.valueOf(this.rs.getLong(3)));

		} finally {
			if (this.rs != null) {
				this.rs.close();
				this.rs = null;
			}
		}
	}

	public void testBug10212() throws Exception {
		String tableName = "testBug10212";

		try {
			createTable(tableName, "(field1 YEAR(4))");
			this.stmt.executeUpdate("INSERT INTO " + tableName
					+ " VALUES (1974)");
			this.rs = this.conn.prepareStatement(
					"SELECT field1 FROM " + tableName).executeQuery();

			ResultSetMetaData rsmd = this.rs.getMetaData();
			assertTrue(this.rs.next());
			assertEquals("java.sql.Date", rsmd.getColumnClassName(1));
			assertEquals("java.sql.Date", this.rs.getObject(1).getClass()
					.getName());

			this.rs = this.stmt.executeQuery("SELECT field1 FROM " + tableName);

			rsmd = this.rs.getMetaData();
			assertTrue(this.rs.next());
			assertEquals("java.sql.Date", rsmd.getColumnClassName(1));
			assertEquals("java.sql.Date", this.rs.getObject(1).getClass()
					.getName());
		} finally {
			if (this.rs != null) {
				this.rs.close();
				this.rs = null;
			}
		}
	}

	/**
	 * Tests fix for BUG#11190 - ResultSet.moveToCurrentRow() fails to work when
	 * preceeded with .moveToInsertRow().
	 * 
	 * @throws Exception
	 *             if the test fails.
	 */
	public void testBug11190() throws Exception {

		createTable("testBug11190", "(a CHAR(4) PRIMARY KEY, b VARCHAR(20))");
		this.stmt
				.executeUpdate("INSERT INTO testBug11190 VALUES('3000','L'),('3001','H'),('1050','B')");

		Statement updStmt = null;

		try {
			updStmt = this.conn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);

			this.rs = updStmt.executeQuery("select * from testBug11190");
			assertTrue("must return a row", this.rs.next());
			String savedValue = this.rs.getString(1);
			this.rs.moveToInsertRow();
			this.rs.updateString(1, "4000");
			this.rs.updateString(2, "C");
			this.rs.insertRow();

			this.rs.moveToCurrentRow();
			assertEquals(savedValue, this.rs.getString(1));
		} finally {
			if (this.rs != null) {
				this.rs.close();
				this.rs = null;
			}

			if (updStmt != null) {
				updStmt.close();
			}
		}
	}

	/**
	 * Tests fix for BUG#12104 - Geometry types not handled with server-side
	 * prepared statements.
	 * 
	 * @throws Exception
	 *             if the test fails
	 */
	public void testBug12104() throws Exception {
		if (versionMeetsMinimum(4, 1)) {
			createTable("testBug12104", "(field1 GEOMETRY)");

			try {
				this.stmt
						.executeUpdate("INSERT INTO testBug12104 VALUES (GeomFromText('POINT(1 1)'))");
				this.pstmt = this.conn
						.prepareStatement("SELECT field1 FROM testBug12104");
				this.rs = this.pstmt.executeQuery();
				assertTrue(this.rs.next());
				System.out.println(this.rs.getObject(1));
			} finally {

			}
		}
	}

	/**
	 * Tests fix for BUG#13043 - when 'gatherPerfMetrics'
	 * is enabled for servers < 4.1.0, a NPE is thrown
	 * from the constructor of ResultSet if the query
	 * doesn't use any tables.
	 * 
	 * @throws Exception if the test fails
	 */
	public void testBug13043() throws Exception {
        if(!versionMeetsMinimum(4, 1)) {
        	Connection perfConn = null;
    
            try {
                Properties props = new Properties();
                props.put("gatherPerfMetrics", "true"); // this property is reported as the cause of NullPointerException
                props.put("reportMetricsIntervalMillis", "30000"); // this property is reported as the cause of NullPointerException
                perfConn = getConnectionWithProps(props);
                perfConn.createStatement().executeQuery("SELECT 1");
            } finally {
            	if (perfConn != null) {
            		perfConn.close();
            	}
            }
        }
	}
	
	/**
	 * Tests fix for BUG#13374 - ResultSet.getStatement() 
	 * on closed result set returns NULL (as per JDBC 4.0 spec,
	 * but not backwards-compatible).
	 * 
	 * @throws Exception if the test fails
	 */
	
	public void testBug13374() throws Exception {
		Statement retainStmt = null;
		Connection retainConn = null;
		
		try {
			Properties props = new Properties();
			
			props.setProperty("retainStatementAfterResultSetClose",
					"true");
			
			retainConn = getConnectionWithProps(props);
			
			retainStmt = retainConn.createStatement();
			
			this.rs = retainStmt.executeQuery("SELECT 1");
			this.rs.close();
			assertNotNull(this.rs.getStatement());
			
			this.rs = this.stmt.executeQuery("SELECT 1");
			this.rs.close();
			
			try {
				this.rs.getStatement();
			} catch (SQLException sqlEx) {
				assertEquals(sqlEx.getSQLState(), 
						SQLError.SQL_STATE_GENERAL_ERROR);
			}
			
		} finally {
			if (this.rs != null) {
				this.rs.close();
				this.rs = null;
			}
			
			if (retainStmt != null) {
				retainStmt.close();
			}
			
			if (retainConn != null) {
				retainConn.close();
			}
		}
	}
	
	public void testNPEWithUsageAdvisor() throws Exception {
		Connection advisorConn = null;

		try {
			Properties props = new Properties();
			props.setProperty("useUsageAdvisor", "true");

			advisorConn = getConnectionWithProps(props);
			this.pstmt = advisorConn.prepareStatement("SELECT 1");
			this.rs = this.pstmt.executeQuery();
			this.rs.close();
			this.rs = this.pstmt.executeQuery();

		} finally {
		}
	}

	public void testAllTypesForNull() throws Exception {
		if (!isRunningOnJdk131()) {
			Properties props = new Properties();
			props.setProperty("jdbcCompliantTruncation", "false");
			props.setProperty("zeroDateTimeBehavior", "round");
			Connection conn2 = getConnectionWithProps(props);
			Statement stmt2 = conn2.createStatement();
	
			DatabaseMetaData dbmd = conn.getMetaData();
	
			this.rs = dbmd.getTypeInfo();
	
			boolean firstColumn = true;
			int numCols = 1;
			StringBuffer createStatement = new StringBuffer(
					"CREATE TABLE testAllTypes (");
			List wasDatetimeTypeList = new ArrayList();
	
			while (this.rs.next()) {
				String dataType = this.rs.getString("TYPE_NAME").toUpperCase();
	
				boolean wasDateTime = false;
	
				if (dataType.indexOf("DATE") != -1
						|| dataType.indexOf("TIME") != -1) {
					wasDateTime = true;
				}
	
				if (!"BOOL".equalsIgnoreCase(dataType)
						&& !"LONG VARCHAR".equalsIgnoreCase(dataType)
						&& !"LONG VARBINARY".equalsIgnoreCase(dataType)
						&& !"ENUM".equalsIgnoreCase(dataType)
						&& !"SET".equalsIgnoreCase(dataType)) {
					wasDatetimeTypeList.add(new Boolean(wasDateTime));
					createStatement.append("\n\t");
					if (!firstColumn) {
						createStatement.append(",");
					} else {
						firstColumn = false;
					}
	
					createStatement.append("field_");
					createStatement.append(numCols++);
					createStatement.append(" ");
	
					createStatement.append(dataType);
	
					if (dataType.indexOf("CHAR") != -1
							|| dataType.indexOf("BINARY") != -1
							&& dataType.indexOf("BLOB") == -1
							&& dataType.indexOf("TEXT") == -1) {
						createStatement.append("(");
						createStatement.append(this.rs.getString("PRECISION"));
						createStatement.append(")");
					}
	
					createStatement.append(" NULL DEFAULT NULL");
				}
			}
	
			createStatement.append("\n)");
	
			stmt2.executeUpdate("DROP TABLE IF EXISTS testAllTypes");
	
			stmt2.executeUpdate(createStatement.toString());
			StringBuffer insertStatement = new StringBuffer(
					"INSERT INTO testAllTypes VALUES (NULL");
			for (int i = 1; i < numCols - 1; i++) {
				insertStatement.append(", NULL");
			}
			insertStatement.append(")");
			stmt2.executeUpdate(insertStatement.toString());
	
			this.rs = stmt2.executeQuery("SELECT * FROM testAllTypes");
	
			testAllFieldsForNull(this.rs);
			this.rs.close();
	
			this.rs = this.conn.prepareStatement("SELECT * FROM testAllTypes")
					.executeQuery();
			testAllFieldsForNull(this.rs);
	
			stmt2.executeUpdate("DELETE FROM testAllTypes");
	
			insertStatement = new StringBuffer("INSERT INTO testAllTypes VALUES (");
	
			boolean needsNow = ((Boolean) wasDatetimeTypeList.get(0))
					.booleanValue();
	
			if (needsNow) {
				insertStatement.append("NOW()");
			} else {
				insertStatement.append("'0'");
			}
	
			for (int i = 1; i < numCols - 1; i++) {
				needsNow = ((Boolean) wasDatetimeTypeList.get(i)).booleanValue();
				insertStatement.append(",");
				if (needsNow) {
					insertStatement.append("NOW()");
				} else {
					insertStatement.append("'0'");
				}
			}
	
			insertStatement.append(")");
	
			stmt2.executeUpdate(insertStatement.toString());
	
			this.rs = stmt2.executeQuery("SELECT * FROM testAllTypes");
	
			testAllFieldsForNotNull(this.rs, wasDatetimeTypeList);
			this.rs.close();
	
			this.rs = conn2.prepareStatement("SELECT * FROM testAllTypes")
					.executeQuery();
			testAllFieldsForNotNull(this.rs, wasDatetimeTypeList);
		}
	}

	private void testAllFieldsForNull(ResultSet rsToTest) throws Exception {
		ResultSetMetaData rsmd = rs.getMetaData();
		int numCols = rsmd.getColumnCount();

		while (rsToTest.next()) {
			for (int i = 0; i < numCols - 1; i++) {
				if (!"BIT".equalsIgnoreCase(rsmd.getColumnTypeName(i + 1))) {
					assertEquals(false, rsToTest.getBoolean(i + 1));
					assertTrue(rsToTest.wasNull());

					assertEquals(0, rsToTest.getDouble(i + 1), 0 /* delta */);
					assertTrue(rsToTest.wasNull());
					assertEquals(0, rsToTest.getFloat(i + 1), 0 /* delta */);
					assertTrue(rsToTest.wasNull());
					assertEquals(0, rsToTest.getInt(i + 1));
					assertTrue(rsToTest.wasNull());
					assertEquals(0, rsToTest.getLong(i + 1));
					assertTrue(rsToTest.wasNull());
					assertEquals(null, rsToTest.getObject(i + 1));
					assertTrue(rsToTest.wasNull());
					assertEquals(null, rsToTest.getString(i + 1));
					assertTrue(rsToTest.wasNull());
					assertEquals(null, rsToTest.getAsciiStream(i + 1));
					assertTrue(rsToTest.wasNull());
					assertEquals(null, rsToTest.getBigDecimal(i + 1));
					assertTrue(rsToTest.wasNull());
					assertEquals(null, rsToTest.getBinaryStream(i + 1));
					assertTrue(rsToTest.wasNull());
					assertEquals(null, rsToTest.getBlob(i + 1));
					assertTrue(rsToTest.wasNull());
					assertEquals(0, rsToTest.getByte(i + 1));
					assertTrue(rsToTest.wasNull());
					assertEquals(null, rsToTest.getBytes(i + 1));
					assertTrue(rsToTest.wasNull());
					assertEquals(null, rsToTest.getCharacterStream(i + 1));
					assertTrue(rsToTest.wasNull());
					assertEquals(null, rsToTest.getClob(i + 1));
					assertTrue(rsToTest.wasNull());
					assertEquals(null, rsToTest.getDate(i + 1));
					assertTrue(rsToTest.wasNull());
					assertEquals(0, rsToTest.getShort(i + 1));
					assertTrue(rsToTest.wasNull());
					assertEquals(null, rsToTest.getTime(i + 1));
					assertTrue(rsToTest.wasNull());
					assertEquals(null, rsToTest.getTimestamp(i + 1));
					assertTrue(rsToTest.wasNull());
					assertEquals(null, rsToTest.getUnicodeStream(i + 1));
					assertTrue(rsToTest.wasNull());
					assertEquals(null, rsToTest.getURL(i + 1));
					assertTrue(rsToTest.wasNull());
				}
			}
		}
	}

	private void testAllFieldsForNotNull(ResultSet rsToTest,
			List wasDatetimeTypeList) throws Exception {
		ResultSetMetaData rsmd = rs.getMetaData();
		int numCols = rsmd.getColumnCount();

		while (rsToTest.next()) {
			for (int i = 0; i < numCols - 1; i++) {
				boolean wasDatetimeType = ((Boolean) wasDatetimeTypeList.get(i))
						.booleanValue();
				String columnType = rsmd.getColumnTypeName(i + 1);
				
				if (!"BIT".equalsIgnoreCase(columnType) &&
					!"BINARY".equalsIgnoreCase(columnType) &&
					!"VARBINARY".equalsIgnoreCase(columnType)) {
					if (!wasDatetimeType) {

						assertEquals(false, rsToTest.getBoolean(i + 1));
						assertTrue(!rsToTest.wasNull());

						assertEquals(0, rsToTest.getDouble(i + 1), 0 /* delta */);
						assertTrue(!rsToTest.wasNull());
						assertEquals(0, rsToTest.getFloat(i + 1), 0 /* delta */);
						assertTrue(!rsToTest.wasNull());
						assertEquals(0, rsToTest.getInt(i + 1));
						assertTrue(!rsToTest.wasNull());
						assertEquals(0, rsToTest.getLong(i + 1));
						assertTrue(!rsToTest.wasNull());
						assertEquals(0, rsToTest.getByte(i + 1));
						assertTrue(!rsToTest.wasNull());
						assertEquals(0, rsToTest.getShort(i + 1));
						assertTrue(!rsToTest.wasNull());
					}

					assertNotNull(rsToTest.getObject(i + 1));
					assertTrue(!rsToTest.wasNull());
					assertNotNull(rsToTest.getString(i + 1));
					assertTrue(!rsToTest.wasNull());
					assertNotNull(rsToTest.getAsciiStream(i + 1));
					assertTrue(!rsToTest.wasNull());

					assertNotNull(rsToTest.getBinaryStream(i + 1));
					assertTrue(!rsToTest.wasNull());
					assertNotNull(rsToTest.getBlob(i + 1));
					assertTrue(!rsToTest.wasNull());
					assertNotNull(rsToTest.getBytes(i + 1));
					assertTrue(!rsToTest.wasNull());
					assertNotNull(rsToTest.getCharacterStream(i + 1));
					assertTrue(!rsToTest.wasNull());
					assertNotNull(rsToTest.getClob(i + 1));
					assertTrue(!rsToTest.wasNull());

					String columnClassName = rsmd.getColumnClassName(i + 1);

					boolean canBeUsedAsDate = !("java.lang.Boolean"
							.equals(columnClassName)
							|| "java.lang.Double".equals(columnClassName)
							|| "java.lang.Float".equals(columnClassName)
							|| "java.lang.Real".equals(columnClassName) || "java.math.BigDecimal"
							.equals(columnClassName));

					if (canBeUsedAsDate) {
						assertNotNull(rsToTest.getDate(i + 1));
						assertTrue(!rsToTest.wasNull());
						assertNotNull(rsToTest.getTime(i + 1));
						assertTrue(!rsToTest.wasNull());
						assertNotNull(rsToTest.getTimestamp(i + 1));
						assertTrue(!rsToTest.wasNull());
					}

					assertNotNull(rsToTest.getUnicodeStream(i + 1));
					assertTrue(!rsToTest.wasNull());

					try {
						assertNotNull(rsToTest.getURL(i + 1));
					} catch (SQLException sqlEx) {
						assertTrue(sqlEx.getMessage().indexOf("URL") != -1);
					}

					assertTrue(!rsToTest.wasNull());
				}
			}
		}
	}

	public void testNPEWithStatementsAndTime() throws Exception {
		try {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testNPETime");
			this.stmt
					.executeUpdate("CREATE TABLE testNPETime (field1 TIME NULL, field2 DATETIME NULL, field3 DATE NULL)");
			this.stmt
					.executeUpdate("INSERT INTO testNPETime VALUES (null, null, null)");
			this.pstmt = this.conn
					.prepareStatement("SELECT field1, field2, field3 FROM testNPETime");
			this.rs = this.pstmt.executeQuery();
			this.rs.next();

			for (int i = 0; i < 3; i++) {
				assertEquals(null, this.rs.getTime(i + 1));
				assertEquals(true, this.rs.wasNull());
			}

			for (int i = 0; i < 3; i++) {
				assertEquals(null, this.rs.getTimestamp(i + 1));
				assertEquals(true, this.rs.wasNull());
			}

			for (int i = 0; i < 3; i++) {
				assertEquals(null, this.rs.getDate(i + 1));
				assertEquals(true, this.rs.wasNull());
			}
		} finally {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS testNPETime");
		}
	}

	public void testEmptyStringsWithNumericGetters() throws Exception {
		try {
			createTable("emptyStringTable", "(field1 char(32))");
			this.stmt.executeUpdate("INSERT INTO emptyStringTable VALUES ('')");
			this.rs = this.stmt
					.executeQuery("SELECT field1 FROM emptyStringTable");
			assertTrue(this.rs.next());
			createTable("emptyStringTable", "(field1 char(32))");
			this.stmt.executeUpdate("INSERT INTO emptyStringTable VALUES ('')");

			this.rs = this.stmt
					.executeQuery("SELECT field1 FROM emptyStringTable");
			assertTrue(this.rs.next());
			checkEmptyConvertToZero();

			this.rs = this.conn.prepareStatement(
					"SELECT field1 FROM emptyStringTable").executeQuery();
			assertTrue(this.rs.next());
			checkEmptyConvertToZero();

			Properties props = new Properties();
			props.setProperty("useFastIntParsing", "false");

			Connection noFastIntParseConn = getConnectionWithProps(props);
			Statement noFastIntStmt = noFastIntParseConn.createStatement();

			this.rs = noFastIntStmt
					.executeQuery("SELECT field1 FROM emptyStringTable");
			assertTrue(this.rs.next());
			checkEmptyConvertToZero();

			this.rs = noFastIntParseConn.prepareStatement(
					"SELECT field1 FROM emptyStringTable").executeQuery();
			assertTrue(this.rs.next());
			checkEmptyConvertToZero();

			//
			// Now, be more pedantic....
			//

			props = new Properties();
			props.setProperty("emptyStringsConvertToZero", "false");

			Connection pedanticConn = getConnectionWithProps(props);
			Statement pedanticStmt = pedanticConn.createStatement();

			this.rs = pedanticStmt
					.executeQuery("SELECT field1 FROM emptyStringTable");
			assertTrue(this.rs.next());

			checkEmptyConvertToZeroException();

			this.rs = pedanticConn.prepareStatement(
					"SELECT field1 FROM emptyStringTable").executeQuery();
			assertTrue(this.rs.next());
			checkEmptyConvertToZeroException();

			props = new Properties();
			props.setProperty("emptyStringsConvertToZero", "false");
			props.setProperty("useFastIntParsing", "false");

			pedanticConn = getConnectionWithProps(props);
			pedanticStmt = pedanticConn.createStatement();

			this.rs = pedanticStmt
					.executeQuery("SELECT field1 FROM emptyStringTable");
			assertTrue(this.rs.next());

			checkEmptyConvertToZeroException();

			this.rs = pedanticConn.prepareStatement(
					"SELECT field1 FROM emptyStringTable").executeQuery();
			assertTrue(this.rs.next());
			checkEmptyConvertToZeroException();

		} finally {
			if (this.rs != null) {
				this.rs.close();

				this.rs = null;
			}
		}
	}

	public void testNegativeOneIsTrue() throws Exception {
		if (!versionMeetsMinimum(5, 0, 3)) {
			String tableName = "testNegativeOneIsTrue";
			Connection tinyInt1IsBitConn = null;

			try {
				createTable(tableName, "(field1 BIT)");
				this.stmt.executeUpdate("INSERT INTO " + tableName
						+ " VALUES (-1)");

				Properties props = new Properties();
				props.setProperty("tinyInt1isBit", "true");
				tinyInt1IsBitConn = getConnectionWithProps(props);

				this.rs = tinyInt1IsBitConn.createStatement().executeQuery(
						"SELECT field1 FROM " + tableName);
				assertTrue(this.rs.next());
				assertEquals(true, this.rs.getBoolean(1));

				this.rs = tinyInt1IsBitConn.prepareStatement(
						"SELECT field1 FROM " + tableName).executeQuery();
				assertTrue(this.rs.next());
				assertEquals(true, this.rs.getBoolean(1));

			} finally {
				if (tinyInt1IsBitConn != null) {
					tinyInt1IsBitConn.close();
				}
			}
		}
	}

	/**
	 * @throws SQLException
	 */
	private void checkEmptyConvertToZero() throws SQLException {
		assertEquals(0, this.rs.getByte(1));
		assertEquals(0, this.rs.getShort(1));
		assertEquals(0, this.rs.getInt(1));
		assertEquals(0, this.rs.getLong(1));
		assertEquals(0, this.rs.getFloat(1), 0.1);
		assertEquals(0, this.rs.getDouble(1), 0.1);
		assertEquals(0, this.rs.getBigDecimal(1).intValue());
	}

	/**
	 * 
	 */
	private void checkEmptyConvertToZeroException() {
		try {
			assertEquals(0, this.rs.getByte(1));
			fail("Should've thrown an exception!");
		} catch (SQLException sqlEx) {
			assertEquals(SQLError.SQL_STATE_INVALID_CHARACTER_VALUE_FOR_CAST,
					sqlEx.getSQLState());
		}
		try {
			assertEquals(0, this.rs.getShort(1));
			fail("Should've thrown an exception!");
		} catch (SQLException sqlEx) {
			assertEquals(SQLError.SQL_STATE_INVALID_CHARACTER_VALUE_FOR_CAST,
					sqlEx.getSQLState());
		}
		try {
			assertEquals(0, this.rs.getInt(1));
			fail("Should've thrown an exception!");
		} catch (SQLException sqlEx) {
			assertEquals(SQLError.SQL_STATE_INVALID_CHARACTER_VALUE_FOR_CAST,
					sqlEx.getSQLState());
		}
		try {
			assertEquals(0, this.rs.getLong(1));
			fail("Should've thrown an exception!");
		} catch (SQLException sqlEx) {
			assertEquals(SQLError.SQL_STATE_INVALID_CHARACTER_VALUE_FOR_CAST,
					sqlEx.getSQLState());
		}
		try {
			assertEquals(0, this.rs.getFloat(1), 0.1);
			fail("Should've thrown an exception!");
		} catch (SQLException sqlEx) {
			assertEquals(SQLError.SQL_STATE_INVALID_CHARACTER_VALUE_FOR_CAST,
					sqlEx.getSQLState());
		}
		try {
			assertEquals(0, this.rs.getDouble(1), 0.1);
			fail("Should've thrown an exception!");
		} catch (SQLException sqlEx) {
			assertEquals(SQLError.SQL_STATE_INVALID_CHARACTER_VALUE_FOR_CAST,
					sqlEx.getSQLState());
		}
		try {
			assertEquals(0, this.rs.getBigDecimal(1).intValue());
			fail("Should've thrown an exception!");
		} catch (SQLException sqlEx) {
			assertEquals(SQLError.SQL_STATE_INVALID_CHARACTER_VALUE_FOR_CAST,
					sqlEx.getSQLState());
		}
	}

	/**
	 * Tests fix for BUG#10485, SQLException thrown when retrieving YEAR(2) with
	 * ResultSet.getString().
	 * 
	 * @throws Exception
	 *             if the test fails.
	 */
	public void testBug10485() throws Exception {
		if (!isRunningOnJdk131()) {
			String tableName = "testBug10485";
	
			createTable(tableName, "(field1 YEAR(2))");
			this.stmt.executeUpdate("INSERT INTO " + tableName + " VALUES ('05')");
	
			this.rs = this.stmt.executeQuery("SELECT field1 FROM " + tableName);
			assertTrue(rs.next());
			assertEquals("2005-01-01", rs.getString(1));
	
			this.rs = this.conn.prepareStatement("SELECT field1 FROM " + tableName)
					.executeQuery();
			assertTrue(rs.next());
			assertEquals("2005-01-01", rs.getString(1));
	
			Properties props = new Properties();
			props.setProperty("yearIsDateType", "false");
	
			Connection yearShortConn = getConnectionWithProps(props);
			this.rs = yearShortConn.createStatement().executeQuery(
					"SELECT field1 FROM " + tableName);
			assertTrue(rs.next());
			assertEquals("05", rs.getString(1));
	
			this.rs = yearShortConn.prepareStatement(
					"SELECT field1 FROM " + tableName).executeQuery();
			assertTrue(rs.next());
			assertEquals("05", rs.getString(1));
	
			if (versionMeetsMinimum(5, 0)) {
				try {
					this.stmt
							.executeUpdate("DROP PROCEDURE IF EXISTS testBug10485");
					this.stmt
							.executeUpdate("CREATE PROCEDURE testBug10485()\nBEGIN\nSELECT field1 FROM "
									+ tableName + ";\nEND");
	
					this.rs = this.conn.prepareCall("{CALL testBug10485()}")
							.executeQuery();
					assertTrue(rs.next());
					assertEquals("2005-01-01", rs.getString(1));
	
					this.rs = yearShortConn.prepareCall("{CALL testBug10485()}")
							.executeQuery();
					assertTrue(rs.next());
					assertEquals("05", rs.getString(1));
				} finally {
					this.stmt
							.executeUpdate("DROP PROCEDURE IF EXISTS testBug10485");
				}
			}
		}
	}

	/**
	 * Tests fix for BUG#11552, wrong values returned from server-side prepared
	 * statements if values are unsigned.
	 * 
	 * @throws Exception
	 *             if the test fails.
	 */
	public void testBug11552() throws Exception {
		try {
			createTable(
					"testBug11552",
					"(field1 INT UNSIGNED, field2 TINYINT UNSIGNED, field3 SMALLINT UNSIGNED, field4 BIGINT UNSIGNED)");
			this.stmt
					.executeUpdate("INSERT INTO testBug11552 VALUES (2, 2, 2, 2), (4294967294, 255, 32768, 18446744073709551615 )");
			this.rs = this.conn
					.prepareStatement(
							"SELECT field1, field2, field3, field4 FROM testBug11552 ORDER BY field1 ASC")
					.executeQuery();
			this.rs.next();
			assertEquals("2", this.rs.getString(1));
			assertEquals("2", this.rs.getObject(1).toString());
			assertEquals("2", String.valueOf(this.rs.getLong(1)));

			assertEquals("2", this.rs.getString(2));
			assertEquals("2", this.rs.getObject(2).toString());
			assertEquals("2", String.valueOf(this.rs.getLong(2)));

			assertEquals("2", this.rs.getString(3));
			assertEquals("2", this.rs.getObject(3).toString());
			assertEquals("2", String.valueOf(this.rs.getLong(3)));

			assertEquals("2", this.rs.getString(4));
			assertEquals("2", this.rs.getObject(4).toString());
			assertEquals("2", String.valueOf(this.rs.getLong(4)));

			this.rs.next();

			assertEquals("4294967294", this.rs.getString(1));
			assertEquals("4294967294", this.rs.getObject(1).toString());
			assertEquals("4294967294", String.valueOf(this.rs.getLong(1)));

			assertEquals("255", this.rs.getString(2));
			assertEquals("255", this.rs.getObject(2).toString());
			assertEquals("255", String.valueOf(this.rs.getLong(2)));

			assertEquals("32768", this.rs.getString(3));
			assertEquals("32768", this.rs.getObject(3).toString());
			assertEquals("32768", String.valueOf(this.rs.getLong(3)));

			assertEquals("18446744073709551615", this.rs.getString(4));
			assertEquals("18446744073709551615", this.rs.getObject(4)
					.toString());
		} finally {
			if (this.rs != null) {
				this.rs.close();
				this.rs = null;
			}
		}
	}

	/**
	 * Tests bugfix for BUG#14562 - metadata/type for
	 * MEDIUMINT UNSIGNED is incorrect.
	 * 
	 * @throws Exception if the test fails.
	 */
	public void testBug14562() throws Exception {
		createTable("testBug14562", "(row_order INT, signed_field MEDIUMINT, unsigned_field MEDIUMINT UNSIGNED)");
	
		try {
			this.stmt.executeUpdate("INSERT INTO testBug14562 VALUES (1, -8388608, 0), (2, 8388607, 16777215)");
			
			this.rs = this.stmt.executeQuery("SELECT signed_field, unsigned_field FROM testBug14562 ORDER BY row_order");
			traverseResultSetBug14562();
			
			this.rs = this.conn.prepareStatement("SELECT signed_field, unsigned_field FROM testBug14562 ORDER BY row_order").executeQuery();
			traverseResultSetBug14562();
			
			if (versionMeetsMinimum(5, 0)) {
				CallableStatement storedProc = null;
				
				try {
					this.stmt.executeUpdate("DROP PROCEDURE IF EXISTS sp_testBug14562");
					this.stmt.executeUpdate("CREATE PROCEDURE sp_testBug14562() BEGIN SELECT signed_field, unsigned_field FROM testBug14562 ORDER BY row_order; END");
					storedProc = this.conn.prepareCall("{call sp_testBug14562()}");
					storedProc.execute();
					this.rs = storedProc.getResultSet();
					traverseResultSetBug14562();
					
					this.stmt.executeUpdate("DROP PROCEDURE IF EXISTS sp_testBug14562_1");
					this.stmt.executeUpdate("CREATE PROCEDURE sp_testBug14562_1(OUT param_1 MEDIUMINT, OUT param_2 MEDIUMINT UNSIGNED) BEGIN SELECT signed_field, unsigned_field INTO param_1, param_2 FROM testBug14562 WHERE row_order=1; END");
					storedProc = this.conn.prepareCall("{call sp_testBug14562_1(?, ?)}");
					storedProc.registerOutParameter(1, Types.INTEGER);
					storedProc.registerOutParameter(2, Types.INTEGER);
	
					storedProc.execute();
					
					assertEquals("java.lang.Integer", storedProc.getObject(1).getClass().getName());
					assertEquals("java.lang.Integer", storedProc.getObject(2).getClass().getName());
					
					if (!isRunningOnJdk131()) {
						ParameterMetaData pmd = storedProc.getParameterMetaData();
						assertEquals(Types.INTEGER, pmd.getParameterType(1));
						assertEquals(Types.INTEGER, pmd.getParameterType(2));
						assertEquals("MEDIUMINT", pmd.getParameterTypeName(1).toUpperCase(Locale.US));
						assertEquals("MEDIUMINT UNSIGNED", pmd.getParameterTypeName(2).toUpperCase(Locale.US));
						assertEquals("java.lang.Integer", pmd.getParameterClassName(1));
						assertEquals("java.lang.Integer", pmd.getParameterClassName(2));
					}
				} finally {
					if (storedProc != null) {
						storedProc.close();
					}
					
					this.stmt.executeUpdate("DROP PROCEDURE IF EXISTS sp_testBug14562");
				}
			}
			
			this.rs = this.conn.getMetaData().getColumns(this.conn.getCatalog(), 
					null, "testBug14562", "%field");
			
			assertTrue(this.rs.next());
			
			assertEquals(Types.INTEGER, this.rs.getInt("DATA_TYPE"));
			assertEquals("MEDIUMINT", this.rs.getString("TYPE_NAME").toUpperCase(Locale.US));
	
			assertTrue(this.rs.next());
			
			assertEquals(Types.INTEGER, this.rs.getInt("DATA_TYPE"));
			assertEquals("MEDIUMINT UNSIGNED", this.rs.getString("TYPE_NAME").toUpperCase(Locale.US));
			
			//
			// The following test is harmless in the 3.1 driver, but
			// is needed for the 5.0 driver, so we'll leave it here
			//
			if (versionMeetsMinimum(5, 0, 14)) {
				Connection infoSchemConn = null;
				
				try {
					Properties props = new Properties();
					props.setProperty("useInformationSchema", "true");
					
					infoSchemConn = getConnectionWithProps(props);
					
					this.rs = infoSchemConn.getMetaData().getColumns(infoSchemConn.getCatalog(), 
							null, "testBug14562", "%field");
					
					assertTrue(this.rs.next());
					
					assertEquals(Types.INTEGER, this.rs.getInt("DATA_TYPE"));
					assertEquals("MEDIUMINT", this.rs.getString("TYPE_NAME").toUpperCase(Locale.US));
	
					assertTrue(this.rs.next());
					
					assertEquals(Types.INTEGER, this.rs.getInt("DATA_TYPE"));
					assertEquals("MEDIUMINT UNSIGNED", this.rs.getString("TYPE_NAME").toUpperCase(Locale.US));
					
				} finally {
					if (infoSchemConn != null) {
						infoSchemConn.close();
					}
				}
			}
		} finally {
			
		}
	}

	private void traverseResultSetBug14562() throws SQLException {
		assertTrue(this.rs.next());
		
		ResultSetMetaData rsmd = this.rs.getMetaData();
		assertEquals("MEDIUMINT", rsmd.getColumnTypeName(1));
		assertEquals("MEDIUMINT UNSIGNED", rsmd.getColumnTypeName(2));
		
		assertEquals(Types.INTEGER, rsmd.getColumnType(1));
		assertEquals(Types.INTEGER, rsmd.getColumnType(2));
		
		assertEquals("java.lang.Integer", rsmd.getColumnClassName(1));
		assertEquals("java.lang.Integer", rsmd.getColumnClassName(2));
		
		assertEquals(-8388608, this.rs.getInt(1));
		assertEquals(0, this.rs.getInt(2));
		
		assertEquals("java.lang.Integer", this.rs.getObject(1).getClass().getName());
		assertEquals("java.lang.Integer", this.rs.getObject(2).getClass().getName());
		
		assertTrue(this.rs.next());
		
		assertEquals(8388607, this.rs.getInt(1));
		assertEquals(16777215, this.rs.getInt(2));
		
		assertEquals("java.lang.Integer", this.rs.getObject(1).getClass().getName());
		assertEquals("java.lang.Integer", this.rs.getObject(2).getClass().getName());
	}

	/**
	 * Tests fix for BUG#14609 - Exception thrown for new decimal
	 * type when using updatable result sets.
	 * 
	 * @throws Exception if the test fails
	 */
	public void testBug14609() throws Exception {
		if (versionMeetsMinimum(5, 0)) {
			createTable("testBug14609", "(field1 int primary key, field2 decimal)");
			this.stmt.executeUpdate("INSERT INTO testBug14609 VALUES (1, 1)");
			
			PreparedStatement updatableStmt = this.conn.prepareStatement(
					"SELECT field1, field2 FROM testBug14609",
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			
			
			try {
				this.rs = updatableStmt.executeQuery();
			} finally {
				if (this.rs != null) {
					ResultSet toClose = this.rs;
					this.rs = null;
					toClose.close();
				}
				
				if (updatableStmt != null) {
					updatableStmt.close();
				}
			}
		}
	}
}
