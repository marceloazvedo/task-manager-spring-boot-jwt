#!/bin/bash

aws dynamodb --version

echo "########### Creating Table User ###########"

aws dynamodb create-table --endpoint-url http://dynamodb-local:8000 \
	--table-name user \
	--attribute-definitions \
    AttributeName=id,AttributeType=S \
  --key-schema \
    AttributeName=id,KeyType=HASH \
	--provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1

echo "########### Creating Table Task ###########"

aws dynamodb create-table --endpoint-url http://dynamodb-local:8000 \
	--table-name task \
	--attribute-definitions \
    AttributeName=id,AttributeType=S \
  --key-schema \
    AttributeName=id,KeyType=HASH \
	--provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1

echo "########### List Tables ###########"

aws dynamodb list-tables
