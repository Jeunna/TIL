#!/usr/bin/python3
import sys
sys.path.append('/crawler')

import pymysql
import os
import sql as query
from crawler import get_all_restriction

os.chdir(os.path.dirname(os.path.realpath(__file__)) )


HOST = '192.168.0.29'
PORT = 3308
USER = "root"
PASSWD = "rootpass"
CHARSET = "utf8"
db_name = 'travel'
table_name = ['countries_location', 'status']


def for_for(data_list: list) -> str:
    line = ''

    for data1 in data_list:
        for data2 in data1:
            for data3 in data2:
                line += data3 + '\n'

    return line


def get_data(data_name:str) -> tuple:
    names = ['뉴질랜드', '러시아', '미국', '영국', '우즈베키스탄', '유럽 (EU 가입국)', 
            '캐나다', '호주', '대만', '일본', '중국', '홍콩', '말레이시아', '미얀마', 
            '베트남', '싱가포르', '인도네시아', '캄보디아', '태국', '필리핀', '대한민국']
    europes = ['그리스','네덜란드','덴마크','독일','라트비아','루마니아','룩셈부르크',
            '리투아니아','몰타','벨기에','불가리아','스웨덴','스페인','슬로바키아','슬로베니아',
            '아일랜드','에스토니아','오스트리아','이탈리아','체코','크로아티아','키프로스',
            '포르투갈','폴란드','프랑스','핀란드','헝가리']
    result = []

    if data_name == 'countries_location':
        with open('countries_location.csv', 'r', encoding='utf8') as input_data:
            datas = input_data.readlines()[1:]
            for i in datas:
                result.append(tuple(i.strip().split(',')))

    elif data_name == 'status':
        # data = 크롤링 해서 받아오는 list (4개)
        datas = get_all_restriction()

        for name in names:
            if name == '유럽 (EU 가입국)':
                europe_data = []

                for i in datas[name]:
                    europe_data.append(for_for(i))

                for europe in europes:
                    tmp = [europe]
                    tmp.extend(europe_data)
                    result.append(tuple(tmp))

            else:
                tmp = [name]

                for i in datas[name]:
                    tmp.append(for_for(i))
            
                result.append(tuple(tmp))
    
    print("GET '{}' DATA".format(data_name))

    return result


def del_db(delete_name: str):
    with pymysql.connect(
        host =      HOST,
        port =      PORT,
        user =      USER,
        password =  PASSWD, 
        charset =   CHARSET,
        database =  db_name
    ) as connection:
        with connection.cursor() as cursor:
            cursor.execute(query._check('database'))
            db_names = cursor.fetchall()
            for i in db_names:
                if i[0].startswith(delete_name):
                    cursor.execute(query._delete_database(i[0]))
                    print('delete {} database'.format(i[0]))


def make_db():
    with pymysql.connect(
        host =      HOST,
        port =      PORT,
        user =      USER,
        password =  PASSWD, 
        charset =   CHARSET
    ) as connection:
        with connection.cursor() as cursor:
            
            check_db = query._check('database')
            cursor.execute(check_db)
            databases = cursor.fetchall()

            for database in databases:
                if db_name in database:
                    return 

            create_db = query._create_database(db_name)
            cursor.execute(create_db)
            connection.commit()

            print('make DATABASE! {}'.format(db_name))


def make_table(cursor, table_name):
    # table_name = ['countries_location', 'status']

    #==== create table ====#
    check_table = query._check('table')
    cursor.execute(check_table)
    tables = cursor.fetchall()

    for name in table_name:
        for table in tables:
            if name in table:
                break
        else:
            cursor.execute(query._create_table(name))
            print("MAKE '{}' TABLE".format(name))



def data_init_insert(cursor, table_name):
    # table_name = ['countries_location', 'status']

    #==== insert data ====# 
    for name in table_name:
        insert_query = query._insert_data(name)
        datas = get_data(name)
        cursor.executemany(insert_query, datas)



def initialize_db():
    # initialize_db
    # db 만드는 함수 먼저 넣고 이후에 테이블, insert구문 넣는함수 실행

    # db 생성
    make_db()

    # create table, insert data
    with pymysql.connect(
        host =      HOST,
        port =      PORT,
        user =      USER,
        password =  PASSWD, 
        charset =   CHARSET,
        database =  db_name
    ) as connection:
        with connection.cursor() as cursor:
            # table create
            make_table(cursor, table_name)
            connection.commit()

            # data first insert
            data_init_insert(cursor, table_name)
            connection.commit()



if __name__ =='__main__':

    initialize_db()

    # db 지우고싶을때 사용.
    # del_db( 'project')