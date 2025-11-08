package br.com.autoinsight.autoinsight_client.modules.reports.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportsService {

  @Autowired
  private DataSource dataSource;

  public String executeYardsReportJson() {
    Connection connection = null;
    try {
      connection = dataSource.getConnection();

      try (CallableStatement enableStmt = connection.prepareCall(
          "{ call DBMS_OUTPUT.ENABLE(1000000) }")) {
        enableStmt.execute();
      }

      try (CallableStatement procStmt = connection.prepareCall(
          "{ call PKG_REPORTS.sp_yards_report_json }")) {
        procStmt.execute();
      }

      return captureDbmsOutput(connection);

    } catch (SQLException e) {
      throw new RuntimeException("Error executing yards report JSON: " + e.getMessage(), e);
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
        }
      }
    }
  }

  public String executeGroupedReportTabular() {
    Connection connection = null;
    try {
      connection = dataSource.getConnection();

      try (CallableStatement enableStmt = connection.prepareCall(
          "{ call DBMS_OUTPUT.ENABLE(1000000) }")) {
        enableStmt.execute();
      }

      try (CallableStatement procStmt = connection.prepareCall(
          "{ call PKG_REPORTS.sp_grouped_report_tabular }")) {
        procStmt.execute();
      }

      return captureDbmsOutput(connection);

    } catch (SQLException e) {
      throw new RuntimeException("Error executing grouped report tabular: " + e.getMessage(), e);
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
        }
      }
    }
  }

  public String executeRelationalToJson(String table, String fields, String condition) {
    try (Connection connection = dataSource.getConnection();
        CallableStatement stmt = connection.prepareCall(
            "{ ? = call PKG_UTILS.fn_relational_to_json(?, ?, ?) }")) {

      stmt.registerOutParameter(1, Types.CLOB);
      stmt.setString(2, table);
      stmt.setString(3, fields);
      if (condition == null || condition.trim().isEmpty()) {
        stmt.setNull(4, Types.VARCHAR);
      } else {
        stmt.setString(4, condition);
      }

      stmt.execute();

      java.sql.Clob clob = stmt.getClob(1);
      if (clob != null) {
        long length = clob.length();
        if (length > 0) {
          return clob.getSubString(1, (int) Math.min(length, Integer.MAX_VALUE));
        }
      }
      return null;

    } catch (SQLException e) {
      throw new RuntimeException("Error executing relational to JSON: " + e.getMessage(), e);
    }
  }

  public String executeValidatePassword(String password) {
    try (Connection connection = dataSource.getConnection();
        CallableStatement stmt = connection.prepareCall(
            "{ ? = call PKG_UTILS.fn_validate_password(?) }")) {

      stmt.registerOutParameter(1, Types.VARCHAR);
      stmt.setString(2, password);

      stmt.execute();

      return stmt.getString(1);

    } catch (SQLException e) {
      throw new RuntimeException("Error validating password: " + e.getMessage(), e);
    }
  }

  private String captureDbmsOutput(Connection connection) throws SQLException {
    StringBuilder output = new StringBuilder();

    try (CallableStatement getLineStmt = connection.prepareCall(
        "{ call DBMS_OUTPUT.GET_LINE(?, ?) }")) {

      getLineStmt.registerOutParameter(1, Types.VARCHAR);
      getLineStmt.registerOutParameter(2, Types.INTEGER);

      boolean hasMore = true;
      while (hasMore) {
        getLineStmt.execute();
        int status = getLineStmt.getInt(2);
        if (status == 0) {
          String line = getLineStmt.getString(1);
          if (line != null) {
            output.append(line);
            if (!line.endsWith("\n")) {
              output.append("\n");
            }
          }
        } else {
          hasMore = false;
        }
      }
    }

    return output.toString().trim();
  }
}
