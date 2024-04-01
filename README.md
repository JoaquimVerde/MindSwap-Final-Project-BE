# final-project-be

docker run --publish 8000:8000 amazon/dynamodb-local:1.11.477 -jar DynamoDBLocal.jar -inMemory -sharedDb


curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install


aws configure

test-key
test-secret
eu-central-1
json

aws dynamodb create-table \
    --endpoint-url http://localhost:8000 \
    --table-name {NOME DA TABELA} \
    --attribute-definitions \
        AttributeName={NOME DA PK},AttributeType=S \
    --key-schema \
        AttributeName={NOME DA PK},KeyType=HASH \
    --provisioned-throughput \
        ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --table-class STANDARD

