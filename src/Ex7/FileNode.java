package Ex7;

public interface FileNode {
    String getName();
    String getPath();
    void accept(Visitor v);
}
