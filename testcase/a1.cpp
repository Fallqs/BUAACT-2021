//
// Created by fall qs on 2021/9/16.
//

#include<bits/stdc++.h>
using namespace std;
inline int getint(){
    int x;cin>>x;
    return x;
}
const int N=300005, SZ=2147483648;
int f[N],c[N][2],v[N],s[N],st[N];
int r[N];
inline bool nrt(int x){
    return c[f[x]][0]==x||c[f[x]][1]==x;
}
void pushu(int x){
    s[x]=s[c[x][0]]+s[c[x][1]]+v[x];
}
void pushr(int x){int t=c[x][0];c[x][0]=c[x][1];c[x][1]=t;r[x]=!r[x];}
void pushdn(int x){
    if(r[x]){
        if(c[x][0])pushr(c[x][0]);
        if(c[x][1])pushr(c[x][1]);
        r[x]=0;
    }
}
void rtt(int x){
    int y=f[x],z=f[y],k=c[y][1]==x,w=c[x][!k];
    if(nrt(y))c[z][c[z][1]==y]=x;c[x][!k]=y;c[y][k]=w;
    if(!!w)f[w]=y;f[y]=x;f[x]=z;
    pushu(y);
}
void sply(int x){
    int y=x,z=0;
    z = z + 1; st[z]=y;
    while(nrt(y)){z = z + 1; st[z]=y=f[y];}
    while(z){pushdn(st[z]);z = z - 1;}
    while(nrt(x)){
        y=f[x];z=f[y];
        if(nrt(y))if((c[y][0]==x)!=(c[z][0]==y)){rtt(x);}else rtt(y);
        rtt(x);
    }
    pushu(x);
}
void acc(int x){
    int y=0;while(x){
        sply(x);c[x][1]=y;pushu(x);
        y = x;
        x = f[x];
    }
}
void mrt(int x){
    acc(x);sply(x);pushr(x);
}
int frt(int x){
    acc(x);sply(x);
    while(c[x][0]){pushdn(x);x=c[x][0];}
    sply(x);
    return x;
}
void spl(int x,int y){
    mrt(x);acc(y);sply(y);
}
void lnk(int x,int y){
    mrt(x);if(frt(y)!=x)f[x]=y;
}
void cut(int x,int y){
    mrt(x);
    if(frt(y)==x&&f[y]==x&&!c[y][0]){
        f[y]=0;c[x][1]=0;
        pushu(x);
    }
}
int main(){
    int n=getint(),m=getint(), i=1;
    while (i<=n){v[i] = getint(); i = i+1;}
    while(m){m = m-1;
        int type=getint(),x=getint(),y=getint();
        if(type==0){spl(x,y);printf("%d\n",s[y]);}
        else if(type==1)lnk(x,y);
        else if(type==2)cut(x,y);
        else {sply(x);v[x]=y;}
    }
    return 0;
}