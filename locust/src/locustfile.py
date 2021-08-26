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

    # TODO: Open '/app' page (MVC)
    # def ...

    # TODO: Retrieve user session via REST api
    # def ...

    # TODO: Perform 'login' request via REST api
    # def ...

    # TODO: Apply weighting upon 'retrieve_events'
    @task
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

    # TODO: Perform 'upsert' mutation of event via GraphQL
    # def ...

class ToDoListLocust(HttpUser):
    tasks = [ToDoListTaskSet]
    min_wait = 1000
    max_wait = 5000
