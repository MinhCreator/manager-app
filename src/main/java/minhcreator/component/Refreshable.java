package minhcreator.component;

public interface Refreshable {
    void refreshData();
    default int getRefreshIntervalMs() { return 0; }
}
