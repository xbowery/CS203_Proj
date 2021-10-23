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

EXPOSE 3001

CMD npm run prod