db.createCollection("CaseFive");
db.createCollection("CaseTen");

const fs = require('fs');
const { MongoClient } = require('mongodb');

async function main() {
    const uri = 'mongodb://localhost:27017';
    const client = new MongoClient(uri, { useNewUrlParser: true, useUnifiedTopology: true });

    try {
        await client.connect();
        const db = client.db('mydatabase');

        const caseTenData = JSON.parse(fs.readFileSync('/docker-entrypoint-initdb.d/mydatabase.CaseTen.json'));

        await db.collection('CaseFive').insertMany(caseFiveData);
        await db.collection('CaseTen').insertMany(caseTenData);

        console.log('Data import completed successfully.');
    } catch (err) {
        console.error('Error importing data:', err);
    } finally {
        await client.close();
    }
}

main().catch(console.error);
