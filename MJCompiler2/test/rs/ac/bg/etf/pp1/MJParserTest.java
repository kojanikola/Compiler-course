package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import java_cup.runtime.Symbol;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;

public class MJParserTest {

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}

	public static void main(String[] args) throws Exception {
		
		CompilerImpl compl = new CompilerImpl();
		List<CompilerError> lista = compl.compile("test/program.mj", "test/program.obj");
		System.out.println(lista.toString());
		
		
//		Logger log = Logger.getLogger(MJParserTest.class);
//
//		Reader br = null;
//		try {
//			File sourceCode = new File("test/program.mj");
//			log.info("Compiling source file: " + sourceCode.getAbsolutePath());
//
//			br = new BufferedReader(new FileReader(sourceCode));
//			Yylex lexer = new Yylex(br);
//			
//			
//
//			MJParser p = new MJParser(lexer);
//			Symbol s = p.parse(); // pocetak parsiranja
//			
//			Program prog = (Program) (s.value);
//			TabExt.init();
//			// ispis sintaksnog stabla
//			log.info(prog.toString(""));
//			log.info("===================================");
//			
//			// ispis prepoznatih programskih konstrukcija
//			SemanticPass v = new SemanticPass();
//			prog.traverseBottomUp(v);
//			log.info("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+p.lista.size());
//			log.info("Print count calls =" + v.printCallCount);
//			log.info("Vars = " + v.varDeclCount);
//			log.info("===================================");
//			TabExt.dump();
//		
//			if (!p.errorDetected && v.passed()) {
//				File objectFile = new File("test/program.obj");
//				if (objectFile.exists())
//					objectFile.delete();
//				CodeGenerator codeGenerator = new CodeGenerator();
//				prog.traverseBottomUp(codeGenerator);
//				Code.dataSize = v.numberOfVars;
//				Code.mainPc = codeGenerator.getMainPc();
//				Code.write(new FileOutputStream(objectFile));
//				log.info("Parsiranje uspesno zavrseno!");
//			} else {
//				log.error("Parsiranje NIJE uspesno zavrseno!");
//				log.info(""+v.lista.size());
//			}
//			
//		} finally {
//			if (br != null)
//				try {
//					br.close();
//				} catch (IOException e1) {
//					log.error(e1.getMessage(), e1);
//				}
//		}

	}

}
