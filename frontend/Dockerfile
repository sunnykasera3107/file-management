FROM node:26-alpine3.22

WORKDIR /home/app

COPY package*.json ./

RUN npm install

COPY . .

CMD [ "npm", "run", "dev" ]