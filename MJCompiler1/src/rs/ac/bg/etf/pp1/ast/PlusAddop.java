// generated with ast extension for cup
// version 0.8
// 26/7/2021 21:7:26


package rs.ac.bg.etf.pp1.ast;

public class PlusAddop extends AddopExpr {

    public PlusAddop () {
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
        buffer.append("PlusAddop(\n");

        buffer.append(tab);
        buffer.append(") [PlusAddop]");
        return buffer.toString();
    }
}