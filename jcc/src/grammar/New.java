package grammar;

import grammar.nd.Decl;
import grammar.nd.StmtLR;
import grammar.node.*;

public class New {
    public static Node typ(NTyp typ) {
        switch (typ) {
            case CompUnit:
                return new CompUnit();
            case ConstInitVal:
                return new InitVal(true);
            case Decl:
                return new Decl();
            case InitVal:
                return new InitVal(false);
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
                return new NullNode();
        }
    }
}
