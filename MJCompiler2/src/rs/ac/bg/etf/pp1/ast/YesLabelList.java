// generated with ast extension for cup
// version 0.8
// 26/7/2021 20:29:37


package rs.ac.bg.etf.pp1.ast;

public class YesLabelList extends LabeledStatement {

    private LabelList LabelList;
    private Statement Statement;

    public YesLabelList (LabelList LabelList, Statement Statement) {
        this.LabelList=LabelList;
        if(LabelList!=null) LabelList.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public LabelList getLabelList() {
        return LabelList;
    }

    public void setLabelList(LabelList LabelList) {
        this.LabelList=LabelList;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(LabelList!=null) LabelList.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(LabelList!=null) LabelList.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(LabelList!=null) LabelList.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("YesLabelList(\n");

        if(LabelList!=null)
            buffer.append(LabelList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [YesLabelList]");
        return buffer.toString();
    }
}
