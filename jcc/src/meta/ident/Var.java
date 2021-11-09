package meta.ident;


import word.Typ;

public class Var {
    public int[] siz, val;
    public boolean cnst = false, zero = false, any = false, vod = false;
    public String name;

    public Var(int dim) {
        this.siz = new int[dim];
        val = new int[0];
    }

    public Var(Var v, int oft) {
        this(0);
        if(v == null) {
            any = true;
            return;
        }
        this.siz = new int[v.siz.length - oft];
        this.cnst = v.cnst;
        this.zero = v.zero;
    }

    public Var(Typ t) {
        this(0);
        vod = (t == Typ.VOIDTK);
    }

    public Var set(int... args) {
        val = args;
        return this;
    }

    public Var setSiz(int... args) {
        System.arraycopy(args, 0, siz, 0, siz.length);
        return this;
    }

    public Var setCnst(boolean v) {
        cnst = v;
        return this;
    }

    public Var setZero(boolean v) {
        zero = v;
        return this;
    }

    public Integer get(int oft) {
        if (oft < val.length) return val[oft];
        if (zero) return 0;
        return null;
    }

    /**
     * @return dimension of var[][]...[]
     */
    public int dim(int... ix) {
        return siz.length - ix.length;
    }

    /**
     * @return offset of var[][]...[] from &var
     */
    public int oft(int... ix) {
        int ans = 0;
        for (int i = 0; i < siz.length; ++i) {
            ans = ans * siz[i] + ix[i];
        }
        return ans;
    }

    /**
     * @return mutable
     */
    public boolean mut() {
        return !cnst;
    }

    public boolean chkParam(Var v) {
        return v.any || v.siz.length == this.siz.length && !v.vod;
    }
}
