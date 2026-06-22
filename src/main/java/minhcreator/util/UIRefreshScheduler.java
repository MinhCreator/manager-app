package minhcreator.util;

import minhcreator.component.Refreshable;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UIRefreshScheduler {

    private static final UIRefreshScheduler instance = new UIRefreshScheduler();
    private final List<Refreshable> panels = new CopyOnWriteArrayList<>();
    private Timer globalTimer;
    private boolean running;

    private UIRefreshScheduler() {}

    public static UIRefreshScheduler getInstance() {
        return instance;
    }

    public void register(Refreshable panel) {
        if (panel == null) return;
        panels.add(panel);
        AppLogger.debug("UIRefresh", "Registered: " + panel.getClass().getSimpleName());
        ensureTimerRunning();
    }

    public void unregister(Refreshable panel) {
        panels.remove(panel);
        if (panels.isEmpty()) stop();
    }

    public void refreshAll() {
        for (Refreshable p : panels) {
            try {
                EventQueue.invokeLater(p::refreshData);
            } catch (Exception e) {
                AppLogger.error("UIRefresh", "Error refreshing " + p.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
    }

    public void refreshByType(Class<?> type) {
        for (Refreshable p : panels) {
            if (type.isInstance(p)) {
                EventQueue.invokeLater(p::refreshData);
            }
        }
    }

    public int getRegisteredCount() {
        return panels.size();
    }

    private synchronized void ensureTimerRunning() {
        if (running) return;
        running = true;
        globalTimer = new Timer(8000, e -> refreshAll());
        globalTimer.start();
        AppLogger.info("UIRefresh", "Auto-refresh started (8s interval)");
    }

    public synchronized void stop() {
        if (globalTimer != null) {
            globalTimer.stop();
            globalTimer = null;
        }
        running = false;
        AppLogger.info("UIRefresh", "Auto-refresh stopped");
    }

    public void restart() {
        stop();
        if (!panels.isEmpty()) ensureTimerRunning();
    }
}
