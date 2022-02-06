# MessageBoard

This is my work on task given in file "Backend Developer - Task Assignment - V2.0.pdf".  
The project requires Java 14 or greater.  

Main entry point - class MainWebEntry; when starting from IDE, the app starts listening on address http://localhost:8000

1. The following endpoints are implemented:  
1.1. New user registration:  
```yaml
POST  
/api/users/register
{  
    "username": "string"  
}
Response:
if success: status 200;
if error: status 400; error message in body 
```
1.2. Login:
```yaml
POST  
/api/users/login
{  
    "username": "string"  
}
Response:
if success: status 200; user id in body
{
    "id": integer
}
if error: status 400; error message in body 
```
1.3. Post message:
```yaml
POST  
/api/messages/post
{  
    "text": "string"  
}
Additional header:
  X-USER-ID: user-id
Response:
if success: status 200;
if error: status 400; error message in body 
```
1.4. Edit message:
```yaml
POST  
/api/messages/edit
{  
    "id": integer,
    "text": "string"  
}
Additional header:
  X-USER-ID: user-id
Response:
if success: status 200;
if error: status 400; error message in body 
```
1.5. Vote for message:
```yaml
POST  
/api/messages/vote
{  
    "id": integer,
    "plus": boolean # set true to upvote; false to down-vote  
}
Additional header:
  X-USER-ID: user-id
Response:
if success: status 200;
if error: status 400; error message in body 
```
1.6. Get top messages:
```yaml
POST  
/api/messages/top-messages
{  
    "count": integer # desired number of top messages
}
Response:
if success: status 200; body
[
  {
    "userName": "string",
    "date": "dd/MM/yyyy HH:mm:ss",
    "messageText": "string",
    "votes": integer
  }
]
if error: status 400; error message in body 
```

2. Implemented unit tests for all repositories (data layer) and for all services (service layer).  
Used "qa" database for testing; before tests all data in that DB is deleted.

3. Used SQLite database for data storage; DB files are in project root:  
MessageBoardQa.s3db - for QA;  
MessageBoardProd.s3db - for Production  
DB names and switching between QA and Production environment is configured in app.config file in project root.

4. The following dependencies are used:  
4.1. jackson - for Json serialization/deserialization;  
4.2. lombok - for simplifying POJO classes creation;  
4.3. sqlite - for SQLite driver;  
4.4. junit - for unit tests.

5. Some benchmarks on my local PC (used simple C# program to send requests):  
5.1. Adding 100000 messages, one by one: ~500s.  
5.2. With 100000 messages in DB, querying top [Random 1..200] messages 1000 times: ~3s.
