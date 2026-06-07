package Ex7;

import Ex7b.Visitor;

public class CountVisitor implements Visitor {
    private int count = 0;

    @Override
    public void visit(File f) {
        count++; // סופרים רק קבצי קצה
    }

    @Override
    public void visit(Folder f) {
        // אין לעשות כלום, ספירה היא רק על קבצים
    }

    public int getCount() { return count; }
}