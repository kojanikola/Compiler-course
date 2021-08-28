// generated with ast extension for cup
// version 0.8
// 26/7/2021 21:7:26


package rs.ac.bg.etf.pp1.ast;

public class DesignatorExpr extends Designator {

    private DesignatorIdentitet DesignatorIdentitet;
    private Expr Expr;

    public DesignatorExpr (DesignatorIdentitet DesignatorIdentitet, Expr Expr) {
        this.DesignatorIdentitet=DesignatorIdentitet;
        if(DesignatorIdentitet!=null) DesignatorIdentitet.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
    }

    public DesignatorIdentitet getDesignatorIdentitet() {
        return DesignatorIdentitet;
    }

    public void setDesignatorIdentitet(DesignatorIdentitet DesignatorIdentitet) {
        this.DesignatorIdentitet=DesignatorIdentitet;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorIdentitet!=null) DesignatorIdentitet.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorIdentitet!=null) DesignatorIdentitet.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorIdentitet!=null) DesignatorIdentitet.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorExpr(\n");

        if(DesignatorIdentitet!=null)
            buffer.append(DesignatorIdentitet.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorExpr]");
        return buffer.toString();
    }
}
