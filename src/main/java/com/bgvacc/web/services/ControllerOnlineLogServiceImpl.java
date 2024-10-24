package com.bgvacc.web.services;

import com.bgvacc.web.responses.sessions.ControllerOnlineLogResponse;
import com.bgvacc.web.responses.sessions.NotCompletedControllerSession;
import com.bgvacc.web.vatsim.atc.VatsimATC;
import java.sql.*;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class ControllerOnlineLogServiceImpl implements ControllerOnlineLogService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<ControllerOnlineLogResponse> getControllerLastOnlineSessions(String cid, int numberOfConnections, boolean shouldIncludeNonCompleted) {

    String getControllerLastOnlineSessionsSql = "SELECT * FROM controllers_online_log WHERE cid = ?";

    if (!shouldIncludeNonCompleted) {
      getControllerLastOnlineSessionsSql += " AND session_ended IS NOT NULL";
    }

    getControllerLastOnlineSessionsSql += " ORDER BY session_started DESC";

    if (numberOfConnections > 0) {
      getControllerLastOnlineSessionsSql += " LIMIT " + numberOfConnections;
    }

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getControllerLastOnlineSessionsPstmt = conn.prepareStatement(getControllerLastOnlineSessionsSql)) {

      try {

        conn.setAutoCommit(false);

        getControllerLastOnlineSessionsPstmt.setString(1, cid);

        ResultSet getNotCompletedControllerSessionsRset = getControllerLastOnlineSessionsPstmt.executeQuery();

        List<ControllerOnlineLogResponse> controllerLastOnlineSessions = new ArrayList<>();

        while (getNotCompletedControllerSessionsRset.next()) {

          ControllerOnlineLogResponse colr = new ControllerOnlineLogResponse();
          colr.setControllerOnlineId(getNotCompletedControllerSessionsRset.getString("controller_online_log_id"));
          colr.setCid(getNotCompletedControllerSessionsRset.getString("cid"));
          colr.setRating(getNotCompletedControllerSessionsRset.getInt("rating"));
          colr.setServer(getNotCompletedControllerSessionsRset.getString("server"));
          colr.setPosition(getNotCompletedControllerSessionsRset.getString("position"));
          colr.setSessionStarted(getNotCompletedControllerSessionsRset.getTimestamp("session_started"));
          colr.setSessionEnded(getNotCompletedControllerSessionsRset.getTimestamp("session_ended"));

          controllerLastOnlineSessions.add(colr);
        }

        return controllerLastOnlineSessions;

      } catch (SQLException ex) {
        log.error("Error getting not completed controller sessions", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting not completed controller sessions", e);
    }

    return new ArrayList<>();
  }

  @Override
  public List<ControllerOnlineLogResponse> getControllerOnlineSessions(String cid, int numberOfConnections) {

    String getControllerLastOnlineSessionsSql = "SELECT * FROM controllers_online_log WHERE cid = ? AND session_ended IS NULL ORDER BY session_started DESC";

    if (numberOfConnections > 0) {
      getControllerLastOnlineSessionsSql += " LIMIT " + numberOfConnections;
    }

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getControllerLastOnlineSessionsPstmt = conn.prepareStatement(getControllerLastOnlineSessionsSql)) {

      try {

        conn.setAutoCommit(false);

        getControllerLastOnlineSessionsPstmt.setString(1, cid);

        ResultSet getNotCompletedControllerSessionsRset = getControllerLastOnlineSessionsPstmt.executeQuery();

        List<ControllerOnlineLogResponse> controllerLastOnlineSessions = new ArrayList<>();

        while (getNotCompletedControllerSessionsRset.next()) {

          ControllerOnlineLogResponse colr = new ControllerOnlineLogResponse();
          colr.setControllerOnlineId(getNotCompletedControllerSessionsRset.getString("controller_online_log_id"));
          colr.setCid(getNotCompletedControllerSessionsRset.getString("cid"));
          colr.setRating(getNotCompletedControllerSessionsRset.getInt("rating"));
          colr.setServer(getNotCompletedControllerSessionsRset.getString("server"));
          colr.setPosition(getNotCompletedControllerSessionsRset.getString("position"));
          colr.setSessionStarted(getNotCompletedControllerSessionsRset.getTimestamp("session_started"));
          colr.setSessionEnded(getNotCompletedControllerSessionsRset.getTimestamp("session_ended"));

          controllerLastOnlineSessions.add(colr);
        }

        return controllerLastOnlineSessions;

      } catch (SQLException ex) {
        log.error("Error getting controller online sessions", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting controller online sessions", e);
    }

    return new ArrayList<>();
  }

  @Override
  public List<NotCompletedControllerSession> getNotCompletedControllerSessions() {

    final String getNotCompletedControllerSessionsSql = "SELECT controller_online_log_id, cid, position FROM controllers_online_log WHERE session_ended IS NULL";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getNotCompletedControllerSessionsPstmt = conn.prepareStatement(getNotCompletedControllerSessionsSql)) {

      try {

        conn.setAutoCommit(false);

        ResultSet getNotCompletedControllerSessionsRset = getNotCompletedControllerSessionsPstmt.executeQuery();

        List<NotCompletedControllerSession> notCompletedControllerSessions = new ArrayList<>();

        while (getNotCompletedControllerSessionsRset.next()) {

          NotCompletedControllerSession nccs = new NotCompletedControllerSession();
          nccs.setControllerOnlineId(getNotCompletedControllerSessionsRset.getString("controller_online_log_id"));
          nccs.setCid(getNotCompletedControllerSessionsRset.getString("cid"));
          nccs.setPosition(getNotCompletedControllerSessionsRset.getString("position"));
          notCompletedControllerSessions.add(nccs);
        }

        return notCompletedControllerSessions;

      } catch (SQLException ex) {
        log.error("Error getting not completed controller sessions", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting not completed controller sessions", e);
    }

    return new ArrayList<>();
  }

  @Override
  public boolean openNewControllerSession(VatsimATC onlineAtc) {

    final String openNewControllerSessionSql = "INSERT INTO controllers_online_log (cid, rating, server, position) VALUES (?, ?, ?, ?)";
    final String checkIfSessionIsActiveSql = "SELECT EXISTS (SELECT 1 FROM controllers_online_log WHERE session_ended IS NULL AND cid = ? AND position = ?)";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement openNewControllerSessionPstmt = conn.prepareStatement(openNewControllerSessionSql);
            PreparedStatement checkIfSessionIsActivePstmt = conn.prepareStatement(checkIfSessionIsActiveSql)) {

      try {

        conn.setAutoCommit(false);

        checkIfSessionIsActivePstmt.setString(1, String.valueOf(onlineAtc.getId()));
        checkIfSessionIsActivePstmt.setString(2, onlineAtc.getCallsign());

        ResultSet checkIfSessionIsActiveRset = checkIfSessionIsActivePstmt.executeQuery();

        if (checkIfSessionIsActiveRset.next()) {
          if (!checkIfSessionIsActiveRset.getBoolean(1)) {
            log.info("Session is not active. Opening a new session.");

            openNewControllerSessionPstmt.setString(1, String.valueOf(onlineAtc.getId()));
            openNewControllerSessionPstmt.setInt(2, onlineAtc.getRating());
            openNewControllerSessionPstmt.setString(3, onlineAtc.getServer());
            openNewControllerSessionPstmt.setString(4, onlineAtc.getCallsign());;

            boolean result = openNewControllerSessionPstmt.executeUpdate() > 0;

            conn.commit();

            return result;
          }
        }

        return false;

      } catch (SQLException ex) {
        log.error("Error opening new controller session.", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error opening new controller session.", e);
    }

    return false;
  }

  @Override
  public boolean endControllerSessionWithId(String controllerOnlineLogId) {

    final String endControllerSessionWithIdSql = "UPDATE controllers_online_log SET session_ended = NOW() WHERE controller_online_log_id = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement endControllerSessionWithIdPstmt = conn.prepareStatement(endControllerSessionWithIdSql)) {

      try {

        conn.setAutoCommit(false);

        endControllerSessionWithIdPstmt.setString(1, controllerOnlineLogId);

        boolean result = endControllerSessionWithIdPstmt.executeUpdate() > 0;

        conn.commit();

        return result;

      } catch (SQLException ex) {
        log.error("Error ending controller session with ID: '" + controllerOnlineLogId + "'.", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error ending controller session with ID: '" + controllerOnlineLogId + "'.", e);
    }

    return false;
  }
}
