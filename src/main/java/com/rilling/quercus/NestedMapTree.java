package com.rilling.quercus;

import com.rilling.quercus.lookup.LookupResult;
import com.rilling.quercus.lookup.LookupStrategy;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Default implementation of a tree, using nested maps.
 */
public class NestedMapTree<TKey, UValue> implements Tree<TKey, UValue> {

    private final TreeNode<TKey, UValue> node;

    /**
     * Creates an empty instance.
     */
    public NestedMapTree() {
        this.node = createNode();
    }

    @Override
    public void setPath(@NotNull List<TKey> path, UValue value) {
        validatePath(path);

        LookupResult<TKey, UValue> lookupResult = resolve(path, ResolverStrategy.CREATE_MISSING);
        lookupResult.getNode().setValue(value);
    }

    @Override
    public boolean hasPath(@NotNull List<TKey> path) {
        return hasPath(path, LookupStrategy.EXISTENCE_BY_VALUE);
    }

    @Override
    public boolean hasPath(@NotNull List<TKey> path, LookupStrategy lookupStrategy) {
        validatePath(path);

        LookupResult<TKey, UValue> lookupResult = resolve(path, ResolverStrategy.RETURN_ON_MISSING);
        if (lookupResult.getNode() == null) {
            return false;
        }

        if (lookupStrategy == LookupStrategy.EXISTENCE_BY_NODE) {
            return true;
        }
        return lookupResult.getNode().getValue() != null;
    }

    @Override
    public LookupResult<TKey, UValue> getPath(@NotNull List<TKey> path) {
        validatePath(path);

        return resolve(path, ResolverStrategy.RETURN_ON_MISSING);
    }

    /**
     * Resolves a path.
     *
     * @param path             Path to get.
     * @param resolverStrategy Strategy to use during resolving.
     * @return lookup result, containing details about which node was retrieved and what path was used.
     */
    private LookupResult<TKey, UValue> resolve(@NotNull List<TKey> path, @NotNull ResolverStrategy resolverStrategy) {
        TreeNode<TKey, UValue> currentNode = node;
        for (int i = 0; i < path.size(); i++) {
            TKey key = path.get(i);

            if (!currentNode.getMap().containsKey(key)) {
                if (resolverStrategy != ResolverStrategy.CREATE_MISSING) {
                    return new LookupResult<>(null, path.subList(0, i), path.subList(i, path.size()));
                }
                TreeNode<TKey, UValue> newNode = createNode();
                currentNode.getMap().put(key, newNode);
                currentNode = newNode;
            } else {
                currentNode = currentNode.getMap().get(key);
            }
        }

        return new LookupResult<>(currentNode, path, Collections.emptyList());
    }

    /**
     * Creates a new (sub-)node
     *
     * @return new node.
     */
    private TreeNode<TKey, UValue> createNode() {
        return new TreeNode<>();
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
