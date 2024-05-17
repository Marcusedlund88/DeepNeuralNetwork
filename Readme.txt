Deep Neural Network. Base for future implementations.

Create image from Powershell:
# docker build -t <image-name> 

Create container from Powershell:
# docker run -d --name <container-name> --network <network-name> <image-name> .

Make mongoDB using docker:
# docker run -d `
>>     --name mongo `
>>     --restart unless-stopped `
>>     -p 27017:27017 `
>>     -e MONGO_INITDB_DATABASE=mydatabase `
>>     --network mynetwork `
>>     mongo

In terminal use:
- mongosh
- use <db name>
- db.createCollection("mycollection")
- show collections

Use docker-compose.yml:
# docker compose up --build -d

