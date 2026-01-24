
public class Main {
    public static void main(String[] args) {
        try {
            Integer[] values = { 90, 70, 50, 20, 40 };
            CompleteBinaryTree tree = new CompleteBinaryTree(values);
            tree.preorder();
        } catch (InvalidTreeException e) {
            System.out.println(e.getMessage());
        }
    }
}
