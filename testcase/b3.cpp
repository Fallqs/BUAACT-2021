//
// Created by fall qs on 2021/9/16.
//
#include<bits/stdc++.h>
using namespace std;
int getint(){
    int x;cin>>x;return x;
}

const int D = 12;

int dot(int x[], int y[]) {
    int r = 0, i = 0;
    while (i < D) {
        r = r + x[i] * y[i];
        i = i + 1;
    }
    return r;
}

void var(int t[][D], int x[], int y[]) {
    int i = 0, j;
    while (i < D) {
        j = 0;
        while (j < D) {
            t[i][j] = x[i] * y[j];
            j = j + 1;
        }
        i = i + 1;
    }
}

int main(){
    int x[D], y[D];
    int i=0;while(i<D) { x[i] = getint();i = i + 1; }
    i=0;while(i<D) { y[i] = getint();i = i + 1; }
    int z[D][D];
    var(z,x,y);
    i=0;while(i<10) { x[i] = getint();i = i + 1; }
    i=0;while(i<10) { y[i] = getint();i = i + 1; }
    i=0;while(i<10) {
        printf("dot%d = %d\n", i+1, dot(z[x[i]], z[y[i]]));
        i = i + 1;
    }
    return 0;
}
