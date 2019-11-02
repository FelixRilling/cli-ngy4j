package com.rilling.quercus.lookup;

import com.rilling.quercus.tree.TreeNode;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Result of resolving a path in a tree.
 */
public class LookupResult<TKey, UValue> {

    private final TreeNode<TKey, UValue> node;
    private final List<TKey> matchedPath;
    private final List<TKey> trailingPath;
    private final LookupResultParent<TKey, UValue> parent;

    public LookupResult(TreeNode<TKey, UValue> node, @NotNull List<TKey> matchedPath, @NotNull List<TKey> trailingPath, LookupResultParent<TKey, UValue> parent) {
        this.node = node;
        this.parent = parent;
        this.matchedPath = Collections.unmodifiableList(matchedPath);
        this.trailingPath = Collections.unmodifiableList(trailingPath);
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

    public LookupResultParent<TKey, UValue> getParent() {
        return parent;
    }
}
