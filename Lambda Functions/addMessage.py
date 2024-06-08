import json
import boto3



client = boto3.resource('dynamodb')


def lambda_handler(event, context):
    username = event['username']
    timestamp = event['timestamp']
    sender = event['sender']
    text = event['text']
    
    dynamodbtable = client.Table('sms_data')
    
    resp = dynamodbtable.put_item(Item ={'user': username, 'timestamp' : timestamp, 'sender' : sender,'text' : text})
    return {
        'statusCode': 200,
        'body': json.dumps(resp)
    }
