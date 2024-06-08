import json
import boto3

client = boto3.client('cognito-idp')

def lambda_handler(event, context):
    
    userId = event['userid']
    password = event['password']
    user_pool_id = "us-east-1_PXVZIfM1O"
    client_id = "7p7pmaukn1nkftaeou8jf565p5"
    
    
    try:
        response = client.initiate_auth(
            AuthFlow='USER_PASSWORD_AUTH',
            AuthParameters={
                'USERNAME': userId,
                'PASSWORD': password
            },
            ClientId=client_id
        )
        
        return {
            'statusCode': 200,
            'body': json.dumps({'access_token': response['AuthenticationResult']['AccessToken']})
        }
        
    except Exception as e:
        return {
            'statusCode': 500,
            'body': json.dumps({'message': str(e)})
        }
