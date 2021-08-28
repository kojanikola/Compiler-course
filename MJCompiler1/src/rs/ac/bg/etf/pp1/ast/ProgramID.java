// generated with ast extension for cup
// version 0.8
// 26/7/2021 21:7:26


package rs.ac.bg.etf.pp1.ast;

public class ProgramID implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Obj obj = null;

    private String ProgramIdentificator;

    public ProgramID (String ProgramIdentificator) {
        this.ProgramIdentificator=ProgramIdentificator;
    }

    public String getProgramIdentificator() {
        return ProgramIdentificator;
    }

    public void setProgramIdentificator(String ProgramIdentificator) {
        this.ProgramIdentificator=ProgramIdentificator;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ProgramID(\n");

        buffer.append(" "+tab+ProgramIdentificator);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ProgramID]");
        return buffer.toString();
    }
}
