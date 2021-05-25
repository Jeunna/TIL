import pymysql
import sql as query
import info

# delete db (편의를 위해 만들었음)
def _db(delete_name: str):
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

            # 'delete_name'을 가진 database가 있으면 삭제
            cursor.execute(query._check('database'))
            db_names = cursor.fetchall()

            for i in db_names:
                if i[0].startswith(delete_name):
                    cursor.execute(query._delete_database(i[0]))
                    
                    print('delete {} database'.format(i[0]))

# delete table
def _table():
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

            # delete 하면 id값이 증가되어서 truncate 해줌
            cursor.execute(query._delete_table('status'))

            print('TRUNCATE {}'.format('status'))