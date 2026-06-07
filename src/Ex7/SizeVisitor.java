package Ex7;

import Ex7b.Visitor;

public class SizeVisitor implements Visitor {
    private int totalSize = 0;

    @Override
    public void visit(File f) {
        totalSize += f.getSize();
    }

    @Override
    public void visit(Folder f) {
        // תיקייה כשלעצמה לא מוסיפה נפח בקבצים,
        // הקבצים הפנימיים יטופלו ברקורסיה ע"י ה-accept ב-Folder
    }

    public int getTotalSize() { return totalSize; }
}
