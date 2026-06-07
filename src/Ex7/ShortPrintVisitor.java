package Ex7;

public class ShortPrintVisitor implements FileDetailsVisitor {
    private StringBuilder output = new StringBuilder();

    public String getOutput() {
        return output.toString();
    }

    private void appendName(FileDetails fileDetails) {
        output.append(fileDetails.getName()).append(System.lineSeparator());
    }

    @Override
    public void visit(DirectoryDetails directoryDetails) { appendName(directoryDetails); }

    @Override
    public void visit(Mp3FileDetails mp3FileDetails) { appendName(mp3FileDetails); }

    @Override
    public void visit(JpgFileDetails jpgFileDetails) { appendName(jpgFileDetails); }

    @Override
    public void visit(HtmlFileDetails htmlFileDetails) { appendName(htmlFileDetails); }

    @Override
    public void visit(PptxFileDetails pptxFileDetails) { appendName(pptxFileDetails); }

    @Override
    public void visit(DocxFileDetails docxFileDetails) { appendName(docxFileDetails); }

    @Override
    public void visit(TxtFileDetails txtFileDetails) { appendName(txtFileDetails); }
}
