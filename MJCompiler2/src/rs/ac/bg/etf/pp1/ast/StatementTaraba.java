// generated with ast extension for cup
// version 0.8
// 26/7/2021 20:29:37


package rs.ac.bg.etf.pp1.ast;

public class StatementTaraba extends Expr {

    private Designator Designator;
    private DummyNum DummyNum;

    public StatementTaraba (Designator Designator, DummyNum DummyNum) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.DummyNum=DummyNum;
        if(DummyNum!=null) DummyNum.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public DummyNum getDummyNum() {
        return DummyNum;
    }

    public void setDummyNum(DummyNum DummyNum) {
        this.DummyNum=DummyNum;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(DummyNum!=null) DummyNum.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(DummyNum!=null) DummyNum.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(DummyNum!=null) DummyNum.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementTaraba(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DummyNum!=null)
            buffer.append(DummyNum.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementTaraba]");
        return buffer.toString();
    }
}
