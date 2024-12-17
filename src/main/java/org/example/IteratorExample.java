package org.example;

import java.util.Stack;

interface Iterator<T> {
    boolean hasNext();
    T next();
}

interface IterableCollection<T> {
    Iterator<T> createPreOrderIterator();
    Iterator<T> createPostOrderIterator();
}

class Node<T extends Comparable<T>> {
    T value;
    Node<T> left;
    Node<T> right;

    Node(T value) {
        this.value = value;
    }
}

class BinarySearchTree<T extends Comparable<T>> implements IterableCollection<T> {
    private Node<T> root;

    public void add(T value) {
        root = addRecursive(root, value);
    }

    private Node<T> addRecursive(Node<T> node, T value) {
        if (node == null) {
            return new Node<>(value);
        }
        if (value.compareTo(node.value) < 0) {
            node.left = addRecursive(node.left, value);
        } else if (value.compareTo(node.value) > 0) {
            node.right = addRecursive(node.right, value);
        }
        return node;
    }

    @Override
    public Iterator<T> createPreOrderIterator() {
        return new PreOrderIterator<>(root);
    }

    @Override
    public Iterator<T> createPostOrderIterator() {
        return new PostOrderIterator<>(root);
    }
}
class PreOrderIterator<T extends Comparable<T>> implements Iterator<T> {
    private final Stack<Node<T>> stack = new Stack<>();

    public PreOrderIterator(Node<T> root) {
        if (root != null) {
            stack.push(root);
        }
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public T next() {
        Node<T> current = stack.pop();
        if (current.right != null) {
            stack.push(current.right);
        }
        if (current.left != null) {
            stack.push(current.left);
        }
        return current.value;
    }
}

class PostOrderIterator<T extends Comparable<T>> implements Iterator<T> {
    private final Stack<Node<T>> stack = new Stack<>();
    private Node<T> lastVisited = null;

    public PostOrderIterator(Node<T> root) {
        pushLeft(root);
    }

    private void pushLeft(Node<T> node) {
        while (node != null) {
            stack.push(node);
            node = node.left;
        }
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public T next() {
        while (!stack.isEmpty()) {
            Node<T> current = stack.peek();
            if (current.right != null && lastVisited != current.right) {
                pushLeft(current.right);
            } else {
                lastVisited = stack.pop();
                return current.value;
            }
        }
        return null;
    }
}

public class IteratorExample {
    public static void main(String[] args) {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.add(10);
        tree.add(5);
        tree.add(15);
        tree.add(3);
        tree.add(7);
        tree.add(13);
        tree.add(18);

        System.out.println("Левый прямой обход (pre-order):");
        Iterator<Integer> preOrderIterator = tree.createPreOrderIterator();
        while (preOrderIterator.hasNext()) {
            System.out.print(preOrderIterator.next() + " ");
        }
        System.out.println();

        System.out.println("Правый обратный обход (post-order):");
        Iterator<Integer> postOrderIterator = tree.createPostOrderIterator();
        while (postOrderIterator.hasNext()) {
            System.out.print(postOrderIterator.next() + " ");
        }
    }
}
