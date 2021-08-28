package rs.ac.bg.etf.pp1;

import java.util.LinkedList;
import java.util.List;

import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.AddopPlus;
import rs.ac.bg.etf.pp1.ast.CondFactExp;
import rs.ac.bg.etf.pp1.ast.CondFactsMultipleExpr;
import rs.ac.bg.etf.pp1.ast.Conditions;
import rs.ac.bg.etf.pp1.ast.Const;
import rs.ac.bg.etf.pp1.ast.DesignatorAssignOp;
import rs.ac.bg.etf.pp1.ast.DesignatorDecOp;
import rs.ac.bg.etf.pp1.ast.DesignatorExpr;
import rs.ac.bg.etf.pp1.ast.DesignatorIdent;
import rs.ac.bg.etf.pp1.ast.DesignatorIdentitet;
import rs.ac.bg.etf.pp1.ast.DesignatorIncOp;
import rs.ac.bg.etf.pp1.ast.ElseIf;
import rs.ac.bg.etf.pp1.ast.Expr;
import rs.ac.bg.etf.pp1.ast.Expr1Minus;
import rs.ac.bg.etf.pp1.ast.ExprAddOp;
import rs.ac.bg.etf.pp1.ast.FactorBool;
import rs.ac.bg.etf.pp1.ast.FactorChar;
import rs.ac.bg.etf.pp1.ast.FactorDesignator;
import rs.ac.bg.etf.pp1.ast.FactorNew;
import rs.ac.bg.etf.pp1.ast.FactorNumber;
import rs.ac.bg.etf.pp1.ast.LparenIf;
import rs.ac.bg.etf.pp1.ast.MehtodTypeNameCustom;
import rs.ac.bg.etf.pp1.ast.MethodDecl;
import rs.ac.bg.etf.pp1.ast.MethodTypeNameVoid;
import rs.ac.bg.etf.pp1.ast.MulopDiv;
import rs.ac.bg.etf.pp1.ast.MulopMul;
import rs.ac.bg.etf.pp1.ast.NumConstOneOrZero;
import rs.ac.bg.etf.pp1.ast.OneNumConst;
import rs.ac.bg.etf.pp1.ast.OrIf;
import rs.ac.bg.etf.pp1.ast.Relop;
import rs.ac.bg.etf.pp1.ast.RelopEqual;
import rs.ac.bg.etf.pp1.ast.RelopGreather;
import rs.ac.bg.etf.pp1.ast.RelopGreatherEqual;
import rs.ac.bg.etf.pp1.ast.RelopLess;
import rs.ac.bg.etf.pp1.ast.RelopNotEqual;
import rs.ac.bg.etf.pp1.ast.RparenIf;
import rs.ac.bg.etf.pp1.ast.SingleCondition;
import rs.ac.bg.etf.pp1.ast.StatementIf;
import rs.ac.bg.etf.pp1.ast.StatementIfWElse;
import rs.ac.bg.etf.pp1.ast.StatementPrint;
import rs.ac.bg.etf.pp1.ast.StatementRead;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.ast.TermMulOpFactor;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;

public class CodeGenerator extends VisitorAdaptor {
	
	List<List<Integer>> addressToFix = new LinkedList<>();
	List<Integer> lastAndInsideIf = new LinkedList<>();
	List<Integer> addressOfElse = new LinkedList<>();

	private int mainPc;

	public int getMainPc() {
		return mainPc;
	}

	public void visit(DesignatorIdentitet des) {
		Code.load(des.obj);
	}

	public void visit(StatementPrint printStmt) {
		Expr printStmtExpr = printStmt.getExpr();
		NumConstOneOrZero constNumber = printStmt.getNumConstOneOrZero();

		int width = 5;
		if (constNumber instanceof OneNumConst) {
			width = ((OneNumConst) constNumber).getValue();
		}

		Code.loadConst(width);
		if (printStmt.getExpr().struct == TabExt.intType) {
			Code.put(Code.print);
		} else {
			Code.put(Code.bprint);
		}
	}

	public void visit(Const cnst) {
		Obj con = Tab.insert(Obj.Con, "$", cnst.struct);
		con.setLevel(0);
		con.setAdr(cnst.getLine());

		Code.load(con);

	}

	public void visit(FactorNumber FactorNum) {
		Code.loadConst(FactorNum.getN());
	}

	public void visit(FactorChar FactorChar) {
		Code.loadConst(FactorChar.getC());
	}

	public void visit(FactorBool FactorBool) {
		Code.loadConst(FactorBool.getB().equals("true") ? 1 : 0);
		
	}

	public void visit(MethodTypeNameVoid methodVoid) {
		if ("main".equals(methodVoid.getMethodName())) {
			mainPc = Code.pc;
		}
		methodVoid.obj.setAdr(Code.pc);

		SyntaxNode methodNode = methodVoid.getParent();
		VarCounter varCnt = new VarCounter();
		methodNode.traverseBottomUp(varCnt);

		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseBottomUp(fpCnt);

		Code.put(Code.enter);
		Code.put(fpCnt.getCount());
		Code.put(fpCnt.getCount() + varCnt.getCount());
	}

	public void visit(MehtodTypeNameCustom methodCustom) {
		methodCustom.obj.setAdr(Code.pc);

		SyntaxNode methodNode = methodCustom.getParent();
		VarCounter varCnt = new VarCounter();
		methodNode.traverseBottomUp(varCnt);

		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseBottomUp(fpCnt);

		
		Code.put(Code.enter);
		Code.put(fpCnt.getCount());
		Code.put(fpCnt.getCount() + varCnt.getCount());
	}

	public void visit(MethodDecl MethodDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(StatementRead readStatement) {
		if (readStatement.getDesignator() instanceof DesignatorExpr) {
			DesignatorExpr array = (DesignatorExpr) readStatement.getDesignator();
			if (array.obj.getType() != TabExt.charType) {
				Code.put(Code.read);
				Code.put(Code.astore);
			} else {
				Code.put(Code.bread);
				Code.put(Code.bastore);
			}
		} else {
			DesignatorIdent ident = (DesignatorIdent) readStatement.getDesignator();
			if (ident.obj.getType() != TabExt.charType) {
				Code.put(Code.read);
			} else {
				Code.put(Code.bread);
			}
			Code.store(readStatement.getDesignator().obj);
		}
	}

	public void visit(DesignatorDecOp designator) {

		if (designator.getDesignator() instanceof DesignatorIdent) {
			Code.load(designator.getDesignator().obj);
			Code.loadConst(1);
			Code.put(Code.sub);
			Code.store(designator.getDesignator().obj);
		} else {
			Code.put(Code.dup2);
			if (((DesignatorExpr) designator.getDesignator()).obj.getType().equals(TabExt.charType)) {
				Code.put(Code.baload);
			} else {

				Code.put(Code.aload);
			}
			Code.loadConst(1);
			Code.put(Code.sub);
			if (((DesignatorExpr) designator.getDesignator()).obj.getType().equals(TabExt.charType)) {
				Code.put(Code.bastore);
			} else {

				Code.put(Code.astore);
			}
		}

	}

	public void visit(DesignatorIncOp designator) {

		if (designator.getDesignator() instanceof DesignatorIdent) {
			Code.load(designator.getDesignator().obj);
			Code.loadConst(1);
			Code.put(Code.add);
			Code.store(designator.getDesignator().obj);

		} else {
			Code.put(Code.dup2);
			if (((DesignatorExpr) designator.getDesignator()).obj.getType().equals(TabExt.charType)) {
				Code.put(Code.baload);
			} else {
				Code.put(Code.aload);
			}
			Code.loadConst(1);
			Code.put(Code.add);
			if (((DesignatorExpr) designator.getDesignator()).obj.getType().equals(TabExt.charType)) {
				Code.put(Code.bastore);
			} else {
				Code.put(Code.astore);
			}
		}

	}

	public void visit(DesignatorAssignOp assignment) {
		if (assignment.getDesignator() instanceof DesignatorIdent) {
			Code.store(assignment.getDesignator().obj);
		} else {
			if (((DesignatorExpr) assignment.getDesignator()).obj.getType() == TabExt.charType) {
				Code.put(Code.bastore);
			} else {
				Code.put(Code.astore);
			}
		}
	}

	public void visit(FactorDesignator factorDesignator) {
		if (factorDesignator.getDesignator() instanceof DesignatorIdent) {
			Code.load(factorDesignator.getDesignator().obj);
		} else {
			if (((DesignatorExpr) factorDesignator.getDesignator()).obj.getType() == TabExt.charType) {
				Code.put(Code.baload);
			} else {
				Code.put(Code.aload);
			}
		}
	}

	public void visit(FactorNew newArray) {
		Code.put(Code.newarray);
		if (newArray.getType().struct.equals(TabExt.charType)) {
			Code.put(0);
		} else {
			Code.put(1);
		}
	}

	public void visit(TermMulOpFactor termMulOpFactor) {
		if (termMulOpFactor.getMulop() instanceof MulopMul) {
			Code.put(Code.mul);
		} else if (termMulOpFactor.getMulop() instanceof MulopDiv) {
			Code.put(Code.div);
		} else {
			Code.put(Code.rem);
		}
	}

	public void visit(Expr1Minus expr1Minus) {
		Code.put(Code.neg);
	}

	public void visit(ExprAddOp exprAddOp) {
		if (exprAddOp.getAddop() instanceof AddopPlus) {
			Code.put(Code.add);
		} else {
			Code.put(Code.sub);
		}
	}
	
	public void visit(LparenIf lparenif) {
		addressToFix.add(new LinkedList<>());
	}
	
	public void visit(CondFactsMultipleExpr condFactsMultipleExpr) {
		int operation=-11;
		Relop relop = condFactsMultipleExpr.getRelop();
		
		if(relop instanceof RelopEqual) {
			operation = Code.eq;
		}else if(relop instanceof RelopNotEqual) {
			operation = Code.ne;
		}else if(relop instanceof RelopGreather) {
			operation = Code.gt;
		}else if(relop instanceof RelopGreatherEqual) {
			operation = Code.ge;
		}else if(relop instanceof RelopLess) {
			operation = Code.lt;
		}else{
			operation = Code.le;
		}
		
		if(operation==-11) {
			System.out.println("error za operaciju");
		}
		
		SyntaxNode secondParent = condFactsMultipleExpr.getParent().getParent();
		
		if((secondParent instanceof Conditions || secondParent instanceof SingleCondition) &&
				(secondParent.getParent() instanceof Conditions)) {
			Code.putFalseJump(Code.inverse[operation], 0);
		}else {
			Code.putFalseJump(operation, 0);
		}
		int addr = Code.pc-2;
		addressToFix.get(addressToFix.size()-1).add(addr);
	}
	
	public void visit(CondFactExp condFactExp) {
		SyntaxNode secondParent = condFactExp.getParent().getParent();
		Code.loadConst(0);
		if((secondParent instanceof Conditions || secondParent instanceof SingleCondition) &&
				(secondParent.getParent() instanceof Conditions)) {
			Code.putFalseJump(Code.inverse[Code.ne], 0);
		}else {
			Code.putFalseJump(Code.ne, 0);
		}
		int addr = Code.pc-2;
		addressToFix.get(addressToFix.size()-1).add(addr);
	}
	
	
	public void visit(RparenIf rParenIf) {
		for(int i = 0; i < lastAndInsideIf.size(); i++) {
			Code.fixup(lastAndInsideIf.get(i));
		}
		lastAndInsideIf.clear();
	}
	
	public void visit(ElseIf elseIf) {
		Code.putJump(0); 
		int addr = Code.pc - 2;
		addressOfElse.add(addr);
		
		for(int i = 0; i < addressToFix.get(addressToFix.size() - 1).size(); i++) { 
			Code.fixup(addressToFix.get(addressToFix.size() - 1).get(i));
		}
		
		addressToFix.remove(addressToFix.size() - 1);
	}
	
	public void visit(OrIf orIf) {
		List<Integer> fixCondList = addressToFix.get(addressToFix.size() - 1);
		for(int i =0; i < fixCondList.size() - 1; i++) {
			Code.fixup(fixCondList.get(i));
		}
		lastAndInsideIf.add(fixCondList.get(fixCondList.size() - 1));
		addressToFix.get(addressToFix.size() - 1).clear();
	}
	
	public void visit(StatementIf ifStat) {
		for(int i = 0; i < addressToFix.get(addressToFix.size() - 1).size(); i++) {
			Code.fixup(addressToFix.get(addressToFix.size() - 1).get(i));
		}
		
		addressToFix.remove(addressToFix.size() - 1);
	}
	
	public void visit(StatementIfWElse elseStat) {
		Code.fixup(addressOfElse.remove(addressOfElse.size() - 1));
	}
}
