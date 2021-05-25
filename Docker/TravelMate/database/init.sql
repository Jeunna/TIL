create table status (
                    id              int auto_increment primary key, 
                    country         varchar(100),
                    entry_rule      varchar(2000),
                    check_covid     varchar(2000),
                    isolation_rule  varchar(2000),
                    transfer_rule   varchar(2000)
                );
create table countries_location (
                    id          int auto_increment primary key, 
                    country     varchar(100),
                    latitude    varchar(20),
                    longitude   varchar(20)
                );

                