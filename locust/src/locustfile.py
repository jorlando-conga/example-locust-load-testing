from locust import HttpUser, TaskSet, task
import json
from util import RandomUtil

class ToDoListTaskSet(TaskSet):

    def on_start(self):
        print("Performing signup")
        headers = {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
        signupPayload = {
            'firstName': 'TestFirstName',
            'lastName': 'TestLastName',
            'emailAddress': "{}@example.com".format(RandomUtil.randomString(size=10)),
            'password': 'testpassword'
        }
        print("Performing signup with E-Mail Address: {}".format(signupPayload['emailAddress']))
        self.client.post("/api/rest/v1/signup", name="User Signup", headers=headers, data=json.dumps(signupPayload))

    @task
    def open_todays_events(self):
        self.client.get('/app', name='MVC - List Today\'s Events')

    @task
    def retrieve_user(self):
        headers = {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
        self.client.get('/api/rest/v1/user', name='REST - Retrieve User', headers=headers)

    @task
    def do_login(self):
        headers = {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
        with self.client.get('/api/rest/v1/user', name='REST - Retrieve User', headers=headers, catch_response=True) as userResponse:
            if userResponse.status_code != 200:
                userResponse.failure("Unexpected Status Code: {}".format(userResponse.status_code))
                return

            userResponseBodyStr = userResponse.content.decode('utf-8')
            userResponseBody = json.loads(userResponseBodyStr)
            if "emailAddress" not in userResponseBody:
                userResponse.failure("E-Mail Address was not found in response body")
                return
            userResponse.success()

            login_payload = {
                'emailAddress': userResponseBody['emailAddress'],
                'password': 'testpassword'
            }
            self.client.post('/api/rest/v1/login', data = json.dumps(login_payload), headers=headers, name='REST - Do Login')

    @task(3)
    def retrieve_events(self):
        query = '''
        query fetchListEvents($endDate: String!) {\n  fetchListEvents(endDate: $endDate) {\n    id\n    description\n    dueDate\n    zipCode\n    completed\n    __typename\n  }\n}\n
        '''
        request = {
            'operationName': 'fetchListEvents',
            'query': query,
            'variables': {
                'endDate': '08/27/2021'
            }
        }
        self.client.post('/api/data/v1', name='GraphQL - Fetch List Events', data=json.dumps(request))

    @task
    def create_event(self):
        query = '''
        mutation upsertListEvent($eventId: ID, $event: UpsertListEvent!) {\n  upsertListEvent(eventId: $eventId, event: $event)\n}\n
        '''
        request = {
            'operationName': 'upsertListEvent',
            'query': query,
            'variables': {
                'event': {
                    'description': 'test event',
                    'dueDate': '08/10/2022',
                    'zipCode': '32713'
                }
            }
        }
        self.client.post('/api/data/v1', name='GraphQL - Create Event', data=json.dumps(request))

class ToDoListLocust(HttpUser):
    tasks = [ToDoListTaskSet]
    min_wait = 1000
    max_wait = 5000
