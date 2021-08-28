// generated with ast extension for cup
// version 0.8
// 26/7/2021 20:29:37


package rs.ac.bg.etf.pp1.ast;

public class StmtList extends StatementList {

    private StatementList StatementList;
    private LabeledStatement LabeledStatement;

    public StmtList (StatementList StatementList, LabeledStatement LabeledStatement) {
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
        this.LabeledStatement=LabeledStatement;
        if(LabeledStatement!=null) LabeledStatement.setParent(this);
    }

    public StatementList getStatementList() {
        return StatementList;
    }

    public void setStatementList(StatementList StatementList) {
        this.StatementList=StatementList;
    }

    public LabeledStatement getLabeledStatement() {
        return LabeledStatement;
    }

    public void setLabeledStatement(LabeledStatement LabeledStatement) {
        this.LabeledStatement=LabeledStatement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StatementList!=null) StatementList.accept(visitor);
        if(LabeledStatement!=null) LabeledStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
        if(LabeledStatement!=null) LabeledStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        if(LabeledStatement!=null) LabeledStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StmtList(\n");

        if(StatementList!=null)
            buffer.append(StatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(LabeledStatement!=null)
            buffer.append(LabeledStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StmtList]");
        return buffer.toString();
    }
}
