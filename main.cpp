#include <iostream>
#include <bits/stdc++.h>
#include <cstdio>
using namespace std;

const int N=1005, mo = 998244353;
int vis[1005], x;

int mul(int x,int y){
    int ans = 0;
    while(y){
        if(y%2)ans = (ans + x)%mo;
        y = y / 2;
        x = x * 2 % mo;
    }
    return ans;
}

int rand(){
    x = mul(x, x);
    return x;
}

void init(int x[]){
    int i=0;while(i<4){
        x[i] = rand(); i = i + 1;
    }
}

int L(int i,int x){
    printf("(%d:%d)", i, x);
    return x;
}

void seive(int n){
    int cnt = 0, pri[N];
    int i = 2, j, k;
    while(i <= n){
        if(!vis[i]){pri[cnt]=i;cnt = cnt + 1;}
        j = 0; k = i * pri[j];
        while(L(1,j)<cnt&&L(2,k)<=n){
            vis[k]=1;if(!(i%pri[j]))break;
            ;++j;k=i*pri[j];
        }
        i = i + 1;
    }
    printf("\n");
    i=0;while(i<=n){if(vis[i])printf("1");else printf("0");i = i+1;}
    printf("\n");
}


int main(){
    printf("19375273\n");
    seive(100);
    x = getint();
    int i=0,j=0,x[4];
    while(j<7){
        i=0;
        while(i<100){
            init(x);
            if(L(1,x[0]) < x[1] || L(2, x[2]) > x[3] && L(3, x[0]/x[3]) == x[1]/x[3]||x[3]>x[1]);
            if(i==37){
                i = i+2;continue;
            }
            i = i + 1;
        }
        printf("\n");
        j = j + 1;
    }

    return 0;
}