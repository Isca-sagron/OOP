package Ex7;

import Ex7b.Visitor;

public class File implements FileNode {
    private String name;
    private String path;
    private int size;
    // ניתן להוסיף שדות נוספים לפי סוג הקובץ (words, lines, etc)

    public File(String name, String path, int size) {
        this.name = name;
        this.path = path;
        this.size = size;
    }

    @Override
    public String getName() { return name; }

    @Override
    public String getPath() { return path; }

    @Override
    public int getSize() { return size; } // שימוש ב-Getter לצורך חישובים

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
