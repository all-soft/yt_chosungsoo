//
// Created by 조남두 on 2017. 9. 25..
//

#include <stdlib.h>
#include "libkey.h"

static char *aeskey = NULL;
char* aes_key()
{
    if (aeskey == NULL) {
        aeskey = new char[24];
        aeskey[0] = '2';
        aeskey[1] = 'p';
        aeskey[2] = 'b';
        aeskey[3] = 'g';
        aeskey[4] = '3';
        aeskey[5] = '7';
        aeskey[6] = 'z';
        aeskey[7] = '3';
        aeskey[8] = '/';
        aeskey[9] = 'C';
        aeskey[10] = '7';
        aeskey[11] = 'z';
        aeskey[12] = 'K';
        aeskey[13] = 's';
        aeskey[14] = 'b';
        aeskey[15] = 'h';
        aeskey[16] = 'B';
        aeskey[17] = 'N';
        aeskey[18] = 'P';
        aeskey[19] = '+';
        aeskey[20] = 'A';
        aeskey[21] = 'g';
        aeskey[22] = '=';
        aeskey[23] = '=';
        aeskey[24] = '\0';
    }
    return aeskey;
}

static char *appkey = NULL;
char* app_key()
{
    if (appkey == NULL) {
        appkey = new char[14];
        appkey[0] = 'a';
        appkey[1] = 'p';
        appkey[2] = 'p';
        appkey[3] = 'l';
        appkey[4] = 'i';
        appkey[5] = 'c';
        appkey[6] = 'a';
        appkey[7] = 't';
        appkey[8] = 'i';
        appkey[9] = 'o';
        appkey[10] = 'n';
        appkey[11] = 'k';
        appkey[12] = 'e';
        appkey[13] = 'y';
        appkey[14] = '\0';
    }
    return appkey;
}

static char *appValue = NULL;
char* app_value()
{
    if (appValue == NULL) {
        appValue = new char[11];
        appValue[0] = 'a';
        appValue[1] = 'l';
        appValue[2] = 'l';
        appValue[3] = 's';
        appValue[4] = 'o';
        appValue[5] = 'f';
        appValue[6] = 't';
        appValue[7] = '2';
        appValue[8] = '0';
        appValue[9] = '1';
        appValue[10] = '8';
        appValue[11] = '\0';
    }
    return appValue;
}