package com.rilling.quercus.tree;

import com.rilling.quercus.lookup.LookupResult;
import com.rilling.quercus.lookup.LookupStrategy;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Data class defining a single node containing a value and a map of sub-nodes.
 */
public class TreeNode<TKey, UValue> {
    private final Map<TKey, TreeNode<TKey, UValue>> paths = new HashMap<>();
    private UValue value;

    /**
     * Creates a new instance.
     */
    public TreeNode() {
        this(null);
    }

    /**
     * Creates a new instance with a value.
     *
     * @param value Value to instantiate the node with.
     */
    public TreeNode(UValue value) {
        this.value = value;
    }

    public UValue getValue() {
        return value;
    }

    public void setValue(UValue value) {
        this.value = value;
    }

    /**
     * Checks if a given path exists in this tree.
     *
     * @param path Path to check for. May not be empty.
     * @return if the path exists, based on the strategy used.
     */
    public boolean hasPath(@NotNull List<TKey> path) {
        return hasPath(path, LookupStrategy.EXISTENCE_BY_NODE);
    }

    /**
     * Checks if a given path exists in this tree.
     *
     * @param path           Path to check for. May not be empty.
     * @param lookupStrategy Strategy to use. See {@link LookupStrategy} for details.
     * @return if the path exists, based on the strategy used.
     */
    public boolean hasPath(@NotNull List<TKey> path, @NotNull LookupStrategy lookupStrategy) {
        validatePath(path);

        LookupResult<TKey, UValue> lookupResult = resolvePath(Collections.unmodifiableList(path), ResolverStrategy.RETURN_ON_MISSING);
        if (lookupResult.getNode() == null) {
            return false;
        }
        if (lookupStrategy == LookupStrategy.EXISTENCE_BY_NODE) {
            return true;
        }
        return lookupResult.getNode().getValue() != null;
    }

    /**
     * Gets a given path in this tree.
     *
     * @param path Path to get. May not be empty.
     * @return lookup result, containing details about which node was retrieved and what path was used.
     */
    @NotNull
    public LookupResult<TKey, UValue> getPath(@NotNull List<TKey> path) {
        validatePath(path);

        return resolvePath(Collections.unmodifiableList(path), ResolverStrategy.RETURN_ON_MISSING);
    }

    /**
     * Sets a value for a given path.
     * Middle nodes will be created automatically.
     *
     * @param path  Path to set the value for. May not be empty.
     * @param value Value to set.
     */
    public void setPath(@NotNull List<TKey> path, UValue value) {
        validatePath(path);

        LookupResult<TKey, UValue> lookupResult = resolvePath(Collections.unmodifiableList(path), ResolverStrategy.CREATE_MISSING);

        lookupResult.getNode().setValue(value);
    }

    /**
     * Sets a tree node for a given path.
     * Middle nodes will be created automatically.
     *
     * @param path  Path to set the value for. May not be empty.
     * @param value Tree node to set.
     */
    public void setPath(@NotNull List<TKey> path, @NotNull TreeNode<TKey, UValue> value) {
        validatePath(path);

        LookupResult<TKey, UValue> lookupResult = resolvePath(Collections.unmodifiableList(path), ResolverStrategy.CREATE_MISSING);

        lookupResult.getParent().getNode().paths.put(lookupResult.getParent().getKey(), value);
    }

    @NotNull
    protected TreeNode<TKey, UValue> createNode() {
        return new TreeNode<>();
    }

    /**
     * Resolves the path against this tree.
     *
     * @param path             Path to resolve
     * @param resolverStrategy Strategy to use for non-existent nodes.
     * @return Lookup result.
     */
    @NotNull
    private LookupResult<TKey, UValue> resolvePath(@NotNull List<TKey> path, @NotNull ResolverStrategy resolverStrategy) {
        return resolvePath(path, resolverStrategy, Collections.emptyList());
    }

    /**
     * Resolves the path against this tree.
     *
     * @param path             Path to resolve
     * @param resolverStrategy Strategy to use for non-existent nodes.
     * @param previousPath     Only used for recursive calls. Path the resolving was delegated from.
     * @return Lookup result.
     */
    @NotNull
    private LookupResult<TKey, UValue> resolvePath(@NotNull List<TKey> path, @NotNull ResolverStrategy resolverStrategy, @NotNull List<TKey> previousPath) {
        TKey key = path.get(0);
        TreeNode<TKey, UValue> node;
        if (!paths.containsKey(key)) {
            if (resolverStrategy != ResolverStrategy.CREATE_MISSING) {
                return new LookupResult<>(null, previousPath, path, this, key);
            }

            node = createNode();
            paths.put(key, node);
        } else {
            node = paths.get(key);
        }

        List<TKey> previousPathNew = new ArrayList<>(previousPath);
        previousPathNew.add(key);

        if (path.size() == 1) {
            return new LookupResult<>(node, previousPathNew, Collections.emptyList(), this, key);
        }

        List<TKey> nextPath = path.subList(1, path.size());
        return node.resolvePath(nextPath, resolverStrategy, previousPathNew);
    }

    /**
     * Validates a given path.
     *
     * @param path Path to check.
     */
    private void validatePath(List<TKey> path) {
        Objects.requireNonNull(path);

        if (path.isEmpty()) {
            throw new IllegalArgumentException("Path may not be empty.");
        }
    }

    /**
     * Strategy to use when resolving tree nodes internally.
     */
    private enum ResolverStrategy {
        /**
         * Return immediately when a node cannot be found.
         * This is usually wanted when looking up an entry that might exist.
         */
        RETURN_ON_MISSING,

        /**
         * Create missing nodes dynamically and continue.
         * This is used when setting a new value that has non-existent middle nodes.
         */
        CREATE_MISSING
    }
}
