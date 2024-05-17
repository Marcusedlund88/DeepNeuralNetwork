Deep Neural Network. Base for future implementations.

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
