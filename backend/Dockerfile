FROM python:3.8-alpine
ENV PYTHONUNBUFFERED=1
WORKDIR /code
COPY requirements.txt /code/
RUN apk update && apk add alpine-sdk gcc musl-dev python3-dev libffi-dev openssl-dev && apk add postgresql-dev gcc python3-dev musl-dev && apk add jpeg-dev zlib-dev libjpeg
RUN apk add --no-cache bash
RUN pip install -r requirements.txt
RUN apk add --update nodejs npm
RUN npm install -g pg-migrator

COPY . /code/
