package com.rilling.quercus.lookup;

import com.rilling.quercus.tree.TreeNode;

/**
 * Parent data of a lookup result.
 */
public class LookupResultParent<TKey, UValue> {

    private final TreeNode<TKey, UValue> node;
    private final TKey key;

    public LookupResultParent(TreeNode<TKey, UValue> node, TKey key) {
        this.node = node;
        this.key = key;
    }

    public TreeNode<TKey, UValue> getNode() {
        return node;
    }

    public TKey getKey() {
        return key;
    }
}
