import json
import boto3
from boto3.dynamodb.conditions import Key


client = boto3.resource('dynamodb')


def lambda_handler(event, context):
    username = event['username']
    
    dynamodbtable = client.Table('sms_data')
    key_condition_expression = Key('user').eq(username)
    response = dynamodbtable.query(
    KeyConditionExpression=key_condition_expression
    )
    messages = response['Items']
    return {
        'statusCode': "200",
        'body': json.dumps(messages)
    }
