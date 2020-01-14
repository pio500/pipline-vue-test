FROM nginx:latest as make-image
WORKDIR /app
COPY nginx.config /etc/nginx/conf.d/default.conf
COPY ./dist /usr/share/nginx/html/
RUN echo 'success make image' 
