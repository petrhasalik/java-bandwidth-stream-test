package cz.ds.stream.config;

import java.util.Date;

/**
 * Created by ds (petr.hasalik@direct-services.cz) on 24.8.2014.
 *
 * A factory object to load and save bandwidth definitions.
 * It is also responsible for search appropriate item in bandwidth pool.
 */
public interface BandwidthConfigFactory {

    /**
     * Method to save config (to file, DB, etc...).
     */
    void saveConfig();

    /**
     * Method to load config (from file, DB, etc...).
     */
    void loadConfig();

    /**
     * Method returns config name
     * @return Name of configuration
     */
    String getConfigName();

    /**
     * Returns true when config is fully initialized
     * @return true when config is initialized
     */
    boolean isInitialized();

    /**
     * Method finds appropriate bandwidth by pool name and date.
     *
     * @param poolName Name of bandwidth pool
     * @param date Date
     * @return Bandwidth
     */
    Bandwidth getBandwidth(String poolName, Date date);
}
