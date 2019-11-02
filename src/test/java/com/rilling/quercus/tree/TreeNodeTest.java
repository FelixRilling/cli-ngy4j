package com.rilling.quercus.tree;

import com.rilling.quercus.lookup.LookupResult;
import com.rilling.quercus.lookup.LookupStrategy;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TreeNodeTest {

    @Test
    void constructorConstructs() {
        TreeNode<String, Integer> tree = new TreeNode<>();

        assertThat(tree).isNotNull();
    }

    @Test
    void setPathThrowsForEmptyPath() {
        TreeNode<String, Integer> tree = new TreeNode<>();

        assertThatThrownBy(() -> tree.setPath(Collections.emptyList(), 1))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("Path may not be empty.");
    }

    @Test
    void hasPathThrowsForEmptyPath() {
        TreeNode<String, Integer> tree = new TreeNode<>();

        assertThatThrownBy(() -> tree.hasPath(Collections.emptyList()))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("Path may not be empty.");
    }

    @Test
    void hasPathReturnsTrueWhenAnExactMatchExists() {
        TreeNode<String, Integer> tree = new TreeNode<>();
        tree.setPath(List.of("foo"), 1);

        assertThat(tree.hasPath(List.of("foo"))).isTrue();
    }

    @Test
    void hasPathReturnsTrueWhenAnExactNestedMatchExists() {
        TreeNode<String, Integer> tree = new TreeNode<>();
        tree.setPath(List.of("foo", "bar", "fizz"), 1);

        assertThat(tree.hasPath(List.of("foo", "bar", "fizz"))).isTrue();
    }

    @Test
    void hasPathReturnsTrueWhenAnNestedMiddleMatchExists() {
        TreeNode<String, Integer> tree = new TreeNode<>();
        tree.setPath(List.of("foo", "bar", "fizz"), 1);

        assertThat(tree.hasPath(List.of("foo", "bar"))).isTrue();
    }

    @Test
    void hasPathReturnsFalseWhenNoMatchExists() {
        TreeNode<String, Integer> tree = new TreeNode<>();

        assertThat(tree.hasPath(List.of("foo", "bar"))).isFalse();
    }

    @Test
    void hasPathReturnsFalseWhenOnlyASubMatchExists() {
        TreeNode<String, Integer> tree = new TreeNode<>();
        tree.setPath(List.of("foo", "bar"), 1);

        assertThat(tree.hasPath(List.of("foo", "bar", "fizz"))).isFalse();
    }

    @Test
    void hasPathReturnsFalseWhenNestedMatchWithoutValueExistsWithExistenceByValue() {
        TreeNode<String, Integer> tree = new TreeNode<>();
        tree.setPath(List.of("foo", "bar", "fizz"), 1);

        assertThat(tree.hasPath(List.of("foo", "bar"), LookupStrategy.EXISTENCE_BY_VALUE)).isFalse();
    }

    @Test
    void hasPathReturnsTrueWhenNestedMatchWithValueExistsWithExistenceByValue() {
        TreeNode<String, Integer> tree = new TreeNode<>();
        tree.setPath(List.of("foo", "bar", "fizz"), 1);
        tree.setPath(List.of("foo", "bar"), 2);

        assertThat(tree.hasPath(List.of("foo", "bar"), LookupStrategy.EXISTENCE_BY_VALUE)).isTrue();
    }

    @Test
    void getPathThrowsForEmptyPath() {
        TreeNode<String, Integer> tree = new TreeNode<>();

        assertThatThrownBy(() -> tree.getPath(Collections.emptyList()))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("Path may not be empty.");
    }

    @Test
    void getPathReturnsResultWhenAnExactMatchExists() {
        TreeNode<String, Integer> tree = new TreeNode<>();
        tree.setPath(List.of("foo"), 1);

        LookupResult<String, Integer> result = tree.getPath(List.of("foo"));
        assertThat(result.getNode().getValue()).isEqualTo(1);
        assertThat(result.getMatchedPath()).containsExactly("foo");
        assertThat(result.getTrailingPath()).isEmpty();
    }

    @Test
    void getPathReturnsResultWhenAnExactNestedMatchExists() {
        TreeNode<String, Integer> tree = new TreeNode<>();
        tree.setPath(List.of("foo", "bar", "fizz"), 1);

        LookupResult<String, Integer> result = tree.getPath(List.of("foo", "bar", "fizz"));
        assertThat(result.getNode().getValue()).isEqualTo(1);
        assertThat(result.getMatchedPath()).containsExactly("foo", "bar", "fizz");
        assertThat(result.getTrailingPath()).isEmpty();
    }

    @Test
    void getPathReturnsResultWhenAnNestedMiddleMatchExists() {
        TreeNode<String, Integer> tree = new TreeNode<>();
        tree.setPath(List.of("foo", "bar", "fizz"), 1);

        LookupResult<String, Integer> result = tree.getPath(List.of("foo", "bar"));
        assertThat(result.getNode().getValue()).isNull();
        assertThat(result.getMatchedPath()).containsExactly("foo", "bar");
        assertThat(result.getTrailingPath()).isEmpty();
    }

    @Test
    void getPathReturnsResultWhenNoMatchExists() {
        TreeNode<String, Integer> tree = new TreeNode<>();

        LookupResult<String, Integer> result = tree.getPath(List.of("foo", "bar"));
        assertThat(result.getNode()).isNull();
        assertThat(result.getMatchedPath()).isEmpty();
        assertThat(result.getTrailingPath()).containsExactly("foo", "bar");
    }

    @Test
    void getPathReturnsResultWhenOnlyASubMatchExists() {
        TreeNode<String, Integer> tree = new TreeNode<>();
        tree.setPath(List.of("foo", "bar"), 1);

        LookupResult<String, Integer> result = tree.getPath(List.of("foo", "bar", "fizz"));
        assertThat(result.getNode()).isNull();
        assertThat(result.getMatchedPath()).containsExactly("foo", "bar");
        assertThat(result.getTrailingPath()).containsExactly("fizz");
    }

}
