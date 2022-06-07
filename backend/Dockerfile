FROM python:3.8-alpine
ENV PYTHONUNBUFFERED=1
WORKDIR /code
COPY requirements.txt /code/
RUN apk update && apk add --no-cache python3-dev && pip3 install -r requirements.txt
RUN apk add --update npm
RUN npm install -g pg-migrator

COPY . /code/
