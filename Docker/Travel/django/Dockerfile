FROM python:3
ENV PYTHONUNBUFFERED=1
WORKDIR /service
COPY requirements.txt /service/
RUN pip install -r requirements.txt
RUN pip install mysqlclient
COPY crawler.py /service/