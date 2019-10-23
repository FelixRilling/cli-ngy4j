package com.rilling.quercus;

import java.util.HashMap;
import java.util.Map;

/**
 * Data class defining a single node containing a value and a map of sub-nodes.
 */
public class TreeNode<TKey, UValue> {
    private UValue value;
    private Map<TKey, TreeNode<TKey, UValue>> map;

    public TreeNode() {
        this.map = new HashMap<>();
    }

    public UValue getValue() {
        return value;
    }

    public void setValue(UValue value) {
        this.value = value;
    }

    public Map<TKey, TreeNode<TKey, UValue>> getMap() {
        return map;
    }
}
