package Ex7;
import Ex7b.Visitor;

import java.util.ArrayList;
import java.util.List;

public class Folder implements FileNode {
    private String name;
    private String path;
    private List<FileNode> children;

    public Folder(String name, String path) {
        this.name = name;
        this.path = path;
        this.children = new ArrayList<>();
    }

    public void add(FileNode node) {
        children.add(node);
    }

    @Override
    public String getName() { return name; }

    @Override
    public String getPath() { return path; }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
        for (FileNode child : children) {
            child.accept(v);
        }
    }
}
