//
// Created by fall qs on 2021/9/16.
//

#include<iostream>
#include<cstdio>

using namespace std;

int getint() {
    int x;
    cin >> x;
    return x;
}

const int x = 998244353;
const int y = 19260817, z = 19491001, w = 13;

int main() {
    int t = x / y;
    printf("seed = %d\n",t);
    printf("mult1 = %d\n", t * t % z);
    printf("mult2 + bool = %d\n", y / w + (y < z));
    int i = 10, j = 7;
    while (2) {
        printf("num*%d = %d\n", j, t * t % y);
        t = t * t % y;;;;
        (((t - y) + 1)+2)+3;
        j = j+1;
        if(i==j)break;
    }
    if(i<1){}
    while(3) {
        printf("num/%d = %d\n", j, t / w % y);
        t = t * t % y;;;;
        (((t - y) + 1)+2)+3;
        j = j+1;
        if(w==j)break;
    }
    printf("SOME+_- useless=_= STRING|/-?:;,.`<>`{}[]()~ ^*^ @!\n");
}
