package grammar;

import grammar.NTyp;
import grammar.Node;
import grammar.node.*;
import grammar.node.Number;

public class New {
    public static Node typ(NTyp typ) {
        return switch (typ) {
            case CompUnit -> new CompUnit();
            case Decl -> new Decl();
            case ConstDecl -> new ConstDecl();
            case BType -> new Btype();
            case ConstDef -> new ConstDef();
            case ConstInitVal -> new ConstInitVal();
            case VarDecl -> new VarDecl();
            case VarDef -> new VarDef();
            case InitVal -> new InitVal();
            case FuncDef -> new FuncDef();
            case MainFuncDef -> new MainFuncDef();
            case FuncType -> new FuncType();
            case FuncFParams -> new FuncFParams();
            case FuncFParam -> new FuncFParam();
            case FuncRParams -> new FuncRParams();
            case Block -> new Block();
            case BlockItem -> new BlockItem();
            case Stmt -> new Stmt();
            case If -> new If();
            case While -> new While();
            case Break -> new Break();
            case Continue -> new Continue();
            case Return -> new Return();
            case Exp -> new Exp();
            case Cond -> new Cond();
            case LVal -> new LVal();
            case PrimaryExp -> new PrimaryExp();
            case Number -> new Number();
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
        };
    }
}
