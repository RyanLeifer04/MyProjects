# Frontend is built using NodeJS
FROM node:20.9.0 as frontend-build

# Docker Compose arguments
ARG BACKEND_API_URL
ARG BACKEND_API_PORT

#Explicitly set working directory in new docker container
WORKDIR /app
#Copy the frontend folder into the working directory
# COPY . /app/
COPY ./src /app/src
COPY ./.editorconfig /app/.editorconfig
COPY ./angular.json /app/angular.json
COPY ./default.conf /app/default.conf
COPY ./package-lock.json /app/package-lock.json
COPY ./package.json /app/package.json
COPY ./replace_envriotment_variables.js /app/replace_envriotment_variables.js
COPY ./tsconfig.app.json /app/tsconfig.app.json
COPY ./tsconfig.json /app/tsconfig.json
COPY ./tsconfig.spec.json /app/tsconfig.spec.json

#Modify environment file with set values
RUN sed -i "s/localhost/$BACKEND_API_URL/g" /app/src/proxy.conf.mjs
RUN sed -i "s/8080/$BACKEND_API_PORT/g" /app/src/proxy.conf.mjs

#Install all required NodeJS packages
RUN npm install --ignore-scripts
#Build NodeJS package (executes ng build)
RUN npm run build #-- --configuration=docker

#Use Nginx as web server
FROM nginx:1.25.3 as frontend
#Create and set non-root user
RUN addgroup -S app && adduser -S app -G app
USER app
#Copy the static files built by NodeJS into the Nginx server's proper location
COPY --from=frontend-build /app/dist/ufund-ui /usr/share/nginx/html
#Proxy out relevant parts of the URL
COPY ./default.conf /etc/nginx/conf.d/default.conf
ARG BACKEND_API_URL
ARG BACKEND_API_PORT
RUN sed -i "s/localhost:/$BACKEND_API_URL:/g" /etc/nginx/conf.d/default.conf
RUN sed -i "s/8080/$BACKEND_API_PORT/g" /etc/nginx/conf.d/default.conf
RUN cat /etc/nginx/conf.d/default.conf
# Expose the HTTP port
EXPOSE 80
