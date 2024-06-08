import json
import boto3

client = boto3.client('cognito-idp')

def lambda_handler(event, context):
    
    userId = event['userid']
    password = event['password']
    email = event['email']
    user_pool_id = "us-east-1_PXVZIfM1O"
    client_id = "7p7pmaukn1nkftaeou8jf565p5"
    response = ''
    
    try:
        response = client.sign_up(Username = userId, Password = password, UserAttributes = [{'Name':'email','Value':email}],ClientId=client_id)
        return {
            'statusCode': 200,
            'body': "Success"
        }
        
    except Exception as e:
        # Return error message for any other exception
        return {
            'statusCode': 500,
            'body': json.dumps({'message': str(e), 'response':response})
        }
