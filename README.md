# Example of using Apache Camel for Telegram bots in Spring Boot

This project serves as an example of using Apache Camel together with the Spring Boot framework to develop Telegram bots. Apache Camel is a powerful tool for integrating various systems, and Spring Boot simplifies the creation and configuration of Java applications.

## Getting Started

1. **Setting up the Telegram bot:** To get started, you must have a Telegram account and your bot's token. Create it through [@BotFather](https://core.telegram.org/bots#6-botfather) and save the token.

2. **Cloning the project:** Use the `git clone` command to clone this repository to your computer.

3. **Project configuration:**
    - Open the project in your favorite development environment.
    - In the `application.yaml` file, fill in `bot.api-token: ${API_TOKEN}` with your bot's token.

4. **Running:** Launch the application and you can start testing your Telegram bot using the specified endpoints and handlers configured with Apache Camel.

## Resources

- [Apache Camel Documentation](https://camel.apache.org/manual/latest/index.html)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)

## License

This project is licensed under the [MIT License](LICENSE).

---

## TODO

* Configure `logback.xml`
* Separate sample bot uri's into different classes (for ex. `direct:messageHandler` > `MessageHandlerProcessor`, `direct:unsupportedOperationHandler` > `UnsupportedOperationHandlerProcessor`)
* More unit tests
* Camel route integration tests (`MockEndpoint`)
* Tests with test containers
* Configure dependencyManagement in pom
* `BaseCommandHandler` shouldn't know anything about `UnknownCommandHandler`. Better extract into field and add to constructor params
* More `CommandHandler`'s examples, with different functionality (for ex. something that class any remote api)
* Configure maven profiles
* Add command priority in `setNext()` method
* Refactor tests
