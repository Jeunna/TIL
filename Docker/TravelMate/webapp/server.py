from flask import Flask, render_template
import pymysql
import folium
from folium import plugins
import netifaces

app = Flask(__name__)

'''
* get_gateway_address()는 LocalHost gateway 정보를 반환
* @ https://pypi.org/project/netifaces/
* @ ex) 작성자의 ip 주소인 '192.168.0.10'을 반환
'''
def get_gateway_address():
    return netifaces.gateways()['default'][2][0]


# DB 기본 설정
HOST = get_gateway_address() #'192.168.0.10'
# HOST = 'localhost'
PORT = 3306
USER = "username"
PASSWD = "dbpass"
CHARSET = "utf8"
db_name = 'travel'
table_name = ['countries_location', 'status']


'''
* Database로 접근하여 status table의 모든 정보를 가져옴
* @ pymysql 모듈을 사용해 DB로 접근하고 ('입국규정', '검역규정','격리규정','환승규정')값을 return함
* @ cursor 객체의 execute() 메서드를 사용하여 작성한 코드를 DB서버에 보냄
'''
def get_datas(country:str) -> str:
    with pymysql.connect(
        host =      HOST,
        port =      PORT,
        user =      USER,
        password =  PASSWD, 
        charset =   CHARSET,
        database =  db_name
    ) as connection:
        with connection.cursor() as cursor:
            cursor.execute("select * from status where country='{}'".format(country))
            entry = list(cursor.fetchall())
            return entry[0][2:]


'''
* Database로 접근하여 countries_location table의 모든 정보를 가져옴
* @ pymysql 모듈을 사용해 DB로 접근하고 경도, 위도 값을 return함
* @ cursor 객체의 execute() 메서드를 사용하여 작성한 코드를 DB서버에 보냄
'''
def get_datas_country():
    with pymysql.connect(
        host =      HOST,
        port =      PORT,
        user =      USER,
        password =  PASSWD, 
        charset =   CHARSET,
        database =  db_name
    ) as connection:
        with connection.cursor() as cursor:
            cursor.execute("select * from countries_location")
            entry = list(cursor.fetchall())
            return entry            




# 웹 페이지의 첫 부분
@app.route('/')
def main():
    entry = get_datas_country()
    folium_map = folium.Map(
        max_bounds= True,
        min_zoom= 2,
        min_lat= -84,
        max_lat= 84,
        min_lon= -175,
        max_lon= 187,
        height= 600,
        width= 1200

    )

    for i in range(0,len(entry)):
        country = entry[i][1]

        latitude = entry[i][2]
        latitude = latitude.replace(" ",".")
        latitude = latitude[0: -2]

        longitude = entry[i][3]
        longitude = longitude.replace(" ",".")
        longitude = longitude[0: -2]

        # html = '<a href="http://'+HOST+':8282/'+country+'" target="_self">'+country+'</a>'
        html = '<a href="http://localhost:8282/'+country+'" target="_self">'+country+'</a>'

        folium.Marker(
            location=[int(float(latitude)),int(float(longitude))],
            tooltip= country,
            popup=html,
            icon=folium.Icon(color='blue', icon='star')
        ).add_to(folium_map)        

    return render_template('map.html', mymap=folium_map._repr_html_())


# 나라 이름을 클릭 시 다른 웹 페이지로 넘어가 DB에 있는 데이터를 출력
@app.route('/<country>')
def about(country):
    print('접속함')
    tmp = [country]
    tmp.append(['입국규정', '검역규정','격리규정','환승규정'])
    tmp.append(get_datas(country))
    return render_template('test.html', tmp = tmp)


app.run(host='0.0.0.0', port=8282, debug=True)