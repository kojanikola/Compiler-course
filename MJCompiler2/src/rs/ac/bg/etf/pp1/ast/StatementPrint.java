// generated with ast extension for cup
// version 0.8
// 26/7/2021 20:29:37


package rs.ac.bg.etf.pp1.ast;

public class StatementPrint extends Statement {

    private Expr Expr;
    private NumConstOneOrZero NumConstOneOrZero;

    public StatementPrint (Expr Expr, NumConstOneOrZero NumConstOneOrZero) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.NumConstOneOrZero=NumConstOneOrZero;
        if(NumConstOneOrZero!=null) NumConstOneOrZero.setParent(this);
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public NumConstOneOrZero getNumConstOneOrZero() {
        return NumConstOneOrZero;
    }

    public void setNumConstOneOrZero(NumConstOneOrZero NumConstOneOrZero) {
        this.NumConstOneOrZero=NumConstOneOrZero;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
        if(NumConstOneOrZero!=null) NumConstOneOrZero.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(NumConstOneOrZero!=null) NumConstOneOrZero.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(NumConstOneOrZero!=null) NumConstOneOrZero.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementPrint(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(NumConstOneOrZero!=null)
            buffer.append(NumConstOneOrZero.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementPrint]");
        return buffer.toString();
    }
}
