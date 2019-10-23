package com.rilling.quercus.lookup;

import com.rilling.quercus.TreeNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Result of resolving a path in a tree.
 */
public class LookupResult<TKey, UValue> {

    private TreeNode<TKey, UValue> node;
    private List<TKey> matchedPath;
    private List<TKey> trailingPath;

    public LookupResult(TreeNode<TKey, UValue> node, @NotNull List<TKey> matchedPath, @NotNull List<TKey> trailingPath) {
        this.node = node;
        this.matchedPath = matchedPath;
        this.trailingPath = trailingPath;
    }

    public TreeNode<TKey, UValue> getNode() {
        return node;
    }

    public List<TKey> getMatchedPath() {
        return matchedPath;
    }

    public List<TKey> getTrailingPath() {
        return trailingPath;
    }
}
