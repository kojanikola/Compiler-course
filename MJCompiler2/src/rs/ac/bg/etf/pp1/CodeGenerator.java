package rs.ac.bg.etf.pp1;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import rs.ac.bg.etf.pp1.ast.DummyNum;
import rs.ac.bg.etf.pp1.ast.DummyNumb;
import rs.ac.bg.etf.pp1.ast.ElseIf;
import rs.ac.bg.etf.pp1.ast.Expr;
import rs.ac.bg.etf.pp1.ast.Expr1Minus;
import rs.ac.bg.etf.pp1.ast.Expr1Term;
import rs.ac.bg.etf.pp1.ast.ExprAddOp;
//import rs.ac.bg.etf.pp1.ast.ExprTaraba;
import rs.ac.bg.etf.pp1.ast.FactorBool;
import rs.ac.bg.etf.pp1.ast.FactorChar;
import rs.ac.bg.etf.pp1.ast.FactorDesignator;
import rs.ac.bg.etf.pp1.ast.FactorNew;
import rs.ac.bg.etf.pp1.ast.FactorNumber;
import rs.ac.bg.etf.pp1.ast.Label;
import rs.ac.bg.etf.pp1.ast.LparenIf;
import rs.ac.bg.etf.pp1.ast.MehtodTypeNameCustom;
import rs.ac.bg.etf.pp1.ast.MethodDecl;
import rs.ac.bg.etf.pp1.ast.MethodTypeNameVoid;
import rs.ac.bg.etf.pp1.ast.MulopDiv;
import rs.ac.bg.etf.pp1.ast.MulopMul;
import rs.ac.bg.etf.pp1.ast.NumConstOneOrZero;
import rs.ac.bg.etf.pp1.ast.OneNumConst;
import rs.ac.bg.etf.pp1.ast.OrIf;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.ast.Relop;
import rs.ac.bg.etf.pp1.ast.RelopEqual;
import rs.ac.bg.etf.pp1.ast.RelopGreather;
import rs.ac.bg.etf.pp1.ast.RelopGreatherEqual;
import rs.ac.bg.etf.pp1.ast.RelopLess;
import rs.ac.bg.etf.pp1.ast.RelopNotEqual;
import rs.ac.bg.etf.pp1.ast.RparenIf;
import rs.ac.bg.etf.pp1.ast.SimpleExpr;
import rs.ac.bg.etf.pp1.ast.SingleCondition;
import rs.ac.bg.etf.pp1.ast.StatementCezar;
import rs.ac.bg.etf.pp1.ast.StatementEt;
import rs.ac.bg.etf.pp1.ast.StatementEta;
import rs.ac.bg.etf.pp1.ast.StatementGOTO;
import rs.ac.bg.etf.pp1.ast.StatementIf;
import rs.ac.bg.etf.pp1.ast.StatementIfWElse;
import rs.ac.bg.etf.pp1.ast.StatementMax;
import rs.ac.bg.etf.pp1.ast.StatementPlusDva;
import rs.ac.bg.etf.pp1.ast.StatementPrint;
import rs.ac.bg.etf.pp1.ast.StatementRead;
import rs.ac.bg.etf.pp1.ast.StatementSwap;
import rs.ac.bg.etf.pp1.ast.StatementTaraba;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.ast.TermFactor;
import rs.ac.bg.etf.pp1.ast.TermMulOpFactor;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;

public class CodeGenerator extends VisitorAdaptor {

	List<List<Integer>> addressToFix = new LinkedList<>();
	List<Integer> lastAndInsideIf = new LinkedList<>();
	List<Integer> addressOfElse = new LinkedList<>();

	public static Map<String, Integer> mapa = new HashMap<>();
	public static Map<String, Integer> adrGoTo = new HashMap<>();

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

		// Generate the entry
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

		// Generate the entry
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

		if (assignment.getDesignator() instanceof DesignatorExpr) {
			DesignatorExpr des = (DesignatorExpr) assignment.getDesignator();
			Integer num = SemanticPass.finalVars.get(des.getDesignatorIdentitet().getIme());
			if (num != null) {
				if (num != 0)
					return;
				else {
					SemanticPass.finalVars.put(des.getDesignatorIdentitet().getIme(), 1);
				}
			}
		} else {
			Integer num = SemanticPass.finalVars.get(((DesignatorIdent) assignment.getDesignator()).getIdentName());
			if (num != null) {
				if (num != 0)
					return;
				else {
					SemanticPass.finalVars.put(((DesignatorIdent) assignment.getDesignator()).getIdentName(), 1);
				}
			}
		}

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
		int operation = -11;
		Relop relop = condFactsMultipleExpr.getRelop();

		if (relop instanceof RelopEqual) {
			operation = Code.eq;
		} else if (relop instanceof RelopNotEqual) {
			operation = Code.ne;
		} else if (relop instanceof RelopGreather) {
			operation = Code.gt;
		} else if (relop instanceof RelopGreatherEqual) {
			operation = Code.ge;
		} else if (relop instanceof RelopLess) {
			operation = Code.lt;
		} else {
			operation = Code.le;
		}

		if (operation == -11) {
			System.out.println("error za operaciju");
		}

		SyntaxNode secondParent = condFactsMultipleExpr.getParent().getParent();

		if ((secondParent instanceof Conditions || secondParent instanceof SingleCondition)
				&& (secondParent.getParent() instanceof Conditions)) {
			Code.putFalseJump(Code.inverse[operation], 0);
		} else {
			Code.putFalseJump(operation, 0);
		}
		int addr = Code.pc - 2;
		addressToFix.get(addressToFix.size() - 1).add(addr);
	}

	public void visit(CondFactExp condFactExp) {
		SyntaxNode secondParent = condFactExp.getParent().getParent();
		Code.loadConst(1);
		if ((secondParent instanceof Conditions || secondParent instanceof SingleCondition)
				&& (secondParent.getParent() instanceof Conditions)) {
			Code.putFalseJump(Code.inverse[Code.ne], 0);
		} else {
			Code.putFalseJump(Code.inverse[Code.ne], 0);
		}
		int addr = Code.pc - 2;
		addressToFix.get(addressToFix.size() - 1).add(addr);
	}

	public void visit(OrIf orIf) {
		List<Integer> fixCondList = addressToFix.get(addressToFix.size() - 1);
		for (int i = 0; i < fixCondList.size() - 1; i++) {
			Code.fixup(fixCondList.get(i));
		}
		lastAndInsideIf.add(fixCondList.get(fixCondList.size() - 1));
		addressToFix.get(addressToFix.size() - 1).clear();
	}

	public void visit(RparenIf rParenIf) {
		for (int i = 0; i < lastAndInsideIf.size(); i++) {
			Code.fixup(lastAndInsideIf.get(i));
		}
		lastAndInsideIf.clear();
	}

	public void visit(ElseIf elseIf) {
		Code.putJump(0);
		int addr = Code.pc - 2;
		addressOfElse.add(addr);

		for (int i = 0; i < addressToFix.get(addressToFix.size() - 1).size(); i++) {
			Code.fixup(addressToFix.get(addressToFix.size() - 1).get(i));
		}

		addressToFix.remove(addressToFix.size() - 1);
	}

	public void visit(StatementIf ifStat) {
		for (int i = 0; i < addressToFix.get(addressToFix.size() - 1).size(); i++) { // if any CondFact within last
																						// CondTerm is false, we jump
																						// directly to else
			Code.fixup(addressToFix.get(addressToFix.size() - 1).get(i));
		}

		addressToFix.remove(addressToFix.size() - 1);
	}

	public void visit(StatementIfWElse elseStat) {
		Code.fixup(addressOfElse.remove(addressOfElse.size() - 1));
	}

	public void visit(StatementSwap statementSwap) {
		Code.load(statementSwap.getDesignator().obj);
		SimpleExpr expr1 = (SimpleExpr) statementSwap.getExpr();
		Expr1Term expr1Term = (Expr1Term) expr1.getExpr1();
		TermFactor termFactor1 = (TermFactor) expr1Term.getTerm();
		FactorNumber factorNum1 = (FactorNumber) termFactor1.getFactor();
		SimpleExpr expr2 = (SimpleExpr) statementSwap.getExpr1();
		Expr1Term expr2Term = (Expr1Term) expr2.getExpr1();
		TermFactor termFactor2 = (TermFactor) expr2Term.getTerm();
		FactorNumber factorNum2 = (FactorNumber) termFactor2.getFactor();

		Code.load(statementSwap.getDesignator().obj);
		Code.loadConst(factorNum1.getN());
		Code.load(statementSwap.getDesignator().obj);
		Code.loadConst(factorNum2.getN());
		Code.put(Code.aload);
		Code.load(statementSwap.getDesignator().obj);
		Code.loadConst(factorNum2.getN());
		Code.load(statementSwap.getDesignator().obj);
		Code.loadConst(factorNum1.getN());
		Code.put(Code.aload);
		Code.put(Code.astore);
		Code.put(Code.astore);
		Code.put(Code.pop);
		Code.put(Code.pop);
	}

	public void visit(StatementEt statementEt) {
		Code.load(statementEt.getDesignator().obj);
		DummyNumb dummy = (DummyNumb) statementEt.getDummyNum();
		Code.loadConst(dummy.getN());
		Code.put(Code.aload);
		Code.load(statementEt.getDesignator().obj);
		Code.put(Code.arraylength);
		Code.loadConst(dummy.getN());
		Code.put(Code.sub);
		Code.load(statementEt.getDesignator().obj);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.aload);
		Code.put(Code.add);
	}

	public void visit(StatementTaraba statementTaraba) {
//		Code.load(((DesignatorExpr)statementTaraba.getDesignator()).getDesignatorIdentitet().obj);
//		DesignatorExpr des = (DesignatorExpr) statementTaraba.getDesignator();
//		SimpleExpr expr1 = (SimpleExpr) des.getExpr();
//		Expr1Term expr1Term = (Expr1Term) expr1.getExpr1();
//		TermFactor termFactor1 = (TermFactor) expr1Term.getTerm();
//		FactorNumber factorNum1 = (FactorNumber) termFactor1.getFactor();
//		Code.loadConst(factorNum1.getN());
		DummyNumb dummy = (DummyNumb) statementTaraba.getDummyNum();
		Code.loadConst(dummy.getN());
		Code.put(Code.add);
		Code.load(((DesignatorExpr) statementTaraba.getDesignator()).getDesignatorIdentitet().obj);
		Code.put(Code.arraylength);
		Code.put(Code.rem);
		Code.put(Code.aload);

	}

	public void visit(StatementCezar statementCezar) {
		int breakAdr;
		Code.loadConst(0);
		int addrWhile = Code.pc;
		Code.put(Code.dup);
		Code.put(Code.dup);
		Code.loadConst(1);
		Code.put(Code.add);
//		Code.put(Code.dup_x1);
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.load(((DesignatorIdent) statementCezar.getDesignator()).obj);
		Code.put(Code.arraylength);
		Code.putFalseJump(Code.lt, 0);
		breakAdr = Code.pc - 2;
		Code.put(Code.dup);
		Code.load(((DesignatorIdent) statementCezar.getDesignator()).obj);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.baload);
		Code.loadConst(97);
		Code.put(Code.sub);
		Code.loadConst(3);
		Code.put(Code.add);
		Code.loadConst(27);
		Code.put(Code.rem);
		Code.loadConst(97);
		Code.put(Code.add);
		Code.load(((DesignatorIdent) statementCezar.getDesignator()).obj);
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.put(Code.bastore);
		Code.putJump(addrWhile);
		Code.fixup(breakAdr);
	}

	public void visit(StatementEta statementEta) {
		Code.put(Code.dup);
		Code.put(Code.dup);
		Code.put(Code.mul);
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.dup);
		Code.put(Code.dup);
		Code.put(Code.mul);
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.loadConst(2);
		Code.put(Code.mul);
		Code.put(Code.mul);
		Code.put(Code.add);
		Code.put(Code.add);
	}

	public void visit(StatementMax statementMax) {
		Code.loadConst(0);
		Code.loadConst(0);
		int adrWhile = Code.pc;
		int adrToFix;
		int adrArraySize;
		Code.put(Code.dup);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.put(Code.dup_x2);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.load(statementMax.getDesignator().obj);
		Code.put(Code.arraylength);

		Code.putFalseJump(Code.lt, 0);
		adrArraySize = Code.pc - 2;
		Code.put(Code.dup_x1);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.dup_x1);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.load(statementMax.getDesignator().obj);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.aload);

		Code.putFalseJump(Code.lt, 0);
		adrToFix = Code.pc - 2;
		Code.put(Code.pop);
		Code.load(statementMax.getDesignator().obj);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.aload);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.putJump(adrWhile);
		Code.fixup(adrToFix);
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.put(Code.pop);
		Code.putJump(adrWhile);
		Code.fixup(adrArraySize);
		Code.put(Code.pop);
	}

	public void visit(StatementPlusDva statementPlusDva) {
		if (statementPlusDva.getDesignator() instanceof DesignatorExpr) {
			Code.put(Code.dup2);
			Code.put(Code.aload);
			Code.loadConst(2);
			Code.put(Code.add);
			Code.put(Code.astore);
		} else {
			Code.load(statementPlusDva.getDesignator().obj);
			Code.loadConst(2);
			Code.put(Code.add);
			Code.store(statementPlusDva.getDesignator().obj);
		}
	}

	public void visit(Label label) {
		mapa.put(label.getNazivLabele(), Code.pc);
	}

	public void visit(StatementGOTO statementGoTo) {
		Code.putJump(0);
		adrGoTo.put(statementGoTo.getNaziv(), Code.pc-2);
	}

	public void jumpOnLabel(int adr, int label) {
		Code.put2(adr, (label - adr + 1));
	}

	public void visit(Program program) {
		Iterator<Map.Entry<String, Integer>> itr = adrGoTo.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, Integer> entry = itr.next();
			
			Integer adrToFix = entry.getValue();
			
			Integer adr = mapa.get(entry.getKey());
			
			this.jumpOnLabel(adrToFix, adr);
			
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
	}

//	 public void visit(FactorIspis factorIspis) {
//		 int addressOfIfCondition;
//		 int addressOfCompareZero;
//		 Code.load(factorIspis.getDesignator().obj);
//		 Code.put(Code.arraylength);
//		 Code.loadConst(0);
//		 int addr = Code.pc-2;
//		 Code.put(Code.dup);
//		 Code.loadConst(1);
//		 Code.put(Code.add);
//		 Code.put(Code.dup_x1);
//		 Code.load(factorIspis.getDesignator().obj);
//		 Code.put(Code.arraylength);
//		 Code.putFalseJump(Code.ne, 0);
//		 addressOfIfCondition = Code.pc-2;
//		 Code.put(Code.dup);
//		 Code.put(Code.dup);
//		 Code.loadConst(1);
//		 Code.put(Code.sub);
//		 Code.load(factorIspis.getDesignator().obj);
//		 Code.put(Code.dup_x1);
//		 Code.put(Code.pop);
//		 Code.put(Code.aload);
//		 Code.put(Code.dup);
//		 Code.loadConst(0);
//		 Code.putFalseJump(Code.ne, 0);
//		 addressOfCompareZero=Code.pc-2;
//		 Code.put(Code.dup_x1);
//		 Code.put(Code.pop);
//	 }
//	
//	public void visit(FuncCall funcCall) {
//		Obj functionObj = funcCall.getDesignator().obj;
//		int offset = functionObj.getAdr()-Code.pc;
//		Code.put(Code.call);
//		
//		Code.put2(offset);
//	}
//	
//	public void visit(ProcCall procCall) {
//		Obj functionObj = procCall.getDesignator().obj;
//		int offset = functionObj.getAdr()-Code.pc;
//		Code.put(Code.call);
//		
//		Code.put2(offset);
//		
//		if(procCall.getDesignator().obj.getType()!=Tab.noType) {
//			Code.put(Code.pop);
//		}
//	}
//	
//	public void visit(ReturnExpr returnExpr) {
//		Code.put(Code.exit);
//		Code.put(Code.return_);
//	}
//	
//	public void visit(ReturnNoExpr returnNoExpr) {
//		Code.put(Code.exit);
//		Code.put(Code.return_);
//	}
//	
//	public void visit(AddExpr addExpr) {
//		Code.put(Code.add);
//	}

}
