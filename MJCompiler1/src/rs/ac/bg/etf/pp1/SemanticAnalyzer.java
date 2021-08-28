package rs.ac.bg.etf.pp1;

import java.lang.invoke.MethodType;
import java.util.LinkedList;
import java.util.List;

import javax.naming.ldap.ControlFactory;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticAnalyzer extends VisitorAdaptor {

	int printCallCount = 0;
	int varDeclCount = 0;

	Logger log = Logger.getLogger(getClass());

	Struct declType = null;
	Obj currentMethod = null;

	int numberOfVars;

	boolean mainFound;

	boolean errorDetected;

	String currentMethodName;

	List<CompilerError> lista = new LinkedList<>();

	public void addError(int line, String message) {
		lista.add(new CompilerError(line, message, CompilerError.CompilerErrorType.SEMANTIC_ERROR));

	}

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" on line ").append(line);
		this.addError(line, msg.toString());
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" on line ").append(line);
		log.info(msg.toString());
	}

	public void visit(VarDecl vardecl) {
		declType = TabExt.noType;
	}

	public void visit(VarPartItem varpart) {
		Obj addedVarPart = TabExt.currentScope.findSymbol(varpart.getVarName());

		if (addedVarPart != null) {
			report_error("Greska: simbol " + varpart.getVarName() + " vec postoji", varpart);
		} else {
			Obj newVarDeclItem = TabExt.insert(Obj.Var, varpart.getVarName(), declType);
			report_info("Dodata promenljiva " + varpart.getVarName(), varpart);
		}
	}

	public void visit(ArrayVarPart arrayDeclarationItem) {
		Obj addedArray = TabExt.currentScope.findSymbol(arrayDeclarationItem.getArrayName());
		if (addedArray != null) {
			report_error("Greska: simbol " + arrayDeclarationItem.getArrayName() + " vec postoji",
					arrayDeclarationItem);
		} else {
			Obj newVarDeclItem = TabExt.insert(Obj.Var, arrayDeclarationItem.getArrayName(),
					new Struct(Struct.Array, declType));
			report_info("Dodat niz " + arrayDeclarationItem.getArrayName(), arrayDeclarationItem);
		}
	}

	public void visit(StatementPrint StatementPrint) {
		if (StatementPrint.getExpr().struct.getKind() != Struct.Int
				&& StatementPrint.getExpr().struct.getKind() != Struct.Char
				&& StatementPrint.getExpr().struct.getKind() != Struct.Bool)
			report_error("Semanticka greska na liniji " + StatementPrint.getLine()
					+ ": Operand instrukcije PRINT mora biti char ili int tipa", null);
		printCallCount++;
	}

	public void visit(ProgramID progID) {
		progID.obj = TabExt.insert(Obj.Prog, progID.getProgramIdentificator(), TabExt.noType);
		TabExt.openScope();
	}

	public void visit(Program program) {
		numberOfVars = TabExt.currentScope.getnVars();
		TabExt.chainLocalSymbols(program.getProgramID().obj);
		TabExt.closeScope();
	}

	public void visit(ConstChar constChar) {

		if (!declType.equals(TabExt.charType)) {
			report_error("Na mestu char konstante tip nije char", constChar);
		}
		Obj constNameExists = TabExt.currentScope.findSymbol(constChar.getConstName());

		if (constNameExists != null) {
			report_error("Konstanta" + constChar.getConstName() + " vec postoji", constChar);
		} else {
			constNameExists = TabExt.insert(Obj.Con, constChar.getConstName(), declType);
			constNameExists.setAdr(constChar.getConstValue());
			report_info("Dodata konstanta "+ constChar.getConstName()+" na liniji " + constChar.getLine(), null);
		}

	}

	public void visit(ConstNumber constNumber) {
		if (!declType.equals(TabExt.intType)) {
			report_error("Na mestu int konstante tip nije int", constNumber);
		}
		Obj constNameExists = TabExt.currentScope.findSymbol(constNumber.getConstName());

		if (constNameExists != null) {
			report_error("Konstanta" + constNumber.getConstName() + " vec postoji", constNumber);
		} else {
			constNameExists = TabExt.insert(Obj.Con, constNumber.getConstName(), declType);
			constNameExists.setAdr(constNumber.getConstValue());
			report_info("Dodata konstanta "+ constNumber.getConstName()+" na liniji " + constNumber.getLine(), constNumber);
		}

	}

	public void visit(ConstBool constBool) {
		if (!declType.equals(TabExt.booleanType)) {
			report_error("Na mestu boolean konstante tip nije boolean", constBool);
		} else {
			Obj constNameExists = TabExt.currentScope.findSymbol(constBool.getConstName());

			if (constNameExists != null) {
				report_error("Konstanta" + constBool.getConstName() + " vec postoji", constBool);
			} else {
				constNameExists = TabExt.insert(Obj.Con, constBool.getConstName(), declType);
				int value = 0;
				if (constBool.getConstValue().equals("true")) {
					value = 1;
					report_info("boolean true nadjen", constBool);
				}
				constNameExists.setAdr(value);
				report_info("Dodata konstanta"+constBool.getConstName()+" na liniji " + constBool.getLine(), null);
			}
		}
	}

	public void visit(ConstDecl constDecl) {
		declType = TabExt.noType;
	}

	public void visit(Type type) {
		Obj typeNode = TabExt.find(type.getTypeName());
		if (typeNode != TabExt.noObj) {
			if (Obj.Type == typeNode.getKind()) {
				type.struct = typeNode.getType();
				declType = typeNode.getType();
			} else {
				report_error("Greska: Ime + " + type.getTypeName() + " ne predstavlja tip", type);
				type.struct = TabExt.noType;
			}
		} else {
			report_error("Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola!", null);
			type.struct = TabExt.noType;
		}
	}

	public void visit(MethodDecl methodDecl) {
		MethodTypeName t1 = methodDecl.getMethodTypeName();
		if (isMain(methodDecl)) {
			mainFound = true;
		}
		TabExt.chainLocalSymbols(currentMethod);
		TabExt.closeScope();
		currentMethod = TabExt.noObj;
	}

	public boolean isMain(MethodDecl methodDecl) {
		return (methodDecl.getMethodTypeName() instanceof MethodTypeNameVoid
				&& methodDecl.getFormParameters() instanceof NoFormParam
				&& "main".equals(((MethodTypeNameVoid) methodDecl.getMethodTypeName()).getMethodName()));
	}

	public void visit(MehtodTypeNameCustom methodTypeName) {
		Obj mehtodDecl = TabExt.find(methodTypeName.getMethodName());
		if (mehtodDecl != TabExt.noObj) {
			methodTypeName.obj = TabExt.noObj;
			report_error("Greska na liniji " + methodTypeName.getLine() + ". Ime " + methodTypeName.getMethodName()
					+ " je vec deklarisano! ", null);
		} else {
			Obj newMethod = TabExt.insert(Obj.Meth, methodTypeName.getMethodName(), declType);
			currentMethod = newMethod;
			currentMethodName = methodTypeName.getMethodName();
			methodTypeName.obj = newMethod;
			TabExt.openScope();
			report_info("Obradjuje se funkcija " + methodTypeName.getMethodName(), methodTypeName);
		}
	}

	public void visit(MethodTypeNameVoid methodTypeName) {
		Obj existingMethod = TabExt.find(methodTypeName.getMethodName());
		if (existingMethod != TabExt.noObj) {
			methodTypeName.obj = TabExt.noObj;
			report_error("Greska na liniji " + methodTypeName.getLine() + ". Ime " + methodTypeName.getMethodName()
					+ " je vec deklarisano! ", null);
		} else {
			Obj newMethod = TabExt.insert(Obj.Meth, methodTypeName.getMethodName(), TabExt.noType);
			currentMethod = newMethod;
			methodTypeName.obj = newMethod;
			currentMethodName = methodTypeName.getMethodName();
			TabExt.openScope();
			report_info("Obradjuje se funkcija " + methodTypeName.getMethodName(), methodTypeName);
		}
	}

	public void visit(SimpleExpr simpleExpr) {
		simpleExpr.struct = simpleExpr.getExpr1().struct;
	}

	public void visit(Expr1Minus minusTerm) {
		if (!minusTerm.getTerm().struct.equals(TabExt.intType)) {
			report_error("Greska uz minus mora biti int", minusTerm);
			minusTerm.struct = TabExt.noType;
		} else {
			minusTerm.struct = minusTerm.getTerm().struct;
		}
	}

	public void visit(Expr1Term exp1Term) {
		exp1Term.struct = exp1Term.getTerm().struct;
	}

	public void visit(ExprAddOp exprAddOp) {
		Struct t1 = exprAddOp.getExpr1().struct;
		Struct t2 = exprAddOp.getTerm().struct;

		if (t1.compatibleWith(t2) && t1 == TabExt.intType) {
			exprAddOp.struct = exprAddOp.getExpr1().struct;
		} else {
			report_error("Greska tipovi razliciti ", exprAddOp);
			exprAddOp.struct = TabExt.noType;
		}

	}

	public void visit(FactorDesignator factorDesignator) {
		if (factorDesignator.getDesignator() instanceof DesignatorExpr) {
			factorDesignator.struct = factorDesignator.getDesignator().obj.getType();
			// report_info("cccc "+factorDesignator.struct,null);
		} else {
			factorDesignator.struct = factorDesignator.getDesignator().obj.getType();
			// report_info("cccc " + factorDesignator.struct, null);
		}
	}

	public void visit(FactorNumber factorNumber) {
		factorNumber.struct = TabExt.intType;
	}

	public void visit(FactorChar factorChar) {
		factorChar.struct = TabExt.charType;
	}

	public void visit(FactorBool factorBool) {
		factorBool.struct = TabExt.booleanType;
	}

	public void visit(FactorNew factorNew) {
		Struct defType = declType;
		if (factorNew.getExpr().struct == TabExt.intType) {
			factorNew.struct = new Struct(Struct.Array, factorNew.getType().struct);
		} else {
			report_error("Greska na liniji " + factorNew.getLine() + " expr za niz nije int", factorNew);
			factorNew.struct = TabExt.noType;
		}
	}

	public void visit(FactorExpr factorExpr) {
		factorExpr.struct = factorExpr.getExpr().struct;
	}

	public void visit(TermFactor termFactor) {
		termFactor.struct = termFactor.getFactor().struct;
		// report_info("bbbbb "+ termFactor.struct.getKind()+" "+
		// termFactor.struct+termFactor.getLine(),null);
	}

	public void visit(TermMulOpFactor termFactor) {
		Struct t1 = termFactor.getTerm().struct;
		Struct t2 = termFactor.getFactor().struct;

		if (t1.equals(t2) && t1 == TabExt.intType) {
			termFactor.struct = t1;
		} else {
			report_error("Greska na liniji " + termFactor.getLine() + " nisu odgovarajuci tipovi", termFactor);
			termFactor.struct = TabExt.noType;
		}
	}

	public void visit(DesignatorExpr designatorExp) {
		Obj desCheck = Tab.find(designatorExp.getDesignatorIdentitet().getIme());
		// report_info("asda " + designatorExp.getExpr() + designatorExp.getLine(),
		// null);
		if (desCheck == Tab.noObj) {
			report_error("Greska na liniji " + designatorExp.getLine() + ". Ime "
					+ designatorExp.getDesignatorIdentitet().getIme() + " nije deklarisano! ", null);
		} else {
			if (!(desCheck.getType().getKind() == Struct.Array)) {
				report_error("Greska na liniji " + designatorExp.getLine() + ". Ime "
						+ designatorExp.getDesignatorIdentitet().getIme() + " nije deklarisano kao niz! "
						+ designatorExp.getExpr().struct, null);
			} else if (designatorExp.getExpr().struct != Tab.intType) {
				report_error("Greska na liniji " + designatorExp.getLine() + ". Ime " + designatorExp.getExpr()
						+ " nije int! " + designatorExp.getExpr().struct, null);
			} else {
				designatorExp.obj = new Obj(Obj.Elem, "", desCheck.getType().getElemType());
				report_info("kreiran elem za niz " + desCheck.getType().getElemType().getKind(), null);
				designatorExp.getDesignatorIdentitet().obj = desCheck;
			}
		}

	}

	public void visit(DesignatorIdent designatorIdent) {
		Obj declaredIdent = TabExt.find(designatorIdent.getIdentName());

		if (declaredIdent == TabExt.noObj) {
			designatorIdent.obj = TabExt.noObj;
			report_error("Ident " + designatorIdent.getIdentName() + " nije prethodno deklarisan", null);
		} else {
			designatorIdent.obj = declaredIdent;
		}
	}

	public void visit(DesignatorAssignOp designatorAssignOp) {
		Obj obj = designatorAssignOp.getDesignator().obj;
		if (obj.getKind() == Obj.Var || obj.getKind() == Obj.Elem || obj.getKind() == Obj.Fld) {
			if (designatorAssignOp.getDesignator().obj.getType().compatibleWith(designatorAssignOp.getExpr().struct)) { // ako
																														// su
				// designator i
				// expr obicni
				// tipovi
				report_info("Designator i expr su odgovarajuceg tipa ",
						designatorAssignOp);
			} else {
				report_error(
						"Error: Designator i expr nisu odgovarajuceg tipa na liniji " + designatorAssignOp.getLine()
								+ "\n" + " " + designatorAssignOp.getDesignator().obj.getType().getKind() + " "
								+ designatorAssignOp.getExpr().struct.getKind(),
						designatorAssignOp);
			}
		} else {
			report_error("Error: Designator nije odgovarajuceg tipa na liniji " + designatorAssignOp.getLine(), null);
		}
	}

	public void visit(DesignatorIncOp desInc) {
		if (desInc.getDesignator() instanceof DesignatorExpr) {
			if (!desInc.getDesignator().obj.getType().equals(TabExt.intType))
				report_error("Greska promenljiva je tipa niz", desInc);
		} else {
			if (desInc.getDesignator().obj.getKind() != Obj.Var) {
				report_error("Greska operand nije promenljiva", desInc);
			} else {
				if (!desInc.getDesignator().obj.getType().equals(TabExt.intType)) {
					report_error("Greska promenljiva nije celobrojnog tipa", desInc);
				}
			}
		}
	}

	public void visit(DesignatorDecOp decOp) {
		if (decOp.getDesignator() instanceof DesignatorExpr) {
			if (!decOp.getDesignator().obj.getType().equals(TabExt.intType))
				report_error("Greska promenljiva je tipa niz", decOp);
		} else {
			if (decOp.getDesignator().obj.getKind() != Obj.Var) {
				report_error("Greska operand nije promenljiva", decOp);
			} else if (!decOp.getDesignator().obj.getType().equals(TabExt.intType)) {
				report_error("Greska promenljiva nije celobrojnog tipa", decOp);
			}
		}
	}

	public void visit(StatementRead readStm) {
		if (readStm.getDesignator() instanceof DesignatorIdent) {
			checkStatementDesIdent(readStm);
		} else {
			checkStatementDesArray(readStm);
		}
	}

	public void visit(CondFactExp condFactExp) {
		Struct type = condFactExp.getExpr().struct;
		if (type.getKind() != Struct.Bool) {
			report_error("Fact nije tipa boolean  ", condFactExp);
		}
	}

	public void visit(CondFactsMultipleExpr condFactsMultipleExpr) {
		// Relop relop = condFactsMultipleExpr.getRelop();
		Expr expr = condFactsMultipleExpr.getExpr();
		Expr expr1 = condFactsMultipleExpr.getExpr1();

		if (expr.struct.getKind() == Struct.None) {
			report_error("Prvi expr je None tipa ", condFactsMultipleExpr);
		}
		if (expr1.struct.getKind() == Struct.None) {
			report_error("Drugi expr je None tipa", condFactsMultipleExpr);
		}

		if (expr.struct.isRefType() != expr1.struct.isRefType()) {
			report_error("Nisu kompatibilni tipovi ", condFactsMultipleExpr);
		} else if (expr.struct.isRefType()) {
			if (!(expr1.struct == TabExt.nullType) && !(expr.struct == TabExt.nullType)
					&& expr1.struct.getElemType().getKind() != expr.struct.getElemType().getKind()) {
				report_error("Nisu kompatibilni tipovi", condFactsMultipleExpr);
			} else if (!(condFactsMultipleExpr.getRelop() instanceof RelopEqual
					|| condFactsMultipleExpr.getRelop() instanceof RelopNotEqual)) {
				report_error("Nizovi se mogu porediti samo sa != i ==", condFactsMultipleExpr);
			}
		} else {
			if (expr.struct.getKind() != expr1.struct.getKind()) {
				report_error("Nisu kompatibilni tipovi", condFactsMultipleExpr);
			} else if (expr.struct.getKind() == Struct.Bool && !(condFactsMultipleExpr.getRelop() instanceof RelopEqual
					|| condFactsMultipleExpr.getRelop() instanceof RelopNotEqual)) {
				report_error("Nizovi se mogu porediti samo sa != i ==", condFactsMultipleExpr);
			}
		}
	}

	public void checkStatementDesIdent(StatementRead readStm) {
		if (readStm.getDesignator().obj.getKind() != Obj.Var) {
			report_error("Greska nije promenljiva", readStm);
		} else {
			if (readStm.getDesignator().obj.getType() != TabExt.intType
					&& readStm.getDesignator().obj.getType() != TabExt.charType
					&& readStm.getDesignator().obj.getType() != TabExt.booleanType) {
				report_error("Greska neodgovarajuci tip", readStm);
			}
		}
	}

	public void checkStatementDesArray(StatementRead readStm) {
		if (readStm.getDesignator().obj.getType().getElemType() != TabExt.intType
				&& readStm.getDesignator().obj.getType().getElemType() != TabExt.charType
				&& readStm.getDesignator().obj.getType().getElemType() != TabExt.booleanType) {
			report_error("Greska neodgovarajuci tip", readStm);
		}

	}

	public void mainExists() {
		if (!mainFound)
			report_error("Main metoda ne postoji", null);
	}

	public boolean passed() {
		return !errorDetected;
	}
}
