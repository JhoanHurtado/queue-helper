# Queue Helper Library

## Introducción

Esta librería está diseñada para facilitar el envío de mensajes a través de diferentes estrategias de mensajería, como RabbitMQ y Kafka. Proporciona una interfaz unificada para enviar mensajes sin depender directamente de la implementación concreta de la estrategia de mensajería.

## Clases Principales

### 1. `QueueHelperApplication`

Esta es la clase principal que inicia la aplicación Spring Boot y configura la estrategia de mensajería.

- **Método **``:
  - Obtiene una conexión a RabbitMQ.
  - Crea una estrategia de mensajería para RabbitMQ.
  - Usa la fachada de mensajería para enviar un mensaje de correo electrónico.

### 2. `MessagingFacade`

Proporciona una interfaz simplificada para enviar mensajes utilizando una estrategia de mensajería específica.

- **Constructor**:
  - `MessagingFacade(MessagingStrategy strategy)`: Inicializa la fachada con una estrategia de mensajería.
- **Método **``:
  - `void send(String queueOrTopic, MessageModel message, int priority, int deliveryMode)`: Envía un mensaje a una cola o tópico utilizando la estrategia definida.

### 3. `RabbitMQStrategy`

Implementa la estrategia de mensajería utilizando RabbitMQ.

- **Constructor**:
  - `RabbitMQStrategy(Connection connection)`: Inicializa la estrategia con una conexión a RabbitMQ.
- **Método **``:
  - `void sendMessage(String queue, MessageModel message, int priority, int deliveryMode)`: Envía un mensaje a una cola específica en RabbitMQ.

### 4. `MessagingConnection`

Singleton para manejar conexiones con RabbitMQ.

- **Método **``:
  - `Connection getRabbitMQConnection(String host, String username, String password)`: Obtiene una conexión a RabbitMQ para un host específico.
- **Método **``:
  - `void closeAllConnections()`: Cierra todas las conexiones activas con RabbitMQ.

### 5. `EmailMessage`

Representa un mensaje de correo electrónico que será enviado a una cola en un broker de mensajería.

- **Clase **``:
  - Proporciona una manera flexible y fluida de construir instancias de `EmailMessage`.
- **Métodos**:
  - `String getDestination()`: Obtiene el correo electrónico del remitente.
  - `String getContent()`: Obtiene el contenido completo del mensaje.

### 6. `MessageObserver`

Permite suscribirse a eventos de mensajes y notifica a los observadores cuando se recibe un mensaje.

- **Método **``:
  - `void subscribe(MessageCallback callback)`: Suscribe un callback que será llamado cuando se reciba un mensaje.

### 7. `RabbitMQConsumer`

Consume mensajes de una cola de RabbitMQ y notifica a los observadores.

- **Constructor**:
  - `RabbitMQConsumer(MessageObserver observer, Connection connection)`: Inicializa el consumidor con un observador y una conexión a RabbitMQ.
- **Método **``:
  - `void startListening()`: Inicia la escucha de mensajes en la cola.

### 8. `LogConfigManager`

El LogConfigManager es responsable de gestionar la configuración de logs en archivo local y en AWS CloudWatch. Permite habilitar o deshabilitar los logs, así como configurar la ubicación del archivo y la región de AWS grupo y stream de cloudwoatch.

- **Métodos Principales:

	-	`setFileLogEnable(boolean enable)`: Habilita o deshabilita el log en archivo local. No se puede deshabilitar el log en archivo si CloudWatch no está habilitado.

	-	`setFileLogLocation(String location)`: Establece la ubicación del log en archivo local.

	- `setCloudWatchEnable(boolean enable)`: Habilita o deshabilita el log en AWS CloudWatch.

	- `setCloudWatchLogGroup(String logGroup)`: Establece el nombre del grupo de logs en CloudWatch.

	- `setCloudWatchLogStream(String logStream)`: Establece el nombre del flujo de logs en CloudWatch.

	- `setCloudWatchRegion(AwsRegion region)`: Establece la región de AWS CloudWatch.

	- `saveConfig()`: Guarda la configuración en el archivo local.properties.

## Implementación Paso a Paso

1. **Configurar el Mensaje de Correo Electrónico**:

   ```java
   EmailMessage email = new EmailMessage.Builder()
           .senderEmail("example@example.com")
           .recipients(List.of("recipient1@example.com", "recipient2@example.com"))
           .ccRecipients(List.of("cc1@example.com"))
           .bccRecipients(List.of("bcc1@example.com"))
           .subject("Subject of the email")
           .body("Body of the email")
           .isHtml(true)
           .attachmentBase64("base64EncodedString")
           .attachmentName("attachment.pdf")
           .build();
   ```

2. **Crear la instancia de la fachada QueueHelper**:

   ```java
   QueueHelper queueHelper = new QueueHelper();
   ```

3. **Conectar a RabbitMQ**:

   ```java
   queueHelper.withRabbitMQ("RABBITMQ_MESSAGE", "127.0.0.1", "rabbit_user", "rabbit_password");
   ```

4. **Usar la fachada con RabbitMQ**:

   ```java
   MessagingFacade messagingFacade = new MessagingFacade(
           new RabbitMQStrategy(queueHelper.getConnection("RABBITMQ_MESSAGE")));
   ```

5. **Enviar el Mensaje**:

   ```java
   messagingFacade.send("RABBITMQ_MESSAGE", email, 50, 1);
   ```

6. **Cerrar la conexión después de usarla**:

   ```java
   queueHelper.disconnect("RABBITMQ_MESSAGE");
   ```

## Consumo de Mensajes

1. **Configurar ubicacion de logs**:
    ```java
    LogConfigManager configManager = new LogConfigManager();
    //CloudWatch configuracion
    configManager.setCloudWatchEnable(true);
    configManager.setCloudWatchLogGroup("MiAppLogs");
    configManager.setCloudWatchLogStream("MiStreamLogs");
    configManager.setCloudWatchRegion(AwsRegion.US_EAST_2);

    //confinguracion de logs locales
    configManager.setFileLogEnable(true);
    configManager.setFileLogLocation("location/app-service.log");
    configManager.saveConfig();
    ```
1. **Crear el Observador**:

   ```java
   MessageObserver observer = new MessageObserver();
   ```

2. **Suscribir el Callback**:

   ```java
   observer.subscribe((MessageModelRequest message) -> {
       try {
           var msg = message.getContent();
           ObjectMapper objectMapper = new ObjectMapper();
           EmailMessage emailMessage = objectMapper.readValue(msg, EmailMessage.class);
           System.out.println("✉️ Email: " + emailMessage);
       } catch (JsonProcessingException e) {
           System.err.println("Error al procesar el mensaje: " + e.getMessage());
       }
   });
   ```

3. **Iniciar el Consumidor de RabbitMQ**:

   ```java
   RabbitMQConsumer consumer = new RabbitMQConsumer(observer, queueHelper.getConnection("RABBITMQ_MESSAGE"));
   consumer.startListening();
   ```

## Flujo de Modificación

1. **Modificar la Estrategia de Mensajería**:

   - Si desea cambiar la estrategia de mensajería (por ejemplo, de RabbitMQ a Kafka), implemente la interfaz `MessagingStrategy` en una nueva clase (por ejemplo, `KafkaStrategy`).

2. **Actualizar la Fachada de Mensajería**:

   - Reemplace la instancia de `RabbitMQStrategy` con la nueva estrategia (por ejemplo, `KafkaStrategy`).

## Compilación de la Librería

Para compilar la librería, siga estos pasos:

### Instalar Maven:

1. Asegúrese de tener Maven instalado en su sistema.

### Ejecutar el Comando de Compilación:

```sh
mvn clean package
```

Este comando compilará el proyecto y generará un archivo JAR en el directorio `target` el compilado `queue-helper-x.x.x.jar`

## Dependencias

Al usar la libreria asegúrate de tener las siguientes dependencias en tu proyecto para evitar problemas con SLF4J:

### Maven

```xml
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-simple</artifactId>
    <version>1.7.32</version>
</dependency>
```

### Gradle

```gradle 
implementation 'org.slf4j:slf4j-simple:1.7.32'
```
## Ejemplo de uso

### Publicar mensaje
```java
LogConfigManager configManager = new LogConfigManager();
Logger logger;

// Configuración de logs si requieres almacenaqr los en cloudwatch, 
// antes se debe de tener configuradas las credenciales o el perfefil default en el equitpo para poder conectarse
configManager.setCloudWatchEnable(true);
configManager.setCloudWatchLogGroup("MiAppLogs");
configManager.setCloudWatchLogStream("MiStreamLogs");
configManager.setCloudWatchRegion(AwsRegion.US_EAST_2);

//configurar el log local, si no se define la informacion se va guardar en documentos/logs/queue-helper
configManager.setFileLogEnable(true);

        //Guarda la configuracion definida
configManager.saveConfig();

        // inicializa el log factory despues de establecer la configuracion de logs para que pueda tomar la configuracion
logger = new LoggerFactory().getCompositeLogger();

try {
    //nombre de la cola donde se va pulicar el mensaje
    String rabbitmqMessage = "queue-email-message-sending";
    //crea el cuerpo del mensaje
    EmailMessage email = createEmailMessage();

    // Crear la instancia de la fachada QueueHelper
    QueueHelper queueHelper = new QueueHelper();

    // Conectar a RabbitMQ
    logger.log("Conectando a RabbitMQ en: 127.0.0.1", LogLevel.INFO);
    queueHelper.withRabbitMQ(rabbitmqMessage, "127.0.0.1", "app-user","app-pass");

    // Usar la fachada con RabbitMQ
    MessagingFacade messagingFacade = new MessagingFacade(
            new RabbitMQStrategy(queueHelper.getConnection(rabbitmqMessage)));
    messagingFacade.send(rabbitmqMessage, email, 1, 1);

    // Cerrar la conexión después de usarla
    queueHelper.disconnect(rabbitmqMessage);

    logger.log("Mensaje enviado correctamente a RabbitMQ", LogLevel.INFO);

  } catch (Exception e) {
      logger.log("Error en la aplicación: " + e.getMessage(), LogLevel.CRITICAL);
  }
    

/**
 * Crea un mensaje de correo electrónico de prueba.
 * 
 * @return Una instancia de {@link EmailMessage} con valores predefinidos.
 */
private static EmailMessage createEmailMessage() {
    return new EmailMessage.Builder()
            .senderEmail("example@example.com")
            .recipients(List.of("recipient1@example.com", "recipient2@example.com"))
            .ccRecipients(List.of("cc1@example.com"))
            .bccRecipients(List.of("bcc1@example.com"))
            .subject("Subject of the email")
            .body("Body of the email")
            .isHtml(true)
            .attachmentBase64("base64EncodedString")
            .attachmentName("attachment.pdf")
            .build();
}
```

### Consumir mensaje

```Java
LogConfigManager configManager = new LogConfigManager();
Logger logger;

// Configuración de logs si requieres almacenaqr los en cloudwatch, 
// antes se debe de tener configuradas las credenciales o el perfefil default en el equitpo para poder conectarse
configManager.setCloudWatchEnable(true);
configManager.setCloudWatchLogGroup("MiAppLogs");
configManager.setCloudWatchLogStream("MiStreamLogs");
configManager.setCloudWatchRegion(AwsRegion.US_EAST_2);

//configurar el log local, si no se define la informacion se va guardar en documentos/logs/queue-helper
configManager.setFileLogEnable(true);

//Guarda la configuracion definida
configManager.saveConfig();

// inicializa el log factory despues de establecer la configuracion de logs para que pueda tomar la configuracion
logger = new LoggerFactory().getCompositeLogger();

try {
  // cola de la que se va leer el mensaje
    String rabbitmqMessage = "queue-email-message-sending";
    EmailMessage email = createEmailMessage();

    // Crear la instancia de la fachada QueueHelper
    QueueHelper queueHelper = new QueueHelper();

    // Conectar a RabbitMQ
    logger.log("Conectando a RabbitMQ en: 127.0.0.1", LogLevel.INFO);
    queueHelper.withRabbitMQ(rabbitmqMessage, "127.0.0.1", "app-user", "app-pass");

    // Configuración para consumir una cola
    // Crear el observador
    MessageObserver observer = new MessageObserver();

    // Suscribir el callback (lambda)
    observer.subscribe((MessageModelRequest message) -> {
        try {
            processMessage(message);
        } catch (JsonProcessingException e) {
            logger.log("Error al procesar el mensaje: " + e.getMessageLogLevel.CRITICAL);
        }
    });

    // Iniciar el consumidor de RabbitMQ
    RabbitMQConsumer consumer = new RabbitMQConsumer(observer, queueHelper.getConnection(rabbitmqMessage), rabbitmqMessage);
    consumer.startListening();

} catch (Exception e) {
    logger.log("Error en la aplicación: " + e.getMessage(), LogLevel.CRITICAL);
}

/**
 * Procesa el mensaje recibido de RabbitMQ y lo convierte en un objeto EmailMessage.
 * 
 * @param message El mensaje recibido en formato JSON.
 * @throws JsonProcessingException Si ocurre un error al procesar el JSON.
 */
private static void processMessage(MessageModelRequest message) throws JsonProcessingException {
    var msg = message.getContent();
    ObjectMapper objectMapper = new ObjectMapper();
    EmailMessage emailMessage = objectMapper.readValue(msg, EmailMessage.class);
    System.out.println("✉️ Email: " + emailMessage.toJson());
}
```
