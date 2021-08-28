// generated with ast extension for cup
// version 0.8
// 26/7/2021 20:29:37


package rs.ac.bg.etf.pp1.ast;

public class StatementGOTO extends Statement {

    private String naziv;

    public StatementGOTO (String naziv) {
        this.naziv=naziv;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv=naziv;
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
        buffer.append("StatementGOTO(\n");

        buffer.append(" "+tab+naziv);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementGOTO]");
        return buffer.toString();
    }
}
