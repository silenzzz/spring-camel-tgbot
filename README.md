# Telegram Bot With Apache Camel On Spring Boot Usage Example

# TODO

* Configure `logback.xml`
* More logging
* Change commit author to silenzzz
* Separate sample bot uri's into different classes (for ex. `direct:messageHandler` > `MessageHandlerProcessor`, `direct:unsupportedOperationHandler` > `UnsupportedOperationHandlerProcessor`)
* More unit tests
* Camel route integration tests (`MockEndpoint`)
* Tests with test containers
* Configure dependencyManagement in pom
* Result value class for handle method in command handlers?
* Extract string commands values in package and add them to the command registry?
* `BaseCommandHandler` shouldn't know anything about `UnknownCommandHandler`. Better extract into field and add to constructor params
* More `CommandHandler`'s examples, with different functionality (for ex. something that class any remote api)
* Configure maven profiles
* Add command priority in `setNext()` method
* Write more comments in code
* Refactor tests
