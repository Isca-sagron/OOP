package Ex7;

import Ex7b.Visitor;

public interface FileNode {
    String getName();
    String getPath();
    void accept(Visitor v);
}
