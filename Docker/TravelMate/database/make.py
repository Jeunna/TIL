import pymysql
import sql as query
import info

# create Database
def _db():
    with pymysql.connect(
        host =      info.HOST,
        port =      info.PORT,
        user =      info.USER,
        password =  info.PASSWD, 
        charset =   info.CHARSET,
        autocommit = True   
    ) as connection:

        with connection.cursor() as cursor:
            
            # check database
            check_db = query._check('database')
            cursor.execute(check_db)
            databases = cursor.fetchall()

            # if exist -> not make table
            for database in databases:
                if info.DB_NAME in database:
                    return 

            # if not exist -> make table
            create_db = query._create_database(info.DB_NAME)
            cursor.execute(create_db)

            print('make DATABASE! {}'.format(info.DB_NAME))


# create table
def _table():
    with pymysql.connect(
        host =      info.HOST,
        port =      info.PORT,
        user =      info.USER,
        password =  info.PASSWD, 
        charset =   info.CHARSET,
        database =  info.DB_NAME,
        autocommit = True   
    ) as connection:

        with connection.cursor() as cursor:

            # check table
            check_table = query._check('table')
            cursor.execute(check_table)
            tables = cursor.fetchall()

            # if exist -> break
            for name in info.TABLE_NAME:
                for table in tables:
                    if name in table:
                        
                        print("WE HAVE '{}' TABLE".format(name))
                        break
            
            # if not exist -> make table
                else:
                    cursor.execute(query._create_table(name))
                    
                    print("MAKE '{}' TABLE".format(name))