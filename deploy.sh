#!/bin/bash

# Load environment variables from .env file
if [ -f .env ]; then
  export $(cat .env | xargs)
fi

# Pull the latest Docker image
docker pull $LINKHUB_IMAGE:latest

# Get the current active environment
ACTIVE_ENV=$(docker exec linkhub-be_nginx_1 nginx -T | grep "server web_" | awk -F'_' '{print $2}' | cut -d ':' -f 1)

# Switch between blue and green environments
if [ "$ACTIVE_ENV" == "green" ]; then
  # Stop and remove the current blue environment
  docker-compose stop web_blue
  docker-compose rm -f web_blue

  # Start the blue environment
  docker-compose up -d web_blue

  # Switch traffic to the blue environment
  docker-compose exec -T nginx sh -c "sed 's/web_green/web_blue/' /etc/nginx/nginx.conf > /etc/nginx/nginx.conf.new && \
  cat /etc/nginx/nginx.conf.new > /etc/nginx/nginx.conf && \
  rm /etc/nginx/nginx.conf.new"
  docker-compose exec -T nginx nginx -s reload
else
  # Stop and remove the current green environment
  docker-compose stop web_green
  docker-compose rm -f web_green

  # Start the green environment
  docker-compose up -d web_green

  # Switch traffic to the green environment
  docker-compose exec -T nginx sh -c "sed 's/web_blue/web_green/' /etc/nginx/nginx.conf > /etc/nginx/nginx.conf.new && \
  cat /etc/nginx/nginx.conf.new > /etc/nginx/nginx.conf && \
  rm /etc/nginx/nginx.conf.new"
  docker-compose exec -T nginx nginx -s reload
fi
