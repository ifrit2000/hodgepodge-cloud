package io.github.cd871127.hodgepodge.mybatis;

public class MultiDataSourceManager {

    private static ThreadLocal<String> currentDataSource = new ThreadLocal<>();

    public static void setCurrentDataSource(String dataSourceName) {
        currentDataSource.set(dataSourceName);
    }

    public static String getCurrentDataSource() {
        return currentDataSource.get();
    }

    public static String clear() {
        String dataSourceName = getCurrentDataSource();
        currentDataSource.remove();
        return dataSourceName;
    }
}
