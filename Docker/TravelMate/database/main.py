#!/usr/bin/python3
import sys
sys.path.append('/crawler')

import make
import insert
import update


def init_data():
    # db 생성
    make._db()

    # table create
    make._table()

    # data first insert
    insert._data(0)



def update_data():

    # data update
    update._data()


if __name__ =='__main__':

    # init_data()
    print()
    update_data()
