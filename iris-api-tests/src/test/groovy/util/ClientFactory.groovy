package util

import groovyx.net.http.RESTClient

class ClientFactory {

    static final ENDPOINT = System.getProperty("endpoint") == null ? "http://localhost:8082" : System.getProperty("endpoint")

    static def newAdminClient() {
        def client = newUnauthorizedClient()

        // prepare for preemptive authentication
        client.headers['Authorization'] = 'Basic ' + "admin@example.com:admin".getBytes('UTF-8').encodeBase64()
        return client
    }

    static def newUserClient() {
        def client = newUnauthorizedClient()
        client.headers['Authorization'] = 'Basic ' + "user@example.com:user".getBytes('UTF-8').encodeBase64()
        return client
    }

    static def newUnauthorizedClient() {
        return new RESTClient(ENDPOINT)
    }
}