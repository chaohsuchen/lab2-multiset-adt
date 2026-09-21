import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A recursive, non-binary tree that stores integer values.
 * An empty tree has a null root and no subtrees.
 */
public class Tree {
    private Integer root;
    private final List<Tree> subtrees;

    /** Create an empty tree. */
    public Tree() {
        root = null;
        subtrees = new ArrayList<>();
    }

    /** Create a leaf containing {@code root}. */
    public Tree(int root) {
        this();
        this.root = root;
    }

    /** Create a tree with a root and a shallow copy of its direct subtrees. */
    public Tree(int root, List<Tree> subtrees) {
        this.root = root;
        this.subtrees = new ArrayList<>(subtrees);
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int getSize() {
        if (isEmpty()) {
            return 0;
        }

        int size = 1;
        for (Tree subtree : subtrees) {
            size += subtree.getSize();
        }
        return size;
    }

    public boolean contains(int item) {
        if (isEmpty()) {
            return false;
        }
        if (root == item) {
            return true;
        }

        for (Tree subtree : subtrees) {
            if (subtree.contains(item)) {
                return true;
            }
        }
        return false;
    }

    public int count(int item) {
        if (isEmpty()) {
            return 0;
        }

        int occurrences = (root == item) ? 1 : 0;
        for (Tree subtree : subtrees) {
            occurrences += subtree.count(item);
        }
        return occurrences;
    }

    /**
     * Delete one occurrence of {@code item}.
     *
     * @return whether an item was removed
     */
    public boolean deleteItem(int item) {
        if (isEmpty()) {
            return false;
        }
        if (root == item) {
            deleteRoot();
            return true;
        }

        for (int i = 0; i < subtrees.size(); i++) {
            Tree subtree = subtrees.get(i);
            if (subtree.deleteItem(item)) {
                if (subtree.isEmpty()) {
                    subtrees.remove(i);
                }
                return true;
            }
        }
        return false;
    }

    /** Remove this tree's root, promoting its last subtree if necessary. */
    private void deleteRoot() {
        if (subtrees.isEmpty()) {
            root = null;
            return;
        }

        Tree replacement = subtrees.remove(subtrees.size() - 1);
        root = replacement.root;
        subtrees.addAll(replacement.subtrees);
    }

    /**
     * Insert an item, distributing nodes randomly among subtrees as in the
     * supplied Python implementation.
     */
    public void insert(int item) {
        if (isEmpty()) {
            root = item;
        } else if (subtrees.isEmpty()) {
            subtrees.add(new Tree(item));
        } else if (ThreadLocalRandom.current().nextInt(1, 4) == 3) {
            subtrees.add(new Tree(item));
        } else {
            int index = ThreadLocalRandom.current().nextInt(subtrees.size());
            subtrees.get(index).insert(item);
        }
    }

    /** Insert {@code item} as a child of the first matching {@code parent}. */
    public boolean insertChild(int item, int parent) {
        if (isEmpty()) {
            return false;
        }
        if (root == parent) {
            subtrees.add(new Tree(item));
            return true;
        }

        for (Tree subtree : subtrees) {
            if (subtree.insertChild(item, parent)) {
                return true;
            }
        }
        return false;
    }
}
