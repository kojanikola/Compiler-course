// generated with ast extension for cup
// version 0.8
// 26/7/2021 20:29:37


package rs.ac.bg.etf.pp1.ast;

public class MultLabelList extends LabelList {

    private LabelList LabelList;
    private Label Label;

    public MultLabelList (LabelList LabelList, Label Label) {
        this.LabelList=LabelList;
        if(LabelList!=null) LabelList.setParent(this);
        this.Label=Label;
        if(Label!=null) Label.setParent(this);
    }

    public LabelList getLabelList() {
        return LabelList;
    }

    public void setLabelList(LabelList LabelList) {
        this.LabelList=LabelList;
    }

    public Label getLabel() {
        return Label;
    }

    public void setLabel(Label Label) {
        this.Label=Label;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(LabelList!=null) LabelList.accept(visitor);
        if(Label!=null) Label.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(LabelList!=null) LabelList.traverseTopDown(visitor);
        if(Label!=null) Label.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(LabelList!=null) LabelList.traverseBottomUp(visitor);
        if(Label!=null) Label.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MultLabelList(\n");

        if(LabelList!=null)
            buffer.append(LabelList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Label!=null)
            buffer.append(Label.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MultLabelList]");
        return buffer.toString();
    }
}
