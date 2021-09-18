#include <iostream>
#include <bits/stdc++.h>
using namespace std;

int main() {
    std::cout << "Hello, World!" << std::endl;
    mt19937 rnd(chrono::system_clock::now().time_since_epoch().count());
    uniform_int_distribution<int> rg(-2147483648, 2147483647);
    uniform_int_distribution<int> rk(0, 11);
    for(int i=0;i<12;++i)cout<<rg(rnd)<<' ';cout<<'\n';
    for(int i=0;i<12;++i)cout<<rg(rnd)<<' ';cout<<'\n';
    for(int i=0;i<12;++i)cout<<rk(rnd)<<' ';cout<<'\n';
    for(int i=0;i<12;++i)cout<<rk(rnd)<<' ';cout<<'\n';
    return 0;
}
