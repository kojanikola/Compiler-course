// generated with ast extension for cup
// version 0.8
// 26/7/2021 20:29:37


package rs.ac.bg.etf.pp1.ast;

public class DesignatorIdentitet implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Obj obj = null;

    private String ime;

    public DesignatorIdentitet (String ime) {
        this.ime=ime;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime=ime;
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
        buffer.append("DesignatorIdentitet(\n");

        buffer.append(" "+tab+ime);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorIdentitet]");
        return buffer.toString();
    }
}
