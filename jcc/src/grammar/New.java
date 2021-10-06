package grammar;

import grammar.nd.Decl;
import grammar.nd.StmtLR;
import grammar.node.*;

public class New {
    public static Node typ(NTyp typ) {
        return switch (typ) {
            case CompUnit -> new CompUnit();
            case ConstDecl -> new ConstDecl();
            case ConstDef -> new ConstDef();
//            case ConstInitVal -> new ConstInitVal();
            case Decl -> new Decl();
            case VarDecl -> new VarDecl();
            case VarDef -> new VarDef();
            case InitVal -> new InitVal();
//            case FuncDef -> new FuncDef();
            case MainFuncDef -> new MainFuncDef();
            case FuncType -> new FuncType();
            case FuncFParams -> new FuncFParams();
            case FuncFParam -> new FuncFParam();
            case FuncRParams -> new FuncRParams();
            case Block -> new Block();
//            case BlockItem -> new BlockItem();
            case Stmt -> new Stmt();
            case StmtLR -> new StmtLR();
            case If -> new If();
            case While -> new While();
            case Break -> new Break();
            case Continue -> new Continue();
            case Return -> new Return();
            case Exp -> new Exp();
            case Cond -> new Cond();
            case LVal -> new LVal();
            case PrimaryExp -> new PrimaryExp();
            case Number -> new Num();
            case UnaryExp -> new UnaryExp();
            case UnaryOp -> new UnaryOp();
            case MulExp -> new MulExp();
            case AddExp -> new AddExp();
            case RelExp -> new RelExp();
            case EqExp -> new EqExp();
            case LAndExp -> new LAndExp();
            case LOrExp -> new LOrExp();
            case ConstExp -> new ConstExp();
            case Getint -> new Getint();
            case Printf -> new Printf();
            default -> null;
        };
    }
}
