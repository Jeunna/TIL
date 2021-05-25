import pymysql
import sql as query
from get_data import _get_data
import info


# (1) flag  0     -> insert (countries_location, status)
# (2) flag  else  -> insert (status)
def _data(flag: int):
    with pymysql.connect(
        host =      info.HOST,
        port =      info.PORT,
        user =      info.USER,
        password =  info.PASSWD, 
        charset =   info.CHARSET,
        database =  info.DB_NAME,
        autocommit =True   
    ) as connection:
    
        with connection.cursor() as cursor:

            # insert (countries_location, status)
            if flag == 0:
                for name in info.TABLE_NAME:
                    insert_query = query._insert_data(name)
                    datas = _get_data(name)
                    cursor.executemany(insert_query, datas)

                    print("INSERT '{}' DATA".format(name))

            # insert (status)
            else:
                name = info.TABLE_NAME[1]

                insert_query = query._insert_data(name)
                datas = _get_data(name)
                cursor.executemany(insert_query, datas)

                print("INSERT '{}' DATA".format(name))

