//
// Created by fall qs on 2021/9/16.
//

#include<iostream>
#include<cstdio>
using namespace std;

int getint() {
    int x;cin>>x;return x;
}
/*
 * this file tests:
 * recursion, multi flower braces,
 * if-elseif, if-if-else,
 * exp as realParam,
 * and annotation
 */
int x=3,y,z;
int fib(int x) {{{
    if(x<=1)return x; // calculates fibonacci array
    return fib(x-1) + fib(x-2);
}}}

int go(int y) {
    z = z + y;
    return z;
}
int to(int z) {
    y = y + z;
    return y;
}

void Switch(int t) {
    if(t<0)x = -1;
    else if(t==2) x = -2;
    else if(t==3){
        x = -5; y=t;
    } else if (t==7){
        x=-8;y=-1;z=9;
    }
    if(t*x<0)z = -z;
    else z = z+z;
}

void prt(){
    printf("x, y, z == %d, %d, %d \n", x,y,z);
}

int main() {
    y = getint();
    printf("fib1 = %d\n", fib(y));
    printf("fib2 = %d\n", fib(getint()));
    printf("fib1 - x = %d\n", fib(y) - x);
    int x = go(y) == y;
    printf("tmpx = %d\n", x);

    int i=0;while(i<6){
        Switch(getint());prt();i = i + 1;
    }

}