ConstExp => AddExp => $(, Ident, IntConst, +,-,!$

AddExp => MulExp { `+-` MulExp } => $(, Ident, IntConst, +,-,!$

MulExp => UnaryExp { `*/%` UnaryExp } => $(, Ident, IntConst, +,-,!$

UnaryExp => PrimaryExp | Ident `(` [ FuncRParams ] `)` | UnaryOp UnaryExp => $(, Ident, IntConst || Ident || +,-,!$

PrimaryExp => `(` Exp `)` | LVal | Num =>  $(, Ident, IntConst$ 

UnaryOp => $+,-,!$



CompUnit → {Decl} {FuncDef} MainFuncDef 

+ CompUnit => {Type REF [ `=` InitVals ]}

Decl → ConstDecl | VarDecl  =>$const || int$     ;      >= $;$

+ Decl => REF [ `=` InitVals ] `;`

常量声明 ConstDecl → `const` BType ConstDef { `,` ConstDef } `;`  => $const$   ;    >= $;$

+ ConstDecl => `const` BType CostDef {`,`ConstDef}

基本类型 BType → `int`   => $int$

常数定义 ConstDef → Ident { `[` ConstExp `]` } `=` ConstInitVal  => $Ident$    ;     >= $CONSTINITVAL$

+ ConstDef => REF(`val`) ConstInitVal

常量初值 ConstInitVal → ConstExp | `{` [ ConstInitVal { `,` ConstInitVal } ] `}`  => $CONSTEXP || \{$     ;      >= $CONSTEXP || \}$

变量声明 VarDecl → BType VarDef { `,` VarDef } `;`  => $int$    >= $;$

变量定义 VarDef → Ident { `[` ConstExp `]` } | Ident { `[` ConstExp `]` } `=` InitVal  => $Ident$   ;    >= $ident, ] || INITVAL$

+ VarDef => REF(`val`) [ `=` InitVal]

变量初值 InitVal → Exp | `{` [ InitVal { `,` InitVal } ] `}` => $EXP || \{$      ;       >= $EXP || \}$

函数定义 FuncDef → FuncType Ident `(` [FuncFParams] `)` Block => $int, void$      ;      >= $\}$

+ FuncDef => FuncType REF(`func`)

主函数定义 MainFuncDef → `int` `main` `(` `)` Block => $int$      ;      >= $\}$

MainFuncDef => `int` REF(`func`, `main`)

函数类型 FuncType → `void` | `int` => $void, int$

函数形参表 FuncFParams → FuncFParam { `,` FuncFParam }    => $int$        ;      >= $ident, ]$

函数形参 FuncFParam → BType Ident [`[` `]` { `[` ConstExp `]` }]  => $int$       ;      >= $ident, ]$

语句块 Block → `{` { BlockItem } `}`  => $\{$      ;      >= $\}$

语句块项 BlockItem → Decl | Stmt  => $const, int || STMT$      ;      >= $; || STMT$

语句 Stmt → LVal `=` Exp `;` 

| [Exp] `;` 

| Block

| `if` `(` Cond `)` Stmt [ `else` Stmt ] 

REF(`func`, `if`)

| `while` `(` Cond `)` Stmt 

REF(`func`, `while`)

| `break` `;` 

REF(`val`, `break`)

| `continue` `;` 

REF(`val`, `continue`)

| `return` [Exp] `;`  

REF(`val`, `return`)

| LVal = `getint` `(` `)` `;` 

| `printf` `(` FormatString{,Exp} `)` `;`

REF(`func`, `printf`)

=> $LVAL\ ||\ (, Ident, IntConst, +,-,!\ ||\ \{ ||\ if \ ||\ while\ ||\ break\ ||\ continue\ ||\ return\ ||\ LVAL\ ||\ printf$                         >= $;||\cdots || \}$

=> LVal = RVal,    RVal $\rightarrow$ getint() | Exp  

表达式 Exp → AddExp =>$(, Ident, IntConst, Ident, +,-,!$

条件表达式 Cond → LOrExp

左值表达式 LVal → Ident {`[` Exp `]`}  => $Ident$

基本表达式 PrimaryExp → `(` Exp `)` | LVal | Number  => $( || LVAL || intconst$

数值 Number → IntConst => $intconst$

⼀元表达式 UnaryExp → PrimaryExp | Ident `(` [FuncRParams] `)`   | UnaryOp UnaryExp  => $(, Ident, intconst || Ident || +,-,!$

单⽬运算符 UnaryOp → `+` | `−` | `!`  => $+,-,!$

函数实参表 FuncRParams → Exp { `,` Exp }  => $(, Ident, IntConst, +,-,!$       ;      >= $(, Ident, IntConst, +,-,!$

乘除模表达式 MulExp → UnaryExp | MulExp (`*` | `/` | `%`) UnaryExp 

加减表达式 AddExp → MulExp | AddExp (`+` | `−`) MulExp 

关系表达式 RelExp → AddExp | RelExp (`<` | `>` | `<=` | `>=`) AddExp 

相等性表达式 EqExp → RelExp | EqExp (`==` | `!=`) RelExp

逻辑与表达式 LAndExp → EqExp | LAndExp `&&` EqExp

逻辑或表达式 LOrExp → LAndExp | LOrExp `||` LAndExp

常量表达式 ConstExp → AddExp

