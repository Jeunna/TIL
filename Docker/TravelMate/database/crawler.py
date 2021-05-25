#!/usr/bin/python3
# from socket import timeout
import bs4 as bs
import requests
import re
from datetime import datetime

URL = 'https://www.koreanair.com/kr/ko/travel-update/covid19'

# 대만, 일본, 중국, 홍콩 / 대한민국 / 말레이시아, 미얀마, 베트남, 싱가포르, 인도네시아, 캄보디아, 태국, 필리핀 / 뉴질랜드, 러시아, 미국, 영국, 유럽 (EU 가입국), 캐나다, 호주
# ex) 'https://www.koreanair.com/kr/ko/travel-update/covid19/travel-restriction/others'
REGION = ['others', 'north-east-asia', 'south-east-asia']
RESTRICTION = 'travel-restriction'
HEADERS = {'User-Agent': 'Mozilla/5.0'}


def get_others_restriction():
    try:
        restrictions = {}
        for region in REGION:
            url = "%s/%s/%s" % (URL, RESTRICTION, region)
            
            sauce = requests.get(url, headers=HEADERS)
            soup = bs.BeautifulSoup(sauce.content,'html.parser')
            soup = soup.find('div', {'class': 'scfe'})

            for data in soup.find_all('div', {'class': 'scfechild _none'}):
                country = data.get('data-first-data')
                print("### %s ###" % (country))
                restrictions[country] = get_all_rules(soup, country)
        
        return restrictions

    #TODO: exception별로 처리하기
    except Error as e:
        print(e)
    # except (HTTPError, URLError) as error:
    #     logging.error('Data of %s not retrieved because %s\nURL: %s', name, error, url)
    # except timeout:
    #     logging.error('socket timed out - URL %s', url)
    # else:
    #     logging.info('Access successful.')


def get_korea_restriction():
    try:
        url = "%s/%s/%s" % (URL, RESTRICTION, 'korea')
        
        sauce = requests.get(url, headers=HEADERS)
        soup = bs.BeautifulSoup(sauce.content,'html.parser')
        soup = soup.find('div', {'class': 'aem-Grid aem-Grid--12 aem-Grid--default--12'})

        return get_all_rules(soup, "대한민국")

    except Error as e:
        print(e)


def check_title(before, string):
    try:
        title = string.text
    except AttributeError as e:
        # print(" -> Title was not found")
        return before
    else:
        if title == None:
            print("Title: None")
            return before
        else:
            return title


def find_rule(title, tag, string):
    try:
        content = string.find_all(tag)
    except AttributeError as e:
        print("[%s] There's no rule" % (title))
    else:
        if not content:
            # print("[%s] <%s> tag: None" % (title, tag))
            pass
        else:
            return [c.text.replace('  ', '').replace('\n','') for c in content]


def get_rules(title, strings):
    content = []
    p_rule = find_rule(title, 'p', strings)
    li_rule = find_rule(title, 'li', strings)

    if p_rule is not None:
        content.append(p_rule)
    if li_rule is not None:
        content.append(li_rule)

    return content


def get_all_rules(soup, country):
    try:
        entry_rule = []
        check_covid = []
        isolation_rule = []
        transfer_rule = []
        title = ''

        if country != "대한민국":
            soup = soup.find('div', {'data-first-data': country})

        for child in soup.find_all('div', {'class': 'ctal'}):
            title = check_title(title, child.find('h2'))
            print(title)

            content = get_rules(title, child)
            print(content)

            if content == None:
                continue
            
            if title == "입국 규정":
                entry_rule.append(content)
            elif title == "검역 규정":
                check_covid.append(content)
            elif title == "격리 규정":
                isolation_rule.append(content)
            elif title == "환승 규정":
                transfer_rule.append(content)

        return entry_rule, check_covid, isolation_rule, transfer_rule
    
    except Error as e:
        print(e)


def get_all_restriction():
    restriction = get_others_restriction()
    restriction['대한민국'] = get_korea_restriction()

    return restriction


def check_updates(soup):
    try:
        date = soup.find('span', {'class': 'latest__heading-date'}).text
        date = date.replace('  ', '').replace('\n', '').replace('\t', '')
        date = re.findall('\d+', date)

        today = datetime.today()
        today = [t for t in today.strftime('%Y %m %d').split()]

        return today == date

    except Error as e:
        print(e)

    
def get_update_restriction(soup):
    update = {}
    for data in soup.find_all('li', {'class': 'latest__article-item'}):
        restriction = data.text.split(' : ')
        update[restriction[0]] = restriction[1].replace('\r', '')

    return update


# check the date and  update the restriction
def cron_update_restriction(flag):
    try:
        sauce = requests.get(URL, headers=HEADERS)
        soup = bs.BeautifulSoup(sauce.content,'html.parser')

        if check_updates(soup) or flag:
            return get_update_restriction(soup)

    except Error as e:
        print(E)



#TODO: 마지막 날짜를 저장해서 확인하고 업데이트 됐으면 다시 가져오기


# # 순서; 입국 규정, 검역 규정, 격리 규정, 환승 규정