# ==============  CHECK  ============== #

# 입력받은 table 혹은 database가 있는지 확인.
# input :  db (database || table)
def _check(db: str) -> str:
    if db.lower() == 'table' or db.lower() == 'tables':  
        return "SHOW {}".format('TABLES')
    
    if db.lower() == 'database' or db.lower() == 'databases':  
        return "SHOW {}".format('DATABASES')

def _check_data() -> str: 
        return "select * from {} where country LIKE '{}'"


# ==============  CREATE  ============== #

def _create_database(db_name:str) -> str:
    return "CREATE DATABASE {}".format(db_name)

# input :  table_name (countries_location || status)
def _create_table(table_name: str) -> str:
    if table_name == 'countries_location':
        sql = """
                create table countries_location (
                    id          int auto_increment primary key, 
                    country     varchar(100),
                    latitude    varchar(20),
                    longitude   varchar(20)
                )"""
    elif table_name == 'status':
        sql = """
                create table status (
                    id              int auto_increment primary key, 
                    country         varchar(100),
                    entry_rule      varchar(2000),
                    check_covid     varchar(2000),
                    isolation_rule  varchar(2000),
                    transfer_rule   varchar(2000)
                );"""
    return sql



# ==============  DELETE  ============== #

def _delete_database(db_name:str) -> str:
    return "drop database {}".format(db_name)

# input :  table_name (country || status)
def _delete_table(table_name: str) -> str:
    if table_name == 'countries_location':
        sql = "TRUNCATE countries_location;"
    else:
        sql = "TRUNCATE status;"

    return sql

# input :  table_name (countries_location || status), city_name
def _delete_data(table_name: str, city_name: str) -> str:
    sql = "DELETE from {} where country='{}';".format(table_name, city_name)
    return sql



# ==============  INSERT  ============== #

def _insert_data(table_name: str) -> str:
    sql = ''
    if table_name == 'countries_location':
        sql = """insert into countries_location 
                    (country, latitude, longitude) 
                    values(%s, %s, %s)"""
    elif table_name == 'status':
        sql = """insert into status 
                    (country, entry_rule, check_covid, isolation_rule, transfer_rule) 
                    values(%s, %s, %s, %s, %s)"""
    
    return sql



# ==============  UPDATE  ============== #
