//
// Created by fall qs on 2021/12/15.
//

#include<bits/stdc++.h>
#include<cstdio>

using namespace std;
typedef long long ll;

ll magic(int d) {
    ll phi = (1ll << 32) % d > (d >> 1) ? d - 1 : 0;
    ll ans = ((1ll << 32) + phi) / d;
    return ans;
}

int main() {
    int d = 10;
    for (int i = 2147483547, j = 0; j < 200; ++j, ++i) {
        if (i / d != i * magic(d) >> 32) {
            printf("i=%d, d=%d, div=%d, divi=%lld, magic=%lld\n", i, d, i / d, i * magic(d) >> 32, magic(d));
        }
    }
    return 0;
}