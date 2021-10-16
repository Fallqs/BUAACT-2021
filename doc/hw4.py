CLASS, TOKEN = '', ''
cnt = 0

def NEXTSYM():
    global CLASS, TOKEN
    CLASS, TOKEN = '', ''

def ERROR():
    raise Exception()

def E() -> int:
    if CLASS == 'word' or CLASS == 'lparent':
        q = T()
        NEXTSYM()
        r = A(q)
        return r if r != -1 else q
    ERROR()

def T() -> int:
    if CLASS == 'word' or CLASS == 'lparent':
        q = F()
        NEXTSYM()
        r = B(q)
        return r if r != -1 else q
    ERROR()

def F() -> int:
    if CLASS == 'lparent':
        NEXTSYM()
        q = E()
        NEXTSYM()
        if CLASS != 'rparent': ERROR()
        return q
    elif CLASS == 'word':
        return Id()
    ERROR()

def Id() -> int:
    q = Word.nex()
    if q == -1: ERROR()
    return q

def A(q: int) -> int:
    if TOKEN == '+':
        NEXTSYM()
        s = A(T())
        return ADD(q, s)
    return -1

def B(q: int) -> int:
    if TOKEN == '*':
        NEXTSYM()
        s = B(F())
        return MULT(q, s)
    return -1

def ADD(q: int, r: int) -> int:
    global cnt
    if q != -1 and r != -1:
        cnt += 1
        print(f'ADD {q}, {r}, {cnt}')
        return cnt
    return q

def MULT(q: int, r: int) -> int:
    global cnt
    if q != -1 and r != -1:
        cnt += 1
        print(f'MULT {q}, {r}, {cnt}')
        return cnt
    return q

if __name__ == __main__:
    E()