import os
os.chdir(os.path.dirname(os.path.realpath(__file__)) )

from crawler import get_all_restriction
import info


# list로 감싸진 데이터를 문장단위로 나눠주는 부분
def for_for(data_list: list) -> str:
    line = ''

    for data1 in data_list:
        for data2 in data1:
            for data3 in data2:
                line += data3 + '\n'

    return line


def _get_data(data_name:str) -> tuple:
    names = info.COUNTRY_NAME
    europes = info.EUROPE_NAME
    result = []

    # countries_location 데이터를 받아오는 부분.
    # 첫번째에 초기화 해주면 되서 한번만 가져온다.
    if data_name == info.TABLE_NAME[0]:
        with open('countries_location.csv', 'r', encoding='utf8') as input_data:
            datas = input_data.readlines()[1:]

            for i in datas:
                result.append(tuple(i.strip().split(',')))


    # status 데이터를 받아오는 부분.
    elif data_name == info.TABLE_NAME[1]:

        # 크롤링 해서 받아오는 데이터 (dictionary형태 (4개 list)) 
        datas = get_all_restriction()

        for name in names:

            # '유럽 (EU 가입국)이 나오면 모든 EU가입국 데이터 처리.
            # 추가적으로 DB에서 처리해 줄 수 있을 것 같다.
            if name == '유럽 (EU 가입국)':
                europe_data = []

                for i in datas[name]:
                    europe_data.append(for_for(i))

                for europe in europes:
                    tmp = [europe]
                    tmp.extend(europe_data)
                    result.append(tuple(tmp))

            # EU 가입국을 제외한 나머지 국가 데이터 처리
            else:
                tmp = [name]

                for i in datas[name]:
                    tmp.append(for_for(i))
            
                result.append(tuple(tmp))
    

    print("GET '{}' DATA".format(data_name))

    return result