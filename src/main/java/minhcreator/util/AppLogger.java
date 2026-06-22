package minhcreator.util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AppLogger {

    public enum Level {
        INFO, WARNING, ERROR, DEBUG
    }

    public static class LogEntry {
        private final LocalDateTime timestamp;
        private final Level level;
        private final String source;
        private final String message;

        public LogEntry(Level level, String source, String message) {
            this.timestamp = LocalDateTime.now();
            this.level = level;
            this.source = source;
            this.message = message;
        }

        public LocalDateTime getTimestamp() { return timestamp; }
        public Level getLevel() { return level; }
        public String getSource() { return source; }
        public String getMessage() { return message; }
    }

    private static final List<LogEntry> logs = new ArrayList<>();
    private static final int MAX_LOG_SIZE = 1000;
    private static DefaultTableModel tableModel;

    public static void bindTable(DefaultTableModel model) {
        tableModel = model;
    }

    public static void info(String source, String message) {
        add(new LogEntry(Level.INFO, source, message));
    }

    public static void warning(String source, String message) {
        add(new LogEntry(Level.WARNING, source, message));
    }

    public static void error(String source, String message) {
        add(new LogEntry(Level.ERROR, source, message));
        System.err.println("[" + source + "] " + message);
    }

    public static void debug(String source, String message) {
        add(new LogEntry(Level.DEBUG, source, message));
    }

    private static synchronized void add(LogEntry entry) {
        logs.add(entry);
        if (logs.size() > MAX_LOG_SIZE) {
            logs.remove(0);
        }
        SwingUtilities.invokeLater(() -> appendToTable(entry));
    }

    private static void appendToTable(LogEntry entry) {
        if (tableModel == null) return;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
        tableModel.addRow(new Object[]{
                fmt.format(entry.getTimestamp()),
                entry.getLevel(),
                entry.getSource(),
                entry.getMessage()
        });
        if (tableModel.getRowCount() > 500) {
            tableModel.removeRow(0);
        }
    }

    public static List<LogEntry> getLogs() {
        return new ArrayList<>(logs);
    }

    public static List<LogEntry> getLogsByLevel(Level level) {
        return logs.stream().filter(e -> e.getLevel() == level).toList();
    }

    public static void clear() {
        logs.clear();
        if (tableModel != null) {
            tableModel.setRowCount(0);
        }
    }
}
