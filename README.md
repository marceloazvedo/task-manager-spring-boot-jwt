# task-manager-spring-boot-jwt

Para esse projeto você vai precisar de:
- Java 17 para buildar e executar o projeto;
- Docker e Docker Compose instalados na máquina.

Antes de startar o projeto vá até a pasta compose 
e execute o comando para inicializar o [docker-compose.yml](./compose/docker-compose.yml):
```shell
docker compose up -d
```
Caso não consiga criar as tabelas do Dynamo, execute novamente o container do
aws-cli usando o seguinte comando:
```shell
docker compose aws-cli up -d
```
Assim você garante que com o localstack rodando o aws-cli vai conseguir criar
as tabelas que você necessita para a aplicação funcionar.

Para rodar a aplicação lembre-se sempre de apontar para o ambiente local!
Para isso, execute a task gradle para limpar a pasta de build e depois realize
o build da aplicação com o comando abaixo:
```shell
./gradlew clean build
```

Após ter o projeto buildado em sua última versão execute o seguinte comando para
gerar o .jar que vamos iniciar o projeto:
```shell
./gradlew bootJar
```
E agora para finalizar de executar o projeto rode o comando:
```shell
java -Dspring.profiles.active=local -jar build/libs/spring-boot-jwt-0.0.1-SNAPSHOT.jar
```
Nesse projeto também temos o [Dockerfile](./Dockerfile) que pode gerar uma
imagem do projeto e executada, assim você pode subir containers com essa
aplicação!

Para isso você pode buildar a imagem do projeto usando o seguinte comando:
````shell
docker build -t spring-boot-jwt .
````

Isso irá gerar uma imagem do projeto que pode ser executada a qualquer momento
pelo docker.
E para rodar essa imagem execute o seguinte comando:
````shell
docker run -p 8080:8080 spring-boot-jwt:latest
````
O argumento `-p 8080:8080` vai expor a porta 8080 no seu localhost e assim você
poderá realizar requisições direto para a máquina.