package grammar;

import grammar.nd.Decl;
import grammar.nd.StmtLR;
import grammar.node.*;

public class New {
    public static Node typ(NTyp typ) {
        switch (typ) {
            case CompUnit:
                return new CompUnit();
            case ConstDecl:
                return new ConstDecl();
            case ConstDef:
                return new ConstDef();
//            case ConstInitVal : return new ConstInitVal();
            case Decl:
                return new Decl();
            case VarDecl:
                return new VarDecl();
            case VarDef:
                return new VarDef();
            case InitVal:
                return new InitVal();
//            case FuncDef : return new FuncDef();
            case MainFuncDef:
                return new MainFuncDef();
            case FuncType:
                return new FuncType();
            case FuncFParams:
                return new FuncFParams();
            case FuncFParam:
                return new FuncFParam();
            case FuncRParams:
                return new FuncRParams();
            case Block:
                return new Block();
//            case BlockItem : return new BlockItem();
            case Stmt:
                return new Stmt();
            case StmtLR:
                return new StmtLR();
            case If:
                return new If();
            case While:
                return new While();
            case Break:
                return new Break();
            case Continue:
                return new Continue();
            case Return:
                return new Return();
            case Exp:
                return new Exp();
            case Cond:
                return new Cond();
            case LVal:
                return new LVal();
            case PrimaryExp:
                return new PrimaryExp();
            case Number:
                return new Num();
            case UnaryExp:
                return new UnaryExp();
            case UnaryOp:
                return new UnaryOp();
            case MulExp:
                return new MulExp();
            case AddExp:
                return new AddExp();
            case RelExp:
                return new RelExp();
            case EqExp:
                return new EqExp();
            case LAndExp:
                return new LAndExp();
            case LOrExp:
                return new LOrExp();
            case ConstExp:
                return new ConstExp();
            case Getint:
                return new Getint();
            case Printf:
                return new Printf();
            default:
                return null;
        }
    }
}
