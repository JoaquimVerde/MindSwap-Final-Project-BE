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
    --table-name course \
    --attribute-definitions \
        AttributeName=PK,AttributeType=S \
    --key-schema \
        AttributeName=PK,KeyType=HASH \
    --provisioned-throughput \
        ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --table-class STANDARD


aws dynamodb create-table \
--endpoint-url http://localhost:8000 \
--table-name course \
--attribute-definitions \
AttributeName=PK,AttributeType=S \
AttributeName=SK,AttributeType=S \
--key-schema \
AttributeName=PK,KeyType=HASH \
AttributeName=SK,KeyType=RANGE \
--billing-mode PAY_PER_REQUEST \
--tags Key=Name,Value=YourTableName \
--global-secondary-indexes \
"IndexName=GSIPK,KeySchema=[{AttributeName=Location,KeyType=HASH}],Projection={ProjectionType=ALL},ProvisionedThroughput={ReadCapacityUnits=1,WriteCapacityUnits=1}"
