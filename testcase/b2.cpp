//
// Created by fall qs on 2021/9/16.
//

#include<bits/stdc++.h>
using namespace std;
int getint(){
    int x;cin>>x;
    return x;
}
const int D=3;
int zro[D][3]; const int eye[D][D]={{1,0,0},{0,1,0},{0,0,1}};
int rst[3][3];
const int arr[10] = {0, 1, 2, 18, 1023, 1000000007, 998244353, 2147483647, -1, 19260817};
void mov(int u[][D], int v[][D]){
    int i=D,j;
    while(i){
        i = i - 1;
        j = 0;
        while(j<D){{{
                    v[j][i] = u[j][i];
                }
                j = j + 1;
            }}
    }
}
void mul(int t[][D], int x[][3], int y[][3]){
    int i=0,j=0,k;
    int z[D][D];mov(zro,z);
    while(i<D){
        j = 0;
        while(j<D){
            {
                {
                    k = 0;
                    while (k < D){
                        z[i][j] = z[i][j] + x[i][k] * y[k][j];
                        k = k + 1;
                    }
                    j = j + 1;
                }
            }
        }
        i = i + 1;
    }
    mov(z,t);
}
int ksm(int t[][D], int xx[][D], int p){
    t[0][0] = eye[0][0]; t[0][1] = eye[0][1]; t[0][2] = eye[0][2];
    t[1][0] = eye[1][0]; t[1][1] = eye[1][1]; t[1][2] = eye[1][2];
    t[2][0] = eye[2][0]; t[2][1] = eye[2][1]; t[2][2] = eye[2][2];
//    mov(eye, t);
    int x[D][D];mov(xx,x);
    while(p) {
        if(p%2)mul(t, t, x);
        mul(x, x, x);
        p = p/2;
    }
}

int main(){
    int n = 10, i = 0, x[D][D] = {
            {1,2,1},
            {1,0,0},
            {0,1,0}
    };
    while(n){
        n = n - 1;
        ksm(rst, x, arr[n]);
//        for(int i=0;i<3;++i)for(int j=0;j<3;++j)cout<<x[i][j]<<' ';cout<<'\n';
        printf("NO.%d = {%d, %d, %d}\n", arr[n], rst[0][0], rst[0][1], rst[0][2]);
    }
    return 0;
}