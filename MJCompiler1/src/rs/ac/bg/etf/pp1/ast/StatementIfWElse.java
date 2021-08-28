// generated with ast extension for cup
// version 0.8
// 26/7/2021 21:7:26


package rs.ac.bg.etf.pp1.ast;

public class StatementIfWElse extends Statement {

    private LparenIf LparenIf;
    private Condition Condition;
    private RparenIf RparenIf;
    private Statement Statement;
    private ElseIf ElseIf;
    private Statement Statement1;

    public StatementIfWElse (LparenIf LparenIf, Condition Condition, RparenIf RparenIf, Statement Statement, ElseIf ElseIf, Statement Statement1) {
        this.LparenIf=LparenIf;
        if(LparenIf!=null) LparenIf.setParent(this);
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
        this.RparenIf=RparenIf;
        if(RparenIf!=null) RparenIf.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.ElseIf=ElseIf;
        if(ElseIf!=null) ElseIf.setParent(this);
        this.Statement1=Statement1;
        if(Statement1!=null) Statement1.setParent(this);
    }

    public LparenIf getLparenIf() {
        return LparenIf;
    }

    public void setLparenIf(LparenIf LparenIf) {
        this.LparenIf=LparenIf;
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public RparenIf getRparenIf() {
        return RparenIf;
    }

    public void setRparenIf(RparenIf RparenIf) {
        this.RparenIf=RparenIf;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public ElseIf getElseIf() {
        return ElseIf;
    }

    public void setElseIf(ElseIf ElseIf) {
        this.ElseIf=ElseIf;
    }

    public Statement getStatement1() {
        return Statement1;
    }

    public void setStatement1(Statement Statement1) {
        this.Statement1=Statement1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(LparenIf!=null) LparenIf.accept(visitor);
        if(Condition!=null) Condition.accept(visitor);
        if(RparenIf!=null) RparenIf.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(ElseIf!=null) ElseIf.accept(visitor);
        if(Statement1!=null) Statement1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(LparenIf!=null) LparenIf.traverseTopDown(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
        if(RparenIf!=null) RparenIf.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(ElseIf!=null) ElseIf.traverseTopDown(visitor);
        if(Statement1!=null) Statement1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(LparenIf!=null) LparenIf.traverseBottomUp(visitor);
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        if(RparenIf!=null) RparenIf.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(ElseIf!=null) ElseIf.traverseBottomUp(visitor);
        if(Statement1!=null) Statement1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementIfWElse(\n");

        if(LparenIf!=null)
            buffer.append(LparenIf.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(RparenIf!=null)
            buffer.append(RparenIf.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ElseIf!=null)
            buffer.append(ElseIf.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement1!=null)
            buffer.append(Statement1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementIfWElse]");
        return buffer.toString();
    }
}
