FROM node:12-alpine AS BUILD_FRONTEND_CONTAINER
WORKDIR /src/app
ADD . /src/app
RUN npm install && npm run build || exit 1

FROM node:12-alpine
WORKDIR /app
COPY --from=BUILD_FRONTEND_CONTAINER /src/app/build /www
ADD ./node-server /app
RUN npm install
ENTRYPOINT ["node", "app.js"]