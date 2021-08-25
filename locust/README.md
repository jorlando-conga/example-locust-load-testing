# Locust Example Tests

For more information, visit: https://locust.io/

### What is locust?

- Locust is an open source load testing tool. 
- Locust 'scripts' are written in python, and define raw HTTP requests.
- Locust ingests these scripts at runtime, and will 'swarm' a pre-defined count of 'users' that will begin sending these requests
at a pre-defined target.
- Locust provides native support for distributed testing, to increase throughput.
  - You can spin-up a 'master', followed by one or more 'workers'.
- Locust provides real-time (and exportable) graphs and statistics.
  - Response time statistics
  - Response size statistics
  - Success/Failure statistics
  - etc.
- Can be run locally (install python package) or via docker.

### What can locust do?

- Submit both simple and complex HTTP requests.
  - You have full control over the headers and body.
- Allows defining of 'expected' responses.
  - Expect an HTTP 201, or HTTP 302 instead of an HTTP 200? No problem.
  - Need to parse the response? No problem.
- Store state in between requests.
  - e.g. session cookies
- Supports organizing groups of tests ('tasks') into 'sets' of test suites.

### What can locust NOT do?

- Render JavaScript and CSS -- it's not a browser!
  - Hence, you cannot 'load test' your UI. **API's only!**
  - It is NOT a 'WebDriver'!

### Things to remember

- Be careful that you aren't 'load testing' someone else's application. Be sure of the scope of your requests.
  - Does your API result in a call-out to a third party? Perhaps don't send millions of request to them.
    - For sake of response-time accuracy, you may consider mocking the call-out (or target service).
      - Or better-yet, **always be asynchronous**.
  - Does your website or API utilize a CDN? Find a way to not send your traffic through the CDN.
  - Be sure of the 'flow' of a request from client to (final destination) server.
    - Are you using a Kubernetes Ingress?
      - Maybe you want to test your app behind the ingress
      - Maybe you don't want to test the app behind the ingress
    - Are you using a Cloud Load Balancer?
      - You're probably fine, but it's best practice to let AWS know when a load test will occur.
        - There may be fine-print legal requirements around this. Though we usually don't generate enough traffic to get in trouble.
- Under the hood, Locust is extending the standard 'python-requests' HTTP Client.
  - If you ever find the Locust API documentation lacking, check out the python-requests.org webpage (link below).
- Try to keep each 'task' limited to a single HTTP request, if possible.
- Writing the tests is the easy part. Maintaining them long-term as the application changes is the hard part.
  - Similar to other codified higher-level testing, such as integration, regression and functional testing.
  - May need to change development workflow to consider how a ticket affects test suite.

### Helpful documentation:

- Structuring locust test projects
  - https://docs.locust.io/en/stable/writing-a-locustfile.html#how-to-structure-your-test-code
- API documentation
  - https://docs.locust.io/en/stable/api.html
- Python-requests HTTP Client
  - https://docs.python-requests.org/en/master/