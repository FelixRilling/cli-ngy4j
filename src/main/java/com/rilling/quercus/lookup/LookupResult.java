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
    private final ParentResult parent;

    public LookupResult(TreeNode<TKey, UValue> node, @NotNull List<TKey> matchedPath, @NotNull List<TKey> trailingPath, TreeNode<TKey, UValue> parentNode, TKey parentKey) {
        this.node = node;
        this.parent = new ParentResult(parentNode, parentKey);
        this.matchedPath = Collections.unmodifiableList(matchedPath);
        this.trailingPath = Collections.unmodifiableList(trailingPath);
    }

    public TreeNode<TKey, UValue> getNode() {
        return node;
    }

    @NotNull
    public List<TKey> getMatchedPath() {
        return matchedPath;
    }

    @NotNull
    public List<TKey> getTrailingPath() {
        return trailingPath;
    }

    @NotNull
    public LookupResult<TKey, UValue>.ParentResult getParent() {
        return parent;
    }

    public class ParentResult {

        private final TreeNode<TKey, UValue> node;
        private final TKey key;

        public ParentResult(@NotNull TreeNode<TKey, UValue> node, @NotNull TKey key) {
            this.node = node;
            this.key = key;
        }

        @NotNull
        public TreeNode<TKey, UValue> getNode() {
            return node;
        }

        @NotNull
        public TKey getKey() {
            return key;
        }
    }
}
