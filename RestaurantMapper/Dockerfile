# syntax=docker/dockerfile:1
FROM node:14-alpine

# Create app directory and epgpub directory
RUN mkdir /app
WORKDIR /app

# Install app dependencies
COPY ["package.json", "package-lock.json*", "./"]
RUN npm install --production

#Bundle app source
COPY . .

EXPOSE 80

CMD npm run prod