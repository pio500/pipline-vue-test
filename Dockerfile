FROM node:lts-alpine
RUN npm install -g http-server
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN ls -al
EXPOSE 80
CMD [ "npm", "run", "serve"]