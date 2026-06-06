package Ex7;

public class ShortPrintVisitor implements Visitor {
    @Override
    public void visit(File f) {
        System.out.println(f.getName());
    }

    @Override
    public void visit(Folder f) {
        // ב-Post order נדפיס את התיקייה אחרי הילדים.
        // מכיוון שה-Folder מבצע accept על הילדים, נצטרך לוגיקה מעט שונה
        // או פשוט להדפיס כאן אם הסדר המבוקש הוא כזה.
        System.out.println(f.getName());
    }
}
