# Authentication Service
Handles the authentication. The following users are valid:
- user-1
- user-2
- user-3
- user-4
- user-5
- user-6
- user-7
- user-8
- user-9
- user-10

Password for all users is `pass`.

More details can be found in the [REST API documentation](http://eplay-auth.markusjura.cloudbees.net)

#### How to start
The service is hosted on [http://eplay-auth.markusjura.cloudbees.net](http://eplay-auth.markusjura.cloudbees.net). You can use your local
`authService` by:

1. Changing the `service.auth.url` in the *eplay application.conf* to your URL
    ```
    service.auth.url="http://localhost:9001"
    ```

2. Starting the local `authService`
    ```
    $ cd authService
    $ activator
    > start 9001
    ```