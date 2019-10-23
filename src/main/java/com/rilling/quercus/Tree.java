package com.rilling.quercus;

import com.rilling.quercus.lookup.LookupResult;
import com.rilling.quercus.lookup.LookupStrategy;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Base tree interface defining map-like access.
 */
public interface Tree<TKey, UValue> {
    /**
     * Sets a value for a given path.
     * Middle nodes will be created automatically.
     *
     * @param path  Path to set the value for. May not be empty.
     * @param value Value to set.
     */
    void setPath(@NotNull List<TKey> path, UValue value);

    /**
     * Checks if a given path exists in this tree.
     *
     * @param path Path to check for. May not be empty.
     * @return if the path exists, based on the strategy used.
     */
    boolean hasPath(@NotNull List<TKey> path);

    /**
     * Checks if a given path exists in this tree.
     *
     * @param path           Path to check for. May not be empty.
     * @param lookupStrategy Strategy to use. See {@link LookupStrategy} for details.
     * @return if the path exists, based on the strategy used.
     */
    boolean hasPath(@NotNull List<TKey> path, LookupStrategy lookupStrategy);

    /**
     * Gets a given path in this tree.
     *
     * @param path Path to get. May not be empty.
     * @return lookup result, containing details about which node was retrieved and what path was used.
     */
    LookupResult<TKey, UValue> getPath(@NotNull List<TKey> path);
}
