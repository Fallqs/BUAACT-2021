//
// Created by fall qs on 2021/9/16.
//
#include<bits/stdc++.h>
using namespace std;
int getint(){
    int x;cin>>x;return x;
}
int main(){
    int x, y, n=getint();
    while(n){
        n = n - 1;
        x = getint(); y = getint();
        printf("(%d,%d) -> {%d,%d,%d,%d,%d,%d}\n", x,y, x<y, x>y, x<=y, x>=y, x!=y, x==y);
    }
    n = getint();
    while(n){
        n = n + 1;
        x = getint(); y = getint();
        printf("[%d, %d] -> {%d}\n",x, y, ((x+y)*(x-y)/ (x - 2*y) + (x+y)*(x+y)/ (2*x - y))*(3*y*x)%998244353);
    }
    return 0;
}
